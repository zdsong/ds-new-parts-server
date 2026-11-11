package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.MastFarmerEntity;
import java.util.List;


/**
 * 農家マスタServiceインタフェース
 *
 * @author nextosd
 */
public interface IMastFarmerBaseService {

     /**
     * 農家マスタを検索
     *
     * @param id 農家マスタプライマリ・キー
     * @return 農家マスタ
     */
    public MastFarmerEntity selectById(String id);

     /**
     * 農家マスタを検索リスト
     *
     * @param mastFarmer 農家マスタパラメータ対象
     * @return 農家マスタリスト
     */
    public List<MastFarmerEntity> selectList(MastFarmerEntity mastFarmer);

     /**
     * 農家マスタを登録
     *
     * @param mastFarmer 農家マスタ
     * @return  結果
     */
    public int insert(MastFarmerEntity mastFarmer);

     /**
     * 農家マスタを修正
     *
     * @param mastFarmer 農家マスタ
     * @return  結果
     */
    public int update(MastFarmerEntity mastFarmer);

     /**
     * 農家マスタを削除
     *
     * @param id 農家マスタプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
