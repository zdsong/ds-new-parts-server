package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.externalwarehouse.biz.domain.vo.BssNoticeVo;

import java.util.List;

/**
 * お知らせ情報 mapperインタフェース
 *
 * @author nextosd
 */
public interface BssNoticeMapper {

    /**
     * 未読お知らせ数取得
     *
     * @param farmerId 農家ID
     *
     * @return
     */
     int selectUnReadNoticeCnt(String farmerId);

    /**
     * お知らせ情報を検索リスト
     *
     * @param farmerId 農家ID
     * @return お知らせ情報リスト
     */
     List<BssNoticeVo> selectUnReadNoticeList(String farmerId);

    /**
     * お知らせを検索
     *
     * @param id お知らせプライマリ・キー
     * @return お知らせ
     */
    BssNoticeVo selectById(String id);
}
