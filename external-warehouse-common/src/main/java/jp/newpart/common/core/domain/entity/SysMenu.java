package jp.newpart.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import jp.newpart.common.core.domain.BaseEntity;

/**
 * メニュー sys_menu
 * 
 * @author nextosd
 */
public class SysMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** メニューID */
    private String menuId;

    /** メニュー名 */
    private String menuName;

    /** Parent メニュー名 */
    private String parentName;

    /** Parent メニューID */
    private String parentId;

    /** 表示順序 */
    private Integer orderNum;

    /** ルーティングアドレス */
    private String path;

    /** コンポーネントパス */
    private String component;

    /** ルーティングパラメータ */
    private String query;

    /** ルーティング名 */
    private String routeName;

    /** IS Frame（0 Yes 1 No） */
    private String isFrame;

    /** キャッシュするかどうか（0 Yes 1 No） */
    private String isCache;

    /** タイプ（M:ディレクトリ C:メニュー F: ボタン） */
    private String menuType;

    /** 表示状態（0表示1非表示） */
    private String visible;

    /** メニュー ステータス（0通常 1停止） */
    private String status;

    /** 権限文字列 */
    private String perms;

    /** メニューICON */
    private String icon;

    /** 備考 */
    private String remark;

    /** Children メニュー */
    private List<SysMenu> children = new ArrayList<SysMenu>();

    public String getMenuId()
    {
        return menuId;
    }

    public void setMenuId(String menuId)
    {
        this.menuId = menuId;
    }

    @NotBlank(message = "名前を空にすることはできません")
    @Size(min = 0, max = 50, message = "名前の長さは50文字以内")
    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    @NotNull(message = "表示順序を空白にすることはできません")
    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    @Size(min = 0, max = 200, message = "ルーティングアドレスは200文字以内")
    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    @Size(min = 0, max = 200, message = "コンポーネントパスは255文字以内")
    public String getComponent()
    {
        return component;
    }

    public void setComponent(String component)
    {
        this.component = component;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public String getRouteName()
    {
        return routeName;
    }

    public void setRouteName(String routeName)
    {
        this.routeName = routeName;
    }

    public String getIsFrame()
    {
        return isFrame;
    }

    public void setIsFrame(String isFrame)
    {
        this.isFrame = isFrame;
    }

    public String getIsCache()
    {
        return isCache;
    }

    public void setIsCache(String isCache)
    {
        this.isCache = isCache;
    }

    @NotBlank(message = "メニュータイプを空にすることはできません")
    public String getMenuType()
    {
        return menuType;
    }

    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    public String getVisible()
    {
        return visible;
    }

    public void setVisible(String visible)
    {
        this.visible = visible;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Size(min = 0, max = 100, message = "権限IDの長さは100文字以内")
    public String getPerms()
    {
        return perms;
    }

    public void setPerms(String perms)
    {
        this.perms = perms;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SysMenu> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysMenu> children)
    {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("menuId", getMenuId())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("path", getPath())
            .append("component", getComponent())
            .append("query", getQuery())
            .append("routeName", getRouteName())
            .append("isFrame", getIsFrame())
            .append("IsCache", getIsCache())
            .append("menuType", getMenuType())
            .append("visible", getVisible())
            .append("status ", getStatus())
            .append("perms", getPerms())
            .append("icon", getIcon())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
