package jp.newpart.web.controller;

import jp.newpart.common.annotation.Log;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.externalwarehouse.biz.service.IBssNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * お知らせ
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/notice")
public class NoticeController extends BaseController {

    @Autowired
    private IBssNoticeService noticeService;

    /**
     * 未読数取得
     */
    @GetMapping(value = "/unReadCnt")
    public AjaxResult getUnReadCnt() {
        return success(noticeService.getUnReadNoticeCnt());
    }

    /**
     * お知らせ未読情報リストを検索
     */
    @GetMapping(value = "/list")
    public AjaxResult getUnReadNoticeList() {
        return success(noticeService.getUnReadNoticeList());
    }

    /**
     * お知らせ既読
     *
     */
    @Log(title = "新産直くん お知らせ 既読", businessType = BusinessType.INSERT)
    @PostMapping("/read/{id}")
    public AjaxResult read(@PathVariable("id") String id) {
        // ID
        if (ValueUtils.isEmpty(id)) {
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.noticeId")));
        }
        return success(noticeService.readNotice(id));
    }

}
