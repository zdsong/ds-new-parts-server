package jp.newpart.common.core.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import jp.newpart.common.core.domain.entity.SysMenu;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TreeSelectツリーストラクチャエンティティクラス
 *
 * @author nextosd
 */
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ノードID
     */
    private String id;

    private String itemId;

    /**
     * ノード名称
     */
    private String label;

    /**
     * ノード無効化
     */
    private boolean disabled = false;

    /**
     * 子ノード
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() {

    }

    public TreeSelect(SysMenu menu) {
        this.id = menu.getMenuId();
        this.itemId = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public List<TreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelect> children) {
        this.children = children;
    }
}
