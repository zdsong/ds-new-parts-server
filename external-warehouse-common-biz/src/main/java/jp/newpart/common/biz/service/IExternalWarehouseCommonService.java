package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.bo.OptionsParamBo;
import jp.newpart.common.biz.domain.entity.MastPostalCodeAddressEntity;
import jp.newpart.common.biz.domain.vo.OptionsAddressVo;
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
     * コンボボックスデータを取得する
     *
     * @param optionsParam コンボボックスパラメータ
     * @return コンボボックスリスト
     */
    public List<OptionsAddressVo> selectAddressOptionsList(OptionsParamBo optionsParam);

    /**
     * 郵便番号を使用して住所を検索します。
     *
     * @param postalCode 郵便番号
     * @return アドレス
     */
    public MastPostalCodeAddressEntity selectMastAddressByPostalCode(String postalCode);

}
