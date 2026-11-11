package jp.newpart.externalwarehouse.biz.sys.domain.vo;

import jp.newpart.common.utils.StringUtils;

/**
 * ルーティング表示情報
 * 
 * @author nextosd
 */
public class MetaVo
{
    /**
     * サイドバーやパンくずに表示されるルーティングの名前を設定する
     */
    private String title;

    /**
     * 経路src/assets/icons/svgに対応する経路のアイコンを設定する
     */
    private String icon;

    /**
     * trueに設定すると、<keep-alive>にキャッシュされません
     */
    private boolean noCache;

    /**
     * イントラチェーンアドレス（http（s）：//先頭）
     */
    private String link;

    public MetaVo()
    {
    }

    public MetaVo(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public MetaVo(String title, String icon, boolean noCache)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }

    public MetaVo(String title, String icon, String link)
    {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public MetaVo(String title, String icon, boolean noCache, String link)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        if (StringUtils.ishttp(link))
        {
            this.link = link;
        }
    }

    public boolean isNoCache()
    {
        return noCache;
    }

    public void setNoCache(boolean noCache)
    {
        this.noCache = noCache;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }
}
