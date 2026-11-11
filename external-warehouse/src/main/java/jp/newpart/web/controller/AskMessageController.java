package jp.newpart.web.controller;

import jp.newpart.common.annotation.Log;
import jp.newpart.common.biz.enums.ExternalWarehousePgIdEnum;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.common.validator.utils.ValidatorGroup;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageReadBo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageSearchBo;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageSendBo;
import jp.newpart.externalwarehouse.biz.service.IBssAskMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 農家メッセージ Controller
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/ask/message")
public class AskMessageController extends BaseController {

    @Autowired
    private IBssAskMessageService messageService;

    /**
     * 未読メッセージ数取得
     */
    @GetMapping(value = "/unReadCnt")
    public AjaxResult getUnReadMessageCnt() {
        BssAskMessageSearchBo messageSearchBo = new BssAskMessageSearchBo();
        return success(messageService.getUnReadMessageCnt(messageSearchBo));
    }

    /**
     * メッセージの履歴を取得
     *
     */
    @GetMapping(value = "/list")
    public AjaxResult getMessageHistory() {
        BssAskMessageSearchBo messageSearchBo = new BssAskMessageSearchBo();
        return success(messageService.getMessageHistory(messageSearchBo));
    }

    /**
     * メッセージの履歴を取得(注文)
     *
     * @param orderId 注文ID
     */
    @GetMapping(value = "/list/{orderId}")
    public AjaxResult getMessageHistoryByOrderId(@PathVariable("orderId") String orderId) {
        // 注文ID
        if (ValueUtils.isEmpty(orderId)) {
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.orderId")));
        }
        BssAskMessageSearchBo messageSearchBo = new BssAskMessageSearchBo();
        messageSearchBo.setOrderId(orderId);
        return success(messageService.getMessageHistory(messageSearchBo));
    }

    /**
     * メッセージ送信
     *
     */
    @Log(title = "新産直くん メッセージ 送信", businessType = BusinessType.INSERT)
    @PostMapping("/send")
    public AjaxResult send(@RequestBody @Validated({ValidatorGroup.AddGroup.class}) BssAskMessageSendBo bssAskMessageSendBo) {
        bssAskMessageSendBo.setPgId(ExternalWarehousePgIdEnum.N019.getCode());
        return success(messageService.sendMessage(bssAskMessageSendBo));
    }

    /**
     * メッセージ送信(注文)
     *
     * @param orderId 注文ID
     */
    @Log(title = "新産直くん メッセージ 送信 注文", businessType = BusinessType.INSERT)
    @PostMapping("/send/{orderId}")
    public AjaxResult sendByOrderId(@PathVariable("orderId") String orderId, @RequestBody @Validated({ValidatorGroup.AddGroup.class}) BssAskMessageSendBo bssAskMessageSendBo) {
        // 注文ID
        if (ValueUtils.isEmpty(orderId)) {
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.orderId")));
        }

        bssAskMessageSendBo.setOrderId(orderId);
        bssAskMessageSendBo.setPgId(ExternalWarehousePgIdEnum.N025.getCode());
        return success(messageService.sendMessage(bssAskMessageSendBo));
    }

    /**
     * メッセージ既読
     *
     */
    @Log(title = "新産直くん メッセージ 既読", businessType = BusinessType.INSERT)
    @PostMapping("/read")
    public AjaxResult read() {
        BssAskMessageReadBo bssAskMessageReadBo = new BssAskMessageReadBo();
        bssAskMessageReadBo.setPgId(ExternalWarehousePgIdEnum.N019.getCode());
        return success(messageService.readMessage(bssAskMessageReadBo));
    }

    /**
     * メッセージ既読(注文)
     *
     * @param orderId 注文ID
     */
    @Log(title = "新産直くん メッセージ 既読 注文", businessType = BusinessType.INSERT)
    @PostMapping("/read/{orderId}")
    public AjaxResult readByOrderId(@PathVariable("orderId") String orderId) {
        // 注文ID
        if (ValueUtils.isEmpty(orderId)) {
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.orderId")));
        }
        BssAskMessageReadBo bssAskMessageReadBo = new BssAskMessageReadBo();
        bssAskMessageReadBo.setOrderId(orderId);
        bssAskMessageReadBo.setPgId(ExternalWarehousePgIdEnum.N025.getCode());
        return success(messageService.readMessage(bssAskMessageReadBo));
    }
}
