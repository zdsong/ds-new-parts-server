package jp.newpart.common.core.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jp.newpart.common.annotation.DecryptField;
import jp.newpart.common.annotation.Excel;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.core.domain.BaseEntity;
import jp.newpart.common.validator.annotation.*;
import jp.newpart.common.validator.enums.CheckTypeEnum;
import jp.newpart.common.validator.utils.ValidatorGroup;
import jp.newpart.common.xss.Xss;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * sys_user
 * 
 * @author nextosd
 */
@Schema(description = "ユーザ")
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ユーザ番号 */
    @Excel(name = "ユーザ番号", type = Excel.Type.EXPORT, cellType = Excel.ColumnType.STRING, prompt = "ユーザ番号")
    @LmNotBlank(params = {"{userId}"}, groups = {ValidatorGroup.CustomerGroup.class})
    private String userId;

    /**
     * ユーザーアカウント
     */
    @Schema(description = "ユーザーID", example = "0001", required = true)
    @DecryptField
    @Excel(name = "ユーザーID")
    @LmNotBlank(params = {"{userName}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 100, params = {"{userName}", "100"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmType(params = {"{userName}", "{halfAlphabetNumericSymbol}"}, checkType = CheckTypeEnum.ALPHABET_NUMERIC_SYMBOL, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String userName;

    /**
     * ユーザー名称
     */
    @Schema(description = "ユーザー名称", example = "御辺　怜太", required = true)
    @DecryptField
    @Excel(name = "ユーザー名称")
    @LmNotBlank(params = {"{nickName}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 30, params = {"{nickName}", "30"}, message = "{E0051}", groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String nickName;

    /** メール */
    @Schema(description = "メール", example = "test@gmail.com")
    @Excel(name = "メール")
    @LmMail(params = {"{mailAddress}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String email;

    /**
     * 携帯電話番号
     */
    @Excel(name = "携帯電話番号", cellType = Excel.ColumnType.TEXT)
    private String phonenumber;

    /**
     * ユーザーセックス
     */
    @Excel(name = "ユーザー性別", readConverterExp = "0=男性,1=女性,2=不明")
    private String sex;

    /** ユーザーの顔 */
    private String avatar;

    /**
     * パスワード
     */
    @Schema(description = "パスワード", example = "*******", required = true)
    @DecryptField
    @LmNotBlank(params = {"{password}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.CustomerGroup.class})
    @LmLength(min = 8, max = 20, params = {"{password}"}, message = "{E0002}", groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.CustomerGroup.class})
    @LmPassword(params = {"{password}"}, message = "{E0002}", groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.CustomerGroup.class})
    private String password;

    /** 利用状態（0利用中 1停止中） */
    @Schema(description = "利用状態", example = "0")
    @Excel(name = "利用状態", readConverterExp = "0=利用中,1=停止中")
    private String status;

    /** ロールID */
    @LmNotBlank(params = {"{role}"}, groups = {ValidatorGroup.AddGroup.class})
    private String roleId;

    /**
     * ユーザーの種類（00:CRM 90:Admin）
     */
    private String userType;

    /** 削除フラグ（0は削除 2が存在） */
    private String delFlag;

    /** 最終ログインIP */
    @Excel(name = "最終ログインIP", type = Excel.Type.EXPORT)
    private String loginIp;

    /** 最終ログイン時間 */
    @Excel(name = "最終ログイン時間", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date loginDate;

    /**
     * リビジョン
     */
    private Integer version;

    /** 備考 */
    private String remark;

    /** ロールオブジェクト */
    private List<SysRole> roles;

    /** ロールグループ */
    private String[] roleIds;

    /**
     * ロール
     */
    private String roleLabel;

    /**
     * 利用状態
     */
    private String statusLabel;

    public SysUser()
    {

    }

    public SysUser(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.userType);
    }

    public static boolean isAdmin(String userType)
    {
        return userType != null && Constants.USER_TYPE_SUPER_ADMIN.equals(userType);
    }

    @Xss(message = "ユーザーニックネームにスクリプト文字を含めることはできません")
    @Size(min = 0, max = 30, message = "ユーザーニックネームの長さは30文字以内")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Xss(message = "ユーザーアカウントにスクリプト文字を含めることはできません")
    @NotBlank(message = "ユーザーアカウントを空にすることはできません")
    @Size(min = 0, max = 30, message = "ユーザーアカウントの長さは30文字以内")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Email(message = "メールボックスのフォーマットが正しくありません")
    @Size(min = 0, max = 50, message = "メールボックスの長さは50文字以内")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Size(min = 0, max = 11, message = "携帯電話番号の長さは11文字以内")
    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getLoginIp()
    {
        return loginIp;
    }

    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public Date getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(Date loginDate)
    {
        this.loginDate = loginDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SysRole> getRoles()
    {
        return roles;
    }

    public void setRoles(List<SysRole> roles)
    {
        this.roles = roles;
    }

    public String[] getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds)
    {
        this.roleIds = roleIds;
    }

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleLabel() {
        return roleLabel;
    }

    public void setRoleLabel(String roleLabel) {
        this.roleLabel = roleLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("email", getEmail())
            .append("phonenumber", getPhonenumber())
            .append("sex", getSex())
            .append("avatar", getAvatar())
            .append("password", getPassword())
            .append("status", getStatus())
            .append("userType", getUserType())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginDate", getLoginDate())
            .append("version", getVersion())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
