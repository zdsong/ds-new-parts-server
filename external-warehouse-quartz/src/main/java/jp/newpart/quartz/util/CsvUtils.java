package jp.newpart.quartz.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVファイル読み書きユーティリティクラス
 * 
 * @author nextosd
 */
public class CsvUtils {
    private static final Logger logger = LoggerFactory.getLogger(CsvUtils.class);
    /**
     * CSVファイルを読み込み、2次元文字列配列として返す
     * 
     * @param filePath CSVファイルのパス
     * @return 2次元文字列配列（行と列のデータ）
     * @throws IOException ファイル読み込みエラーが発生した場合
     */
    public static String[][] readCsv(String filePath) throws IOException {
        return readCsv(filePath, java.nio.charset.Charset.forName("UTF-8"));
    }

    /**
     * CSVファイルを読み込み、2次元文字列配列として返す
     * 
     * @param filePath CSVファイルのパス
     * @param charset  文字コード
     * @return 2次元文字列配列（行と列のデータ）
     * @throws IOException ファイル読み込みエラーが発生した場合
     */
    public static String[][] readCsv(String filePath, java.nio.charset.Charset charset) throws IOException {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 空行をスキップ
                if (line.trim().isEmpty()) {
                    continue;
                }

                // CSV解析（カンマ区切り、ダブルクォート対応）
                String[] columns = parseCsvLine(line);
                rows.add(columns);
            }
        }

        return rows.toArray(new String[0][]);
    }

    /**
     * 2次元文字列配列をCSVファイルに書き込む
     * 
     * @param filePath CSVファイルのパス
     * @param data     書き込むデータ（2次元文字列配列）
     * @throws IOException ファイル書き込みエラーが発生した場合
     */
    public static void writeCsv(String filePath, String[][] data) throws IOException {
        writeCsv(filePath, data, StandardCharsets.UTF_8);
    }

    /**
     * 2次元文字列配列をCSVファイルに書き込む
     * 
     * @param filePath CSVファイルのパス
     * @param data     書き込むデータ（2次元文字列配列）
     * @param charset  文字コード
     * @throws IOException ファイル書き込みエラーが発生した場合
     */
    public static void writeCsv(String filePath, String[][] data, java.nio.charset.Charset charset) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), charset))) {
            for (int i = 0; i < data.length; i++) {
                String[] row = data[i];
                StringBuilder line = new StringBuilder();

                for (int j = 0; j < row.length; j++) {
                    String value = row[j] != null ? row[j] : "";

                    // 値にカンマ、ダブルクォート、改行が含まれる場合はダブルクォートで囲む
                    if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
                        value = "\"" + value.replace("\"", "\"\"") + "\"";
                    }

                    line.append(value);
                    if (j < row.length - 1) {
                        line.append(",");
                    }
                }

                writer.write(line.toString());
                if (i < data.length - 1) {
                    writer.newLine();
                }
            }
        }
    }

    /**
     * CSV行を解析してカラムに分割する
     * 
     * @param line CSV行
     * @return 分割されたカラム配列
     */
    private static String[] parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder currentColumn = new StringBuilder();
        boolean inQuotes = false;
        boolean fieldHasQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                fieldHasQuotes = true;
                // エスケープされたダブルクォーテーション "" の処理
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentColumn.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // フィールド終了
                addField(columns, currentColumn, fieldHasQuotes);
                // リセット
                currentColumn.setLength(0);
                fieldHasQuotes = false;
            } else {
                currentColumn.append(c);
            }
        }

        // 最後のフィールド
        addField(columns, currentColumn, fieldHasQuotes);

        return columns.toArray(new String[0]);
    }

    // 最後のフィールドの統一されたフィールド追加ロジック：コアルールはここにあります
    private static void addField(List<String> columns, StringBuilder currentColumn, boolean fieldHasQuotes) {
        if (fieldHasQuotes) {
            // 引用符が付いていれば、空でも「」を返します
            columns.add(currentColumn.toString());
        } else {
            // 引用符なし:NULL=null、非NULL=元の値
            columns.add(currentColumn.length() == 0 ? null : currentColumn.toString());
        }
    }

    /**
     * 複数のCSVファイルを読み込み、1つの2次元配列として返す
     * 
     * @param filePaths CSVファイルパスのリスト
     * @return 2次元文字列配列（行と列のデータ）、ヘッダー行をスキップして結合
     * @throws IOException ファイル読み込みエラーが発生した場合
     */
    public static String[][] readCsvList(List<String> filePaths) throws IOException {
        return readCsvList(filePaths, java.nio.charset.Charset.forName("UTF-8"));
    }

    /**
     * 複数のCSVファイルを読み込み、1つの2次元配列として返す
     *
     * @param filePaths CSVファイルパスのリスト
     * @return 2次元文字列配列（行と列のデータ）、ヘッダー行をスキップして結合
     * @throws IOException ファイル読み込みエラーが発生した場合
     */
    public static String[][] readCsvList4Item(List<String> filePaths) throws IOException {
        return readCsvList4Item(filePaths, java.nio.charset.Charset.forName("UTF-8"));
    }

    /**
     * 複数のCSVファイルを読み込み、1つの2次元配列として返す
     * 
     * @param filePaths CSVファイルパスのリスト
     * @param charset   文字コード
     * @return 2次元文字列配列（行と列のデータ）、ヘッダー行をスキップして結合
     * @throws IOException ファイル読み込みエラーが発生した場合
     */
    public static String[][] readCsvList4Item(List<String> filePaths, java.nio.charset.Charset charset) throws IOException {
        List<String[]> allRows = new ArrayList<>();
        boolean isFirstFile = true;

        for (String filePath : filePaths) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filePath), charset))) {

                // CSV行を解析（フィールド内の改行に対応）
                List<String[]> fileRows = parseCsvFile(reader, !isFirstFile);

                if (isFirstFile && !fileRows.isEmpty()) {
                    allRows.add(fileRows.get(0)); // 最初のファイルのヘッダーを追加
                    if (fileRows.size() > 1) {
                        allRows.addAll(fileRows.subList(1, fileRows.size()));
                    }
                } else if (!isFirstFile && !fileRows.isEmpty()) {
                    // 2つ目以降のファイルはヘッダーをスキップ
                    if (fileRows.size() > 1) {
                        allRows.addAll(fileRows.subList(1, fileRows.size()));
                    }
                }

                isFirstFile = false;
            } catch (IOException e) {
                logger.error("CSVファイルの読み込みエラー: " + filePath, e);
            }
        }
        
        return allRows.toArray(new String[0][]);
    }

    /**
     * 複数のCSVファイルを読み込み、1つの2次元配列として返す
     *
     * @param filePaths CSVファイルパスのリスト
     * @param charset   文字コード
     * @return 2次元文字列配列（行と列のデータ）、ヘッダー行をスキップして結合
     * @throws IOException ファイル読み込みエラーが発生した場合
     */
    public static String[][] readCsvList(List<String> filePaths, java.nio.charset.Charset charset) throws IOException {
        List<String[]> allRows = new ArrayList<>();
        boolean isFirstFile = true;

        for (String filePath : filePaths) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filePath), charset))) {
                String line;
                boolean isFirstLine = true;
                while ((line = reader.readLine()) != null) {
                    // 空行をスキップ
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    // 最初のファイルの最初の行を列頭として保持
                    if (isFirstFile && isFirstLine) {
                        String[] header = parseCsvLine(line);
                        allRows.add(header);
                        isFirstLine = false;
                        continue;
                    }

                    // 最初のファイル以外のヘッダー行をスキップ
                    if (!isFirstFile && isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    // CSV解析（カンマ区切り、ダブルクォート対応）
                    String[] columns = parseCsvLine(line);
                    allRows.add(columns);
                }
                isFirstFile = false;
            }
        }

        return allRows.toArray(new String[0][]);
    }

    /**
     * CSVファイルの内容を解析（フィールド内の改行に対応）
     *
     * @param reader BufferedReader
     * @param skipHeader ヘッダー行をスキップするかどうか
     * @return 解析されたCSVデータ
     * @throws IOException
     */
    private static List<String[]> parseCsvFile(BufferedReader reader, boolean skipHeader) throws IOException {
        List<String[]> rows = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        int quoteCount = 0;
        boolean isFirstLine = true;

        int ch;
        while ((ch = reader.read()) != -1) {
            char c = (char) ch;

            if (c == '"') {
                // 引用符の数をカウント
                quoteCount++;
            }

            currentLine.append(c);

            // 行の終了判定
            if (c == '\n' || c == '\r') {
                // 引用符が偶数（またはゼロ）の場合のみ行末とみなす
                if (quoteCount % 2 == 0) {
                    String line = currentLine.toString().trim();

                    // 連続する改行文字をスキップ
                    if (line.isEmpty()) {
                        // 次の文字を先読みして、連続する改行文字をスキップ
                        reader.mark(1);
                        int nextChar = reader.read();
                        if (nextChar == '\n' || nextChar == '\r') {
                            reader.reset();
                        }
                        currentLine.setLength(0);
                        quoteCount = 0;
                        continue;
                    }

                    // 最初の行（ヘッダー）をスキップする場合
                    if (skipHeader && isFirstLine) {
                        isFirstLine = false;
                    } else {
                        // 完全な行を解析
                        String parsedLine = currentLine.toString();

                        // 末尾の改行文字を削除
                        if (parsedLine.endsWith("\n") || parsedLine.endsWith("\r")) {
                            parsedLine = parsedLine.substring(0, parsedLine.length() - 1);
                            if (parsedLine.endsWith("\r")) {
                                parsedLine = parsedLine.substring(0, parsedLine.length() - 1);
                            }
                        }

                        // 引用符の対応をチェックして解析
                        String[] columns = parseCsvLineWithQuotes(parsedLine);
                        rows.add(columns);
                    }

                    currentLine.setLength(0);
                    quoteCount = 0;
                }
            }
        }

        // 最後の行の処理（ファイル末尾に改行がない場合）
        if (currentLine.length() > 0) {
            String parsedLine = currentLine.toString().trim();
            if (!parsedLine.isEmpty() && (!skipHeader || !isFirstLine)) {
                String[] columns = parseCsvLineWithQuotes(parsedLine);
                rows.add(columns);
            }
        }

        return rows;
    }

    /**
     * CSV行を解析（ダブルクォート対応、フィールド内の改行対応）
     *
     * @param line CSV行
     * @return 分割されたフィールドの配列
     */
    private static String[] parseCsvLineWithQuotes(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder currentColumn = new StringBuilder();
        boolean inQuotes = false;
        boolean quoteStart = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes) {
                    // 引用符内で、次の文字も引用符かチェック
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        // 連続する引用符はエスケープ
                        currentColumn.append('"');
                        i++; // 次の引用符をスキップ
                    } else {
                        // 引用符の終了
                        inQuotes = false;
                    }
                } else {
                    // 引用符の開始
                    inQuotes = true;
                    quoteStart = true;
                }
            } else if (c == ',' && !inQuotes) {
                // フィールドの終了
                String column = currentColumn.toString();
                if (quoteStart) {
                    // 引用符で始まった場合は引用符内の内容のみ
                    columns.add(column);
                } else {
                    // 通常のフィールド
                    columns.add(column.trim());
                }
                currentColumn.setLength(0);
                quoteStart = false;
            } else {
                currentColumn.append(c);
            }
        }

        // 最後のフィールド
        String column = currentColumn.toString();
        if (quoteStart) {
            columns.add(column);
        } else {
            columns.add(column.trim());
        }

        return columns.toArray(new String[0]);
    }



    /**
     * CSVファイルの内容をコンソールに表示する（デバッグ用）
     * 
     * @param filePath CSVファイルのパス
     * @throws IOException ファイル読み込みエラーが発生した場合
     */
    public static void printCsv(String filePath) throws IOException {
        String[][] data = readCsv(filePath);
        printCsvData(data);
    }

    /**
     * 2次元文字列配列をコンソールに表示する（デバッグ用）
     * 
     * @param data 表示するデータ
     */
    public static void printCsvData(String[][] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print("行" + (i + 1) + ": ");
            for (int j = 0; j < data[i].length; j++) {
                System.out.print("[" + data[i][j] + "] ");
            }
            System.out.println();
        }
    }

}