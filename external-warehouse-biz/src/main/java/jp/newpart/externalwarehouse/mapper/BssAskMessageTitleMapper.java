package jp.newpart.externalwarehouse.biz.mapper;

import jp.newpart.common.biz.domain.entity.BssAskMessageEntity;
import jp.newpart.common.biz.domain.entity.BssAskMessageTitleEntity;
import jp.newpart.externalwarehouse.biz.domain.bo.BssAskMessageSearchBo;
import jp.newpart.externalwarehouse.biz.domain.vo.BssAskMessageVo;

import java.util.List;

/**
 * メッセージ情報 Mapperインタフェース
 *
 * @author nextosd
 */
public interface BssAskMessageTitleMapper {

    /**
     *メッセージのタイトルを検索リスト
     *
     * @param bssAskMessageTitle メッセージのタイトル
     * @return メッセージのタイトルリスト
     */
    public List<BssAskMessageTitleEntity> selectAskMessageTitleListForCheck(BssAskMessageTitleEntity bssAskMessageTitle);

}
