package jp.newpart.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import jp.newpart.common.utils.poi.ExcelHandlerAdapter;

/**
 * カスタムExcelデータエクスポートアノテーション
 *
 * @author nextosd
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {
    /**
     * エクスポート時にExcelでの並び順
     */
    public int sort() default Integer.MAX_VALUE;

    /**
     * Excelにエクスポートする際の名前
     */
    public String name() default "";

    /**
     * 日付形式, 例: yyyy-MM-dd
     */
    public String dateFormat() default "";

    /**
     * 辞書型の場合、辞書のtype値を設定 (例: sys_user_sex)
     */
    public String dictType() default "";

    /**
     * 読み込み内容を表現式に変換 (例: 0=男,1=女,2=未知)
     */
    public String readConverterExp() default "";

    /**
     * 区切り文字、文字列グループ内容の読み込み
     */
    public String separator() default ",";

    /**
     * BigDecimalの精度 デフォルト:-1(デフォルトではBigDecimalフォーマットを無効にする)
     */
    public int scale() default -1;

    /**
     * BigDecimalの丸め規則 デフォルト:BigDecimal.ROUND_HALF_EVEN
     */
    public int roundingMode() default BigDecimal.ROUND_HALF_EVEN;

    /**
     * エクスポート時にExcelの各列の高さ
     */
    public double height() default 14;

    /**
     * エクスポート時にExcelの各列の幅
     */
    public double width() default 16;

    /**
     * 文字列の後ろに付けるサフィックス, 例: % 90 は 90% になる
     */
    public String suffix() default "";

    /**
     * 値が空の場合、フィールドのデフォルト値
     */
    public String defaultValue() default "";

    /**
     * ヒントメッセージ
     */
    public String prompt() default "";

    /**
     * 内容の折り返しを許可するかどうか
     */
    public boolean wrapText() default false;

    /**
     * 選択のみ可能な列内容を設定
     */
    public String[] combo() default {};

    /**
     * 辞書からデータを読み込むかどうか、デフォルトは読み込まない、読み込む場合はdictType注解を設定する
     */
    public boolean comboReadDict() default false;

    /**
     * 単元格を縦方向に結合するかどうか、リスト集合を含む単元格の要件に対応
     */
    public boolean needMerge() default false;

    /**
     * データをエクスポートするかどうか、テンプレートをエクスポートする場合、タイトルが必要だが内容はユーザーが手動で入力する必要がある
     */
    public boolean isExport() default true;

    /**
     * 他のクラスのプロパティ名、複数レベルの取得をサポート、ピリオドで区切る
     */
    public String targetAttr() default "";

    /**
     * データを自動的に統計するかどうか、最後に統計データの合計行を追加
     */
    public boolean isStatistics() default false;

    /**
     * エクスポートタイプ（0: 数字 1: 文字列 2: 画像）
     */
    public ColumnType cellType() default ColumnType.STRING;

    /**
     * エクスポート列ヘッダーの背景色
     */
    public IndexedColors headerBackgroundColor() default IndexedColors.GREY_50_PERCENT;

    /**
     * エクスポート列ヘッダーの文字色
     */
    public IndexedColors headerColor() default IndexedColors.WHITE;

    /**
     * エクスポートセルの背景色
     */
    public IndexedColors backgroundColor() default IndexedColors.WHITE;

    /**
     * エクスポートセルの文字色
     */
    public IndexedColors color() default IndexedColors.BLACK;

    /**
     * エクスポートフィールドの揃え方
     */
    public HorizontalAlignment align() default HorizontalAlignment.CENTER;

    /**
     * カスタムデータハンドラ
     */
    public Class<?> handler() default ExcelHandlerAdapter.class;

    /**
     * カスタムデータハンドラの引数
     */
    public String[] args() default {};

    /**
     * フィールドタイプ（0: エクスポートとインポート 1: エクスポートのみ 2: インポートのみ）
     */
    Type type() default Type.ALL;

    /**
     * 必須項目入力（true）
     */
    public boolean isNotBlank() default false;

    /**
     * 出力の列番号
     *
     * @return
     */
    public int colIndex() default -1;

    /**
     * テーブルのカラム名
     *
     * @return
     */
    public String tableField() default "";

    /**
     * セル値の最大Lengthを設定する
     *
     * @return
     */
    public int cellMinVal() default Integer.MIN_VALUE;

    /**
     * セル値の最大必須入力Lengthを設定する
     *
     * @return
     */
    public int cellMaxVal() default Integer.MAX_VALUE;

    /**
     * 入力値のタイプ
     */
    public InputValueType inputValueType() default InputValueType.NONE;

    /**
     * フィールドタイプ
     */
    public enum Type {
        /**
         * 全て
         */
        ALL(0),
        /**
         * エクスポートのみ
         */
        EXPORT(1),
        /**
         * インポートのみ
         */
        IMPORT(2);
        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }


    /**
     * エクスポートタイプ（0: 数字 1: 文字列 2: 画像）
     */
    public enum ColumnType {
        /**
         * 数字
         */
        NUMERIC(0),
        /**
         * 文字列
         */
        STRING(1),
        /**
         * 画像
         */
        IMAGE(2),
        /**
         * テキスト
         */
        TEXT(3),
        /**
         * ナンバリング
         */
        SERIAL_NUMBER(4);
        private final int value;

        ColumnType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    public enum InputValueType {
        /**
         * なし
         */
        NONE,
        /**
         * 英数記号
         */
        ALPHABET_NUMERIC_SYMBOL,
        /**
         * 英字（大小文字）
         */
        ALPHABET,
        /**
         * 英数字
         */
        ALPHABET_NUMERIC,
        /**
         * 半角数字
         */
        NUMERIC,
        /**
         * 文字数
         */
        CHAR_COUNT,
        /**
         * 並び順
         */
        ORDER_NO,
        /**
         * 入力要否
         */
        IS_INPUT,
        /**
         * 文字種
         */
        CHAR_TYPE;
    }
}