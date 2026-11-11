package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssAskMessageEntity;
import jp.newpart.externalwarehouse.biz.domain.vo.BssAskMessageVo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageSearchBo;

import java.util.List;

/**
 * メッセージ情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssAskMessageMapper {

    /**
     * 未読メッセージ数取得
     *
     * @param messageSearchBo メッセージパラメータ情報
     *
     * @return
     */
    public int selectUnReadMessageCnt(BssAskMessageSearchBo messageSearchBo);

    /**
     * メッセージ情報を検索リスト
     *
     * @param messageSearchBo メッセージパラメータ情報
     * @return メッセージ情報リスト
     */
    public List<BssAskMessageVo> selectAskMessageList(BssAskMessageSearchBo messageSearchBo);

    /**
     *
     * @param bssAskMessageBo サービスパラメータ対象
     * @return 結果
     */
    public int readMessage(BssAskMessageEntity bssAskMessageBo);

}
