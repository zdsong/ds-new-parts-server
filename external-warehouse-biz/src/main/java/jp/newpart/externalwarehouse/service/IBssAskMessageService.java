package jp.newpart.externalwarehouse.biz.service;

import jp.newpart.common.biz.domain.entity.BssAskMessageEntity;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageReadBo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageSearchBo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageSendBo;
import jp.newpart.externalwarehouse.biz.domain.vo.BssAskMessageVo;

import java.util.List;

/**
 * メッセージ Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssAskMessageService {

    /**
     * 未読メッセージ数取得
     *
     * @param messageSearchBo メッセージパラメータ対象
     *
     * @return 未読数
     */
    public int getUnReadMessageCnt(BssAskMessageSearchBo messageSearchBo);

    /**
     * メッセージの履歴を取得
     * @param messageSearchBo メッセージパラメータ対象
     * @return メッセージ履歴
     */
    public List<BssAskMessageVo> getMessageHistory(BssAskMessageSearchBo messageSearchBo);

    /**
     * コールセンター メール送信
     *
     * @param messageBo サービスパラメータ対象
     * @return  結果
     */
    public int sendMessage(BssAskMessageSendBo messageBo);

    /**
     * コールセンター メール既読メッセージ
     *
     * @param messageBo サービスパラメータ対象
     * @return  結果
     */
    public int readMessage(BssAskMessageReadBo messageBo);

    /**
     * 追加システムメッセージ
     *
     * @param bssAskMessageParams メッセージ
     *
     */
    public void insertSystemMessage(BssAskMessageEntity bssAskMessageParams, String sendUserDesc);
}
