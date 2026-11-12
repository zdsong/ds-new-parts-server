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
}
