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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelToJavaGenerator {
    public static final String CONST_DECRYPT = "○1";

    public static void main(String[] args) {
        // 文件路径配置

        String excelFilePath = "/Users/apple/mys/project/テーブル設計書_新部品管理システム｜外部倉庫管理v1.0.xlsx";; // 输入的Excel文件l文件路径
        String outputSqlPath = "/Users/apple/mys/project/autocodes"; // 输出的SQL文件路径
        generateMapperL(excelFilePath, outputSqlPath);

    }

    public static void generateMapperL(String excelFilePath, String outputSqlPath) {

        List<String> fieldGenerateLst = new ArrayList<String>();

        fieldGenerateLst.add("create_time");
        fieldGenerateLst.add("create_by");
        fieldGenerateLst.add("create_pg_id");
        fieldGenerateLst.add("update_time");
        fieldGenerateLst.add("update_by");
        fieldGenerateLst.add("update_pg_id");

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = getWorkbook(fis, excelFilePath)) {

            // 创建输出目录（如果不存在）
            Path outputDir = Paths.get(outputSqlPath);
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            // 遍历每个Sheet，每个Sheet生成一个文件
            for (int sheetIndex = 6; sheetIndex < 10; sheetIndex++) {
            //for (int sheetIndex = 4; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
//	            if (sheetIndex > 6) {
//	                continue;
//	            }

                Sheet sheet = workbook.getSheetAt(sheetIndex);
                Row rowTable = sheet.getRow(1);

                if (rowTable == null || rowTable.getCell(27) == null) {
                    System.out.println("Sheet " + sheetIndex + " 没有找到表名信息，跳过");
                    continue;
                }
                // 表名
                String tableName = rowTable.getCell(27).getStringCellValue();
                String tablrNameHanzi = sheet.getRow(0).getCell(27).getStringCellValue();
                // 驼峰表名
                String camelTableName = convertToCamelCase(tableName);
                // 首字母大些驼峰表名
                String camelTableName2 = convertToPascalCase(tableName);

                Map<String, StringBuilder> generateBuilder = generateCodes(sheet, tableName,
                        camelTableName, camelTableName2, tablrNameHanzi, fieldGenerateLst);

                for (String key : generateBuilder.keySet()) {

                    StringBuilder outTempPathBuilder = new StringBuilder();
                    outTempPathBuilder.append(outputSqlPath + "/" + key);
                    // 创建输出目录（如果不存在）
                    String fileOutTempPath = outTempPathBuilder.toString();
                    outputDir = Paths.get(fileOutTempPath);
                    if (!Files.exists(outputDir)) {
                        Files.createDirectories(outputDir);
                    }
                    String serviceFile ="";
                    outTempPathBuilder.append("/" + camelTableName2);
                    if ("mapper".equals(key)) {
                        outTempPathBuilder.append("BaseMapper.xml");
                    } else if ("entity".equals(key)) {
                        outTempPathBuilder.append("Entity.java");
                    } else if ("interface".equals(key)) {
                        outTempPathBuilder.append("BaseMapper.java");
                    } else if ("json".equals(key)) {
                        outTempPathBuilder.append("Json.txt");
                    } else if ("message".equals(key)) {
                        outTempPathBuilder.append("Message.txt");
                    } else if ("entityVo".equals(key)) {
                        outTempPathBuilder.append("BaseVo").append(".java");
                    } else if ("entityBo".equals(key)) {
                        outTempPathBuilder.append("BaseBo").append(".java");
                    } else if ("service".equals(key)) {
                        serviceFile = fileOutTempPath + "/I"+ camelTableName2+ "BaseService.java";
//                        outTempPathBuilder.append("BaseService").append(".java");
                    } else if ("impl".equals(key)) {
                        outTempPathBuilder.append("BaseServiceImpl").append(".java");
                    } else if ("alterTable".equals(key)) {
                        outTempPathBuilder.append("AlterTable").append(".txt");
                    }
                    // 为每个Sheet创建独立的文件
                    Path filePath = Paths.get("service".equals(key) ?serviceFile : outTempPathBuilder.toString());

                    // 删除已存在的文件[6,7](@ref)
                    if (Files.exists(filePath)) {
                        try {
                            Files.deleteIfExists(filePath);
                            System.out.println("文件已删除: " + filePath);
                        } catch (IOException e) {
                            System.err.println("删除文件时发生错误: " + e.getMessage());
                        }
                    }

                    // 为当前Sheet构建Mapper内容
                    StringBuilder sheetMapperContent = generateBuilder.get(key);

                    // 为每个Sheet单独写入文件[6,8](@ref)
                    try (FileWriter writer = new FileWriter(filePath.toFile())) {
                        writer.write(sheetMapperContent.toString());
                        System.out.println("Mapper已生成: " + filePath);
                    } catch (IOException e) {
                        System.err.println("写入文件时发生错误: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为单个Sheet构建Mapper内容
     */
    private static Map<String, StringBuilder> generateCodes(Sheet sheet, String tableName, String camelTableName, String camelTableName2, String tablrNameHanzi, List<String> fieldGenerateLst) {


        Map<String, StringBuilder> generateBuilder = new HashMap<String, StringBuilder>();

        // Mapper
        StringBuilder mapperContent = new StringBuilder();
        // Result列Builder
        StringBuilder resultMapColumnSb = new StringBuilder();
        // Select field Builder
        StringBuilder selectFieldMapColumnSb = new StringBuilder();
        // Select List Builder
        StringBuilder selectListMapColumnSb = new StringBuilder();
        // Select By Id Builder
        StringBuilder selectByIdMapColumnSb = new StringBuilder();
        // Insert Builder
        StringBuilder insertBeforeMapColumnSb = new StringBuilder();
        StringBuilder insertAfterMapColumnSb = new StringBuilder();
        // Update Builder
        StringBuilder updateMapColumnSb = new StringBuilder();

        // Alter Table Builder
        StringBuilder AlterColumnMapSb = new StringBuilder();

        // Entity
        StringBuilder entityContent = new StringBuilder();
        StringBuilder entityFieldMapSb = new StringBuilder();
        StringBuilder entityContentVo = new StringBuilder();
        StringBuilder entityContentBo = new StringBuilder();
        StringBuilder entityFieldMapSbBo = new StringBuilder();
        // Json
        StringBuilder jsonFieldMapSb = new StringBuilder();

        // Message
        StringBuilder jsonMessageMapSb = new StringBuilder();

        // Interface
        StringBuilder interfaceContent = new StringBuilder();

        // Service
        StringBuilder serviceContent = new StringBuilder();
        StringBuilder serviceImpl = new StringBuilder();

        boolean hasPrimaryKey = false;
        String primaryKeyColumn = null;

        List<String> noEncodeList = new ArrayList<>();
        noEncodeList.add("user_password");
        noEncodeList.add("password");

        int startRow = 8; // 通常第1行 (索引0) 是标题行，数据从第2行开始
        for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null || isEmptyRow(row)) continue;

            // 解析单元格，假设列顺序为：字段名, 类型, 长度, 是否主键, 是否允许空, 默认值, 注释
            String fieldName = getCellValueSafe(row.getCell(12));
            if (fieldName == null || fieldName == "") {
                continue;
            }

            String camelFieldName = convertToCamelCase(fieldName);

            String fieldType = getCellValueSafe(row.getCell(21));
            String fieldLength = getCellValueSafe(row.getCell(24));
            String fieldDit = getCellValueSafe(row.getCell(26));
            String isPrimaryKey = getCellValueSafe(row.getCell(33));
            String searchField = "";
            String isSeachCode = getCellValueSafe(row.getCell(34));
            String isNullable = getCellValueSafe(row.getCell(28));
            String defaultValue = getCellValueSafe(row.getCell(30));
            String fieldComment = getCellValueSafe(row.getCell(2));
            String decrypt = getCellValueSafe(row.getCell(44));
            String fieldLengthEncode = getCellValueSafe(row.getCell(25));

            if (fieldName == null || fieldName.trim().isEmpty()) {
                break; // 遇到空行则结束当前表的解析
            }

            if (!fieldGenerateLst.contains(fieldName)) {
                // 开始拼接单个字段定义
                entityFieldMapSb.append("    /**").append("\n");
                entityFieldMapSb.append("     * ").append(fieldComment).append("\n");
                entityFieldMapSb.append("     */").append("\n");
                if(ValueUtils.isEqual("○",decrypt) && !noEncodeList.contains(fieldName)) {
                    entityFieldMapSb.append("    @EncryptedField").append("\n");
                }
                entityFieldMapSb.append("    private ");

                // 处理数据类型和长度
                if (("varchar".equalsIgnoreCase(fieldType)
                        || ("text".equalsIgnoreCase(fieldType)) && !fieldLength.isEmpty())) {
                    entityFieldMapSb.append("String");

                } else if ("DECIMAL".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                    entityFieldMapSb.append("BigDecimal");
                } else if ("timestamp".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                    entityFieldMapSb.append("Date");
                } else if ("integer".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                    entityFieldMapSb.append("Integer");
                } else {
                    entityFieldMapSb.append("String");
                }
                entityFieldMapSb.append(" ");
                entityFieldMapSb.append(camelFieldName).append(";").append("\n");
                entityFieldMapSb.append("\n");

                // 开始拼接单个字段定义
                entityFieldMapSbBo.append("    /**").append("\n");
                entityFieldMapSbBo.append("     * ").append(fieldComment).append("\n");
                entityFieldMapSbBo.append("     */").append("\n");

                if (isNullable.equals("NO") && "varchar".equalsIgnoreCase(fieldType)) {
                    entityFieldMapSbBo.append("    @LmNotBlank(params = {\"{").append(camelFieldName).append("}\"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})").append("\n");
                    ;
                }

                if ("varchar".equalsIgnoreCase(fieldType)) {
                    entityFieldMapSbBo.append("    @LmLength(min = 1, max = ").append(fieldLength).append(", params = {\"{")
                            .append(camelFieldName).append("}\", \"").append(fieldLength).append("\"}, groups = {ValidatorGroup.AddGroup.class, ValidatorGroup.UpdateGroup.class})").append("\n");
                    ;
                }

                entityFieldMapSbBo.append("    private ");
                // 处理数据类型和长度
                if (("varchar".equalsIgnoreCase(fieldType)
                        || ("text".equalsIgnoreCase(fieldType)) && !fieldLength.isEmpty())) {
                    entityFieldMapSbBo.append("String");

                } else if ("DECIMAL".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                    entityFieldMapSbBo.append("BigDecimal");
                } else if ("timestamp".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                    entityFieldMapSbBo.append("Date");
                } else if ("integer".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                    entityFieldMapSbBo.append("Integer");
                } else {
                    entityFieldMapSbBo.append("String");
                }
                entityFieldMapSbBo.append(" ");
                entityFieldMapSbBo.append(camelFieldName).append(";").append("\n");
                entityFieldMapSbBo.append("\n");

            }


            // COMMENT ON COLUMN "public"."mast_user"."user_cd" IS '111';
            // 处理是否为主键
            if ("1".equalsIgnoreCase(isPrimaryKey)) {
                hasPrimaryKey = true;
                primaryKeyColumn = fieldName;
            }

            // 处理是否为查询code
            if ("1".equalsIgnoreCase(isSeachCode)) {
                searchField = fieldName;
            }

            // 处理是否允许为空 (PostgreSQL 中 NOT NULL 约束)
            if ("NO".equalsIgnoreCase(isNullable)) {

            }

            // 处理默认值
            if (defaultValue != null && !defaultValue.trim().isEmpty()) {
                // 对字符串类型默认值加单引号，数字和函数不加
                if (needsQuotesForDefault(fieldType)) {
//                    fieldDefinition.append(" DEFAULT '").append(defaultValue).append("'");
                } else {
//                    fieldDefinition.append(" DEFAULT ").append(defaultValue);
                }
            }

            // ResultMap构建
            resultMapColumnSb.append("        <result property=").append("\"").append(camelFieldName).append("\"").append(" column=").append("\"").append(fieldName).append("\"").append("/>\n");

            List<String> fieldWhereLst = new ArrayList<String>();
            fieldWhereLst.add("create_time");
            fieldWhereLst.add("create_by");
            fieldWhereLst.add("update_time");
            fieldWhereLst.add("update_by");

            List<String> fieldUpdateExistIfLst = new ArrayList<String>();
            fieldUpdateExistIfLst.add("login_cnt");
            // Select Filed 构建
            if(ValueUtils.isEqual(CONST_DECRYPT,decrypt) && !noEncodeList.contains(fieldName)) {

                selectFieldMapColumnSb.append("          convert_from(decrypt_iv(decode(NULLIF(");
                selectFieldMapColumnSb.append(fieldName);
                selectFieldMapColumnSb.append(", ''), 'base64'), <include refid=\"jp.newpart.common.mapper.AesKeyAndIvMapper.aes_key\"/>, <include refid=\"jp.newpart.common.mapper.AesKeyAndIvMapper.aes_iv\"/>, 'aes-cbc/pad:pkcs'), 'UTF-8')");
                selectFieldMapColumnSb.append(" as ").append(fieldName);;
            } else {
                selectFieldMapColumnSb.append("          ").append(fieldName);
            }
            selectFieldMapColumnSb.append(",\n");

            if(!fieldWhereLst.contains(fieldName)) {
                if ("varchar".equalsIgnoreCase(fieldType) && !fieldLength.isEmpty()) {
                    // Select List 构建
                    selectListMapColumnSb.append("            <if test=").append("\"").append(camelFieldName).append(" != null  and ").append(camelFieldName).append(" != ").append("\'").append("\'").append("\"").append(">\n");
                } else {
                    selectListMapColumnSb.append("            <if test=").append("\"").append(camelFieldName).append(" != null ").append("\"").append(">\n");
                }

                // 暗号化
                if(ValueUtils.isEqual(CONST_DECRYPT,decrypt) && !noEncodeList.contains(fieldName)) {
                    selectListMapColumnSb.append("             and  convert_from(decrypt_iv(decode(NULLIF(");
                    selectListMapColumnSb.append(fieldName);
                    selectListMapColumnSb.append(", ''), 'base64'), <include refid=\"jp.newpart.common.mapper.AesKeyAndIvMapper.aes_key\"/>, <include refid=\"jp.newpart.common.mapper.AesKeyAndIvMapper.aes_iv\"/>, 'aes-cbc/pad:pkcs'), 'UTF-8')");
                    selectListMapColumnSb.append(" = #{").append(camelFieldName).append("}\n");
                } else {
                    selectListMapColumnSb.append("                and ").append(fieldName).append(" = #{").append(camelFieldName).append("}\n");
                }


                selectListMapColumnSb.append("            </if>\n");
            }

            if (primaryKeyColumn.equals(fieldName)) {
                selectByIdMapColumnSb.append(fieldName).append(" = #{").append(camelFieldName).append("}\n");
            }
            // Insert 构建
            insertBeforeMapColumnSb.append("            <if test=").append("\"").append(camelFieldName).append(" != null").append("\"").append(">\n");
            insertBeforeMapColumnSb.append("                ").append(fieldName).append(",\n");
            ;
            insertBeforeMapColumnSb.append("            </if>\n");
            insertAfterMapColumnSb.append("            <if test=").append("\"").append(camelFieldName).append(" != null").append("\"").append(">\n");

            if(ValueUtils.isEqual(CONST_DECRYPT,decrypt) && !noEncodeList.contains(fieldName)) {
                insertAfterMapColumnSb.append("              encode(  encrypt_iv(convert_to(#{");
                insertAfterMapColumnSb.append(camelFieldName);
               // insertAfterMapColumnSb.append("}, 'UTF-8'), decode('59fdbeefefefcff8efef158e8feff9f7', 'hex'), decode('f3faeffb6fb5efef32fdefaebfdf937f', 'hex'), 'aes-cbc/pad:pkcs'),'base64'),\n");
                insertAfterMapColumnSb.append("}, 'UTF-8'),  <include refid=\"jp.newpart.common.mapper.AesKeyAndIvMapper.aes_key\"/>, <include refid=\"jp.newpart.common.mapper.AesKeyAndIvMapper.aes_iv\"/>, 'aes-cbc/pad:pkcs'),'base64'),\n");
            } else {
                insertAfterMapColumnSb.append("                #{").append(camelFieldName).append("},\n");
            }

            insertAfterMapColumnSb.append("            </if>\n");
            // Update 构建
            if (  ( fieldUpdateExistIfLst.contains(fieldName) ||  "varchar".equalsIgnoreCase(fieldType) ||  "timestamp".equalsIgnoreCase(fieldType)) && !fieldLength.isEmpty()) {
                updateMapColumnSb.append("            <if test=").append("\"").append(camelFieldName).append(" != null").append("\"").append(">\n");

                if(ValueUtils.isEqual(CONST_DECRYPT,decrypt) && !noEncodeList.contains(fieldName)) {
                    updateMapColumnSb.append("                ").append(fieldName).append(" =" );
                    updateMapColumnSb.append(" encode(  encrypt_iv(convert_to(#{");
                    updateMapColumnSb.append(camelFieldName);
//                    updateMapColumnSb.append("}, 'UTF-8'), decode('59fdbeefefefcff8efef158e8feff9f7', 'hex'), decode('f3faeffb6fb5efef32fdefaebfdf937f', 'hex'), 'aes-cbc/pad:pkcs'),'base64'),\n");
                    updateMapColumnSb.append("}, 'UTF-8'),  <include refid=\"jp.newpart.common.mapper.AesKeyAndIvMapper.aes_key\"/>, <include refid=\"jp.newpart.common.mapper.AesKeyAndIvMapper.aes_iv\"/>, 'aes-cbc/pad:pkcs'),'base64'),\n");
                } else {
                    updateMapColumnSb.append("                ").append(fieldName).append(" = #{").append(camelFieldName).append("},\n");
                }

                updateMapColumnSb.append("            </if>\n");
            } else if ("version".equals(fieldName)) {
                updateMapColumnSb.append("                ").append(fieldName).append(" = #{").append(camelFieldName).append("}+1\n");
            } else {
                updateMapColumnSb.append("                ").append(fieldName).append(" = #{").append(camelFieldName).append("},\n");
            }

            // 修改表长度
            if(ValueUtils.isEqual(CONST_DECRYPT,decrypt) && ValueUtils.isNotEmpty(fieldLengthEncode) && !noEncodeList.contains(fieldName)) {
                AlterColumnMapSb.append("ALTER TABLE ").append(tableName).append("\n");
                AlterColumnMapSb.append("ALTER COLUMN  ").append(fieldName).append("  TYPE VARCHAR(").append(fieldLengthEncode).append(");").append("\n");

            }

            jsonFieldMapSb.append("\"").append(camelFieldName).append("\"").append(":");
            if (("integer".equalsIgnoreCase(fieldType) || "decimal".equalsIgnoreCase(fieldType)) && !fieldLength.isEmpty()) {
                jsonFieldMapSb.append(0).append(",\n");
            } else {
                jsonFieldMapSb.append("\"").append("\"").append(",\n");
            }

            jsonMessageMapSb.append("label.").append(camelFieldName).append("=").append(fieldComment).append("\n");
        }

        // 构建完整的 Mapper 语句
        if (resultMapColumnSb.length() > 0) {

            StringBuilder createMapperSb = generatorMapperXml(tableName, camelTableName2
                    , resultMapColumnSb
                    , selectFieldMapColumnSb
                    , selectListMapColumnSb
                    , selectByIdMapColumnSb
                    , insertBeforeMapColumnSb
                    , insertAfterMapColumnSb
                    , updateMapColumnSb);
            mapperContent.append(createMapperSb);

        }

        // 构建完整的 Mapper 语句
        if (entityFieldMapSb.length() > 0) {


            // VO
            entityContentVo.append(generatorVo(tableName, camelTableName2, tablrNameHanzi, entityFieldMapSb));
            // Entity
            entityContent.append(generatorEntity(tableName, camelTableName2, tablrNameHanzi, entityFieldMapSb));

            // BO
            entityContentBo.append(generatorBo(tableName, camelTableName2, tablrNameHanzi, entityFieldMapSbBo));

            serviceContent.append( generatorSerivce( tableName,  camelTableName,  camelTableName2,  tablrNameHanzi));
            serviceImpl.append( generatorServiceImpl( tableName,  camelTableName,  camelTableName2,  tablrNameHanzi));
        }

        // 构建完整的 Interface 语句
        interfaceContent.append(generatorMapper(tableName, camelTableName, camelTableName2, tablrNameHanzi));

        generateBuilder.put("mapper", mapperContent);
        generateBuilder.put("entity", entityContent);
        generateBuilder.put("entityVo", entityContentVo);
        generateBuilder.put("entityBo", entityContentBo);
        generateBuilder.put("interface", interfaceContent);
        generateBuilder.put("service", serviceContent);
        generateBuilder.put("impl", serviceImpl);

        generateBuilder.put("json", jsonFieldMapSb);
        generateBuilder.put("message", jsonMessageMapSb);
        generateBuilder.put("alterTable", AlterColumnMapSb);
        return generateBuilder;
    }

    /**
     * 生成Serivce
     * @param tableName
     * @param camelTableName
     * @param camelTableName2
     * @param tablrNameHanzi
     * @return
     */
    private static StringBuilder generatorSerivce(String tableName, String camelTableName, String camelTableName2, String tablrNameHanzi) {
        StringBuilder createSb = new StringBuilder();
        createSb.append("package jp.newpart.common.biz.mapper;").append("\n");
        createSb.append("\n");
        createSb.append("import jp.newpart.common.biz.domain.entity.").append(camelTableName2).append("Entity").append(";\n");

        createSb.append("import java.util.List;").append("\n");
        createSb.append("import lombok.EqualsAndHashCode;").append("\n");
        createSb.append("\n");

        createSb.append("\n");

        createSb.append("/**").append("\n");
        createSb.append(" * ").append(tablrNameHanzi).append("Serviceインタフェース").append("\n");
        createSb.append(" *").append("\n");
        createSb.append(" * @author nextosd").append("\n");
        createSb.append(" */").append("\n");

        createSb.append("public interface I").append(camelTableName2);
        createSb.append("BaseService {").append("\n");
        createSb.append("\n");

        // 检索byID
        createSb.append("     /**").append("\n");
        createSb.append("     * ").append(tablrNameHanzi).append("を検索").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param id ").append(tablrNameHanzi).append("プライマリ・キー").append("\n");
        createSb.append("     * @return ").append(tablrNameHanzi).append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public ").append(camelTableName2).append("Entity");
        createSb.append(" selectById(String id);").append("\n");
        createSb.append("\n");

        // 检索List by entity
        createSb.append("     /**").append("\n");
        createSb.append("     * ").append(tablrNameHanzi).append("を検索リスト").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param ").append(camelTableName).append(" ").append(tablrNameHanzi).append("パラメータ対象\n");
        createSb.append("     * @return ").append(tablrNameHanzi).append("リスト").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public List<").append(camelTableName2).append("Entity");
        createSb.append("> selectList(").append(camelTableName2).append("Entity").append(" ").append(camelTableName).append(");\n");
        createSb.append("\n");

        // Insert
        createSb.append("     /**").append("\n");
        createSb.append("     * ").append(tablrNameHanzi).append("を登録").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param ").append(camelTableName).append(" ").append(tablrNameHanzi).append("\n");
        createSb.append("     * @return ").append(" 結果").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public int insert(").append(camelTableName2).append("Entity");
        createSb.append(" ").append(camelTableName).append(");\n");
        createSb.append("\n");

        // Update
        createSb.append("     /**").append("\n");
        createSb.append("     * ").append(tablrNameHanzi).append("を修正").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param ").append(camelTableName).append(" ").append(tablrNameHanzi).append("\n");
        createSb.append("     * @return ").append(" 結果").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public int update(").append(camelTableName2).append("Entity");
        createSb.append(" ").append(camelTableName).append(");\n");
        createSb.append("\n");

        // delete
        createSb.append("     /**").append("\n");
        createSb.append("     * ").append(tablrNameHanzi).append("を削除").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param id ").append(tablrNameHanzi).append("プライマリ・キー").append("\n");
        createSb.append("     * @return ").append(" 結果").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public int");
        createSb.append(" deleteById(String id);").append("\n");
        createSb.append("\n");

        createSb.append("}").append("\n");

        return createSb;
    }

    /**
     * 生成Service Impl
     * @param tableName
     * @param camelTableName
     * @param camelTableName2
     * @param tablrNameHanzi
     * @return
     */
    private static StringBuilder generatorServiceImpl(String tableName, String camelTableName, String camelTableName2, String tablrNameHanzi) {
        StringBuilder createSb = new StringBuilder();
        createSb.append("package jp.newpart.crm.biz.master.service.impl;").append("\n");
        createSb.append("\n");
        createSb.append("import jp.newpart.common.biz.domain.entity.").append(camelTableName2).append("Entity").append(";\n");
        createSb.append("import jp.newpart.common.biz.mapper.").append(camelTableName2).append("BaseMapper").append(";\n");
        createSb.append("import jp.newpart.common.biz.service.I").append(camelTableName2).append("BaseService").append(";\n");

        createSb.append("import jp.newpart.common.exception.ServiceException;").append("\n");
        createSb.append("import jp.newpart.common.utils.MessageUtils;").append("\n");
        createSb.append("import org.springframework.beans.BeanUtils;").append("\n");
        createSb.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
        createSb.append("import org.springframework.stereotype.Service;").append("\n");
        createSb.append("import org.springframework.transaction.annotation.Transactional;").append("\n");
        createSb.append("import java.util.List;").append("\n");
        createSb.append("\n");

        createSb.append("\n");

        createSb.append("/**").append("\n");
        createSb.append(" * ").append(tablrNameHanzi).append("Serviceインタフェース").append("\n");
        createSb.append(" *").append("\n");
        createSb.append(" * @author nextosd").append("\n");
        createSb.append(" */").append("\n");

        createSb.append("@Service").append("\n");
        createSb.append("public class ").append(camelTableName2);
        createSb.append("BaseServiceImpl ");
        createSb.append("implements ").append("I").append(camelTableName2).append("BaseService").append(" {\n");
        createSb.append("\n");
        createSb.append("\n");

        createSb.append("    @Autowired").append(" \n");
        createSb.append("    private ");
        createSb.append(camelTableName2).append("BaseMapper ");

        String mapperObject = camelTableName + "BaseMapper";;

        String entityDecare = camelTableName2 + "Entity ";;
        String entityParam = camelTableName + "Entity ";;
        createSb.append(mapperObject).append(";\n");
        createSb.append("\n");
        // 检索byID
        createSb.append("     /**").append("\n");
        createSb.append("     *" ).append(tablrNameHanzi).append("を検索").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param id ").append(tablrNameHanzi).append("プライマリ・キー").append("\n");
        createSb.append("     * @return ").append(tablrNameHanzi).append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    @Override").append("\n");
        createSb.append("    public ").append(camelTableName2).append("Entity");
        createSb.append(" selectById(String id) {").append("\n");
        createSb.append("\n");
        createSb.append("        ").append(entityDecare).append(" ").append(entityParam).append(" = ");
        createSb.append(mapperObject).append(".selectById(id) ;").append("\n");
        createSb.append("\n");
        createSb.append("        if (").append(entityParam).append(" == null) {").append("\n");
        createSb.append("            throw new ServiceException(MessageUtils.message(\"E0007\"));").append("\n");
        createSb.append("        }").append("\n");

//        return item;
        createSb.append("        return ").append(entityParam).append(";\n");
        createSb.append("    }\n");
        createSb.append("\n");
        createSb.append("\n");


        // 检索List by entity
        createSb.append("     /**").append("\n");
        createSb.append("     * ").append(tablrNameHanzi).append("を検索リスト").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param ").append(camelTableName).append(" ").append(tablrNameHanzi).append("パラメータ対象\n");
        createSb.append("     * @return ").append(tablrNameHanzi).append("リスト").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    @Override").append("\n");
        createSb.append("    public List<").append(camelTableName2).append("Entity");
        createSb.append("> selectList(").append(camelTableName2).append("Entity").append(" ").append(camelTableName).append(") {\n");
        createSb.append("\n");



//        return item;
        createSb.append("         return ").append(mapperObject).append(".selectList(").append(camelTableName).append(");") .append("\n");
        createSb.append("    }\n");
        createSb.append("\n");
        createSb.append("\n");


        // Insert
        createSb.append("     /**").append("\n");
        createSb.append("     * ").append(tablrNameHanzi).append("を登録").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param ").append(camelTableName).append(" ").append(tablrNameHanzi).append("\n");
        createSb.append("     * @return ").append(" 結果").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    @Override").append("\n");
        createSb.append("    @Transactional(rollbackFor = Exception.class)").append("\n");
        createSb.append("    public int insert(").append(camelTableName2).append("Entity");
        createSb.append(" ").append(camelTableName).append(") {\n");
        createSb.append("\n");
//        return item;
        createSb.append("         return ").append(mapperObject).append(".insert(").append(camelTableName).append(");") .append("\n");
        createSb.append("    }\n");
        createSb.append("\n");
        createSb.append("\n");



        // Update
        createSb.append("     /**").append("\n");
        createSb.append("     *").append(tablrNameHanzi).append("を修正").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param ").append(camelTableName).append(" ").append(tablrNameHanzi).append("\n");
        createSb.append("     * @return ").append(" 結果").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    @Override").append("\n");
        createSb.append("    @Transactional(rollbackFor = Exception.class)").append("\n");
        createSb.append("    public int update(").append(camelTableName2).append("Entity");
        createSb.append(" ").append(camelTableName).append(") {\n");
        createSb.append("\n");
        createSb.append("        int result = ").append(mapperObject).append(".update(").append(camelTableName).append(");") .append("\n");

        createSb.append("        if (result < 1) {").append("\n");
        createSb.append("            throw new ServiceException(MessageUtils.message(\"E0007\"));").append("\n");
        createSb.append("        }").append("\n");
        createSb.append("        return result;").append("\n");
        createSb.append("    }\n");


        createSb.append("     /**").append("\n");
        createSb.append("     *" ).append(tablrNameHanzi).append("を削除").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param id ").append(tablrNameHanzi).append("プライマリ・キー").append("\n");
        createSb.append("     * @return ").append(" 結果").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    @Override").append("\n");
        createSb.append("    public int");
        createSb.append(" deleteById(String id) {").append("\n");
        createSb.append("\n");
        createSb.append("        int result = ").append(mapperObject).append(".deleteById(id);") .append("\n");

        createSb.append("        if (result < 1) {").append("\n");
        createSb.append("            throw new ServiceException(MessageUtils.message(\"E0007\"));").append("\n");
        createSb.append("        }").append("\n");
        createSb.append("        return result;").append("\n");
        createSb.append("    }\n");
        createSb.append("\n");
        createSb.append("\n");

        createSb.append("}").append("\n");

        return createSb;
    }

    /**
     * 生成Bo
     * @param tableName
     * @param camelTableName2
     * @param tablrNameHanzi
     * @param entityFieldMapSbBo
     * @return
     */
    private static StringBuilder generatorBo(String tableName, String camelTableName2, String tablrNameHanzi, StringBuilder entityFieldMapSbBo) {
        StringBuilder createSbBo = new StringBuilder();
        createSbBo.append("package jp.newpart.common.biz.domain.bo;").append("\n");
        createSbBo.append("\n");
        createSbBo.append("import jp.newpart.common.validator.annotation.*;").append("\n");
        createSbBo.append("import jp.newpart.common.validator.enums.CheckTypeEnum;").append("\n");
        createSbBo.append("import jp.newpart.common.validator.utils.ValidatorGroup;").append("\n");
        createSbBo.append("import lombok.Data;").append("\n");
        createSbBo.append("\n");
        createSbBo.append("import java.io.Serial;").append("\n");
        createSbBo.append("import java.io.Serializable;").append("\n");
        createSbBo.append("import java.math.BigDecimal;").append("\n");
        createSbBo.append("\n");

        createSbBo.append("/**").append("\n");
        createSbBo.append(" * ").append(tablrNameHanzi).append(" サービスパラメータ対象").append("\n");
        createSbBo.append(" *").append("\n");
        createSbBo.append(" * @author nextosd").append("\n");
        createSbBo.append(" */").append("\n");
        createSbBo.append("@Data").append("\n");
        createSbBo.append("public class ").append(camelTableName2);
        createSbBo.append("BaseBo implements Serializable {").append("\n");
        createSbBo.append("\n");

        createSbBo.append("    @Serial").append("\n");
        createSbBo.append("    private static final long serialVersionUID = 1L;").append("\n");
        createSbBo.append("\n");

        createSbBo.append(entityFieldMapSbBo.toString());

        createSbBo.append("    /**").append("\n");
        createSbBo.append("     * ").append("リビジョン").append("\n");
        createSbBo.append("     */").append("\n");
        createSbBo.append("    private Integer version;").append("\n");
        ;

        createSbBo.append("}").append("\n");
        return createSbBo;
    }

    /**
     * 生成Mapper Xml
     * @param tableName
     * @param camelTableName2
     * @param resultMapColumnSb
     * @param selectFieldMapColumnSb
     * @param selectListMapColumnSb
     * @param selectByIdMapColumnSb
     * @param insertBeforeMapColumnSb
     * @param insertAfterMapColumnSb
     * @param updateMapColumnSb
     * @return
     */
    private static StringBuilder generatorMapperXml(String tableName, String camelTableName2
            , StringBuilder resultMapColumnSb
            , StringBuilder selectFieldMapColumnSb
            , StringBuilder selectListMapColumnSb
            , StringBuilder selectByIdMapColumnSb
            , StringBuilder insertBeforeMapColumnSb
            , StringBuilder insertAfterMapColumnSb
            , StringBuilder updateMapColumnSb
    ) {
        // 不用加version的表
        List<String> noVersionTables =new  ArrayList<>();
        noVersionTables.add("mast_item_extends");
        noVersionTables.add("bss_order_extends");
        noVersionTables.add("bss_batch_csv_validation_info");

        // 创建MapBuilder类
        StringBuilder createMapperSb = new StringBuilder();

        String tableReslut = camelTableName2 + "EntityResult";
        // 创建Result类
        StringBuilder resultMapSb = new StringBuilder();
        createMapperSb.append("<?xml version=").append("\"").append("1.0").append("\"").append(" encoding=").append("\"").append("UTF-8").append("\"").append(" ?>").append("\n");
        createMapperSb.append("<!DOCTYPE mapper").append("\n");
        createMapperSb.append("        PUBLIC ").append("\"").append("-//mybatis.org//DTD Mapper 3.0//EN").append("\"").append("\n");
        createMapperSb.append("        ").append("\"").append("http://mybatis.org/dtd/mybatis-3-mapper.dtd").append("\"").append(">\n");
        createMapperSb.append("<mapper namespace=").append("\"").append("jp.newpart.common.biz.mapper.").append(camelTableName2).append("BaseMapper").append("\"").append(">\n");

        // 创建Result类
        resultMapSb.append("   <resultMap type=").append("\"").append(camelTableName2).append("Entity").append("\"").append(" id=").append("\"").append(tableReslut).append("\"").append(">\n");
        resultMapSb.append(resultMapColumnSb.toString()).append("\n");
        // Result结尾
        resultMapSb.append("    </resultMap>");
        // 添加Result结构
        createMapperSb.append(resultMapSb.toString()).append("\n");

        // Select Field
        StringBuilder selectFieldMapSb = new StringBuilder();
        StringBuilder selectFieldVoSb = new StringBuilder();
        selectFieldVoSb.append("select").append(camelTableName2);
        selectFieldMapSb.append("    <sql id=").append("\"").append(selectFieldVoSb.toString()).append("\"").append(">\n");
        selectFieldMapSb.append("        select").append("\n");
        ;
        // 移除最后一个逗号
        String selectFieldDefs = selectFieldMapColumnSb.toString();
        if (selectFieldDefs.endsWith(",\n")) {
            selectFieldDefs = selectFieldDefs.substring(0, selectFieldDefs.length() - 2);
        }
        selectFieldMapSb.append(selectFieldDefs).append("\n");
        ;
        selectFieldMapSb.append("        from ").append(tableName).append("\n");
        selectFieldMapSb.append("    </sql>");
        // 添加Select Field 结构
        createMapperSb.append(selectFieldMapSb.toString()).append("\n");

        // Select List
        StringBuilder selectListMapSb = new StringBuilder();
        selectListMapSb.append("    <select id=\"selectList\" parameterType=").append("\"").append(camelTableName2).append("Entity").append("\"").append(" resultMap=").append("\"").append(tableReslut).append("\"").append(">\n");
        selectListMapSb.append("        <include refid=").append("\"").append(selectFieldVoSb.toString()).append("\"").append("/>\n");
        selectListMapSb.append("        <where>").append("\n");
        selectListMapSb.append(selectListMapColumnSb.toString()).append("\n");
        selectListMapSb.append("        </where>").append("\n");

        // 排序增加
        if(ValueUtils.isEqual("mast_farmer_rakuuru_cart",tableName)) {
            selectListMapSb.append("        order by create_time").append("\n");
        }

        selectListMapSb.append("    </select>").append("\n");
        // 添加Select List 结构
        createMapperSb.append(selectListMapSb.toString()).append("\n");

        // Select By Id
        StringBuilder selectByIdMapSb = new StringBuilder();
        selectByIdMapSb.append("    <select id=\"selectById\" parameterType=\"String\" ").append(" resultMap=").append("\"").append(tableReslut).append("\"").append(">\n");
        selectByIdMapSb.append("        <include refid=").append("\"").append(selectFieldVoSb.toString()).append("\"").append("/>\n");
        selectByIdMapSb.append("        where ").append(selectByIdMapColumnSb.toString()).append("\n");


        selectByIdMapSb.append("    </select>").append("\n");
        // 添加Select By Id 结构
        createMapperSb.append(selectByIdMapSb.toString()).append("\n");

        // Insert
        StringBuilder insertMapSb = new StringBuilder();
        insertMapSb.append("    <insert id=\"insert\" parameterType=\"").append(camelTableName2).append("Entity").append("\"").append(">\n");
        insertMapSb.append("        insert into ").append(tableName).append("\n");
        insertMapSb.append("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">").append("\n");
        insertMapSb.append(insertBeforeMapColumnSb.toString()).append("\n");
        insertMapSb.append("        </trim>").append("\n");
        insertMapSb.append("        <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">").append("\n");
        insertMapSb.append(insertAfterMapColumnSb.toString()).append("\n");
        insertMapSb.append("        </trim>").append("\n");
        insertMapSb.append("    </insert>").append("\n");
        // 添加Insert 结构
        createMapperSb.append(insertMapSb.toString()).append("\n");

        // Insert
        StringBuilder updateMapSb = new StringBuilder();
        updateMapSb.append("    <update id=\"update\" parameterType=\"").append(camelTableName2).append("Entity").append("\"").append(">\n");
        updateMapSb.append("        update ").append(tableName).append("\n");
        updateMapSb.append("        <trim prefix=\"SET\" suffixOverrides=\",\">").append("\n");
        updateMapSb.append(updateMapColumnSb.toString()).append("\n");
        updateMapSb.append("        </trim>").append("\n");
        updateMapSb.append("        where").append("\n");
        updateMapSb.append("        ").append(selectByIdMapColumnSb.toString()).append("\n");
        if(!noVersionTables.contains(tableName)){
            updateMapSb.append("            <if test=").append("\"").append("version").append(" != null").append("\"").append(">\n");
            updateMapSb.append("                and version = #{version}").append("\n");
            updateMapSb.append("            </if").append(">\n");
        }

        updateMapSb.append("    </update>").append("\n");
        // 添加Update 结构
        createMapperSb.append(updateMapSb.toString()).append("\n");

        // Delete
        StringBuilder deleteMapSb = new StringBuilder();
        deleteMapSb.append("    <delete id=\"deleteById\" parameterType=\"String\"").append(">\n");
        deleteMapSb.append("        delete").append("\n");
        deleteMapSb.append("        from").append("\n");
        deleteMapSb.append("        ").append(tableName).append("\n");
        deleteMapSb.append("        where").append("\n");
        deleteMapSb.append("        ").append(selectByIdMapColumnSb.toString()).append("\n");
        deleteMapSb.append("    </delete>").append("\n");
        // 添加Delete 结构
        createMapperSb.append(deleteMapSb.toString()).append("\n");

        // 文件结尾
        createMapperSb.append(" </mapper>");

        return createMapperSb;
    }

    /**
     * 生成Mapper
     * @param tableName
     * @param camelTableName
     * @param camelTableName2
     * @param tablrNameHanzi
     * @return
     */
    private static StringBuilder generatorMapper(String tableName, String camelTableName, String camelTableName2, String tablrNameHanzi) {
        StringBuilder createSb = new StringBuilder();
        createSb.append("package jp.newpart.common.biz.mapper;").append("\n");
        createSb.append("\n");
        createSb.append("import jp.newpart.common.biz.domain.entity.").append(camelTableName2).append("Entity").append(";\n");

        createSb.append("import java.util.List;").append("\n");
        createSb.append("import lombok.EqualsAndHashCode;").append("\n");
        createSb.append("\n");

        createSb.append("\n");

        createSb.append("/**").append("\n");
        createSb.append(" * ").append(tablrNameHanzi).append(" Mapperインタフェース").append("\n");
        createSb.append(" *").append("\n");
        createSb.append(" * @author nextosd").append("\n");
        createSb.append(" */").append("\n");

        createSb.append("public interface ").append(camelTableName2);
        createSb.append("BaseMapper {").append("\n");
        createSb.append("\n");

        // 检索byID
        createSb.append("     /**").append("\n");
        createSb.append("     *").append(tablrNameHanzi).append("を検索").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param id ").append(tablrNameHanzi).append("プライマリ・キー").append("\n");
        createSb.append("     * @return ").append(tablrNameHanzi).append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public ").append(camelTableName2).append("Entity");
        createSb.append(" selectById(String id);").append("\n");
        createSb.append("\n");

        // 检索List by entity
        createSb.append("     /**").append("\n");
        createSb.append("     *").append(tablrNameHanzi).append("を検索リスト").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param ").append(camelTableName).append(" ").append(tablrNameHanzi).append("\n");
        createSb.append("     * @return ").append(tablrNameHanzi).append("リスト").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public List<").append(camelTableName2).append("Entity");
        createSb.append("> selectList(").append(camelTableName2).append("Entity").append(" ").append(camelTableName).append(");\n");
        createSb.append("\n");

        // Insert
        createSb.append("     /**").append("\n");
        createSb.append("     *").append(tablrNameHanzi).append("を登録").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param ").append(camelTableName).append(" ").append(tablrNameHanzi).append("\n");
        createSb.append("     * @return ").append(" 結果").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public int insert(").append(camelTableName2).append("Entity");
        createSb.append(" ").append(camelTableName).append(");\n");
        createSb.append("\n");

        // Update
        createSb.append("     /**").append("\n");
        createSb.append("     *").append(tablrNameHanzi).append("を修正").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param ").append(camelTableName).append(" ").append(tablrNameHanzi).append("\n");
        createSb.append("     * @return ").append(" 結果").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public int update(").append(camelTableName2).append("Entity");
        createSb.append(" ").append(camelTableName).append(");\n");
        createSb.append("\n");

        // delete
        createSb.append("     /**").append("\n");
        createSb.append("     * ").append(tablrNameHanzi).append("を削除").append("\n");
        createSb.append("     *").append("\n");
        createSb.append("     * @param id ").append(tablrNameHanzi).append("プライマリ・キー").append("\n");
        createSb.append("     * @return ").append(" 結果").append("\n");
        createSb.append("     */").append("\n");
        createSb.append("    public int");
        createSb.append(" deleteById(String id);").append("\n");
        createSb.append("\n");

        createSb.append("}").append("\n");

        return createSb;
    }


    /**
     * 生成 Entity
     * @param tableName
     * @param camelTableName2
     * @param tablrNameHanzi
     * @param entityFieldMapSb
     * @return
     */
    private static StringBuilder generatorEntity(String tableName, String camelTableName2, String tablrNameHanzi, StringBuilder entityFieldMapSb) {

        StringBuilder entityContent = new StringBuilder();

        StringBuilder createSb = new StringBuilder();
        createSb.append("package jp.newpart.common.biz.domain.entity;").append("\n");
        createSb.append("\n");
        createSb.append("import jp.newpart.common.core.domain.BaseEntity;").append("\n");
        createSb.append("import lombok.Data;").append("\n");
        createSb.append("import lombok.EqualsAndHashCode;").append("\n");
        createSb.append("\n");
        createSb.append("import java.io.Serial;").append("\n");
        createSb.append("import java.io.Serial;").append("\n");

        createSb.append("import java.math.BigDecimal;").append("\n");

        createSb.append("import jp.newpart.common.annotation.EncryptedField;").append("\n");
        createSb.append("\n");

        createSb.append("/**").append("\n");
        createSb.append(" * ").append(tablrNameHanzi).append(" (").append(tableName).append(")").append("\n");
        createSb.append(" *").append("\n");
        createSb.append(" * @author nextosd").append("\n");
        createSb.append(" */").append("\n");
        createSb.append("@EqualsAndHashCode(callSuper = true)").append("\n");
        createSb.append("@Data").append("\n");
        createSb.append("public class ").append(camelTableName2).append("Entity");
        createSb.append(" extends BaseEntity {").append("\n");
        createSb.append("\n");

        createSb.append("    @Serial").append("\n");
        createSb.append("    private static final long serialVersionUID = 1L;").append("\n");
        createSb.append("\n");

        createSb.append(entityFieldMapSb.toString());


        createSb.append("}").append("\n");
        entityContent.append(createSb);

        return entityContent;
    }

    /**
     * 生成 Vo
     * @param tableName
     * @param camelTableName2
     * @param tablrNameHanzi
     * @param entityFieldMapSb
     * @return
     */
    private static StringBuilder generatorVo(String tableName, String camelTableName2, String tablrNameHanzi, StringBuilder entityFieldMapSb) {

        StringBuilder entityContentVo = new StringBuilder();

        StringBuilder createSb1 = new StringBuilder();
        createSb1.append("package jp.newpart.xx.xxx.xxxx.domain.entity;").append("\n");
        createSb1.append("\n");
        createSb1.append("import jp.newpart.common.domain.core.BaseEntity;").append("\n");
        createSb1.append("import lombok.Data;").append("\n");
        createSb1.append("import lombok.EqualsAndHashCode;").append("\n");
        createSb1.append("\n");
        createSb1.append("import java.io.Serial;").append("\n");

        createSb1.append("import java.math.BigDecimal;").append("\n");
        createSb1.append("\n");

        createSb1.append("/**").append("\n");
        createSb1.append(" * ").append(tablrNameHanzi).append("詳細情報Vo").append("\n");
        createSb1.append(" *").append("\n");
        createSb1.append(" * @author nextosd").append("\n");
        createSb1.append(" */").append("\n");
        createSb1.append("@Data").append("\n");
        createSb1.append("public class ").append(camelTableName2).append("Vo");
        createSb1.append(" extends BaseEntity {").append("\n");
        createSb1.append("\n");


        createSb1.append(entityFieldMapSb.toString());

        createSb1.append("    /**").append("\n");
        createSb1.append("     * ").append("リビジョン").append("\n");
        createSb1.append("     */").append("\n");
        createSb1.append("    private Integer version;").append("\n");
        ;

        createSb1.append("}").append("\n");
        entityContentVo.append(createSb1.toString());

        return entityContentVo;
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
                typeLower.contains("date");
    }

    public static String convertToCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        // 使用标志位判断是否需要大写下一个字符[7](@ref)
        boolean nextUpperCase = false;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '_') {
                nextUpperCase = true; // 遇到下划线，标记下一个字符大写
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpperCase = false;
                } else {
                    result.append(currentChar);
                }
            }
        }
        return result.toString();
    }

    public static String convertToPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        String[] words = input.split("_");

        for (String word : words) {
            if (!word.isEmpty()) { // 避免连续下划线导致空字符串
                // 将每个单词的首字母大写，其余字母小写
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
}
