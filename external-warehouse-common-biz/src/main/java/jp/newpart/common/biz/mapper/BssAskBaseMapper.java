package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssAskEntity;

import java.util.List;


/**
 * 問合せ情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssAskBaseMapper {

     /**
     *問合せ情報を検索
     *
     * @param id 問合せ情報プライマリ・キー
     * @return 問合せ情報
     */
    public BssAskEntity selectById(String id);

     /**
     *問合せ情報を検索リスト
     *
     * @param bssAsk 問合せ情報
     * @return 問合せ情報リスト
     */
    public List<BssAskEntity> selectList(BssAskEntity bssAsk);

     /**
     *問合せ情報を登録
     *
     * @param bssAsk 問合せ情報
     * @return  結果
     */
    public int insert(BssAskEntity bssAsk);

     /**
     *問合せ情報を修正
     *
     * @param bssAsk 問合せ情報
     * @return  結果
     */
    public int update(BssAskEntity bssAsk);

     /**
     * 問合せ情報を削除
     *
     * @param id 問合せ情報プライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
