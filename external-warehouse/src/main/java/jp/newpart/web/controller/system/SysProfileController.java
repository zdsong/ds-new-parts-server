package jp.newpart.web.controller.system;

import jp.newpart.common.annotation.Log;
import jp.newpart.common.config.AppConfig;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.core.service.TokenService;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.file.FileUploadUtils;
import jp.newpart.common.utils.file.MimeTypeUtils;
import jp.newpart.common.validator.utils.ValidatorGroup;
import jp.newpart.externalwarehouse.sys.domain.SysUserParam;
import jp.newpart.externalwarehouse.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * ユーザー情報 コントロール
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/system/user/profile")
public class SysProfileController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * ユーザー情報
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = userService.selectUserByUserName(loginUser.getUsername());
        AjaxResult ajax = AjaxResult.success(user);
        return ajax;
    }

    /**
     * ユーザー情報を更新
     */
    @Log(title = "ユーザー情報", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@Validated({ValidatorGroup.UpdateGroup.class}) @RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        loginUser.setNickName(user.getNickName());
        SysUser currentUser = userService.selectUserByUserName(loginUser.getUsername());
        currentUser.setNickName(user.getNickName());
        currentUser.setVersion(null);

        int result = userService.updateUserProfile(currentUser);
        if (result > 0) {
            // キャッシュのユーザー情報を更新
            //loginUser.setUser(userService.selectUserByUserName(loginUser.getUsername()));
            tokenService.setLoginUser(loginUser);
        }
        return success();
    }

    /**
     * パスワードをリセット
     */
    @Log(title = "ユーザー情報", businessType = BusinessType.UPDATE)
    @PostMapping("/updatePwd")
    public AjaxResult updatePwd(@Validated({ValidatorGroup.UpdateGroup.class}) SysUserParam sysUserParam) {
        String oldPassword = sysUserParam.getOldPassword();
        String newPassword = sysUserParam.getNewPassword();

        LoginUser loginUser = getLoginUser();
        SysUser user = userService.selectUserByUserName(loginUser.getUsername());
        String userName = user.getUserName();
        String password = user.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return error("パスワードの変更に失敗しました: 古いパスワードが間違っています。");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return error("新しいパスワードは古いパスワードと異なる必要があります。");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userName, newPassword) > 0) {
            // キャッシュのユーザーのパスワードを更新
            //loginUser.setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("パスワードの変更中に異常が発生しました。管理者に連絡してください。");
    }

    /**
     * アバターをアップロード
     */
    @Log(title = "ユーザーのアバター", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            try {
                String avatar = FileUploadUtils.upload(AppConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
                if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                    AjaxResult ajax = AjaxResult.success();
                    ajax.put("imgUrl", avatar);
                    return ajax;
                }
            } catch (Exception ex) {
                return AjaxResult.error("画像のアップロード中に異常が発生しました。管理者に連絡してください");
            }
        }
        return AjaxResult.error("画像のアップロード中に異常が発生しました。管理者に連絡してください");
    }
}
