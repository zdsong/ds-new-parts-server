package jp.newpart.common.utils.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excelデータフォーマット処理アダプタ
 * 
 * @author nextosd
 */
public interface ExcelHandlerAdapter
{
    /**
     * フォーマット
     * 
     * @param value セルのデータ値
     * @param args excel注記argsパラメータグループ
     * @param cell セル・オブジェクト
     * @param wb ワークブック・オブジェクト
     *
     * @return 処理後の値
     */
    Object format(Object value, String[] args, Cell cell, Workbook wb);
}
