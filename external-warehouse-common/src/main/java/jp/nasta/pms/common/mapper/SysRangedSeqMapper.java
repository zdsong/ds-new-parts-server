package jp.nasta.pms.common.mapper;

import jp.nasta.pms.common.core.domain.entity.SysRangedSeq;

/**
 * シリアルナンバー状況情報
 *
 * @author nextosd
 */
public interface SysRangedSeqMapper
{
    /**
     * シリアルナンバー追加
     *
     * @param rangeSeq ログ・オブジェクトへのアクセス
     */
    public void insert(SysRangedSeq rangeSeq);

    /**
     * シリアルナンバー更新
     *
     * @param rangeSeq ログ・オブジェクトへのアクセス
     */
    public void update(SysRangedSeq rangeSeq);

    /**
     * シリアルナンバーを検索リスト
     *
     * @param rangeSeq ID List
     * @return シリアルナンバー
     */
    public SysRangedSeq selectSysRangedSeq(SysRangedSeq rangeSeq);

}
