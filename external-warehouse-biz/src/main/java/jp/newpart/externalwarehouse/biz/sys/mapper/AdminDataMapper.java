package jp.newpart.externalwarehouse.biz.sys.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 管理画面用の参照マッパー
 *
 * @author nextosd
 */
@Mapper
public interface AdminDataMapper
{
    /**
     * カテゴリ一覧を取得する
     *
     * @return カテゴリ一覧
     */
    @Select("select id, category_id, parent_category_id, category_type, category_name, discontinued_flg, create_time from mast_item_category order by id")
    List<Map<String, Object>> selectCategoryList();

    /**
     * 農家メッセージ一覧を取得する
     *
     * @return メッセージ一覧
     */
    @Select("select id, farmer_id, messages, send_user, send_time, read_status, message_type, order_id, create_time from bss_ask_message order by create_time desc limit 100")
    List<Map<String, Object>> selectMessageList();
}
