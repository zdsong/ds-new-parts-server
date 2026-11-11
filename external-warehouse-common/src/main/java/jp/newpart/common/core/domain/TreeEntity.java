package jp.newpart.common.core.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree
 * 
 * @author nextosd
 */
public class TreeEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 親メニュー名 */
    private String parentName;

    /** 親メニューID */
    private Long parentId;

    /** 表示順序 */
    private Integer orderNum;

    private String ancestors;

    private List<?> children = new ArrayList<>();

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    public List<?> getChildren()
    {
        return children;
    }

    public void setChildren(List<?> children)
    {
        this.children = children;
    }
}
