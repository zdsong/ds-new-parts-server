package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.bo.OptionsParamBo;

import jp.newpart.common.biz.domain.vo.OptionsKeyValueVo;

import java.util.List;

/**
 * 共通 Serviceインタフェース
 *
 * @author nextosd
 */
public interface IExternalWarehouseCommonService {

    /**
     * コンボボックスデータを取得する
     *
     * @param optionsParam コンボボックスパラメータ
     * @return コンボボックスリスト
     */
    public List<OptionsKeyValueVo> getCommonOptions(OptionsParamBo optionsParam);


    /**
     * コンボボックスデータ(倉庫)を取得する
     *
     * @param userName コンボボックスパラメータ
     * @return コンボボックスリスト
     */
    public List<OptionsKeyValueVo> getWarehouseCommonOptions(String userName);

    /**
     * コンボボックスデータ(倉庫ロケーション)を取得する
     *
     * @param whCd 倉庫コード
     * @return コンボボックスリスト
     */
    public List<OptionsKeyValueVo> getLocationCommonOptions(String whCd);


    /**
     * パレット番号の生成
     *
     * @return パレット番号
     */
    public String generatePalletNo();
}
