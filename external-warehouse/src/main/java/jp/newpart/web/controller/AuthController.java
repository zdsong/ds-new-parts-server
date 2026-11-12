package jp.newpart.web.controller;

import jp.newpart.common.annotation.Log;
import jp.newpart.common.biz.domain.vo.UserInfoVo;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.core.domain.model.LoginBody;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.validator.utils.ValidatorGroup;
import jp.newpart.externalwarehouse.auth.domain.AuthReSendVerifyCodeBo;

import jp.newpart.externalwarehouse.auth.domain.AuthResetPwdBo;
import jp.newpart.externalwarehouse.auth.domain.AuthSecondVerifyBo;
import jp.newpart.externalwarehouse.auth.domain.AuthSendVerifyCodeBo;
import jp.newpart.externalwarehouse.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Log(title = "新産直くん ログイン パスワード再設定", businessType = BusinessType.UPDATE)
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
        userInfoVo.setFirstLogin(user.getVersion() <= 1);

        AjaxResult ajax = AjaxResult.success(userInfoVo);

        return ajax;
    }

}
