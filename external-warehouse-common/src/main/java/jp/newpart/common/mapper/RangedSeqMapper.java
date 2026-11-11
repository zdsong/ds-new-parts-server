package jp.newpart.common.mapper;

import jp.newpart.common.core.domain.entity.RangedSeq;

/**
 * シリアルナンバー状況情報
 *
 * @author nextosd
 */
public interface RangedSeqMapper
{
    /**
     * シリアルナンバー追加
     *
     * @param rangeSeq ログ・オブジェクトへのアクセス
     */
    public void insert(RangedSeq rangeSeq);

    /**
     * シリアルナンバー更新
     *
     * @param rangeSeq ログ・オブジェクトへのアクセス
     */
    public void update(RangedSeq rangeSeq);

    /**
     * シリアルナンバーを検索リスト
     *
     * @param rangeSeq ID List
     * @return シリアルナンバー
     */
    public RangedSeq selectRangedSeq(RangedSeq rangeSeq);

}
