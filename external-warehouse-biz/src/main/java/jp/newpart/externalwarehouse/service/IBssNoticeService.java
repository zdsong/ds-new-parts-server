package jp.newpart.externalwarehouse.biz.service;

import jp.newpart.externalwarehouse.biz.domain.vo.BssNoticeVo;

import java.util.List;

/**
 * お知らせ Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssNoticeService {

    /**
     * 未読お知らせ数取得
     *
     * @return
     */
    int getUnReadNoticeCnt();

    /**
     * お知らせ未読情報を検索リスト
     *
     * @return お知らせ情報リスト
     */
    List<BssNoticeVo> getUnReadNoticeList();

    /**
     * お知らせ既読
     *
     * @param id お知らせID
     * @return  結果
     */
     int readNotice(String id);
}
