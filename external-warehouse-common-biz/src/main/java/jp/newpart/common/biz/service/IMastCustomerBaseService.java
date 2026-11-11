package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.MastCustomerEntity;
import java.util.List;


/**
 * 顧客マスタServiceインタフェース
 *
 * @author nextosd
 */
public interface IMastCustomerBaseService {

     /**
     * 顧客マスタを検索
     *
     * @param id 顧客マスタプライマリ・キー
     * @return 顧客マスタ
     */
    public MastCustomerEntity selectById(String id);

     /**
     * 顧客マスタを検索リスト
     *
     * @param mastCustomer 顧客マスタパラメータ対象
     * @return 顧客マスタリスト
     */
    public List<MastCustomerEntity> selectList(MastCustomerEntity mastCustomer);

     /**
     * 顧客マスタを登録
     *
     * @param mastCustomer 顧客マスタ
     * @return  結果
     */
    public int insert(MastCustomerEntity mastCustomer);

     /**
     * 顧客マスタを修正
     *
     * @param mastCustomer 顧客マスタ
     * @return  結果
     */
    public int update(MastCustomerEntity mastCustomer);

     /**
     * 顧客マスタを削除
     *
     * @param id 顧客マスタプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
