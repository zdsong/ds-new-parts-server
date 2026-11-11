package jp.newpart.common.dev;

import jp.newpart.common.utils.ValueUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelToSQLGenerator {
    static class IndexInfo {
        boolean isUnique;
        List<String> fields;
    }

    public static final String OWNER_TO_USER = "ew";
    public static final String CONST_ECODE = "○";
	public static void main(String[] args) {
        // 文件路径配置
        String excelFilePath = "/Users/apple/mys/project/テーブル設計書_新部品管理システム｜外部倉庫管理v1.0.xlsx";; // 输入的Excel文件l文件路径
        String outputSqlPath = "/Users/apple/mys/project/autocodes/create_table_sql.sql"; // 输出的SQL文件路径
        String outputIndexPath = "/Users/apple/mys/project/autocodes/create-index_structure_sql.sql"; // 输出的SQL文件路径


        generatePostgresqlDDL(excelFilePath, outputSqlPath);
       // generatePostgresqIndex(excelFilePath, outputIndexPath);
    }

    public static void generatePostgresqIndex(String excelFilePath, String outputSqlPath) {
        StringBuilder finalSQL = new StringBuilder();
        Path path = Paths.get(outputSqlPath);
        if (Files.exists(path)) {
            try {
                // 如果文件存在，则删除它
                boolean deleted = Files.deleteIfExists(path);
                if (deleted) {
                    System.out.println("文件已成功删除: " + path);
                } else {
                    System.out.println("尝试删除文件但失败: " + path);
                }
            } catch (IOException e) {
                // 处理可能发生的IO异常
                System.err.println("删除文件时发生错误: " + e.getMessage());
            }
        } else {
            System.out.println("文件不存在: " + path);
        }

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = getWorkbook(fis, excelFilePath);
             FileWriter writer = new FileWriter(outputSqlPath)) {


            // 遍历每个Sheet，假设每个Sheet定义一张表
            for (int sheetIndex = 4; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {

                Sheet sheet = workbook.getSheetAt(sheetIndex);

                Row rowTable = sheet.getRow(1);

                String tableName = rowTable.getCell(27).getStringCellValue(); // 使用工作表名作为表名

                // 获取列定义（假设数据从第2行开始，第1行是标题）
                StringBuilder columnDefinitions = new StringBuilder();
                StringBuilder columnComments = new StringBuilder();
                boolean hasPrimaryKey = false;
                String primaryKeyColumn = null;

                Map<Integer, IndexInfo> indexMap = new HashMap<>();

                int startRow = 8; // 通常第1行 (索引0) 是标题行，数据从第2行开始
                for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null || isEmptyRow(row)) continue;

                    // 解析单元格，假设列顺序为：字段名, 类型, 长度, 是否主键, 是否允许空, 默认值, 注释
                    String fieldName = getCellValueSafe(row.getCell(12));
                    if(fieldName == null || fieldName =="") {
                        continue;
                    }

                    for (int colIndex = 34; colIndex <= 43; colIndex++) {
                        String indexValue = getCellValueSafe(row.getCell(colIndex));
                        if (indexValue != null && !indexValue.trim().isEmpty()) {
                            // 获取索引编号：0对应34列，1对应35列，...，9对应43列
                            int indexNum = colIndex - 34;

                            // 判断是否为唯一索引
                            boolean isUnique = "U".equalsIgnoreCase(indexValue.trim());

                            // 获取或创建索引信息对象
                            IndexInfo indexInfo = indexMap.get(indexNum);
                            if (indexInfo == null) {
                                indexInfo = new IndexInfo();
                                indexInfo.isUnique = isUnique;
                                indexInfo.fields = new ArrayList<>();
                                indexMap.put(indexNum, indexInfo);
                            } else {
                                // 检查索引类型是否一致（同一索引应该都是唯一或都不是唯一）
                                if (indexInfo.isUnique != isUnique) {
                                    System.err.println("警告: インデックス " + indexNum + " 複数のタイプが混在している場合は、最後に検出されたタイプが使用されます。");
                                    indexInfo.isUnique = isUnique;
                                }
                            }

                            // 添加字段到对应索引
                            indexInfo.fields.add(fieldName);
                        }
                    }

                    String fieldType = getCellValueSafe(row.getCell(21));
                    String fieldLength = getCellValueSafe(row.getCell(24));
                    String fieldDit = getCellValueSafe(row.getCell(26));
                    String isPrimaryKey = getCellValueSafe(row.getCell(33));
                    String isNullable = getCellValueSafe(row.getCell(28));
                    String defaultValue = getCellValueSafe(row.getCell(30));
                    String fieldComment = getCellValueSafe(row.getCell(2));

                    String index2 = getCellValueSafe(row.getCell(32));

                    if (fieldName == null || fieldName.trim().isEmpty()) {
                        break; // 遇到空行则结束当前表的解析
                    }

                    // 开始拼接单个字段定义
                    StringBuilder fieldDefinition = new StringBuilder();
                    fieldDefinition.append("    \"").append(fieldName).append("\" "); // PostgreSQL 通常使用双引号

                    // 处理数据类型和长度
                    if ("varchar".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                        fieldDefinition.append("VARCHAR(").append(fieldLength).append(")");
                    } else if ("DECIMAL".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                        fieldDefinition.append("DECIMAL(").append(fieldLength).append(",").append(fieldDit).append(")");
                    } else {
                        fieldDefinition.append(fieldType); // 直接使用类型，如 INT, TEXT, DATE, TIMESTAMP
                    }
                    columnComments.append("    COMMENT ON COLUMN ");
                    columnComments.append("\"").append(tableName).append("\"");
                    columnComments.append(".");
                    columnComments.append("\"").append(fieldName).append("\"").append(" IS '").append(fieldComment).append("';");; // PostgreSQL 通常使用双引号
                    columnComments.append("\n");
                    // COMMENT ON COLUMN "public"."mast_user"."user_cd" IS '111';
                    // 处理是否为主键
                    if ("1".equalsIgnoreCase(isPrimaryKey)) {
                        hasPrimaryKey = true;
                        primaryKeyColumn = fieldName;
                    }

                    // 处理是否允许为空 (PostgreSQL 中 NOT NULL 约束)
                    if ("NO".equalsIgnoreCase(isNullable)) {
                        fieldDefinition.append(" NOT NULL");
                    }

                    // 处理默认值
                    if (defaultValue != null && !defaultValue.trim().isEmpty()) {
                        // 对字符串类型默认值加单引号，数字和函数不加
                        if (needsQuotesForDefault(fieldType)) {
                            fieldDefinition.append(" DEFAULT '").append(defaultValue).append("'");
                        } else {
                            fieldDefinition.append(" DEFAULT ").append(defaultValue);
                        }
                    }

                    columnDefinitions.append(fieldDefinition).append(",\n");

                }

                // 构建完整的 CREATE TABLE 语句
                StringBuilder createTableSQL = new StringBuilder();

                if (!indexMap.isEmpty()) {
                    createTableSQL.append("-- テーブル ").append(tableName).append(" のインデックスを作成します。\n");


                    List<Integer> indexNumbers = new ArrayList<>(indexMap.keySet());
                    Collections.sort(indexNumbers);

                    for (Integer indexNum : indexNumbers) {
                        IndexInfo indexInfo = indexMap.get(indexNum);
                        if (indexInfo != null && indexInfo.fields != null && !indexInfo.fields.isEmpty()) {
                            String indexName = "idx_" + tableName.toLowerCase() + "_" + indexNum;


                            createTableSQL.append("DROP INDEX IF EXISTS ").append(indexName).append(";\n");
                            createTableSQL.append("ALTER TABLE \"").append(tableName).append("\" \n");
                            createTableSQL.append("  OWNER TO \"").append(OWNER_TO_USER).append("\";\n");


                            if (indexInfo.isUnique) {
                                createTableSQL.append("CREATE UNIQUE INDEX \"").append(indexName)
                                        .append("\" ON \"").append(tableName).append("\" USING btree (\n");
                            } else {
                                createTableSQL.append("CREATE INDEX \"").append(indexName)
                                        .append("\" ON \"").append(tableName).append("\" USING btree (\n");
                            }
                            
                            List<String> fields = indexInfo.fields;
                            for (int i = 0; i < fields.size(); i++) {
                                if (i < fields.size() - 1) {
                                    createTableSQL.append("  \"").append(fields.get(i)).append("\"  ASC NULLS LAST,\n");
                                } else {
                                    createTableSQL.append("  \"").append(fields.get(i)).append("\"  ASC NULLS LAST\n");
                                }
                            }

                            createTableSQL.append(");\n\n");
                        }
                    }
                    createTableSQL.append("\n");
                    finalSQL.append(createTableSQL);
                }
            }

            // 将生成的SQL写入文件
            writer.write(finalSQL.toString());
            System.out.println("PostgreSQL 建表语句已生成至: " + outputSqlPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void generatePostgresqlDDL(String excelFilePath, String outputSqlPath) {
        StringBuilder finalSQL = new StringBuilder();
        Path path = Paths.get(outputSqlPath);
        if (Files.exists(path)) {
            try {
                // 如果文件存在，则删除它
                boolean deleted = Files.deleteIfExists(path);
                if (deleted) {
                    System.out.println("文件已成功删除: " + path);
                } else {
                    System.out.println("尝试删除文件但失败: " + path);
                }
            } catch (IOException e) {
                // 处理可能发生的IO异常
                System.err.println("删除文件时发生错误: " + e.getMessage());
            }
        } else {
            System.out.println("文件不存在: " + path);
        }
        
        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = getWorkbook(fis, excelFilePath);
             FileWriter writer = new FileWriter(outputSqlPath)) {
        	

            // 遍历每个Sheet，假设每个Sheet定义一张表
            for (int sheetIndex = 4; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
         
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                
                Row rowTable = sheet.getRow(1);
                
                String tableName = rowTable.getCell(27).getStringCellValue(); // 使用工作表名作为表名

                // 获取列定义（假设数据从第2行开始，第1行是标题）
                StringBuilder columnDefinitions = new StringBuilder();
                StringBuilder columnComments = new StringBuilder();
                boolean hasPrimaryKey = false;
                String primaryKeyColumn = null;

                int startRow = 8; // 通常第1行 (索引0) 是标题行，数据从第2行开始
                for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null || isEmptyRow(row)) continue;

                    // 解析单元格，假设列顺序为：字段名, 类型, 长度, 是否主键, 是否允许空, 默认值, 注释
                    String fieldName = getCellValueSafe(row.getCell(12));
                    if(fieldName == null || fieldName =="") {
                    	continue;
                    } 
                    
                    String fieldType = getCellValueSafe(row.getCell(21));
                    String fieldLength = getCellValueSafe(row.getCell(24));
                    String fieldDit = getCellValueSafe(row.getCell(26));
                    String isPrimaryKey = getCellValueSafe(row.getCell(33));
                    String isNullable = getCellValueSafe(row.getCell(28));
                    String defaultValue = getCellValueSafe(row.getCell(30));
                    String fieldComment = getCellValueSafe(row.getCell(2));
                    String fieldLengthEncode = getCellValueSafe(row.getCell(25));
                    String decrypt = getCellValueSafe(row.getCell(44));
                    if (ValueUtils.isEqual(CONST_ECODE, decrypt) && ValueUtils.isNotEmpty(fieldLengthEncode)) {
                        fieldLength = fieldLengthEncode;
                    }
                    if (fieldName == null || fieldName.trim().isEmpty()) {
                        break; // 遇到空行则结束当前表的解析
                    }

                    // 开始拼接单个字段定义
                    StringBuilder fieldDefinition = new StringBuilder();
                    fieldDefinition.append("    \"").append(fieldName).append("\" "); // PostgreSQL 通常使用双引号

                    // 处理数据类型和长度
                    if ("varchar".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                        fieldDefinition.append("VARCHAR(").append(fieldLength).append(")");
                    } else if ("DECIMAL".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                        fieldDefinition.append("DECIMAL(").append(fieldLength).append(",").append(fieldDit).append(")");
                    } else {
                        fieldDefinition.append(fieldType); // 直接使用类型，如 INT, TEXT, DATE, TIMESTAMP
                    }
                    columnComments.append("    COMMENT ON COLUMN ");
                    columnComments.append("\"").append(tableName).append("\"");
                    columnComments.append(".");
                    columnComments.append("\"").append(fieldName).append("\"").append(" IS '").append(fieldComment).append("';");; // PostgreSQL 通常使用双引号
                    columnComments.append("\n");
                    // COMMENT ON COLUMN "public"."mast_user"."user_cd" IS '111';
                    // 处理是否为主键
                    if ("1".equalsIgnoreCase(isPrimaryKey)) {
                        hasPrimaryKey = true;
                        primaryKeyColumn = fieldName;
                    }

                    // 处理是否允许为空 (PostgreSQL 中 NOT NULL 约束)
                    if ("NO".equalsIgnoreCase(isNullable)) {
                        fieldDefinition.append(" NOT NULL");
                    }

                    // 处理默认值
                    if (defaultValue != null && !defaultValue.trim().isEmpty()) {
                        // 对字符串类型默认值加单引号，数字和函数不加
                        if (needsQuotesForDefault(fieldType)) {
                            fieldDefinition.append(" DEFAULT '").append(defaultValue).append("'");
                        } else {
                            fieldDefinition.append(" DEFAULT ").append(defaultValue);
                        }
                    }

                    columnDefinitions.append(fieldDefinition).append(",\n");
                   
                }

                // 构建完整的 CREATE TABLE 语句
                if (columnDefinitions.length() > 0) {
                    StringBuilder createTableSQL = new StringBuilder();
                    
                    createTableSQL.append("\n");
                    createTableSQL.append("-- ----------------------------").append("\n");
                    createTableSQL.append("-- Table structure for ").append(tableName).append("\n");
                    createTableSQL.append("-- ----------------------------").append("\n");

                    createTableSQL.append("DROP TABLE  IF EXISTS ").append(tableName).append(";\n");

                    createTableSQL.append("CREATE TABLE ").append(tableName).append(" (\n");
                    
                    // 添加列定义（移除最后一个逗号）
                    String columnDefs = columnDefinitions.toString();
                    if (columnDefs.endsWith(",\n")) {
                        columnDefs = columnDefs.substring(0, columnDefs.length() - 2);
                    }
                    createTableSQL.append(columnDefs).append("\n");

                    // 添加表级主键约束
                    if (hasPrimaryKey && primaryKeyColumn != null) {
                        createTableSQL.append("    ,CONSTRAINT pk_").append(tableName)
                                     .append(" PRIMARY KEY (\"").append(primaryKeyColumn).append("\")\n");
                    }

                    createTableSQL.append(");\n\n");
                    createTableSQL.append("ALTER TABLE ");
                    createTableSQL.append("\"").append(tableName).append("\"");
                    createTableSQL.append(" OWNER TO ");
                    createTableSQL.append("\"").append( OWNER_TO_USER).append("\"");
                    
                    createTableSQL.append(";\n");
                    createTableSQL.append(columnComments.toString());
                    createTableSQL.append("COMMENT ON TABLE ");
                    //COMMENT ON TABLE "public"."mast_user" IS 'ユーザーマスタ';

                    createTableSQL.append("\"").append(tableName).append("\"");
                    createTableSQL.append(" IS '").append(sheet.getSheetName()).append("';");
                    createTableSQL.append("\n");
                    finalSQL.append(createTableSQL);
                }
            }

            // 将生成的SQL写入文件
            writer.write(finalSQL.toString());
            System.out.println("PostgreSQL 建表语句已生成至: " + outputSqlPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件扩展名创建合适的Workbook对象
     */
    private static Workbook getWorkbook(FileInputStream fis, String filePath) throws IOException {
        if (filePath.endsWith(".xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (filePath.endsWith(".xls")) {
            return new HSSFWorkbook(fis);
        } else {
            throw new IllegalArgumentException("不支持的文件格式");
        }
    }

    /**
     * 安全获取单元格的字符串值，处理不同类型单元格[6,7](@ref)
     */
    private static String getCellValueSafe(Cell cell) {
        if (cell == null) {
            return ""; // 如果单元格为空，返回空字符串
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // 判断是否为日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(cell.getDateCellValue());
                } else {
                    // 处理数字，避免显示为科学计数法
                    double num = cell.getNumericCellValue();
                    if (num == (long) num) {
                        return String.valueOf((long) num); // 整数去除小数点
                    } else {
                        return String.valueOf(num);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // 对于公式单元格，尝试计算公式结果，然后递归调用本方法
                try {
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    // 根据公式计算结果类型再次处理
                    switch (cellValue.getCellType()) {
                        case STRING:
                            return cellValue.getStringValue();
                        case NUMERIC:
                            return String.valueOf(cellValue.getNumberValue()).replace(".0", "");
                        case BOOLEAN:
                            return String.valueOf(cellValue.getBooleanValue());
                        default:
                            return "FORMULA_RESULT_UNKNOWN";
                    }
                } catch (Exception e) {
                    // 如果公式计算失败，退回直接读取公式字符串或缓存值
                    return cell.getCellFormula();
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * 辅助方法：处理公式单元格的计算值[6](@ref)
     */
    private static Cell getCellValueFromFormula(Cell cell) {
        // 这里可以引入 FormulaEvaluator 来计算公式结果[6](@ref)
        // 简化处理：返回单元格的缓存值或尝试计算
        return cell; // 实际应用中应使用 FormulaEvaluator
    }

    private static boolean isEmptyRow(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断该数据类型的默认值是否需要引号（字符串、日期等需要）
     */
    private static boolean needsQuotesForDefault(String fieldType) {
        if (fieldType == null) return false;
        String typeLower = fieldType.toLowerCase();
        return typeLower.contains("char") || typeLower.contains("text") || 
                typeLower.contains("date") ;
    }
}
