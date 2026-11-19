package jp.nasta.pms.common.biz.domain.entity;

import jp.nasta.pms.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 品目マスタ (mast_item)
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MastItemEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 品目コード
     */
    private String itemCode;

    /**
     * 品目名
     */
    private String itemName;

    /**
     * 品目名カナ
     */
    private String prprkn;

    /**
     * 品名（伝票用）
     */
    private String prprkj;

    /**
     * 規格
     */
    private String prkku;

    /**
     * 旧仕様
     */
    private String prktsk;

    /**
     * 図番
     */
    private String przmbn;

    /**
     * 仕様
     */
    private String prsiyo;

    /**
     * サイズ１
     */
    private BigDecimal prsiz1;

    /**
     * サイズ２
     */
    private BigDecimal prsiz2;

    /**
     * サイズ３
     */
    private BigDecimal prsiz3;

    /**
     * 品目区分
     */
    private String prhnmx;

    /**
     * 在庫単位コード
     */
    private String prztic;

    /**
     * 売上単価単位コード
     */
    private String prutic;

    /**
     * 仕入単価単位コード
     */
    private String prsttc;

    /**
     * 材質コード
     */
    private String przscd;

    /**
     * 単重
     */
    private BigDecimal prtanj;

    /**
     * 重量単位コード
     */
    private String prjtic;

    /**
     * ローレベル・コード
     */
    private String prllcd;

    /**
     * ＩＳＯ対象区分
     */
    private String prisoc;

    /**
     * 原料色コード
     */
    private String prgrsc;

    /**
     * 有効期間日数
     */
    private Integer prukhi;

    /**
     * 税率（％）
     */
    private BigDecimal prtxr;

    /**
     * 出庫区分
     */
    private String prskx;

    /**
     * 品目諸口区分
     */
    private String przatx;

    /**
     * 製造拠点コード
     */
    private String prszkc;

    /**
     * 廃盤区分
     */
    private String prhbhb;

    /**
     * 備考
     */
    private String prbiko;

    /**
     * 予備区分１
     */
    private String prykb1;

    /**
     * 予備区分２
     */
    private String prykb2;

    /**
     * 予備区分３
     */
    private String prykb3;

    /**
     * 予備区分４
     */
    private BigDecimal prykb4;

    /**
     * 予備区分５
     */
    private BigDecimal prykb5;

    /**
     * 予備コード１
     */
    private BigDecimal prycd1;

    /**
     * 予備コード２
     */
    private String prycd2;

    /**
     * 予備コード３
     */
    private String prycd3;

    /**
     * 予備コード４
     */
    private String prycd4;

    /**
     * 予備コード５
     */
    private String prycd5;

    /**
     * 予備数値１
     */
    private BigDecimal prysui1;

    /**
     * 予備数値２
     */
    private BigDecimal prysui2;

    /**
     * 予備数値３
     */
    private BigDecimal prysui3;

    /**
     * 予備数値４
     */
    private BigDecimal prysui4;

    /**
     * 予備数値５
     */
    private String prysui5;

    /**
     * 登録日
     */
    private String prywdt;

    /**
     * 登録時刻
     */
    private String prywtm;

    /**
     * 登録オペレータコード
     */
    private String prywus;

    /**
     * 最終更新日
     */
    private String pryudt;

    /**
     * 最終更新時刻
     */
    private String pryutm;

    /**
     * 最終更新オペレータコード
     */
    private BigDecimal pryuus;

    /**
     * 削除日
     */
    private BigDecimal pryddt;

    /**
     * 削除時刻
     */
    private BigDecimal prydtt;

    /**
     * 削除オペレータコード
     */
    private String prydus;

    /**
     * 状態区分
     */
    private String prjtx;

    /**
     * フィールド更新/処理識別
     */
    private String upid;

    /**
     * Version
     */
    private Integer version;

}
