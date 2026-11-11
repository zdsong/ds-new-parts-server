package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssOrderEntity;

import java.util.List;


/**
 * 注文情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssOrderBaseMapper {

     /**
     *注文情報を検索
     *
     * @param id 注文情報プライマリ・キー
     * @return 注文情報
     */
    public BssOrderEntity selectById(String id);

     /**
     *注文情報を検索リスト
     *
     * @param bssOrder 注文情報
     * @return 注文情報リスト
     */
    public List<BssOrderEntity> selectList(BssOrderEntity bssOrder);

     /**
     *注文情報を登録
     *
     * @param bssOrder 注文情報
     * @return  結果
     */
    public int insert(BssOrderEntity bssOrder);

     /**
     *注文情報を修正
     *
     * @param bssOrder 注文情報
     * @return  結果
     */
    public int update(BssOrderEntity bssOrder);

     /**
     * 注文情報を削除
     *
     * @param id 注文情報プライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
