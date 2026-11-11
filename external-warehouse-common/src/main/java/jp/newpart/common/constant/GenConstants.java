package jp.newpart.common.constant;

/**
 * コード生成共通定数
 * 
 * @author nextosd
 */
public class GenConstants
{
    /** シングルテーブル（CRUD） */
    public static final String TPL_CRUD = "crud";

    /** Tree Table（CRUD） */
    public static final String TPL_TREE = "tree";

    /** マスタサブテーブル（CRUD） */
    public static final String TPL_SUB = "sub";

    /** Tree Code */
    public static final String TREE_CODE = "treeCode";

    /** Tree Parent Code */
    public static final String TREE_PARENT_CODE = "treeParentCode";

    /** Tree Name */
    public static final String TREE_NAME = "treeName";

    /** Parent Menu Id */
    public static final String PARENT_MENU_ID = "parentMenuId";

    /** Parent Menu Name */
    public static final String PARENT_MENU_NAME = "parentMenuName";

    /** データベース文字列タイプ */
    public static final String[] COLUMNTYPE_STR = { "char", "varchar", "nvarchar", "varchar2" };

    /** データベース・テキスト・タイプ */
    public static final String[] COLUMNTYPE_TEXT = { "tinytext", "text", "mediumtext", "longtext" };

    /** データベース時間タイプ */
    public static final String[] COLUMNTYPE_TIME = { "datetime", "time", "date", "timestamp" };

    /** データベースの数値タイプ */
    public static final String[] COLUMNTYPE_NUMBER = { "tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal" };

    /** ページに編集フィールドは必要ありません */
    public static final String[] COLUMNNAME_NOT_EDIT = { "id", "create_by", "create_time", "del_flag" };

    /** ページに表示する必要のないリストフィールド */
    public static final String[] COLUMNNAME_NOT_LIST = { "id", "create_by", "create_time", "del_flag", "update_by",
            "update_time" };

    /** ページにクエリーフィールドは必要ありません */
    public static final String[] COLUMNNAME_NOT_QUERY = { "id", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "remark" };

    /** Entity 基本クラスフィールド */
    public static final String[] BASE_ENTITY = { "createBy", "createTime", "updateBy", "updateTime", "remark" };

    /** Tree基本クラスフィールド */
    public static final String[] TREE_ENTITY = { "parentName", "parentId", "orderNum", "ancestors", "children" };

    /** テキストボックス */
    public static final String HTML_INPUT = "input";

    /** テキスト領域 */
    public static final String HTML_TEXTAREA = "textarea";

    /** Select */
    public static final String HTML_SELECT = "select";

    /** radio */
    public static final String HTML_RADIO = "radio";

    /** checkbox */
    public static final String HTML_CHECKBOX = "checkbox";

    /** datetime */
    public static final String HTML_DATETIME = "datetime";

    /** 画像アップロードコントロール */
    public static final String HTML_IMAGE_UPLOAD = "imageUpload";

    /** ファイルアップロードコントロール */
    public static final String HTML_FILE_UPLOAD = "fileUpload";

    /** Editor */
    public static final String HTML_EDITOR = "editor";

    /** String */
    public static final String TYPE_STRING = "String";

    /** Integer */
    public static final String TYPE_INTEGER = "Integer";

    /** Long */
    public static final String TYPE_LONG = "Long";

    /** Double */
    public static final String TYPE_DOUBLE = "Double";

    /** BigDecimal */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /** Date */
    public static final String TYPE_DATE = "Date";

    /** LIKE */
    public static final String QUERY_LIKE = "LIKE";

    /** EQ */
    public static final String QUERY_EQ = "EQ";

    /** 必要 */
    public static final String REQUIRE = "1";
}
