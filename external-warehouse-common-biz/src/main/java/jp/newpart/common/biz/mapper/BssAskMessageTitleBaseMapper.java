package jp.newpart.common.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssAskMessageTitleEntity;

import java.util.List;


/**
 * メッセージのタイトル Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssAskMessageTitleBaseMapper {

     /**
     *メッセージのタイトルを検索
     *
     * @param id メッセージのタイトルプライマリ・キー
     * @return メッセージのタイトル
     */
    public BssAskMessageTitleEntity selectById(String id);

     /**
     *メッセージのタイトルを検索リスト
     *
     * @param bssAskMessageTitle メッセージのタイトル
     * @return メッセージのタイトルリスト
     */
    public List<BssAskMessageTitleEntity> selectList(BssAskMessageTitleEntity bssAskMessageTitle);

     /**
     *メッセージのタイトルを登録
     *
     * @param bssAskMessageTitle メッセージのタイトル
     * @return  結果
     */
    public int insert(BssAskMessageTitleEntity bssAskMessageTitle);

     /**
     *メッセージのタイトルを修正
     *
     * @param bssAskMessageTitle メッセージのタイトル
     * @return  結果
     */
    public int update(BssAskMessageTitleEntity bssAskMessageTitle);

     /**
     * メッセージのタイトルを削除
     *
     * @param id メッセージのタイトルプライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

}
