package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssAskMessageEntity;

import java.util.List;


/**
 * 問合せメッセージ Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssAskMessageBaseMapper {

     /**
     *問合せメッセージを検索
     *
     * @param id 問合せメッセージプライマリ・キー
     * @return 問合せメッセージ
     */
    public BssAskMessageEntity selectById(String id);

     /**
     *問合せメッセージを検索リスト
     *
     * @param bssAskMessage 問合せメッセージ
     * @return 問合せメッセージリスト
     */
    public List<BssAskMessageEntity> selectList(BssAskMessageEntity bssAskMessage);

     /**
     *問合せメッセージを登録
     *
     * @param bssAskMessage 問合せメッセージ
     * @return  結果
     */
    public int insert(BssAskMessageEntity bssAskMessage);

     /**
     *問合せメッセージを修正
     *
     * @param bssAskMessage 問合せメッセージ
     * @return  結果
     */
    public int update(BssAskMessageEntity bssAskMessage);

     /**
     * 問合せメッセージを削除
     *
     * @param id 問合せメッセージプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
