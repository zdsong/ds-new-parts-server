package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssNoticeEntity;

import java.util.List;


/**
 * お知らせ Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssNoticeBaseMapper {

     /**
     *お知らせを検索
     *
     * @param id お知らせプライマリ・キー
     * @return お知らせ
     */
    public BssNoticeEntity selectById(String id);

     /**
     *お知らせを検索リスト
     *
     * @param bssNotice お知らせ
     * @return お知らせリスト
     */
    public List<BssNoticeEntity> selectList(BssNoticeEntity bssNotice);

     /**
     *お知らせを登録
     *
     * @param bssNotice お知らせ
     * @return  結果
     */
    public int insert(BssNoticeEntity bssNotice);

     /**
     *お知らせを修正
     *
     * @param bssNotice お知らせ
     * @return  結果
     */
    public int update(BssNoticeEntity bssNotice);

     /**
     * お知らせを削除
     *
     * @param id お知らせプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
