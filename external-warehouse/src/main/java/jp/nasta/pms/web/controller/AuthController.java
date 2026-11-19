package jp.nasta.pms.web.controller;

import jp.nasta.pms.common.annotation.Log;
import jp.nasta.pms.common.biz.domain.vo.UserInfoVo;
import jp.nasta.pms.common.core.domain.AjaxResult;
import jp.nasta.pms.common.core.domain.entity.SysUser;
import jp.nasta.pms.common.core.domain.model.LoginBody;
import jp.nasta.pms.common.core.domain.model.LoginUser;
import jp.nasta.pms.common.enums.BusinessType;
import jp.nasta.pms.common.utils.DateUtils;
import jp.nasta.pms.common.utils.SecurityUtils;
import jp.nasta.pms.common.validator.utils.ValidatorGroup;
import jp.nasta.pms.externalwarehouse.auth.domain.AuthReSendVerifyCodeBo;

import jp.nasta.pms.externalwarehouse.auth.domain.AuthResetPwdBo;
import jp.nasta.pms.externalwarehouse.auth.domain.AuthSecondVerifyBo;
import jp.nasta.pms.externalwarehouse.auth.domain.AuthSendVerifyCodeBo;
import jp.nasta.pms.externalwarehouse.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * ログイン認証
 * 
 * @author nextosd
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * ログイン方法
     *
     * @param loginBody ログイン情報
     * @return 結果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        return AjaxResult.success(authService.login(loginBody.getUsername(), loginBody.getPassword()));
    }

    /**
     * 直接ログイン方法
     * ユーザーIDとパスワードの検証を行い、トークンを発行する
     *
     * @param loginBody ログイン情報
     * @return 結果
     */
    @PostMapping("/directLogin")
    public AjaxResult directLogin(@RequestBody LoginBody loginBody)
    {
        return AjaxResult.success(authService.directLogin(loginBody.getUsername(), loginBody.getPassword()));
    }

    /**
     * 二次認証
     */
    @PostMapping("/loginByCode")
    public AjaxResult authVerifySecond(@RequestBody @Validated({ValidatorGroup.AddGroup.class}) AuthSecondVerifyBo authSecondBo) {
        return AjaxResult.success(authService.authenticateSecond(authSecondBo));
    }

    /**
     * パスワード再発行用の認証コードを送信します。
     * 登録しているメールアドレスを入力してください
     * 認証コードを送信
     */
    @PostMapping("/sendAuthCode")
    public AjaxResult sendAuthCode(@RequestBody @Validated({ValidatorGroup.AddGroup.class}) AuthSendVerifyCodeBo authSendVerifyCodeBo) {
        return AjaxResult.success(authService.sendAuthCode(authSendVerifyCodeBo));
    }

    /**
     * 認証コードを再送信する
     */
    @PostMapping("/resendAuthCode")
    public AjaxResult resendAuthCode(@RequestBody @Validated({ValidatorGroup.AddGroup.class}) AuthReSendVerifyCodeBo authReSendVerifyCodeBo) throws Exception {
        return AjaxResult.success(authService.resendAuthCode(authReSendVerifyCodeBo));
    }

    /**
     * パスワード再設定
     */
    @Log(title = "ログイン パスワード再設定", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody @Validated({ValidatorGroup.AddGroup.class}) AuthResetPwdBo userResetPwdBo) {
        authService.resetPwd(userResetPwdBo);
        return AjaxResult.success();
    }

    /**
     * ユーザー情報を取得
     *
     * @return ユーザー情報
     */
    @GetMapping("userInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = authService.selectUserByUserName(loginUser.getUsername());

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserName(user.getUserName());
        userInfoVo.setUserId(user.getUserId());
        userInfoVo.setNickName(user.getNickName());
        userInfoVo.setUserType(user.getUserType());
        userInfoVo.setFirstLogin(user.getVersion() == null || user.getVersion() <= 1);

        // 最終ログイン日時（AppBar 表示用に整形して返す）
        Date lastLoginDate = authService.selectLastLoginDate(loginUser.getUsername());
        userInfoVo.setLastLoginDate(lastLoginDate == null ? null
                : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM, lastLoginDate));
        userInfoVo.setLastLoginIp(user.getLoginIp());

        AjaxResult ajax = AjaxResult.success(userInfoVo);

        return ajax;
    }

}
