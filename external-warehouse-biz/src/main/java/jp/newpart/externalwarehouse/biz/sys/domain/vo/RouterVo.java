package jp.newpart.externalwarehouse.biz.sys.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * ルーティング構成情報
 * 
 * @author nextosd
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo
{
    /**
     * ルーティング名
     */
    private String name;

    /**
     * ルーティングアドレス
     */
    private String path;

    /**
     * ルーティングを非表示にするかどうか、trueを設定したときにルーティングがサイドバーに表示されなくなる
     */
    private boolean hidden;

    /**
     * noRedirectを設定するときにパンくずナビゲーションでルートをクリックできないアドレスをリダイレクトします。
     */
    private String redirect;

    /**
     * コンポーネントアドレス
     */
    private String component;

    /**
     * ルーティングパラメータ：例｛「id」：1、「name」：「test」｝
     */
    private String query;

    /**
     * ルートの下にあるチルドレン宣言のルートが1つより大きい場合、コンポーネントページなどのネストされたモードに自動的になります
     */
    private Boolean alwaysShow;

    /**
     * その他の要素
     */
    private MetaVo meta;

    /**
     * サブルーティング
     */
    private List<RouterVo> children;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public boolean getHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public String getRedirect()
    {
        return redirect;
    }

    public void setRedirect(String redirect)
    {
        this.redirect = redirect;
    }

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

    public Boolean getAlwaysShow()
    {
        return alwaysShow;
    }

    public void setAlwaysShow(Boolean alwaysShow)
    {
        this.alwaysShow = alwaysShow;
    }

    public MetaVo getMeta()
    {
        return meta;
    }

    public void setMeta(MetaVo meta)
    {
        this.meta = meta;
    }

    public List<RouterVo> getChildren()
    {
        return children;
    }

    public void setChildren(List<RouterVo> children)
    {
        this.children = children;
    }
}
