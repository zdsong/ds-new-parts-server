package jp.newpart.externalwarehouse.biz.service;

import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.externalwarehouse.biz.domain.bo.MastItemSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastItemVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 品目マスタ Serviceインタフェース
 *
 * @author nextosd
 */
public interface IMastItemService {

    /**
     * 品目コードで品目マスタを検索
     *
     * @param itemCode 品目コード
     * @return 品目マスタ
     */
    MastItemVo selectByItemCode(String itemCode);

    /**
     * 品目マスタを検索リスト
     *
     * @param mastItemSearchBo 検索パラメータ対象
     * @return 品目マスタリスト
     */
    List<MastItemVo> selectList(MastItemSearchBo mastItemSearchBo);

    /**
     * 品目情報を取り込む
     *
     * @param csvFile CSVファイル
     * @return エラー情報のリスト
     */
    List<ImportFileErrorVo> importCsvData(MultipartFile csvFile);
}
