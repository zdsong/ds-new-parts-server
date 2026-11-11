package jp.newpart.externalwarehouse.biz.service.impl;

import jp.newpart.common.biz.constants.ExternalWarehouseConstants;
import jp.newpart.common.biz.domain.entity.BssAskMessageEntity;
import jp.newpart.common.biz.domain.entity.BssAskMessageTitleEntity;
import jp.newpart.common.biz.enums.BssMessageType;
import jp.newpart.common.biz.mapper.BssAskMessageTitleBaseMapper;
import jp.newpart.common.biz.service.IBssAskMessageBaseService;
import jp.newpart.common.core.enums.DelFlgEnum;
import jp.newpart.common.enums.MessageStatusEnum;
import jp.newpart.common.enums.SystemFlgEnum;
import jp.newpart.common.utils.DateUtils;
import jp.newpart.common.utils.SecurityUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageReadBo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageSearchBo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageSendBo;
import jp.newpart.externalwarehouse.biz.domain.vo.BssAskMessageVo;
import jp.newpart.externalwarehouse.biz.mapper.BssAskMessageMapper;
import jp.newpart.externalwarehouse.biz.mapper.BssAskMessageTitleMapper;
import jp.newpart.externalwarehouse.biz.service.IBssAskMessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * メッセージ Service業務処理
 *
 * @author nextosd
 */
@Service
public class BssAskMessageServiceImpl implements IBssAskMessageService {

    @Autowired
    private IBssAskMessageBaseService askMessageBaseService;

    @Autowired
    private BssAskMessageMapper bssAskMessageMapper;

    @Autowired
    private BssAskMessageTitleBaseMapper bssAskMessageTitleBaseMapper;

    @Autowired
    private BssAskMessageTitleMapper bssAskMessageTitleMapper;
    /**
     * 未読メッセージ数取得
     *
     * @param messageSearchBo メッセージパラメータ対象
     *
     * @return 未読数
     */
    @Override
    public int getUnReadMessageCnt(BssAskMessageSearchBo messageSearchBo) {
        messageSearchBo.setFarmerId(SecurityUtils.getLoginUser().getUsername());
        return bssAskMessageMapper.selectUnReadMessageCnt(messageSearchBo);
    }

    /**
     * メッセージの履歴を取得
     *
     * @param messageSearchBo メッセージパラメータ対象
     * @return メッセージ履歴
     */
    @Override
    public List<BssAskMessageVo> getMessageHistory(BssAskMessageSearchBo messageSearchBo) {
        messageSearchBo.setFarmerId(SecurityUtils.getLoginUser().getUsername());
        return bssAskMessageMapper.selectAskMessageList(messageSearchBo);
    }

    /**
     * コールセンター メール送信
     *
     * @param sendMessageBo サービスパラメータ対象
     * @return  結果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int sendMessage(BssAskMessageSendBo sendMessageBo) {
        // システム送信時間
        String sysSendTime = DateUtils.getTime();

        BssAskMessageEntity bssAskMessage = new BssAskMessageEntity();
        BeanUtils.copyProperties(sendMessageBo, bssAskMessage);
        bssAskMessage.setId(IdUtils.nextIdStr());
        // メッセージの種類 = 農家
        bssAskMessage.setMessageType(BssMessageType.FARMER.getCode());
        // 農家ID
        bssAskMessage.setFarmerId(SecurityUtils.getLoginUser().getUsername());
        // ログインユーザー情報を取得します
        bssAskMessage.setSendUser(SecurityUtils.getLoginUser().getUsername());
        // 送信時間
        bssAskMessage.setSendTime(DateUtils.getTime());
        bssAskMessage.setCreatePgId(sendMessageBo.getPgId());
        bssAskMessage.setUpdatePgId(sendMessageBo.getPgId());

        // 既読
        BssAskMessageReadBo askMessageReadBo = new BssAskMessageReadBo();
        askMessageReadBo.setOrderId(sendMessageBo.getOrderId());
        askMessageReadBo.setPgId(sendMessageBo.getPgId());
        this.readMessage(askMessageReadBo);

        BssAskMessageTitleEntity searchBean = new BssAskMessageTitleEntity();
        searchBean.setFarmerId(SecurityUtils.getLoginUser().getUsername());
        searchBean.setOrderId(ValueUtils.isEmpty(sendMessageBo.getOrderId()) ? null : sendMessageBo.getOrderId());
        List<BssAskMessageTitleEntity> bssAskMessageTitleList = bssAskMessageTitleMapper.selectAskMessageTitleListForCheck(searchBean);
        if (!CollectionUtils.isEmpty(bssAskMessageTitleList)) {
            for (BssAskMessageTitleEntity bssAskMessageTitleEntity : bssAskMessageTitleList) {
                // セッションが終了したら、新しいシステムメッセージを追加します。
                if( ValueUtils.isEqual(MessageStatusEnum.RESOLVED.getCode() ,bssAskMessageTitleList.get(0).getStatus())) {
                    BssAskMessageEntity bssSysAskMessage = new BssAskMessageEntity();
                    BeanUtils.copyProperties( bssAskMessage ,bssSysAskMessage);
                    bssSysAskMessage.setSendTime(sysSendTime);
                    // 追加システムメッセージ
                    this.insertSystemMessage(bssSysAskMessage, SecurityUtils.getLoginUser().getNickName());
                }

                bssAskMessageTitleEntity.setSendTime(bssAskMessage.getSendTime());
                bssAskMessageTitleEntity.setStatus(MessageStatusEnum.PROCESS.getCode());
                bssAskMessageTitleBaseMapper.update(bssAskMessageTitleEntity);
            }
        } else {
            BssAskMessageTitleEntity bssAskMessageTitle = new BssAskMessageTitleEntity();
            BeanUtils.copyProperties(sendMessageBo, bssAskMessageTitle);
            bssAskMessageTitle.setId(IdUtils.nextIdStr());
            bssAskMessageTitle.setStatus(MessageStatusEnum.PROCESS.getCode());
            bssAskMessageTitle.setFarmerId(SecurityUtils.getLoginUser().getUsername());
            bssAskMessageTitle.setOrderId(ValueUtils.isEmpty(sendMessageBo.getOrderId()) ? null : sendMessageBo.getOrderId());
            bssAskMessageTitle.setCreatePgId(sendMessageBo.getPgId());
            bssAskMessageTitle.setUpdatePgId(sendMessageBo.getPgId());
            bssAskMessageTitle.setSendTime(bssAskMessage.getSendTime());
            bssAskMessageTitleBaseMapper.insert(bssAskMessageTitle);
        }

        return askMessageBaseService.insert(bssAskMessage);
    }


    /**
     * コールセンター メール既読メッセージ
     *
     * @param readMessageBo サービスパラメータ対象
     * @return  結果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int readMessage(BssAskMessageReadBo readMessageBo) {

        BssAskMessageEntity message = new BssAskMessageEntity();
        BeanUtils.copyProperties(readMessageBo, message);

        message.setReadStatus(DelFlgEnum.ON.getCode());
        message.setReadUser(SecurityUtils.getLoginUser().getUsername());
        message.setReceptionTime(DateUtils.getTime());
        message.setFarmerId(SecurityUtils.getLoginUser().getUsername());
        message.setUpdatePgId(readMessageBo.getPgId());

        return  bssAskMessageMapper.readMessage(message);
    }

    /**
     * 追加システムメッセージ
     *
     * @param bssAskMessageParams メッセージ
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void insertSystemMessage(BssAskMessageEntity bssAskMessageParams,String sendUserDesc) {

        BssAskMessageEntity bssAskMessage = new BssAskMessageEntity();
        BeanUtils.copyProperties(bssAskMessageParams, bssAskMessage);
        bssAskMessage.setId(IdUtils.nextIdStr());
        // メッセージの種類 = CRM
        bssAskMessage.setMessageType(BssMessageType.FARMER.getCode());
        // ログインユーザー情報を取得します
        bssAskMessage.setSendUser(bssAskMessageParams.getSendUser());
        // 既読者
        bssAskMessage.setReadUser(bssAskMessageParams.getSendUser());
        // 既読ステータス
        bssAskMessage.setReadStatus(DelFlgEnum.ON.getCode());
        // 送信時間
        bssAskMessage.setSendTime(bssAskMessageParams.getSendTime());
        // システムメッセージフラグ
        bssAskMessage.setSysMessageFlg(SystemFlgEnum.SYS.getCode());
        // メッセージ設定
        bssAskMessage.setMessages(StringUtils.join(sendUserDesc, ExternalWarehouseConstants.BSS_MESSAGE_BEGIN_LABEL));

        // 登録プログラムＩＤ
        bssAskMessage.setCreatePgId(bssAskMessageParams.getCreatePgId());
        // 更新プログラムＩＤ
        bssAskMessage.setUpdatePgId(bssAskMessageParams.getUpdatePgId());

        askMessageBaseService.insert(bssAskMessage);
    }
}
