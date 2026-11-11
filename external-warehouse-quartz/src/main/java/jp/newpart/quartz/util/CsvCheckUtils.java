package jp.newpart.quartz.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.newpart.common.biz.domain.entity.BssBatchCsvValidationInfoEntity;
import jp.newpart.common.core.domain.entity.SysDictData;
import jp.newpart.common.utils.DictUtils;
import jp.newpart.common.utils.StringUtils;

import java.util.List;

/**
 * CSVファイル検証共通ユーティリティクラス
 * 
 * @author External Warehouse
 * @version 1.0
 */
public class CsvCheckUtils {

    private static final Logger logger = LoggerFactory.getLogger(CsvCheckUtils.class);

    /**
     * フィールド検証
     * 
     * @param rowData         行データ
     * @param validationInfos 検証情報リスト
     * @return 検証結果
     */
    public static boolean csvValidateField(String[] rowData, List<BssBatchCsvValidationInfoEntity> validationInfos) {
        for (BssBatchCsvValidationInfoEntity validationInfo : validationInfos) {
            // 列インデックスを取得
            int columnIndex = validationInfo.getCsvFileColIdx();
            // 列インデックスが行データの範囲内かチェック
            if (columnIndex >= rowData.length) {
                return false;
            }
            // フィールド値を取得
            String fieldValue = rowData[columnIndex];
            // 必須フラグを取得
            String required = validationInfo.getFieldRequired();
            // 最大文字数を取得
            String maxLength = validationInfo.getFieldLength();
            // フィールドフォーマットを取得
            String fieldFormat = validationInfo.getFieldFormat();
            // フィールドタイプを取得
            String fieldType = validationInfo.getFieldType() != null ? validationInfo.getFieldType() : "";
            // フィールドカテゴリを取得
            String fieldCategory = validationInfo.getFieldCategory() != null
                    && !validationInfo.getFieldCategory().isEmpty() ? validationInfo.getFieldCategory() : "";
            // データディクショナリ名を取得
            String dataDictName = validationInfo.getDataDictName();
            // CSVファイルの列名を取得
            String csvFileColName = validationInfo.getCsvFileColName();
            // テーブル名を取得
            String tableName = validationInfo.getTableName();
            // フィールド名を取得
            String fieldName = validationInfo.getFieldName();

            if (required.equals("1")) {
                // 必須フィールドの場合
                if (fieldValue == null || fieldValue.isEmpty()) {
                    logger.error("必須フィールドが空です。フィールド名: {} ({}), 列名: {}, 列インデックス: {}, フィールド値: {}", tableName, fieldName,
                            csvFileColName, columnIndex, fieldValue);
                    return false;
                }
            }

            if (fieldValue != null && fieldValue.length() > Integer.parseInt(maxLength) && !fieldCategory.equals("1")) {
                // 最大文字数を超えた場合
                logger.error("最大文字数を超えた場合。フィールド名: {} ({}), 列名: {}, 列インデックス: {}, フィールド値: {}", tableName, fieldName,
                        csvFileColName, columnIndex, fieldValue);
                return false;
            }

            if ("INTEGER".equals(fieldType) && (fieldValue != null && !fieldValue.isEmpty() && fieldValue != "")
                    && !fieldCategory.equals("1")) {
                // 整数型の場合
                try {
                    Integer.parseInt(fieldValue);
                } catch (NumberFormatException e) {
                    logger.error("整数型でない値が入力されました。フィールド名: {} ({}), 列名: {}, 列インデックス: {}, フィールド値: {}", tableName, fieldName,
                            csvFileColName, columnIndex, fieldValue);
                    return false;
                }
            }
            
            if ("DECIMAL".equals(fieldType) && (fieldValue != null && !fieldValue.isEmpty() && fieldValue != "")
                    && !fieldCategory.equals("1")) {
                // 小数型の場合
                try {
                    Double.parseDouble(fieldValue);
                } catch (NumberFormatException e) {
                    logger.error("小数型でない値が入力されました。フィールド名: {} ({}), 列名: {}, 列インデックス: {}, フィールド値: {}", tableName, fieldName,
                            csvFileColName, columnIndex, fieldValue);
                    return false;
                }
            }

            if (fieldFormat != null && !fieldFormat.isEmpty() && fieldValue != null && !fieldValue.isEmpty()) {
                // フィールドフォーマットが指定された場合
                if (!fieldValue.matches(fieldFormat)) {
                    logger.error("フィールドフォーマットが一致しません。フィールド名: {} ({}), 列名: {}, 列インデックス: {}, フィールド値: {}", tableName, fieldName,
                            csvFileColName, columnIndex, fieldValue);
                    return false;
                }
            }

            // if (fieldCategory.equals("1")) {
            // // データディクショナリ名を使用してチェック
            // String dictValue = DictUtils.getDictValue(dataDictName, fieldValue);
            // if (StringUtils.isEmpty(dictValue)) {
            // return false;
            // }
            // }

        }
        return true;
    }
}
