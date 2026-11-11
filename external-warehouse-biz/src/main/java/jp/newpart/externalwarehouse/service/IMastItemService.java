package jp.newpart.externalwarehouse.biz.service;

import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastItemVo;
import jp.newpart.externalwarehouse.biz.domain.bo.MastItemSearchBo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商品マスタ Serviceインタフェース
 *
 * @author nextosd
 */
public interface IMastItemService {


    /**
     * 商品マスタを検索
     *
     * @param itemId 商品ID
     * @return 商品マスタ
     */
    MastItemVo selectByItemId(String itemId);

    /**
     * 商品マスタを検索リスト
     *
     * @param mastItemSearchBo 検索パラメータ対象
     * @return 商品マスタリスト
     */
     List<MastItemVo> selectList(MastItemSearchBo mastItemSearchBo);

    /**
     * 商品情報を取り込む
     *
     * @param csvFile エクセルファイル
     * @return エラー情報のリスト
     */
     List<ImportFileErrorVo> importCsvData(MultipartFile csvFile);
}
