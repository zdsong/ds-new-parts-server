package jp.newpart.externalwarehouse.biz.service;

import jp.newpart.common.biz.domain.entity.MastLocationEntity;
import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.externalwarehouse.biz.domain.bo.MastLocationSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.MastLocationVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ロケーションマスタ Serviceインタフェース
 *
 * @author nextosd
 */
public interface IMastLocationService {

    /**
     * ロケーションマスタを検索リスト
     *
     * @param searchBo 検索パラメータ
     * @return ロケーションマスタリスト
     */
    List<MastLocationVo> selectList(MastLocationSearchBo searchBo);

    /**
     * ロケーションマスタを検索
     *
     * @param id ロケーションマスタプライマリ・キー
     * @return ロケーションマスタ
     */
    MastLocationVo selectById(String id);

    /**
     * 同一の倉庫コード・ロケーションコードが存在するか確認する
     *
     * @param whCd      倉庫コード
     * @param locCd     ロケーションコード
     * @param excludeId 除外するロケーションマスタID（更新時）
     * @return true:重複なし / false:重複あり
     */
    boolean checkLocationUnique(String whCd, String locCd, String excludeId);

    /**
     * ロケーションマスタを登録
     *
     * @param entity ロケーションマスタ
     * @return 結果
     */
    int insertLocation(MastLocationEntity entity);

    /**
     * ロケーションマスタを修正
     *
     * @param entity ロケーションマスタ
     * @return 結果
     */
    int updateLocation(MastLocationEntity entity);

    /**
     * ロケーションマスタを論理削除
     *
     * @param entity ロケーションマスタ
     * @return 結果
     */
    int deleteLocation(MastLocationEntity entity);

    /**
     * CSVインポートを実行する
     *
     * @param csvFile アップロードされたCSVファイル
     * @return エラー情報リスト（エラーがない場合は空）
     */
    List<ImportFileErrorVo> importCsvData(MultipartFile csvFile);
}
