package jp.newpart.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品マスタ 検索パラメータ対象
 *
 * @author nextosd
 */
@Data
public class MastItemSearchBo  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 商品ID
     */
    private String itemId;

    /**
     * 商品コード
     */
    private String itemCode;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * JANコード
     */
    private String janCode;

    /**
     * スマレジ商品ID
     */
    private String sumarejiItemId;

    /**
     * 販売停止 1:停止 0:販売
     */
    private String salesFlg;

    /**
     * 確認 1:確認 0:確認しない
     */
    private String confirmFlg;

    /**
     * システム商品番号
     */
    private String sysItemCode;

    /**
     * 商品カテゴリ(大)
     */
    private String category1;

    /**
     * 商品カテゴリ(中)
     */
    private String category2;
}
