package jp.newpart.common.core.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jp.newpart.common.annotation.Excel;
import jp.newpart.common.core.domain.BaseEntity;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.common.validator.annotation.LmLength;
import jp.newpart.common.validator.annotation.LmNotBlank;
import jp.newpart.common.validator.utils.ValidatorGroup;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;

/**
 * ロールテーブル sys_role
 * 
 * @author nextosd
 */
public class SysRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ロール番号 */
    @Excel(name = "ロール番号", cellType = Excel.ColumnType.STRING)
    private String roleId;

    /**
     * ロール名称
     */
    @Excel(name = "ロール名称")
    @LmNotBlank(params = {"{roleName}"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    @LmLength(min = 1, max = 30, params = {"{roleName}", "30"}, message = "{E0051}", groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})
    private String roleName;

    /** ロール権限 */
    @Excel(name = "ロール権限")
    private String roleKey;

    /** ロールソート */
    @Excel(name = "ロールソート")
    private Integer roleSort;

    /**
     * データ範囲（1：すべてのデータ権限；2：カスタムデータ権限；3：自部門データ権限；4：自部門および以下データ権限；5：自分自身のデータ権限）
     */
    @Excel(name = "データ範囲", readConverterExp = "1=すべてのデータ権限,2=カスタムデータ権限,3=自部門データ権限,4=自部門および以下データ権限,5=自分自身のデータ権限")
    private String dataScope;

    /** メニューツリー項目を関連付けて表示するかどうかを選択します（0：親子非関連表示1：親子相互関連表示） */
    private Integer menuCheckStrictly;

    /**
     * ロール状態（0正常 1停止）
     */
    @Excel(name = "ロール状態", readConverterExp = "0=正常,1=停止")
    private String status;

    /** 削除フラグ（0存在 2削除） */
    private String delFlag;

    /** ユーザーにこのロールIDが存在するかどうか  デフォルトで存在しません */
    private boolean flag = false;

    /**
     * リビジョン
     */
    private Integer version;

    /** 備考 */
    private String remark;

    /** メニューグループ */
    private String[] menuIds;

    /** ロールメニュー権限 */
    private Set<String> permissions;

    public SysRole()
    {

    }

    public SysRole(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(String roleId)
    {
        return ValueUtils.isEqual(roleId, "1");
    }

    @NotBlank(message = "ロール名前を空にすることはできません")
    @Size(min = 0, max = 30, message = "ロール名前の長さは30文字以内")
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    @NotBlank(message = "権限文字を空白にすることはできません")
    @Size(min = 0, max = 100, message = "権限文字長は100文字以内")
    public String getRoleKey()
    {
        return roleKey;
    }

    public void setRoleKey(String roleKey)
    {
        this.roleKey = roleKey;
    }

    @NotNull(message = "表示順序を空白にすることはできません")
    public Integer getRoleSort()
    {
        return roleSort;
    }

    public void setRoleSort(Integer roleSort)
    {
        this.roleSort = roleSort;
    }

    public String getDataScope()
    {
        return dataScope;
    }

    public void setDataScope(String dataScope)
    {
        this.dataScope = dataScope;
    }

    public Integer getMenuCheckStrictly()
    {
        return menuCheckStrictly;
    }

    public void setMenuCheckStrictly(Integer menuCheckStrictly)
    {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
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

    public String[] getMenuIds()
    {
        return menuIds;
    }

    public void setMenuIds(String[] menuIds)
    {
        this.menuIds = menuIds;
    }

    public Set<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("dataScope", getDataScope())
            .append("menuCheckStrictly", getMenuCheckStrictly())
            .append("status", getStatus())
            .append("version", getVersion())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
