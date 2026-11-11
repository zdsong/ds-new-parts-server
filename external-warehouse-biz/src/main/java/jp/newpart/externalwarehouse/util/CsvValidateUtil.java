package jp.newpart.externalwarehouse.biz.util;

import io.micrometer.common.util.StringUtils;
import jp.newpart.common.biz.domain.entity.BssBatchCsvValidationInfoEntity;
import jp.newpart.common.core.domain.ImportFileErrorVo;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.ValueUtils;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvValidateUtil {

    public static void csvValidateField(String csvFileType, String detailFileName, CSVRecord record, String fileRowLine, List<BssBatchCsvValidationInfoEntity> validationInfos, List<ImportFileErrorVo> errorList, List<Map<String,Object>> saveList) {
        int len = record.size();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < len; i++) {
            BssBatchCsvValidationInfoEntity validationInfo = validationInfos.get(i);
            Object fieldValue = record.get(i);
            // 必須フラグを取得
            String required = validationInfo.getFieldRequired();
            // 最大文字数を取得
            String maxLength = validationInfo.getFieldLength();
            // フィールドフォーマットを取得
            String fieldFormat = validationInfo.getFieldFormat();
            // フィールドタイプを取得
            String fieldType = validationInfo.getFieldType() != null ? validationInfo.getFieldType() : "";
            // フィールドカテゴリを取得
            String fieldCategory = StringUtils.isNotBlank(validationInfo.getFieldCategory()) ? validationInfo.getFieldCategory() : "";
            // CSVファイルの列名を取得
            String csvFileColName = validationInfo.getCsvFileColName();
            // フィールド名を取得 company_name
            String fieldName = validationInfo.getFieldName();
            // table name
            //String tableName = validationInfo.getTableName();
            String humpName = CamelCaseUtil.toLowerCamelCase(fieldName);
            if (!required.equals("1") && StringUtils.isBlank(fieldFormat) && fieldCategory.equals("1")) {
                map.put(humpName, fieldValue);
                continue;
            }
            if (required.equals("1")) {
                // 必須フィールドの場合
                validateFieldEmpty(fieldValue, csvFileColName, detailFileName, fileRowLine, errorList);
            }
            validateFieldType(fieldValue, csvFileColName, fieldType, detailFileName, fileRowLine, errorList);
            validateFieldLength(fieldValue, csvFileColName, Integer.parseInt(maxLength), detailFileName, fileRowLine, errorList);
            validateFieldFormat(fieldFormat, csvFileType + '.' + fieldName, fieldValue, csvFileColName, detailFileName, fileRowLine, errorList);
            if (CollectionUtils.isEmpty(errorList)) {
                map.put(humpName, fieldValue);
            }
        }
        if (CollectionUtils.isEmpty(errorList)) {
            saveList.add(map);
        }
    }

    /**
     * 検査結果詳細情報のアップロード検証
     *
     * @param fieldValue     フィールド値
     * @param colName      フィールド名
     * @param fieldType      type
     * @param detailFileName ファイル名
     * @param fileRowLine    行番号
     * @param errorInfoList  エラー情報リスト
     */
    private static void validateFieldType(Object fieldValue, String colName, String fieldType, String detailFileName, String fileRowLine, List<ImportFileErrorVo> errorInfoList) {
        if (ValueUtils.isNotEmpty(fieldValue)) {
            if (fieldType.equals("INTEGER") && !isInteger(fieldValue.toString())) {
                String message = MessageUtils.message("E0066", colName);
                makeErrorInfo(detailFileName, fileRowLine, message, errorInfoList);
            } else if (fieldType.equals("DECIMAL") && !isBigDecimal(fieldValue.toString())) {
                String message = MessageUtils.message("E0066", colName);
                makeErrorInfo(detailFileName, fileRowLine, message, errorInfoList);
            }
        }
    }

    /**
     * 検査結果詳細情報のアップロード検証
     *
     * @param fieldValue     フィールド値
     * @param colName      フィールド名
     * @param maxLength      最大桁数
     * @param detailFileName ファイル名
     * @param fileRowLine    行番号
     * @param errorInfoList  エラー情報リスト
     */
    private static void validateFieldLength(Object fieldValue, String colName, int maxLength, String detailFileName, String fileRowLine, List<ImportFileErrorVo> errorInfoList) {
        if (ValueUtils.isNotEmpty(fieldValue) && maxLength > 0 && fieldValue.toString().length() > maxLength) {
            // E0051={0}の桁数は{1}桁以上、もしくはサロゲートペアが含まれ最長バイト数を超過
            String message = MessageUtils.message("E0051", colName, maxLength);
            makeErrorInfo(detailFileName, fileRowLine, message, errorInfoList);
        }
    }

    /**
     * 検査結果詳細情報のアップロード検証
     *
     * @param fieldValue     フィールド値
     * @param colName      フィールド名
     * @param detailFileName ファイル名
     * @param fileRowLine    行番号
     * @param errorInfoList  エラー情報リスト
     */
    private static void validateFieldEmpty(Object fieldValue, String colName, String detailFileName, String fileRowLine, List<ImportFileErrorVo> errorInfoList) {
        if (ValueUtils.isEmpty(fieldValue)) {
            // E0003={0}は必須入力です。
            String message = MessageUtils.message("E0003", colName);
            makeErrorInfo(detailFileName, fileRowLine, message, errorInfoList);
        }
    }

    /**
     * 検査結果詳細情報のアップロード検証
     *
     * @param fieldValue     フィールド値
     * @param fieldName      フィールド名
     * @param detailFileName ファイル名
     * @param fileRowLine    行番号
     * @param errorInfoList  エラー情報リスト
     */
    private static void validateFieldFormat(String fieldFormat, String fieldName, Object fieldValue, String colName, String detailFileName, String fileRowLine, List<ImportFileErrorVo> errorInfoList) {
        if (ValueUtils.isNotEmpty(fieldValue) && ValueUtils.isNotEmpty(fieldFormat)) {
            if (!fieldValue.toString().matches(fieldFormat)) {
                String message = "";
                if ("C.mail_address".equals(fieldName)) {
                    message = MessageUtils.message("E0008", colName);
                } else if ("C.birth".equals(fieldName) || "C.last_order_date".equals(fieldName)) {
                    message = MessageUtils.message("E0056", colName);
                }
                if (ValueUtils.isNotEmpty(message)) {
                    makeErrorInfo(detailFileName, fileRowLine, message, errorInfoList);
                }
            }

        }
    }

    /**
     * エラー情報を生成する
     *
     * @param fileName    不正ファイル名
     * @param fileRowLine 不正行番号
     * @param message     メッセージ
     * @param errorList   格納エラーリスト
     */
    public static void makeErrorInfo(String fileName, String fileRowLine, String message, List<ImportFileErrorVo> errorList) {
        ImportFileErrorVo errorInfo = new ImportFileErrorVo();
        errorInfo.setObjectName(fileName);
        errorInfo.setRowLine(fileRowLine);
        errorInfo.setMessage(message);
        errorList.add(errorInfo);
    }

    /**
     * BigDecimal型か判定する
     *
     * @param str 判定対象文字列
     * @return BigDecimal型の場合true
     */
    public static boolean isBigDecimal(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Integer型か判定する
     *
     * @param str 判定対象文字列
     * @return Integer型の場合true
     */
    public static boolean isInteger(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
