package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.MastFarmerRakuuruCartEntity;

import java.util.List;


/**
 * 農家らくうるカートマスタServiceインタフェース
 *
 * @author nextosd
 */
public interface IMastFarmerRakuuruCartBaseService {

     /**
     * 農家らくうるカートマスタを検索
     *
     * @param id 農家らくうるカートマスタプライマリ・キー
     * @return 農家らくうるカートマスタ
     */
    public MastFarmerRakuuruCartEntity selectById(String id);

     /**
     * 農家らくうるカートマスタを検索リスト
     *
     * @param mastFarmerRakuuruCart 農家らくうるカートマスタパラメータ対象
     * @return 農家らくうるカートマスタリスト
     */
    public List<MastFarmerRakuuruCartEntity> selectList(MastFarmerRakuuruCartEntity mastFarmerRakuuruCart);

     /**
     * 農家らくうるカートマスタを登録
     *
     * @param mastFarmerRakuuruCart 農家らくうるカートマスタ
     * @return  結果
     */
    public int insert(MastFarmerRakuuruCartEntity mastFarmerRakuuruCart);

     /**
     * 農家らくうるカートマスタを修正
     *
     * @param mastFarmerRakuuruCart 農家らくうるカートマスタ
     * @return  結果
     */
    public int update(MastFarmerRakuuruCartEntity mastFarmerRakuuruCart);

     /**
     * 農家らくうるカートマスタを削除
     *
     * @param id 農家らくうるカートマスタプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
