package jp.nasta.pms.web.controller;

import jp.nasta.pms.common.annotation.Log;
import jp.nasta.pms.common.core.controller.BaseController;
import jp.nasta.pms.common.core.domain.AjaxResult;
import jp.nasta.pms.common.core.page.TableDataInfo;
import jp.nasta.pms.common.enums.BusinessType;
import jp.nasta.pms.common.utils.MessageUtils;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationBatchSaveBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationSaveBo;
import jp.nasta.pms.externalwarehouse.biz.domain.bo.MastUserRelationSearchBo;
import jp.nasta.pms.externalwarehouse.biz.domain.vo.MastUserRelationVo;
import jp.nasta.pms.externalwarehouse.biz.service.IMastUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ユーザー倉庫関係 Controller
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/master/userRelation")
public class MastUserRelationController extends BaseController {

    @Autowired
    private IMastUserRelationService mastUserRelationService;

    /**
     * ユーザー倉庫関係の一覧
     */
    @GetMapping("/list")
    public TableDataInfo list(MastUserRelationSearchBo searchBo) {
        List<MastUserRelationVo> list = mastUserRelationService.selectList(searchBo);
        return getDataTable(list);
    }

    /**
     * ユーザー選択リスト
     */
    @GetMapping("/users")
    public AjaxResult users() {
        return success(mastUserRelationService.selectUserOptions());
    }

    /**
     * 保管倉庫の選択リスト
     */
    @GetMapping("/warehouses")
    public AjaxResult warehouses() {
        return success(mastUserRelationService.selectWarehouseOptions());
    }

    /**
     * 関係タイプを保存する
     */
    @Log(title = "ユーザー倉庫関係", businessType = BusinessType.SAVE)
    @PostMapping
    public AjaxResult save(@RequestBody MastUserRelationSaveBo saveBo) {
        mastUserRelationService.save(saveBo);
        return success(MessageUtils.message("I0001"));
    }

    /**
     * 選択したユーザーと倉庫へ関係タイプを一括設定する
     */
    @Log(title = "ユーザー倉庫関係", businessType = BusinessType.SAVE)
    @PostMapping("/batch")
    public AjaxResult batch(@RequestBody MastUserRelationBatchSaveBo batchBo) {
        mastUserRelationService.saveBatch(batchBo);
        return success(MessageUtils.message("I0001"));
    }

    /**
     * ユーザーと倉庫の関係を論理削除する
     */
    @Log(title = "ユーザー倉庫関係", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody List<MastUserRelationSaveBo> targets) {
        mastUserRelationService.delete(targets);
        return success(MessageUtils.message("I0001"));
    }
}
