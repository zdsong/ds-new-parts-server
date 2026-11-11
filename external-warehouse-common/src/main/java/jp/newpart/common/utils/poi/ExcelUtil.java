package jp.newpart.common.utils.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.annotation.Excel;
import jp.newpart.common.annotation.Excel.Type;
import jp.newpart.common.annotation.Excel.ColumnType;
import jp.newpart.common.annotation.Excels;
import jp.newpart.common.exception.UtilException;
import jp.newpart.common.utils.*;
import jp.newpart.common.utils.file.FileTypeUtils;
import jp.newpart.common.utils.file.FileUtils;
import jp.newpart.common.utils.file.ImageUtils;
import jp.newpart.common.utils.poi.vo.ExcelCellValueErrorVo;
import jp.newpart.common.utils.spring.SpringUtils;
import jp.newpart.common.validator.service.IValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import jp.newpart.common.config.AppConfig;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.text.Convert;
import jp.newpart.common.utils.reflect.ReflectUtils;

/**
 * Excel関連処理
 *
 * @author nextosd
 */
@Slf4j
public class ExcelUtil<T> {
    public static final String FORMULA_REGEX_STR = "=|-|\\+|@";
    public static final String[] FORMULA_STR = {"=", "-", "+", "@"};
    /**
     * Excel sheet最大行数、デフォルト65536
     */
    public static final int SHEET_SIZE = 65536;
    /**
     * 数値形式
     */
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");
    /**
     * dictTypeプロパティデータ格納、キャッシュの繰り返し検索を避ける
     */
    public Map<String, String> sysDictMap = new HashMap<String, String>();
    /**
     * エンティティオブジェクト
     */
    public Class<T> clazz;
    /**
     * 表示する列プロパティ
     */
    public String[] includeFields;
    /**
     * 除外する列プロパティ
     */
    public String[] excludeFields;
    /**
     * ワークシート名
     */
    private String sheetName;
    /**
     * エクスポートタイプ（EXPORT:データエクスポート；IMPORT：インポートテンプレート）
     */
    private Type type;
    /**
     * ワークブックオブジェクト
     */
    private Workbook wb;
    /**
     * ワークシートオブジェクト
     */
    private Sheet sheet;
    /**
     * スタイルリスト
     */
    private Map<String, CellStyle> styles;
    /**
     * インポートエクスポートデータリスト
     */
    private List<T> list;
    /**
     * アノテーションリスト
     */
    private List<Object[]> fields;
    /**
     * 現在の行番号
     */
    private int rownum;
    /**
     * タイトル
     */
    private String title;
    /**
     * 最大高さ
     */
    private short maxHeight;
    /**
     * 結合後の最終行数
     */
    private int subMergedLastRowNum = 0;
    /**
     * 結合後の開始行数
     */
    private int subMergedFirstRowNum = 1;
    /**
     * オブジェクトのサブリストメソッド
     */
    private Method subMethod;
    /**
     * オブジェクトのサブリストプロパティ
     */
    private List<Field> subFields;
    /**
     * 統計リスト
     */
    private Map<Integer, Double> statistics = new HashMap<Integer, Double>();

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * キャンバスを取得
     */
    public static Drawing<?> getDrawingPatriarch(Sheet sheet) {
        if (sheet.getDrawingPatriarch() == null) {
            sheet.createDrawingPatriarch();
        }
        return sheet.getDrawingPatriarch();
    }

    /**
     * エクスポート値を解析 0=男,1=女,2=未知
     *
     * @param propertyValue パラメータ値
     * @param converterExp  翻訳アノテーション
     * @param separator     区切り文字
     * @return 解析後値
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[0].equals(value)) {
                        propertyString.append(itemArray[1] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 値を逆解析 男=0,女=1,未知=2
     *
     * @param propertyValue パラメータ値
     * @param converterExp  翻訳アノテーション
     * @param separator     区切り文字
     * @return 解析後値
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[1].equals(value)) {
                        propertyString.append(itemArray[0] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 辞書値を解析
     *
     * @param dictValue 辞書値
     * @param dictType  辞書タイプ
     * @param separator 区切り文字
     * @return 辞書ラベル
     */
    public static String convertDictByExp(String dictValue, String dictType, String separator) {
        return DictUtils.getDictLabel(dictType, dictValue, separator);
    }

    /**
     * 値を逆解析
     *
     * @param dictLabel 辞書ラベル
     * @param dictType  辞書タイプ
     * @param separator 区切り文字
     * @return 辞書値
     */
    public static String reverseDictByExp(String dictLabel, String dictType, String separator) {
        return DictUtils.getDictValue(dictType, dictLabel, separator);
    }

    /**
     * Excel2003画像を取得
     *
     * @param sheet    現在のシートオブジェクト
     * @param workbook ワークブックオブジェクト
     * @return Map key:画像セルインデックス（1_1）String，value:画像ストリームPictureData
     */
    public static Map<String, PictureData> getSheetPictures03(HSSFSheet sheet, HSSFWorkbook workbook) {
        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        if (!pictures.isEmpty()) {
            for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
                if (shape instanceof HSSFPicture) {
                    HSSFPicture pic = (HSSFPicture) shape;
                    int pictureIndex = pic.getPictureIndex() - 1;
                    HSSFPictureData picData = pictures.get(pictureIndex);
                    String picIndex = anchor.getRow1() + "_" + anchor.getCol1();
                    sheetIndexPicMap.put(picIndex, picData);
                }
            }
            return sheetIndexPicMap;
        } else {
            return sheetIndexPicMap;
        }
    }

    /**
     * Excel2007画像を取得
     *
     * @param sheet    現在のシートオブジェクト
     * @param workbook ワークブックオブジェクト
     * @return Map key:画像セルインデックス（1_1）String，value:画像ストリームPictureData
     */
    public static Map<String, PictureData> getSheetPictures07(XSSFSheet sheet, XSSFWorkbook workbook) {
        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
        for (POIXMLDocumentPart dr : sheet.getRelations()) {
            if (dr instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) dr;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    if (shape instanceof XSSFPicture) {
                        XSSFPicture pic = (XSSFPicture) shape;
                        XSSFClientAnchor anchor = pic.getPreferredSize();
                        CTMarker ctMarker = anchor.getFrom();
                        String picIndex = ctMarker.getRow() + "_" + ctMarker.getCol();
                        sheetIndexPicMap.put(picIndex, pic.getPictureData());
                    }
                }
            }
        }
        return sheetIndexPicMap;
    }

    /**
     * 文字列中の最後の半角または全角コロンの後の値を取得します。
     *
     * @param input 入力文字列
     * @return 最後のコロンの後の値。コロンがない場合は空文字列を返します。
     */
    public static String getValueAfterLastColon(String input) {
        if (StringUtils.isObjectEmpty(input)) {
            return StringUtils.EMPTY;
        }
        int lastHalfColonIndex = input.lastIndexOf(':');
        int lastFullColonIndex = input.lastIndexOf('：');

        int lastColonIndex = Math.max(lastHalfColonIndex, lastFullColonIndex);

        if (lastColonIndex != -1) {
            return input.substring(lastColonIndex + 1);
        }
        return input;
    }

    /**
     * 文字列中の最初の半角または全角コロンの前の値を取得します。
     *
     * @param input 入力文字列
     * @return 最初のコロンの前の値。コロンがない場合は入力文字列をそのまま返します。
     */
    public static String getValueBeforeFirstColon(String input) {
        if (StringUtils.isObjectEmpty(input)) {
            return StringUtils.EMPTY;
        }
        int firstHalfColonIndex = input.indexOf(':');
        int firstFullColonIndex = input.indexOf('：');

        if (firstHalfColonIndex == -1) {
            firstHalfColonIndex = Integer.MAX_VALUE;
        }
        if (firstFullColonIndex == -1) {
            firstFullColonIndex = Integer.MAX_VALUE;
        }

        int firstColonIndex = Math.min(firstHalfColonIndex, firstFullColonIndex);
        if (firstColonIndex != Integer.MAX_VALUE) {
            return input.substring(0, firstColonIndex);
        }
        return input;
    }

    /**
     * Excelで列プロパティを表示
     *
     * @param fields 列プロパティ名 示例[単一"name"/複数"id","name"]
     */
    public void showColumn(String... fields) {
        this.includeFields = fields;
    }

    /**
     * Excelで列プロパティを隠す
     *
     * @param fields 列プロパティ名 示例[単一"name"/複数"id","name"]
     */
    public void hideColumn(String... fields) {
        this.excludeFields = fields;
    }

    public void init(List<T> list, String sheetName, String title, Excel.Type type) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        this.title = title;
        createExcelField();
        createWorkbook();
        createTitle();
        createSubHead();
    }

    /**
     * Excelの最初のタイトル行を作成
     */
    public void createTitle() {
        if (StringUtils.isNotEmpty(title)) {
            int titleLastCol = this.fields.size() - 1;
            if (isSubList()) {
                titleLastCol = titleLastCol + subFields.size() - 1;
            }
            Row titleRow = sheet.createRow(rownum == 0 ? rownum++ : 0);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, titleLastCol));
        }
    }

    /**
     * オブジェクトのサブリスト名を作成
     */
    public void createSubHead() {
        if (isSubList()) {
            Row subRow = sheet.createRow(rownum);
            int column = 0;
            int subFieldSize = subFields != null ? subFields.size() : 0;
            for (Object[] objects : fields) {
                Field field = (Field) objects[0];
                Excel attr = (Excel) objects[1];
                if (Collection.class.isAssignableFrom(field.getType())) {
                    Cell cell = subRow.createCell(column);
                    cell.setCellValue(attr.name());
                    cell.setCellStyle(styles.get(StringUtils.format("header_{}_{}", attr.headerColor(), attr.headerBackgroundColor())));
                    if (subFieldSize > 1) {
                        CellRangeAddress cellAddress = new CellRangeAddress(rownum, rownum, column, column + subFieldSize - 1);
                        sheet.addMergedRegion(cellAddress);
                    }
                    column += subFieldSize;
                } else {
                    Cell cell = subRow.createCell(column++);
                    cell.setCellValue(attr.name());
                    cell.setCellStyle(styles.get(StringUtils.format("header_{}_{}", attr.headerColor(), attr.headerBackgroundColor())));
                }
            }
            rownum++;
        }
    }

    /**
     * Excel表のデフォルト最初のインデックス名をlistに変換
     *
     * @param is 入力ストリーム
     * @return 変換後リスト
     */
    public List<T> importExcel(InputStream is) {
        List<T> list = null;
        try {
            list = importExcel(is, 0);
        } catch (Exception e) {
            log.error("Excelインポート異常{}", e.getMessage());
            throw new UtilException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return list;
    }

    /**
     * Excel表のデフォルト最初のインデックス名をlistに変換
     *
     * @param is       入力ストリーム
     * @param titleNum タイトル占有行数
     * @return 変換後リスト
     */
    public List<T> importExcel(InputStream is, int titleNum) throws Exception {
        return importExcel(StringUtils.EMPTY, is, titleNum);
    }

    /**
     * Excel表の指定されたシート名をリストに変換
     *
     * @param sheetName シート名
     * @param titleNum  タイトルが占める行数
     * @param is        入力ストリーム
     * @return 変換後のリスト
     */
    public List<T> importExcel(String sheetName, InputStream is, int titleNum) throws Exception {
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        // シート名が指定されている場合は指定のシートの内容を使用 それ以外の場合、最初のシートを使用
        Sheet sheet = StringUtils.isNotEmpty(sheetName) ? wb.getSheet(sheetName) : wb.getSheetAt(0);
        if (sheet == null) {
            throw new IOException("ファイルのシートが存在しません");
        }
        boolean isXssfWorkbook = !(wb instanceof HSSFWorkbook);
        Map<String, PictureData> pictures;
        if (isXssfWorkbook) {
            pictures = getSheetPictures07((XSSFSheet) sheet, (XSSFWorkbook) wb);
        } else {
            pictures = getSheetPictures03((HSSFSheet) sheet, (HSSFWorkbook) wb);
        }
        // 最後の非空行の行番号を取得 例えば総行数がnの場合、n-1が返されます
        int rows = sheet.getLastRowNum();
        if (rows > 0) {
            // Excel列の番号とフィールドを格納するためのマップを定義します
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            // ヘッダーを取得
            Row header = sheet.getRow(titleNum);
            for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
                Cell cell = header.getCell(i);
                if (StringUtils.isNotNull(cell)) {
                    String value = this.getCellValue(header, i).toString();
                    cellMap.put(value, i);
                } else {
                    cellMap.put(null, i);
                }
            }
            // データがある場合のみ処理を行います クラスのすべてのフィールドを取得します
            List<Object[]> fields = this.getFields();
            Map<Integer, Object[]> fieldsMap = new HashMap<Integer, Object[]>();
            for (Object[] objects : fields) {
                Excel attr = (Excel) objects[1];
                Integer column = cellMap.get(attr.name());
                if (column != null) {
                    fieldsMap.put(column, objects);
                }
            }
            for (int i = titleNum + 1; i <= rows; i++) {
                // 2行目からデータを取得 通常、1行目はヘッダーです
                Row row = sheet.getRow(i);
                // 現在の行が空行かどうかを判断します
//                if (isRowEmpty(row)) {
//                    continue;
//                }
                T entity = null;
                for (Map.Entry<Integer, Object[]> entry : fieldsMap.entrySet()) {
                    Object val = this.getCellValue(row, entry.getKey());

                    // インスタンスが存在しない場合は新規作成します
                    entity = (entity == null ? clazz.getDeclaredConstructor().newInstance() : entity);
                    // マップから対応する列のフィールドを取得します
                    Field field = (Field) entry.getValue()[0];
                    Excel attr = (Excel) entry.getValue()[1];
                    // 型を取得し、オブジェクトの型に基づいて値を設定します
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType) {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0")) {
                            val = StringUtils.substringBefore(s, ".0");
                        } else {
                            String dateFormat = field.getAnnotation(Excel.class).dateFormat();
                            if (StringUtils.isNotEmpty(dateFormat)) {
                                val = parseDateToStr(dateFormat, val);
                            } else {
                                val = Convert.toStr(val);
                            }
                        }
                    } else if ((Integer.TYPE == fieldType || Integer.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val))) {
                        val = Convert.toInt(val);
                    } else if ((Long.TYPE == fieldType || Long.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val))) {
                        val = Convert.toLong(val);
                    } else if (Double.TYPE == fieldType || Double.class == fieldType) {
                        val = Convert.toDouble(val);
                    } else if (Float.TYPE == fieldType || Float.class == fieldType) {
                        val = Convert.toFloat(val);
                    } else if (BigDecimal.class == fieldType) {
                        val = Convert.toBigDecimal(val);
                    } else if (Date.class == fieldType) {
                        if (val instanceof String) {
                            val = DateUtils.parseDate(val);
                        } else if (val instanceof Double) {
                            val = DateUtil.getJavaDate((Double) val);
                        }
                    } else if (Boolean.TYPE == fieldType || Boolean.class == fieldType) {
                        val = Convert.toBool(val, false);
                    }
                    if (StringUtils.isNotNull(fieldType)) {
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr())) {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        }
                        if (StringUtils.isNotEmpty(attr.readConverterExp())) {
                            val = reverseByExp(Convert.toStr(val), attr.readConverterExp(), attr.separator());
                        } else if (StringUtils.isNotEmpty(attr.dictType())) {
                            if (!sysDictMap.containsKey(attr.dictType() + val)) {
                                String dictValue = reverseDictByExp(Convert.toStr(val), attr.dictType(), attr.separator());
                                sysDictMap.put(attr.dictType() + val, dictValue);
                            }
                            val = sysDictMap.get(attr.dictType() + val);
                        } else if (!attr.handler().equals(ExcelHandlerAdapter.class)) {
                            val = dataFormatHandlerAdapter(val, attr, null);
                        } else if (ColumnType.IMAGE == attr.cellType() && StringUtils.isNotEmpty(pictures)) {
                            PictureData image = pictures.get(row.getRowNum() + "_" + entry.getKey());
                            if (image == null) {
                                val = "";
                            } else {
                                byte[] data = image.getData();
                                val = FileUtils.writeImportBytes(data);
                            }
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 指定されたテーブルインデックス名をExcelシートのリストに変換します
     *
     * @param sheetName テーブルインデックス名
     * @param is        入力ストリーム
     * @return 変換されたコレクション（テーブルコードをキーとする）
     */
    public Sheet getExcelSheet(String sheetName, InputStream is) throws Exception {
        this.type = Excel.Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        // シート名が指定されている場合は、指定されたシートのコンテンツが取得されます。指定されていない場合は、デフォルトで最初のシートを指します。
        Sheet sheet = StringUtils.isNotEmpty(sheetName) ? wb.getSheet(sheetName) : wb.getSheetAt(0);
        if (sheet == null) {
            throw new IOException("ファイルシートが存在しません。");
        }

        return sheet;
    }

    /**
     * Excel表の指定されたシート名をリストに変換
     *
     * @param sheetName シート名
     * @param titleNum  タイトルが占める行数
     * @param is        入力ストリーム
     * @return 変換後のリスト
     */
    public List<T> importExcelWithEmptyRow(String sheetName, InputStream is, int titleNum, List<ExcelCellValueErrorVo> excelCellValueErrorList, List<String> allowedKeywords) throws Exception {
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        String defaultMsgCode = StringUtils.EMPTY;
        String[] params = new String[3];
        String msg = StringUtils.EMPTY;
        ExcelCellValueErrorVo errorInfo = null;
        // シート名が指定されている場合は指定のシートの内容を使用 それ以外の場合、最初のシートを使用
        Sheet sheet = StringUtils.isNotEmpty(sheetName) ? wb.getSheet(sheetName) : wb.getSheetAt(0);
        if (sheet == null) {
            throw new IOException("ファイルのシートが存在しません");
        }
        boolean isXssfWorkbook = !(wb instanceof HSSFWorkbook);
        Map<String, PictureData> pictures;
        if (isXssfWorkbook) {
            pictures = getSheetPictures07((XSSFSheet) sheet, (XSSFWorkbook) wb);
        } else {
            pictures = getSheetPictures03((HSSFSheet) sheet, (HSSFWorkbook) wb);
        }
        // 最後の非空行の行番号を取得 例えば総行数がnの場合、n-1が返されます
        int rows = sheet.getLastRowNum();
        if (rows > 0) {

            // データがある場合のみ処理を行います クラスのすべてのフィールドを取得します
            List<Object[]> fields = this.getFields();

            if (fields.isEmpty()) {
                return list;
            }

            // カラムはソート処理
            List<Object[]> sortFields = fields.stream().sorted(Comparator.comparing(objects -> ((Excel) objects[1]).sort())).toList();

            for (int i = 2; i <= rows; i++) {
                // 2行目からデータを取得 通常、1行目はヘッダーです
                Row row = sheet.getRow(i);
                // 現在の行が空行かどうかを判断します
                if (isRowEmpty(row)) {
                    continue;
                }
                // キーワード必須入力フラグ
                boolean keyWordFlg = false;

                T entity = null;
                for (int idx = 0; idx < sortFields.size(); idx++) {
                    errorInfo = null;
                    // インスタンスが存在しない場合は新規作成します
                    entity = (entity == null ? clazz.getDeclaredConstructor().newInstance() : entity);
                    // マップから対応する列のフィールドを取得します
                    Object[] os = sortFields.get(idx);
                    Field field = (Field) os[0];
                    Excel attr = (Excel) os[1];

                    // セルの値を取得する
                    Object val = this.getCellValue(row, attr.colIndex() - 1);
                    // 型を取得し、オブジェクトの型に基づいて値を設定します
                    Class<?> fieldType = field.getType();

                    if (StringUtils.isObjectEmpty(val) && attr.isNotBlank()) {
                        // E0029={0}が入力されていません。
                        defaultMsgCode = "{E0029}";
                        params[0] = attr.name();
                        msg = SpringUtils.getBean(IValidatorService.class).getErrorMessage(defaultMsgCode, params);
                        errorInfo = new ExcelCellValueErrorVo(sheetName, i + 1, attr.colIndex(), msg);
                        excelCellValueErrorList.add(errorInfo);
                        continue;
                    }

                    if ("key_word".equals(attr.tableField())) {
                        String strObjValue = ConvertUtils.toStr(val, StringUtils.EMPTY);
                        String keyWordLetters = getValueAfterLastColon(strObjValue);

                        if (keyWordFlg) {
                            // 入力要否 = 否の場合、キーワードが入力なし場合
                            if (StringUtils.isObjectEmpty(keyWordLetters)) {
                                // E0029={0}が入力されていません
                                defaultMsgCode = "{E0029}";
                                params[0] = attr.name();
                                msg = SpringUtils.getBean(IValidatorService.class).getErrorMessage(defaultMsgCode, params);
                                errorInfo = new ExcelCellValueErrorVo(sheetName, i + 1, attr.colIndex(), msg);
                                excelCellValueErrorList.add(errorInfo);
                                continue;
                            } else {
                                if (!allowedKeywords.contains(keyWordLetters)) {
                                    // EM006={0}が不正です。
                                    defaultMsgCode = "{EM006}";
                                    params[0] = attr.name();
                                    msg = SpringUtils.getBean(IValidatorService.class).getErrorMessage(defaultMsgCode, params);
                                    errorInfo = new ExcelCellValueErrorVo(sheetName, i + 1, attr.colIndex(), msg);
                                    excelCellValueErrorList.add(errorInfo);
                                    continue;
                                }
                            }
                        }
                    }

                    if (String.class == fieldType) {

                        if (!StringUtils.isObjectEmpty(val)) {
                            String strObjValue = ConvertUtils.toStr(val, StringUtils.EMPTY);
                            // ・桁数チェック
                            int objSize = strObjValue.length();
                            int cellValMaxLength = attr.cellMaxVal();

                            if ("is_input".equals(attr.tableField())) {
                                String isInputNumberPart = getValueBeforeFirstColon(strObjValue);
                                objSize = isInputNumberPart.length();
                                if ("020".equals(isInputNumberPart)) {
                                    keyWordFlg = true;
                                }
                                strObjValue = isInputNumberPart;
                                val = strObjValue;
                            }

                            if ("char_type".equals(attr.tableField())) {
                                String charTypePart = getValueBeforeFirstColon(strObjValue);
                                objSize = charTypePart.length();
                                strObjValue = charTypePart;
                                val = strObjValue;
                            }

                            if ("key_word".equals(attr.tableField())) {
                                String keyWordLetters = getValueAfterLastColon(strObjValue);
                                // 入力要否が010：要の場合、キーワードが空白として設定
                                if (!keyWordFlg) {
                                    keyWordLetters = "";
                                }
                                objSize = keyWordLetters.length();
                                val = keyWordLetters;
                            }

                            if (cellValMaxLength > 0 && objSize > cellValMaxLength) {
                                // E0030={0}の桁数は{1}桁以上になる。
                                defaultMsgCode = "{E0030}";
                                if ("exam_key".equals(attr.tableField())
                                        || "exam_name".equals(attr.tableField())
                                        || "item_label".equals(attr.tableField()) ) {
                                    // E0051={0}の桁数は{1}桁以上、もしくはサロゲートペアが含まれ最長バイト数を超過
                                    defaultMsgCode = "{E0051}";
                                }

                                params[0] = attr.name();
                                params[1] = ConvertUtils.toStr(cellValMaxLength);
                                msg = SpringUtils.getBean(IValidatorService.class).getErrorMessage(defaultMsgCode, params);
                                errorInfo = new ExcelCellValueErrorVo(sheetName, i + 1, attr.colIndex(), msg);
                                excelCellValueErrorList.add(errorInfo);
                            }

                            // データ型チェック (全半角数字記号等）
                            msg = checkInputValueType(strObjValue, attr.inputValueType(), attr.name());
                            if (!StringUtils.isObjectEmpty(msg)) {
                                errorInfo = new ExcelCellValueErrorVo(sheetName, i + 1, attr.colIndex(), msg);
                                excelCellValueErrorList.add(errorInfo);
                            }
                        }
                    } else if ((Integer.TYPE == fieldType || Integer.class == fieldType)) {
                        if (!StringUtils.isObjectEmpty(val)) {
                            String strObjValue = ConvertUtils.toStr(val, StringUtils.EMPTY);
                            // ・桁数チェック
                            int objSize = strObjValue.length();
                            int cellValMaxLength = attr.cellMaxVal();

                            if (cellValMaxLength > 0 && objSize > cellValMaxLength) {
                                // E0030={0}の桁数は{1}桁以上になる。
                                defaultMsgCode = "{E0030}";
                                params[0] = attr.name();
                                params[1] = ConvertUtils.toStr(cellValMaxLength);
                                msg = SpringUtils.getBean(IValidatorService.class).getErrorMessage(defaultMsgCode, params);
                                errorInfo = new ExcelCellValueErrorVo(sheetName, i + 1, attr.colIndex(), msg);
                                excelCellValueErrorList.add(errorInfo);
                            }

                            // データ型チェック (全半角数字記号等）
                            msg = checkInputValueType(strObjValue, attr.inputValueType(), attr.name());
                            if (!StringUtils.isObjectEmpty(msg)) {
                                errorInfo = new ExcelCellValueErrorVo(sheetName, i + 1, attr.colIndex(), msg);
                                excelCellValueErrorList.add(errorInfo);
                            }
                        }
                    } else if ((Long.TYPE == fieldType || Long.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val))) {
                        val = Convert.toLong(val);
                    } else if (Double.TYPE == fieldType || Double.class == fieldType) {
                        val = Convert.toDouble(val);
                    } else if (Float.TYPE == fieldType || Float.class == fieldType) {
                        val = Convert.toFloat(val);
                    } else if (BigDecimal.class == fieldType) {
                        val = Convert.toBigDecimal(val);
                    } else if (Date.class == fieldType) {
                        if (val instanceof String) {
                            val = DateUtils.parseDate(val);
                        } else if (val instanceof Double) {
                            val = DateUtil.getJavaDate((Double) val);
                        }
                    } else if (Boolean.TYPE == fieldType || Boolean.class == fieldType) {
                        val = Convert.toBool(val, false);
                    }
                    if (errorInfo == null) {
                        String propertyName = field.getName();
                        if ("no".equals(propertyName)) {
                            ReflectUtils.invokeSetter(entity, propertyName, i + 1);
                        } else {
                            ReflectUtils.invokeSetter(entity, propertyName, val);
                        }
                    }
                }
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * データ型チェック
     *
     * @param checkValue
     * @param inputValueType
     * @param colName
     * @return
     */
    public String checkInputValueType(String checkValue, Excel.InputValueType inputValueType, String colName) {
        String messageString = StringUtils.EMPTY;
        boolean isValid = true;
        String[] params = new String[3];
        params[0] = colName;

        if (StringUtils.isNotEmpty(checkValue)) {
            String defaultMsgCode = StringUtils.EMPTY;
            switch (inputValueType) {
                case ALPHABET_NUMERIC_SYMBOL:
                    // 英数字記号のみで入力してください。
                    isValid = ValidatorUtils.isAlphabetNumericSymbol(StringUtils.trimLeftBlank(checkValue));
                    params[1] = "{halfAlphabetNumeric}";
                    defaultMsgCode = "{EM004}";
                    break;
                case ALPHABET_NUMERIC:
                    // 英数字のみで入力してください。
                    isValid = ValidatorUtils.isAlphabetNumeric(StringUtils.trimLeftBlank(checkValue));
                    params[1] = "{halfAlphabetNumeric}";
                    defaultMsgCode = "{EM004}";
                    break;
                case NUMERIC:
                    // 半角数字のみで入力してください
                    isValid = ValidatorUtils.isNumber(StringUtils.trimLeftBlank(checkValue));
                    params[1] = "{halfNumeric}";
                    defaultMsgCode = "{EM004}";
                    break;
                case CHAR_COUNT:
                    // 文字数は1～9で入力してください。
                    isValid = ValidatorUtils.isCharCount(StringUtils.trimLeftBlank(checkValue));
                    defaultMsgCode = "{EM001}";
                    break;
                case ORDER_NO:
                    // 並び順は1～10で入力してください。
                    isValid = ValidatorUtils.isOrderNo(StringUtils.trimLeftBlank(checkValue));
                    defaultMsgCode = "{EM002}";
                    break;
                case CHAR_TYPE:
                    // {0}が{1}以外になる。
                    isValid = ValidatorUtils.isCharType(StringUtils.trimLeftBlank(checkValue));
                    params[1] = "{charTypeNumber}";
                    defaultMsgCode = "{E0037}";
                    break;
                case IS_INPUT:
                    // {0}が{1}以外になる。
                    isValid = ValidatorUtils.isInput(StringUtils.trimLeftBlank(checkValue));
                    params[1] = "{isInputNumber}";
                    defaultMsgCode = "{E0037}";
                    break;
                default:
                    break;
            }

            if (!isValid) {
                messageString = SpringUtils.getBean(IValidatorService.class).getErrorMessage(defaultMsgCode, params);
            }
        }

        return messageString;
    }


    /**
     * リストデータソースをExcelシートにインポート
     *
     * @param list      エクスポートデータのコレクション
     * @param sheetName シートの名称
     * @return 結果
     */
    public AjaxResult exportExcel(List<T> list, String sheetName) {
        return exportExcel(list, sheetName, StringUtils.EMPTY);
    }

    /**
     * リストデータソースをExcelシートにインポート
     *
     * @param list      エクスポートデータのコレクション
     * @param sheetName シートの名称
     * @param title     タイトル
     * @return 結果
     */
    public AjaxResult exportExcel(List<T> list, String sheetName, String title) {
        this.init(list, sheetName, title, Excel.Type.EXPORT);
        return exportExcel();
    }

    /**
     * リストデータソースをExcelシートにインポート
     *
     * @param response  返却データ
     * @param list      エクスポートデータのコレクション
     * @param sheetName シートの名称
     * @return 結果
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String sheetName) {
        exportExcel(response, list, sheetName, StringUtils.EMPTY);
    }

    /**
     * リストデータソースをExcelシートにインポート
     *
     * @param response  返却データ
     * @param list      エクスポートデータのコレクション
     * @param sheetName シートの名称
     * @param title     タイトル
     * @return 結果
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String sheetName, String title) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        this.init(list, sheetName, title, Type.EXPORT);
        exportExcel(response);
    }

    /**
     * リストデータソースをExcelシートにインポート
     *
     * @param sheetName シートの名称
     * @return 結果
     */
    public AjaxResult importTemplateExcel(String sheetName) {
        return importTemplateExcel(sheetName, StringUtils.EMPTY);
    }

    /**
     * リストデータソースをExcelシートにインポート
     *
     * @param sheetName シートの名称
     * @param title     タイトル
     * @return 結果
     */
    public AjaxResult importTemplateExcel(String sheetName, String title) {
        this.init(null, sheetName, title, Type.IMPORT);
        return exportExcel();
    }

    /**
     * リストデータソースをExcelシートにインポート
     *
     * @param sheetName シートの名称
     * @return 結果
     */
    public void importTemplateExcel(HttpServletResponse response, String sheetName) {
        importTemplateExcel(response, sheetName, StringUtils.EMPTY);
    }

    /**
     * リストデータソースをExcelシートにインポート
     *
     * @param sheetName シートの名称
     * @param title     タイトル
     * @return 結果
     */
    public void importTemplateExcel(HttpServletResponse response, String sheetName, String title) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        this.init(null, sheetName, title, Type.IMPORT);
        exportExcel(response);
    }

    /**
     * リストデータソースをExcelシートにインポート
     *
     * @return 結果
     */
    public void exportExcel(HttpServletResponse response) {
        try {
            writeSheet();
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("Excelエクスポート例外{}", e.getMessage());
        } finally {
            IOUtils.closeQuietly(wb);
        }
    }

    /**
     * リストデータソースをExcelシートにインポート
     *
     * @return 結果
     */
    public AjaxResult exportExcel() {
        OutputStream out = null;
        try {
            writeSheet();
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return AjaxResult.success(filename);
        } catch (Exception e) {
            log.error("Excelエクスポート例外{}", e.getMessage());
            throw new UtilException("Excelエクスポートに失敗しました。ウェブサイト管理者にお問い合わせください！");
        } finally {
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * シートにデータを書き込む
     */
    public void writeSheet() {
        // 何個のシートがあるか取得
        int sheetNo = Math.max(1, (int) Math.ceil(list.size() * 1.0 / SHEET_SIZE));
        for (int index = 0; index < sheetNo; index++) {
            createSheet(sheetNo, index);

            // 一行を作成
            Row row = sheet.createRow(rownum);
            int column = 0;
            // 各フィールドの列ヘッダーネームを書き込む
            for (Object[] os : fields) {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                if (Collection.class.isAssignableFrom(field.getType())) {
                    for (Field subField : subFields) {
                        Excel subExcel = subField.getAnnotation(Excel.class);
                        this.createHeadCell(subExcel, row, column++);
                    }
                } else {
                    this.createHeadCell(excel, row, column++);
                }
            }
            if (Type.EXPORT.equals(type)) {
                fillExcelData(index, row);
                addStatisticsRow();
            }
        }
    }

    /**
     * Excelデータを埋める
     *
     * @param index インデックス
     * @param row   セル行
     */
    @SuppressWarnings("unchecked")
    public void fillExcelData(int index, Row row) {
        int startNo = index * SHEET_SIZE;
        int endNo = Math.min(startNo + SHEET_SIZE, list.size());
        // タイトル行の後から開始
        int currentRowNum = rownum + 1;

        for (int i = startNo; i < endNo; i++) {
            row = sheet.createRow(currentRowNum);
            T vo = (T) list.get(i);
            int column = 0;
            int maxSubListSize = getCurrentMaxSubListSize(vo);
            for (Object[] os : fields) {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                if (Collection.class.isAssignableFrom(field.getType())) {
                    try {
                        Collection<?> subList = (Collection<?>) getTargetValue(vo, field, excel);
                        if (subList != null && !subList.isEmpty()) {
                            int subIndex = 0;
                            for (Object subVo : subList) {
                                Row subRow = sheet.getRow(currentRowNum + subIndex);
                                if (subRow == null) {
                                    subRow = sheet.createRow(currentRowNum + subIndex);
                                }

                                int subColumn = column;
                                for (Field subField : subFields) {
                                    Excel subExcel = subField.getAnnotation(Excel.class);
                                    addCell(subExcel, subRow, (T) subVo, subField, subColumn++);
                                }
                                subIndex++;
                            }
                            column += subFields.size();
                        }
                    } catch (Exception e) {
                        log.error("サブリストデータの埋め込みに失敗", e);
                    }
                } else {
                    // セルを作成して値を設定
                    addCell(excel, row, vo, field, column);
                    if (maxSubListSize > 1 && excel.needMerge()) {
                        sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum + maxSubListSize - 1, column, column));
                    }
                    column++;
                }
            }
            currentRowNum += maxSubListSize;
        }
    }

    /**
     * サブリストの最大サイズを取得
     */
    private int getCurrentMaxSubListSize(T vo) {
        int maxSubListSize = 1;
        for (Object[] os : fields) {
            Field field = (Field) os[0];
            if (Collection.class.isAssignableFrom(field.getType())) {
                try {
                    Collection<?> subList = (Collection<?>) getTargetValue(vo, field, (Excel) os[1]);
                    if (subList != null && !subList.isEmpty()) {
                        maxSubListSize = Math.max(maxSubListSize, subList.size());
                    }
                } catch (Exception e) {
                    log.error("サブリストサイズの取得に失敗", e);
                }
            }
        }
        return maxSubListSize;
    }

    /**
     * シートスタイルを作成
     *
     * @param wb ワークブックオブジェクト
     * @return スタイルリスト
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        // 各レコードを書き込む 各レコードはExcelシートの一行に対応
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        style.setFont(titleFont);
        DataFormat dataFormat = wb.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("@"));
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font totalFont = wb.createFont();
        totalFont.setFontName("Arial");
        totalFont.setFontHeightInPoints((short) 10);
        style.setFont(totalFont);
        styles.put("total", style);

        styles.putAll(annotationHeaderStyles(wb, styles));

        styles.putAll(annotationDataStyles(wb));

        return styles;
    }

    /**
     * Excelアノテーションに基づいてヘッダースタイルを作成
     *
     * @param wb ワークブックオブジェクト
     * @return カスタムスタイルリスト
     */
    private Map<String, CellStyle> annotationHeaderStyles(Workbook wb, Map<String, CellStyle> styles) {
        Map<String, CellStyle> headerStyles = new HashMap<String, CellStyle>();
        for (Object[] os : fields) {
            Excel excel = (Excel) os[1];
            String key = StringUtils.format("header_{}_{}", excel.headerColor(), excel.headerBackgroundColor());
            if (!headerStyles.containsKey(key)) {
                CellStyle style = wb.createCellStyle();
                style.cloneStyleFrom(styles.get("data"));
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                style.setFillForegroundColor(excel.headerBackgroundColor().index);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font headerFont = wb.createFont();
                headerFont.setFontName("Arial");
                headerFont.setFontHeightInPoints((short) 10);
                headerFont.setBold(true);
                headerFont.setColor(excel.headerColor().index);
                style.setFont(headerFont);
                // セルヘッダーのテキスト形式を設定
                DataFormat dataFormat = wb.createDataFormat();
                style.setDataFormat(dataFormat.getFormat("@"));
                headerStyles.put(key, style);
            }
        }
        return headerStyles;
    }

    /**
     * Excelアノテーションに基づいてデータスタイルを作成
     *
     * @param wb ワークブックオブジェクト
     * @return カスタムスタイルリスト
     */
    private Map<String, CellStyle> annotationDataStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        for (Object[] os : fields) {
            Field field = (Field) os[0];
            Excel excel = (Excel) os[1];
            if (Collection.class.isAssignableFrom(field.getType())) {
                ParameterizedType pt = (ParameterizedType) field.getGenericType();
                Class<?> subClass = (Class<?>) pt.getActualTypeArguments()[0];
                List<Field> subFields = FieldUtils.getFieldsListWithAnnotation(subClass, Excel.class);
                for (Field subField : subFields) {
                    Excel subExcel = subField.getAnnotation(Excel.class);
                    annotationDataStyles(styles, subField, subExcel);
                }
            } else {
                annotationDataStyles(styles, field, excel);
            }
        }
        return styles;
    }

    /**
     * Excelアノテーションに基づいてデータスタイルを作成
     *
     * @param styles カスタムスタイルリスト
     * @param field  属性列情報
     * @param excel  アノテーション情報
     */
    public void annotationDataStyles(Map<String, CellStyle> styles, Field field, Excel excel) {
        String key = StringUtils.format("data_{}_{}_{}_{}_{}", excel.align(), excel.color(), excel.backgroundColor(), excel.cellType(), excel.wrapText());
        if (!styles.containsKey(key)) {
            CellStyle style = wb.createCellStyle();
            style.setAlignment(excel.align());
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderRight(BorderStyle.THIN);
            style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderLeft(BorderStyle.THIN);
            style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderTop(BorderStyle.THIN);
            style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderBottom(BorderStyle.THIN);
            style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(excel.backgroundColor().getIndex());
            style.setWrapText(excel.wrapText());
            Font dataFont = wb.createFont();
            dataFont.setFontName("Arial");
            dataFont.setFontHeightInPoints((short) 10);
            dataFont.setColor(excel.color().index);
            style.setFont(dataFont);
            if (ColumnType.TEXT == excel.cellType()) {
                DataFormat dataFormat = wb.createDataFormat();
                style.setDataFormat(dataFormat.getFormat("@"));
            }
            styles.put(key, style);
        }
    }

    /**
     * セルを作成
     */
    public Cell createHeadCell(Excel attr, Row row, int column) {
        // 列を作成
        Cell cell = row.createCell(column);
        // 列情報を書き込む
        cell.setCellValue(attr.name());
        setDataValidation(attr, row, column);
        cell.setCellStyle(styles.get(StringUtils.format("header_{}_{}", attr.headerColor(), attr.headerBackgroundColor())));
        if (isSubList()) {
            // 既定のスタイルを設定し、マージされたセルスタイルが無効になるのを防ぐ
            sheet.setDefaultColumnStyle(column, styles.get(StringUtils.format("data_{}_{}_{}_{}_{}", attr.align(), attr.color(), attr.backgroundColor(), attr.cellType(), attr.wrapText())));
            if (attr.needMerge()) {
                sheet.addMergedRegion(new CellRangeAddress(rownum - 1, rownum, column, column));
            }
        }
        return cell;
    }

    /**
     * セル情報を設定
     *
     * @param value セル値
     * @param attr  アノテーション関連
     * @param cell  セル情報
     */
    public void setCellVo(Object value, Excel attr, Cell cell) {
        if (ColumnType.STRING == attr.cellType() || ColumnType.TEXT == attr.cellType()) {
            String cellValue = Convert.toStr(value);
            // 式トリガー文字 =-+@ で始まるセルの場合、CSVインジェクションを防ぐためにタブ文字を前に付ける。
            if (StringUtils.startsWithAny(cellValue, FORMULA_STR)) {
                cellValue = RegExUtils.replaceFirst(cellValue, FORMULA_REGEX_STR, "\t$0");
            }
            if (value instanceof Collection && StringUtils.equals("[]", cellValue)) {
                cellValue = StringUtils.EMPTY;
            }
            cell.setCellValue(StringUtils.isNull(cellValue) ? attr.defaultValue() : cellValue + attr.suffix());
        } else if (ColumnType.NUMERIC == attr.cellType()) {
            if (StringUtils.isNotNull(value)) {
                cell.setCellValue(StringUtils.contains(Convert.toStr(value), ".") ? Convert.toDouble(value) : Convert.toInt(value));
            }
        } else if (ColumnType.IMAGE == attr.cellType()) {
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) cell.getColumnIndex(), cell.getRow().getRowNum(), (short) (cell.getColumnIndex() + 1), cell.getRow().getRowNum() + 1);
            String imagePath = Convert.toStr(value);
            if (StringUtils.isNotEmpty(imagePath)) {
                byte[] data = ImageUtils.getImage(imagePath);
                getDrawingPatriarch(cell.getSheet()).createPicture(anchor,
                        cell.getSheet().getWorkbook().addPicture(data, getImageType(data)));
            }
        }
    }

    /**
     * 画像タイプを取得し、画像挿入タイプを設定
     */
    public int getImageType(byte[] value) {
        String type = FileTypeUtils.getFileExtendName(value);
        if ("JPG".equalsIgnoreCase(type)) {
            return Workbook.PICTURE_TYPE_JPEG;
        } else if ("PNG".equalsIgnoreCase(type)) {
            return Workbook.PICTURE_TYPE_PNG;
        }
        return Workbook.PICTURE_TYPE_JPEG;
    }

    /**
     * セルスタイルを設定
     */
    public void setDataValidation(Excel attr, Row row, int column) {
        if (attr.name().indexOf("注：") >= 0) {
            sheet.setColumnWidth(column, 6000);
        } else {
            // 列幅を設定
            sheet.setColumnWidth(column, (int) ((attr.width() + 0.72) * 256));
        }
        if (StringUtils.isNotEmpty(attr.prompt()) || attr.combo().length > 0 || attr.comboReadDict()) {
            String[] comboArray = attr.combo();
            if (attr.comboReadDict()) {
                if (!sysDictMap.containsKey("combo_" + attr.dictType())) {
                    String labels = DictUtils.getDictLabels(attr.dictType());
                    sysDictMap.put("combo_" + attr.dictType(), labels);
                }
                String val = sysDictMap.get("combo_" + attr.dictType());
                comboArray = StringUtils.split(val, DictUtils.SEPARATOR);
            }
            if (comboArray.length > 15 || StringUtils.join(comboArray).length() > 255) {
                // 下拉数が15以上または文字列長さが255を超える場合、新しいシートに保存して、生成されたテンプレートの下拉値を取得できない問題を防ぐ
                setXssfValidationWithHidden(sheet, comboArray, attr.prompt(), 1, 100, column, column);
            } else {
                // ヒント情報または選択のみで入力できない列内容.
                setPromptOrValidation(sheet, comboArray, attr.prompt(), 1, 100, column, column);
            }
        }
    }

    /**
     * セルを追加
     */
    public Cell addCell(Excel attr, Row row, T vo, Field field, int column) {
        Cell cell = null;
        try {
            // 行高を設定
            row.setHeight(maxHeight);
            // Excelで設定されている場合にのみエクスポートするかどうかを決定します。ユーザーが入力することを希望する列もあります。
            if (attr.isExport()) {
                // セルを作成
                cell = row.createCell(column);
                if (isSubListValue(vo) && getListCellValue(vo).size() > 1 && attr.needMerge()) {
                    if (subMergedLastRowNum >= subMergedFirstRowNum) {
                        sheet.addMergedRegion(new CellRangeAddress(subMergedFirstRowNum, subMergedLastRowNum, column, column));
                    }
                }
                cell.setCellStyle(styles.get(StringUtils.format("data_{}_{}_{}_{}_{}", attr.align(), attr.color(), attr.backgroundColor(), attr.cellType(), attr.wrapText())));

                // オブジェクトのプロパティを読み取るための使用
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                String readConverterExp = attr.readConverterExp();
                String separator = attr.separator();
                String dictType = attr.dictType();
                if (StringUtils.isNotEmpty(dateFormat) && StringUtils.isNotNull(value)) {
                    cell.setCellValue(parseDateToStr(dateFormat, value));
                } else if (StringUtils.isNotEmpty(readConverterExp) && StringUtils.isNotNull(value)) {
                    cell.setCellValue(convertByExp(Convert.toStr(value), readConverterExp, separator));
                } else if (StringUtils.isNotEmpty(dictType) && StringUtils.isNotNull(value)) {
                    if (!sysDictMap.containsKey(dictType + value)) {
                        String lable = convertDictByExp(Convert.toStr(value), dictType, separator);
                        sysDictMap.put(dictType + value, lable);
                    }
                    cell.setCellValue(sysDictMap.get(dictType + value));
                } else if (value instanceof BigDecimal && -1 != attr.scale()) {
                    cell.setCellValue((((BigDecimal) value).setScale(attr.scale(), attr.roundingMode())).doubleValue());
                } else if (!attr.handler().equals(ExcelHandlerAdapter.class)) {
                    cell.setCellValue(dataFormatHandlerAdapter(value, attr, cell));
                } else {
                    // セルタイプを設定
                    setCellVo(value, attr, cell);
                }
                addStatisticsData(column, Convert.toStr(value), attr);
            }
        } catch (Exception e) {
            log.error("Excelのエクスポートに失敗しました{}", e);
        }
        return cell;
    }

    /**
     * POI XSSFSheet セルのヒントまたは選択ボックスを設定
     *
     * @param sheet         シート
     * @param textlist      ドロップダウンリストに表示される内容
     * @param promptContent ヒント内容
     * @param firstRow      開始行
     * @param endRow        終了行
     * @param firstCol      開始列
     * @param endCol        終了列
     */
    public void setPromptOrValidation(Sheet sheet, String[] textlist, String promptContent, int firstRow, int endRow,
                                      int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = textlist.length > 0 ? helper.createExplicitListConstraint(textlist) : helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        if (StringUtils.isNotEmpty(promptContent)) {
            // ヒント情報を設定した場合は、マウスを当てるとヒントが表示されます。
            dataValidation.createPromptBox("", promptContent);
            dataValidation.setShowPromptBox(true);
        }
        // Excelの互換性問題を処理
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * 一部の列の値が事前に定義されたデータのみ入力できるように設定し、ドロップダウンリストを表示します（大量のドロップダウンリストに対応）.
     *
     * @param sheet         設定するシート
     * @param textlist      ドロップダウンリストに表示される内容
     * @param promptContent ヒント内容
     * @param firstRow      開始行
     * @param endRow        終了行
     * @param firstCol      開始列
     * @param endCol        終了列
     */
    public void setXssfValidationWithHidden(Sheet sheet, String[] textlist, String promptContent, int firstRow, int endRow, int firstCol, int endCol) {
        String hideSheetName = "combo_" + firstCol + "_" + endCol;
        // ドロップダウンメニューのデータを保存するために使用
        Sheet hideSheet = wb.createSheet(hideSheetName);
        for (int i = 0; i < textlist.length; i++) {
            hideSheet.createRow(i).createCell(0).setCellValue(textlist[i]);
        }
        // 他のセルから参照できるように名前を作成
        Name name = wb.createName();
        name.setNameName(hideSheetName + "_data");
        name.setRefersToFormula(hideSheetName + "!$A$1:$A$" + textlist.length);
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // ドロップダウンリストの内容をロード
        DataValidationConstraint constraint = helper.createFormulaListConstraint(hideSheetName + "_data");
        // データ有効性がどのセルにロードされるかを設定します。4つのパラメータはそれぞれ開始行、終了行、開始列、終了列です。
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // データ有効性オブジェクト
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        if (StringUtils.isNotEmpty(promptContent)) {
            // ヒント情報を設定した場合は、マウスを当てるとヒントが表示されます。
            dataValidation.createPromptBox("", promptContent);
            dataValidation.setShowPromptBox(true);
        }
        // Excelの互換性問題を処理
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }

        sheet.addValidationData(dataValidation);
        // hiddenSheetを非表示に設定
        wb.setSheetHidden(wb.getSheetIndex(hideSheet), true);
    }

    /**
     * データハンドラー
     *
     * @param value データ値
     * @param excel データ注釈
     * @return
     */
    public String dataFormatHandlerAdapter(Object value, Excel excel, Cell cell) {
        try {
            Object instance = excel.handler().getDeclaredConstructor().newInstance();
            Method formatMethod = excel.handler().getMethod("format", new Class[]{Object.class, String[].class, Cell.class, Workbook.class});
            value = formatMethod.invoke(instance, value, excel.args(), cell, this.wb);
        } catch (Exception e) {
            log.error("データをフォーマットできません " + excel.handler(), e.getMessage());
        }
        return Convert.toStr(value);
    }

    /**
     * 合計統計情報
     */
    private void addStatisticsData(Integer index, String text, Excel entity) {
        if (entity != null && entity.isStatistics()) {
            Double temp = 0D;
            if (!statistics.containsKey(index)) {
                statistics.put(index, temp);
            }
            try {
                temp = Double.valueOf(text);
            } catch (NumberFormatException e) {
            }
            statistics.put(index, statistics.get(index) + temp);
        }
    }

    /**
     * 統計行を作成
     */
    public void addStatisticsRow() {
        if (statistics.size() > 0) {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Set<Integer> keys = statistics.keySet();
            Cell cell = row.createCell(0);
            cell.setCellStyle(styles.get("total"));
            cell.setCellValue("合計");

            for (Integer key : keys) {
                cell = row.createCell(key);
                cell.setCellStyle(styles.get("total"));
                cell.setCellValue(DOUBLE_FORMAT.format(statistics.get(key)));
            }
            statistics.clear();
        }
    }

    /**
     * ファイル名をエンコード
     */
    public String encodingFilename(String filename) {
        filename = UUID.randomUUID() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * ダウンロードパスを取得
     *
     * @param filename ファイル名
     */
    public String getAbsoluteFile(String filename) {
        String downloadPath = AppConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * Beanのプロパティ値を取得
     *
     * @param vo    エンティティオブジェクト
     * @param field フィールド
     * @param excel アノテーション
     * @return 最終的なプロパティ値
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception {
        field.setAccessible(true);
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr())) {
            String target = excel.targetAttr();
            if (target.contains(".")) {
                String[] targets = target.split("[.]");
                for (String name : targets) {
                    o = getValue(o, name);
                }
            } else {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * クラスのプロパティのgetメソッド形式で値を取得
     *
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception {
        if (StringUtils.isNotNull(o) && StringUtils.isNotEmpty(name)) {
            Class<?> clazz = o.getClass();
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            o = field.get(o);
        }
        return o;
    }

    /**
     * 定義されたすべてのフィールドを取得
     */
    private void createExcelField() {
        this.fields = getFields();
        this.fields = this.fields.stream().sorted(Comparator.comparing(objects -> ((Excel) objects[1]).sort())).collect(Collectors.toList());
        this.maxHeight = getRowHeight();
    }

    /**
     * フィールドアノテーション情報を取得
     */
    public List<Object[]> getFields() {
        List<Object[]> fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (StringUtils.isNotEmpty(includeFields)) {
            for (Field field : tempFields) {
                if (ArrayUtils.contains(this.includeFields, field.getName()) || field.isAnnotationPresent(Excels.class)) {
                    addField(fields, field);
                }
            }
        } else if (StringUtils.isNotEmpty(excludeFields)) {
            for (Field field : tempFields) {
                if (!ArrayUtils.contains(this.excludeFields, field.getName())) {
                    addField(fields, field);
                }
            }
        } else {
            for (Field field : tempFields) {
                addField(fields, field);
            }
        }
        return fields;
    }

    /**
     * フィールド情報を追加
     */
    public void addField(List<Object[]> fields, Field field) {
        // 単一アノテーション
        if (field.isAnnotationPresent(Excel.class)) {
            Excel attr = field.getAnnotation(Excel.class);
            if (attr != null && (attr.type() == Type.ALL || attr.type() == type)) {
                fields.add(new Object[]{field, attr});
            }
            if (Collection.class.isAssignableFrom(field.getType())) {
                subMethod = getSubMethod(field.getName(), clazz);
                ParameterizedType pt = (ParameterizedType) field.getGenericType();
                Class<?> subClass = (Class<?>) pt.getActualTypeArguments()[0];
                this.subFields = FieldUtils.getFieldsListWithAnnotation(subClass, Excel.class);
            }
        }

        // 複数アノテーション
        if (field.isAnnotationPresent(Excels.class)) {
            Excels attrs = field.getAnnotation(Excels.class);
            Excel[] excels = attrs.value();
            for (Excel attr : excels) {
                if (StringUtils.isNotEmpty(includeFields)) {
                    if (ArrayUtils.contains(this.includeFields, field.getName() + "." + attr.targetAttr())
                            && (attr != null && (attr.type() == Type.ALL || attr.type() == type))) {
                        fields.add(new Object[]{field, attr});
                    }
                } else {
                    if (!ArrayUtils.contains(this.excludeFields, field.getName() + "." + attr.targetAttr())
                            && (attr != null && (attr.type() == Type.ALL || attr.type() == type))) {
                        fields.add(new Object[]{field, attr});
                    }
                }
            }
        }
    }

    /**
     * アノテーションから最大行高を取得
     */
    public short getRowHeight() {
        double maxHeight = 0;
        for (Object[] os : this.fields) {
            Excel excel = (Excel) os[1];
            maxHeight = Math.max(maxHeight, excel.height());
        }
        return (short) (maxHeight * 20);
    }

    /**
     * ワークブックを作成
     */
    public void createWorkbook() {
        this.wb = new SXSSFWorkbook(500);
        this.sheet = wb.createSheet();
        wb.setSheetName(0, sheetName);
        this.styles = createStyles(wb);
    }

    /**
     * シートを作成
     *
     * @param sheetNo シート数
     * @param index   インデックス
     */
    public void createSheet(int sheetNo, int index) {
        // シートの名前を設定.
        if (sheetNo > 1 && index > 0) {
            this.sheet = wb.createSheet();
            this.createTitle();
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * セル値を取得
     *
     * @param row    取得する行
     * @param column 取得するセルの列番号
     * @return セル値
     */
    public Object getCellValue(Row row, int column) {
        if (row == null) {
            return row;
        }
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (StringUtils.isNotNull(cell)) {
                if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
                    val = cell.getNumericCellValue();
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // POI Excel 日付形式変換
                        val = DateUtil.getJavaDate((Double) val);
                    } else {
                        if ((Double) val % 1 != 0) {
                            val = new BigDecimal(val.toString());
                        } else {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                } else if (cell.getCellType() == CellType.STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.BOOLEAN) {
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellType() == CellType.ERROR) {
                    val = cell.getErrorCellValue();
                }

            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }

    /**
     * 行が空かどうかを判断する
     *
     * @param row 判断する行
     * @return
     */
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 異なるタイプの日付オブジェクトをフォーマットする
     *
     * @param dateFormat 日付フォーマット
     * @param val        フォーマットされる日付オブジェクト
     * @return フォーマット後の日付文字列
     */
    public String parseDateToStr(String dateFormat, Object val) {
        if (val == null) {
            return "";
        }
        String str;
        if (val instanceof Date) {
            str = DateUtils.parseDateToStr(dateFormat, (Date) val);
        } else if (val instanceof LocalDateTime) {
            str = DateUtils.parseDateToStr(dateFormat, DateUtils.toDate((LocalDateTime) val));
        } else if (val instanceof LocalDate) {
            str = DateUtils.parseDateToStr(dateFormat, DateUtils.toDate((LocalDate) val));
        } else {
            str = val.toString();
        }
        return str;
    }

    /**
     * オブジェクトにサブリストがあるかどうか
     */
    public boolean isSubList() {
        return StringUtils.isNotNull(subFields) && subFields.size() > 0;
    }

    /**
     * オブジェクトにサブリストがあり、集合が空でないかどうか
     */
    public boolean isSubListValue(T vo) {
        return StringUtils.isNotNull(subFields) && subFields.size() > 0 && StringUtils.isNotNull(getListCellValue(vo)) && getListCellValue(vo).size() > 0;
    }

    /**
     * 集合の値を取得する
     */
    public Collection<?> getListCellValue(Object obj) {
        Object value;
        try {
            value = subMethod.invoke(obj, new Object[]{});
        } catch (Exception e) {
            return new ArrayList<Object>();
        }
        return (Collection<?>) value;
    }

    /**
     * オブジェクトのサブリストメソッドを取得する
     *
     * @param name      名称
     * @param pojoClass クラスオブジェクト
     * @return サブリストメソッド
     */
    public Method getSubMethod(String name, Class<?> pojoClass) {
        StringBuffer getMethodName = new StringBuffer("get");
        getMethodName.append(name.substring(0, 1).toUpperCase());
        getMethodName.append(name.substring(1));
        Method method = null;
        try {
            method = pojoClass.getMethod(getMethodName.toString(), new Class[]{});
        } catch (Exception e) {
            log.error("オブジェクト取得異常{}", e.getMessage());
        }
        return method;
    }

}
