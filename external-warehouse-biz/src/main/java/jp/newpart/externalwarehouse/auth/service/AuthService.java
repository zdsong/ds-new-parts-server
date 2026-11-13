package jp.newpart.externalwarehouse.auth.service;

import jakarta.annotation.Resource;
import jp.newpart.common.biz.enums.BssUserCertificationType;
import jp.newpart.common.biz.enums.ExternalWarehousePgIdEnum;
import jp.newpart.common.config.AppConfig;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.core.service.TokenService;
import jp.newpart.common.enums.UserStatus;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.exception.user.AuthVerifyCodeNotMatchException;
import jp.newpart.common.exception.user.UserPasswordNotMatchException;
import jp.newpart.common.mapper.SysLogininforMapper;
import jp.newpart.common.manager.AsyncManager;
import jp.newpart.common.manager.factory.AsyncFactory;
import jp.newpart.common.security.context.AuthenticationContextHolder;
import jp.newpart.common.utils.*;
import jp.newpart.externalwarehouse.auth.domain.*;
import jp.newpart.externalwarehouse.biz.domain.bo.BssUserAuthCodeMailBo;
import jp.newpart.externalwarehouse.biz.service.IBssUserCertificationService;
import jp.newpart.externalwarehouse.sys.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 農家 認証
 * 
 * @author nextosd
 */
@Component
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private IBssUserCertificationService bssUserCertificationService;

    @Autowired
    private SysLogininforMapper sysLogininforMapper;

    /**
     * ログイン検証
     * 一次認証は「ユーザーID」「パスワード」入力によって行う
     *
     * @param userName ユーザーID
     * @param password パスワード
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String login(String userName, String password)
    {
        // ユーザー
        SysUser userDetails = sysUserMapper.selectUserByUserName(userName);

        if (StringUtils.isNull(userDetails))
        {
            log.info("ログインユーザ:{}は存在しません.", userName);
            throw new UserPasswordNotMatchException();
        }

        if(userDetails == null || !SecurityUtils.matchesPassword(password, userDetails.getPassword())){
            // E0004=ユーザー名またはパスワードが間違っています。
            throw new UserPasswordNotMatchException();
        }

        // 認証コードを送信する
        BssUserAuthCodeMailBo bssUserAuthCodeMailBo = new BssUserAuthCodeMailBo();
        bssUserAuthCodeMailBo.setUserId(userDetails.getUserName());
        bssUserAuthCodeMailBo.setUserName(userDetails.getNickName());
        bssUserAuthCodeMailBo.setMailAddress(userDetails.getEmail());
        bssUserAuthCodeMailBo.setType(BssUserCertificationType.FRAMER);
        bssUserAuthCodeMailBo.setPgId(ExternalWarehousePgIdEnum.N001.getCode());

        String sendId = bssUserCertificationService.sendAuthCodeByEmail(bssUserAuthCodeMailBo);

        return this.createTempToken(sendId, userDetails.getUserName());
    }

    /**
     * 直接ログイン検証
     * ユーザーIDとパスワードを検証し、トークンを発行してトークンの正確性を検証する
     *
     * @param userName ユーザーID
     * @param password パスワード
     * @return ログイントークン
     */
    @Transactional(rollbackFor = Exception.class)
    public String directLogin(String userName, String password)
    {
        // ユーザー情報の取得と利用状態の検証
        SysUser userDetails = this.selectUserByUserName(userName);

        // パスワード検証
        if (!SecurityUtils.matchesPassword(password, userDetails.getPassword()))
        {
            // E0004=ユーザー名またはパスワードが間違っています。
            throw new UserPasswordNotMatchException();
        }

        // ログインユーザー情報の作成
        LoginUser loginUser = new LoginUser(userDetails.isAdmin(), userDetails.getUserId(), userDetails.getUserName(), userDetails.getNickName(), userDetails.getPassword(), null);

        // トークンの作成
        String token = tokenService.createToken(loginUser);

        // トークン署名の検証
        if (!tokenService.validateToken(token))
        {
            throw new ServiceException("トークンの検証に失敗しました。");
        }

        // キャッシュ上のログインユーザー情報の検証
        LoginUser cacheLoginUser = tokenService.getLoginUserByToken(token);
        if (StringUtils.isNull(cacheLoginUser))
        {
            throw new ServiceException("トークンの検証に失敗しました。");
        }

        // ログイン成功記録
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), userDetails.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("I0011")));

        return token;
    }

    /**
     * 二次認証
     * @param authSecondBo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String authenticateSecond(AuthSecondVerifyBo authSecondBo){

        // ユーザー認証
        Authentication authentication = null;
        AuthTempTokenBo authTempTokenBo = getTempToken(authSecondBo.getTempToken());

        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authTempTokenBo.getUserId(), authSecondBo.getVerifyCode());
            AuthenticationContextHolder.setContext(authenticationToken);
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), authTempTokenBo.getUserId(), Constants.LOGIN_FAIL, MessageUtils.message("E0063")));
                throw new AuthVerifyCodeNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), authTempTokenBo.getUserId(), Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        // I0011=二次認証に成功しました。
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(appConfig.getName(), authTempTokenBo.getUserId(), Constants.LOGIN_SUCCESS, MessageUtils.message("I0011")));

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        sysUserMapper.updateUser(this.selectUserByUserName(loginUser.getUsername()));

        // token
        return tokenService.createToken(loginUser);
    }

    /**
     * 認証コードを送信します。
     * パスワードを忘れた方
     *
     * @param authSendVerifyCodeBo
     *
     * @return 一時token
     */
    @Transactional(rollbackFor = Exception.class)
    public String sendAuthCode(AuthSendVerifyCodeBo authSendVerifyCodeBo){
        // メールアドレス 必須入力
        if(ValueUtils.isEmpty(authSendVerifyCodeBo.getMailAddress())){
            // E0003={0}は必須入力です。
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("mailAddress")));
        }

        // ユーザー メールアドレス
        SysUser user = sysUserMapper.selectUserByUserName(authSendVerifyCodeBo.getMailAddress());

        if(user == null){
            // E0062 確認用のパスワードと新パスワードが一致しません。
            throw new ServiceException(MessageUtils.message("E0062", authSendVerifyCodeBo.getMailAddress()));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            throw new ServiceException(MessageUtils.message("E0049"));
        }

        // 認証コードを送信する
        BssUserAuthCodeMailBo bssUserAuthCodeMailBo = new BssUserAuthCodeMailBo();
        bssUserAuthCodeMailBo.setUserId(user.getUserName());
        bssUserAuthCodeMailBo.setUserName(user.getNickName());
        bssUserAuthCodeMailBo.setMailAddress(user.getEmail());
        bssUserAuthCodeMailBo.setType(BssUserCertificationType.FRAMER);
        bssUserAuthCodeMailBo.setPgId(ExternalWarehousePgIdEnum.N002.getCode());

        String sendId = bssUserCertificationService.sendAuthCodeByEmail(bssUserAuthCodeMailBo);

        return this.createTempToken(sendId, user.getUserName());
    }


    /**
     * 認証コードを再送信する
     *
     * @return 一時token
     */
    @Transactional(rollbackFor = Exception.class)
    public String resendAuthCode(AuthReSendVerifyCodeBo authReSendVerifyCodeBo) throws Exception {

        AuthTempTokenBo authTempTokenBo = getTempToken(authReSendVerifyCodeBo.getTempToken());

        // ユーザー
        SysUser userDetails = this.selectUserByUserName(authTempTokenBo.getUserId());

        // 認証コードを再送信する
        BssUserAuthCodeMailBo bssUserAuthCodeMailBo = new BssUserAuthCodeMailBo();
        bssUserAuthCodeMailBo.setUserId(userDetails.getUserName());
        bssUserAuthCodeMailBo.setUserName(userDetails.getNickName());
        bssUserAuthCodeMailBo.setMailAddress(userDetails.getEmail());
        bssUserAuthCodeMailBo.setType(BssUserCertificationType.FRAMER);
        bssUserAuthCodeMailBo.setPgId(ExternalWarehousePgIdEnum.N003.getCode());

        String sendId = bssUserCertificationService.sendAuthCodeByEmail(bssUserAuthCodeMailBo);

        return this.createTempToken(sendId, userDetails.getUserName());
    }

    /**
     * パスワード再設定
     *
     * @param authResetPwdBo
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(AuthResetPwdBo authResetPwdBo){
        LoginUser loginUser = SecurityUtils.getLoginUser();

        if(ValueUtils.isNotEqual(authResetPwdBo.getNewPassword(), authResetPwdBo.getConfimPassword())){
            // E0048 確認用のパスワードと新パスワードが一致しません。
            throw new ServiceException(MessageUtils.message("E0048"));
        }

        sysUserMapper.resetUserPwd(loginUser.getUsername(), SecurityUtils.encryptPassword(authResetPwdBo.getNewPassword()));
    }

    /**
     * 最終ログイン日時を取得する
     * ログイン記録（sys_logininfor）の直近成功日時を優先し、
     * 記録が無い場合のみ sys_user の最終ログイン日時にフォールバックする。
     * （sys_user.login_date は二段階認証ログインでしか更新されないため、
     * 　直接ログインも含めた実際のログイン履歴はログイン記録側が正確）
     *
     * @param userName ユーザーID
     * @return 最終ログイン日時（取得できない場合は null）
     */
    public Date selectLastLoginDate(String userName){
        // I0008=ログインしました。 I0011=二段階認証に成功しました。
        Date lastLoginDate = sysLogininforMapper.selectLastLoginTime(userName,
                List.of(MessageUtils.message("I0008"), MessageUtils.message("I0011")));
        if (lastLoginDate != null) {
            return lastLoginDate;
        }

        SysUser user = sysUserMapper.selectUserByUserName(userName);
        return StringUtils.isNull(user) ? null : user.getLoginDate();
    }

    /**
     * ユーザーネームでユーザーを查詢
     *
     * @param username ユーザーネーム
     * @return ユーザーオブジェクト情報
     */
    public SysUser selectUserByUserName(String username){
        // ユーザー
        SysUser userDetails = sysUserMapper.selectUserByUserName(username);
        if (StringUtils.isNull(userDetails))
        {
            log.info("ログインユーザ:{}は存在しません.", username);
            throw new ServiceException(MessageUtils.message("E0050", username));
        }
        else if (UserStatus.DISABLE.getCode().equals(userDetails.getStatus()))
        {
            log.info("ログインユーザ:{}が停止されました", username);
            throw new ServiceException(MessageUtils.message("E0049"));
        }

        userDetails.setLoginDate(new Date());

        return userDetails;
    }

    /**
     * 認証トークンの生成
     *
     * @param sendAuthVerifyCodeId ユーザー認証情報ID
     * @param userId               ユーザーID
     * @return 認証トークン
     */
    private String createTempToken(String sendAuthVerifyCodeId, String userId) {
        String resultToken = StringUtils.EMPTY;
        try {
            Map<String, Object> encryptMap = Map.of(
                    "sendId", sendAuthVerifyCodeId,
                    "userId", userId);
            String tokenFormat = JsonUtil.toJsonStr(encryptMap);
            resultToken = DecryptUtils.encryptAES(tokenFormat);
        } catch (Exception ex) {
            log.error("【認証ユーザー異常】一時トークンの生成に失敗しました。", ex);
            throw new ServiceException("一時トークン生成失敗しますので、管理者を連絡してください。");
        }
        return resultToken;
    }

    private AuthTempTokenBo getTempToken(String tempToken){
        AuthTempTokenBo result = new AuthTempTokenBo();
        String decryptTokenStr;
        try {
            // 認証トークンの復号化
            decryptTokenStr = DecryptUtils.decryptAES(tempToken);
        } catch (Exception e) {
            log.error("【認証ユーザー異常】認証トークンの復号化に失敗しました。 {}", e);
            throw new ServiceException( "認証トークンが無効です。");
        }

        if (StringUtils.isEmpty(decryptTokenStr)) {
            log.error("【認証ユーザー異常】認証ユーザー情報を取得する。復号化情報は空白");
            throw new ServiceException("認証トークンが無効です。");
        } else if (!JsonUtil.isJsonStr(decryptTokenStr)) {
            log.error("【認証ユーザー異常】認証ユーザー情報を取得する。復号化情報はJSON形式以外です。");
            throw new ServiceException("認証トークンが無効です。");
        }

        // 復号化のトーケン情報
        Map<String, Object> tokenMap = JsonUtil.parseObject(decryptTokenStr, Map.class);

        if (null != tokenMap && tokenMap.containsKey("sendId") && StringUtils.isNotNull(tokenMap.get("sendId"))) {
            result.setSendVerifyCodeId(String.valueOf(tokenMap.get("sendId")));
            result.setUserId(String.valueOf(tokenMap.get("userId")));
        }

        return result;
    }
}
