package jp.newpart.externalwarehouse.biz.service;

import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.externalwarehouse.biz.domain.bo.MastCustomerSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastCustomerVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 顧客マスタ Serviceインタフェース
 *
 * @author nextosd
 */
public interface IMastCustomerService {

    /**
     * 顧客マスタの検索
     *
     * @param id 顧客マスタプライマリ・キー
     * @return 顧客マスタ
     */
     MastCustomerVo selectCustomerById(String id);

    /**
     * 顧客マスタの検索
     *
     * @param customerId 顧客ID
     * @return 顧客マスタ
     */
    MastCustomerVo selectCustomerByCustomerId(String customerId);

    /**
     * 顧客マスタのリストを検索
     *
     * @param mastCustomerBo 顧客マスタパラメータ対象
     * @return 顧客マスタリスト
     */
     List<MastCustomerVo> selectCustomerList(MastCustomerSearchBo mastCustomerBo);

    /**
     * 顧客情報を取り込む
     *
     * @param csvFile エクセルファイル
     * @return エラー情報のリスト
     */
     List<ImportFileErrorVo> importCsvData(MultipartFile csvFile);
}
