package jp.newpart.common.utils.poi.vo;

import lombok.Data;

/**
 * Excelセルのエラー情報
 *
 * @author nextosd
 */
@Data
public class ExcelCellValueErrorVo {
    /**
     * エラー対象（ファイル名或はシート名）
     */
    private String objectName;
    /**
     * 行番号
     */
    private Integer rowLine;
    /**
     * 列番号
     */
    private Integer cellNo;
    /**
     * メッセージ情報
     */
    private String message;


    public ExcelCellValueErrorVo(String objectName, Integer rowLine, Integer cellNo, String message) {
        this.objectName = objectName;
        this.rowLine = rowLine;
        this.cellNo = cellNo;
        this.message = message;
    }
}
