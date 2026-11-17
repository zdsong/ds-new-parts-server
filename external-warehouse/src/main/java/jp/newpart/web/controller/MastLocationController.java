package jp.newpart.web.controller;

import jp.newpart.common.annotation.Log;
import jp.newpart.common.biz.domain.entity.MastLocationEntity;
import jp.newpart.common.core.controller.BaseController;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.common.core.page.TableDataInfo;
import jp.newpart.common.enums.BusinessType;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.externalwarehouse.biz.domain.bo.MastLocationSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastLocationVo;
import jp.newpart.externalwarehouse.biz.service.IMastLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * ロケーションマスタ Controller
 *
 * @author nextosd
 */
@RestController
@RequestMapping("/api/master/location")
public class MastLocationController extends BaseController {

    @Autowired
    private IMastLocationService mastLocationService;

    /**
     * ロケーションマスタのリスト
     */
    @GetMapping("/list")
    public TableDataInfo list(MastLocationSearchBo searchBo) {
        startPage();
        List<MastLocationVo> list = mastLocationService.selectList(searchBo);
        return getDataTable(list);
    }

    /**
     * ロケーションマスタの詳細情報
     */
    @GetMapping(value = "/info/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(mastLocationService.selectById(id));
    }

    /**
     * CSVインポート
     *
     * @param file アップロードするファイル
     * @return アップロード結果
     */
    @Log(title = "ロケ番マスタ", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public AjaxResult importCsvData(@RequestParam("file") MultipartFile file) {
        List<ImportFileErrorVo> errorList = mastLocationService.importCsvData(file);

        if (!CollectionUtils.isEmpty(errorList)) {
            // I0004=取込に失敗しました。
            AjaxResult ajax = AjaxResult.success(MessageUtils.message("I0004"), "error");
            ajax.put("errorList", errorList);
            return ajax;
        }

        // I0003=取込に成功しました。
        return AjaxResult.success(MessageUtils.message("I0003"));
    }

    /**
     * ロケーションマスタを登録
     */
    @Log(title = "ロケ番マスタ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MastLocationEntity entity) {
        AjaxResult validateResult = validateLocation(entity);
        if (validateResult != null) {
            return validateResult;
        }

        // 同一倉庫・同一ロケ番の重複登録を防止する
        if (!mastLocationService.checkLocationUnique(entity.getWhCd(), entity.getLocCd(), null)) {
            return error(MessageUtils.message("E0012", MessageUtils.message("label.locationCd")));
        }

        Date now = new Date();
        String username = getUsername();
        entity.setId(IdUtils.nextIdStr());
        entity.setDeleteFlg("0");
        entity.setVersion(1);
        entity.setCreateTime(now);
        entity.setCreateBy(username);
        entity.setCreatePgId("P602");
        entity.setUpdateTime(now);
        entity.setUpdateBy(username);
        entity.setUpdatePgId("P602");

        int rows = mastLocationService.insertLocation(entity);
        return rows > 0 ? success(MessageUtils.message("I0001")) : error(MessageUtils.message("I0002"));
    }

    /**
     * ロケーションマスタを更新
     */
    @Log(title = "ロケ番マスタ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MastLocationEntity entity) {
        if (StringUtils.isEmpty(entity.getId()) || entity.getVersion() == null) {
            // 該当データは既に別のユーザーより更新されました。
            return error(MessageUtils.message("E0007"));
        }

        AjaxResult validateResult = validateLocation(entity);
        if (validateResult != null) {
            return validateResult;
        }

        // 自身を除いて同一倉庫・同一ロケ番の重複を防止する
        if (!mastLocationService.checkLocationUnique(entity.getWhCd(), entity.getLocCd(), entity.getId())) {
            return error(MessageUtils.message("E0012", MessageUtils.message("label.locationCd")));
        }

        entity.setUpdateTime(new Date());
        entity.setUpdateBy(getUsername());
        entity.setUpdatePgId("P603");

        int rows = mastLocationService.updateLocation(entity);
        if (rows == 0) {
            // 該当データは既に別のユーザーより更新されました。
            return error(MessageUtils.message("E0007"));
        }
        return success(MessageUtils.message("I0001"));
    }

    /**
     * ロケーションマスタを論理削除
     */
    @Log(title = "ロケ番マスタ", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody MastLocationEntity entity) {
        if (StringUtils.isEmpty(entity.getId()) || entity.getVersion() == null) {
            // 該当データは既に別のユーザーより更新されました。
            return error(MessageUtils.message("E0007"));
        }

        entity.setUpdateTime(new Date());
        entity.setUpdateBy(getUsername());
        entity.setUpdatePgId("P603");

        int rows = mastLocationService.deleteLocation(entity);
        if (rows == 0) {
            // 該当データは既に別のユーザーより更新されました。
            return error(MessageUtils.message("E0007"));
        }
        return success(MessageUtils.message("I0001"));
    }

    /**
     * 入力チェックを行う
     *
     * @param entity ロケーションマスタ
     * @return エラーがない場合は null
     */
    private AjaxResult validateLocation(MastLocationEntity entity) {
        if (StringUtils.isEmpty(entity.getWhCd())) {
            return error(MessageUtils.message("E0003", MessageUtils.message("label.warehouse")));
        }
        if (StringUtils.isEmpty(entity.getLocCd())) {
            return error(MessageUtils.message("E0003", MessageUtils.message("label.locationCd")));
        }

        // 前後の空白を除去して保存する
        entity.setWhCd(entity.getWhCd().trim());
        entity.setLocCd(entity.getLocCd().trim());
        if (entity.getMemo() != null) {
            entity.setMemo(entity.getMemo().trim());
        }
        return null;
    }
}
