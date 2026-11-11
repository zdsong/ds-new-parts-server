package jp.newpart.quartz.util;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

/**
 * ZIPファイル圧縮・解凍共通ユーティリティクラス
 * 
 * @author External Warehouse
 * @version 1.0
 */
public class ZipUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);
    
    private static final int BUFFER_SIZE = 1024;
    
    /**
     * 単一ファイルをZIPに圧縮
     * 
     * @param sourceFile ソースファイルパス
     * @param zipFile ZIPファイルパス
     * @return 圧縮成功時はtrueを返す
     */
    public static boolean compressFile(String sourceFile, String zipFile) {
        return compressFiles(new String[]{sourceFile}, zipFile);
    }
    
    /**
     * 複数ファイルをZIPに圧縮
     * 
     * @param sourceFiles ソースファイルパス配列
     * @param zipFile ZIPファイルパス
     * @return 圧縮成功時はtrueを返す
     */
    public static boolean compressFiles(String[] sourceFiles, String zipFile) {
        if (sourceFiles == null || sourceFiles.length == 0) {
            logger.error("ソースファイルリストが空です");
            return false;
        }
        
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            
            for (String sourceFile : sourceFiles) {
                File file = new File(sourceFile);
                if (!file.exists()) {
                    logger.warn("ファイルが存在しません: {}", sourceFile);
                    continue;
                }
                
                addFileToZip(file, file.getName(), zos);
            }
            
            logger.info("ファイル圧縮成功 - ZIPファイル: {}, 圧縮ファイル数: {}", 
                zipFile, sourceFiles.length);
            return true;
            
        } catch (IOException e) {
            logger.error("ファイル圧縮失敗 - ZIPファイル: {}, エラー: {}", zipFile, e.getMessage());
            return false;
        }
    }
    
    /**
     * ディレクトリ全体をZIPに圧縮
     * 
     * @param sourceDir ソースディレクトリパス
     * @param zipFile ZIPファイルパス
     * @return 圧縮成功時はtrueを返す
     */
    public static boolean compressDirectory(String sourceDir, String zipFile) {
        return compressDirectory(sourceDir, zipFile, true);
    }
    
    /**
     * ディレクトリをZIPに圧縮
     * 
     * @param sourceDir ソースディレクトリパス
     * @param zipFile ZIPファイルパス
     * @param includeSubdirs サブディレクトリを含めるかどうか
     * @return 圧縮成功時はtrueを返す
     */
    public static boolean compressDirectory(String sourceDir, String zipFile, boolean includeSubdirs) {
        File dir = new File(sourceDir);
        if (!dir.exists() || !dir.isDirectory()) {
            logger.error("ディレクトリが存在しないか、ディレクトリではありません: {}", sourceDir);
            return false;
        }
        
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            
            int fileCount = addDirectoryToZip(dir, dir.getName(), zos, includeSubdirs);
            
            logger.info("ディレクトリ圧縮成功 - ZIPファイル: {}, ソースディレクトリ: {}, 圧縮ファイル数: {}", 
                zipFile, sourceDir, fileCount);
            return true;
            
        } catch (IOException e) {
            logger.error("ディレクトリ圧縮失敗 - ZIPファイル: {}, エラー: {}", zipFile, e.getMessage());
            return false;
        }
    }
    
    /**
     * パスワード付きZIPファイルを指定ディレクトリに解凍（最も簡単な方法）
     * 
     * @param zipFilePath ZIPファイルパス
     * @param destDir ターゲットディレクトリパス（nullの場合、現在のディレクトリを使用）
     * @param password パスワード（nullの場合、パスワードなしで解凍）
     * @return 解凍成功時はtrueを返す
     */
    public static boolean decompressWithPassword(String zipFilePath, String destDir, String password) {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            logger.error("ZIPファイルが存在しません: {}", zipFilePath);
            return false;
        }
        
        // ターゲットディレクトリがnullの場合、現在のディレクトリを使用
        if (destDir == null || destDir.isEmpty()) {
            destDir = System.getProperty("user.dir");
        }
        
        File destDirFile = new File(destDir);
        if (!destDirFile.exists()) {
            destDirFile.mkdirs();
        }
        
        try {
            ZipFile zip = new ZipFile(zipFilePath);
            // パスワードを設定
            if (password != null && !password.isEmpty()) {
                zip.setPassword(password.toCharArray());
            }
            
            zip.extractAll(destDir);
            
            logger.info("ZIPファイル解凍成功 - ZIPファイル: {}, ターゲットディレクトリ: {}", 
                zipFilePath, destDir);
            return true;
            
        } catch (ZipException e) {
            logger.error("ZIPファイル解凍失敗 - ZIPファイル: {}, エラー: {}", zipFilePath, e.getMessage());
            return false;
        }
    }
    
    /**
     * ZIPファイル内のファイルリストを取得
     * 
     * @param zipFilePath ZIPファイルパス
     * @return ファイルリスト、失敗時はnullを返す
     */
    public static List<String> listZipEntries(String zipFilePath) {
        return listZipEntries(zipFilePath, null);
    }
    
    /**
     * パスワード保護されたZIPファイル内のファイルリストを取得
     * 
     * @param zipFilePath ZIPファイルパス
     * @param password ZIPファイルのパスワード
     * @return ファイルリスト、失敗時はnullを返す
     */
    public static List<String> listZipEntries(String zipFilePath, String password) {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            logger.error("ZIPファイルが存在しません: {}", zipFilePath);
            return null;
        }
        
        try {
            ZipFile zip = new ZipFile(zipFilePath);
            // パスワードを設定
            if (password != null && !password.isEmpty()) {
                zip.setPassword(password.toCharArray());
            }
            
            List<String> entries = new ArrayList<>();
            List<FileHeader> fileHeaders = zip.getFileHeaders();
            for (FileHeader fileHeader : fileHeaders) {
                entries.add(fileHeader.getFileName());
            }
            
            logger.debug("ZIPファイルリスト取得成功 - ZIPファイル: {}, ファイル数: {}", 
                zipFilePath, entries.size());
            return entries;
            
        } catch (ZipException e) {
            logger.error("ZIPファイルリスト取得失敗 - ZIPファイル: {}, エラー: {}", zipFilePath, e.getMessage());
            return null;
        }
    }
    
    /**
     * ZIPファイルから単一ファイルを抽出
     * 
     * @param zipFilePath ZIPファイルパス
     * @param entryName ZIP内のファイルパス
     * @param destFile ターゲットファイルパス
     * @return 抽出成功時はtrueを返す
     */
    public static boolean extractSingleFile(String zipFilePath, String entryName, String destFile) {
        return extractSingleFile(zipFilePath, entryName, destFile, null);
    }
    
    /**
     * パスワード保護されたZIPファイルから単一ファイルを抽出
     * 
     * @param zipFilePath ZIPファイルパス
     * @param entryName ZIP内のファイルパス
     * @param destFile ターゲットファイルパス
     * @param password ZIPファイルのパスワード
     * @return 抽出成功時はtrueを返す
     */
    public static boolean extractSingleFile(String zipFilePath, String entryName, String destFile, String password) {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            logger.error("ZIPファイルが存在しません: {}", zipFilePath);
            return false;
        }
        
        try {
            ZipFile zip = new ZipFile(zipFilePath);
            // パスワードを設定
            if (password != null && !password.isEmpty()) {
                zip.setPassword(password.toCharArray());
            }
            
            FileHeader fileHeader = zip.getFileHeader(entryName);
            if (fileHeader == null) {
                logger.error("ZIPファイル内に指定ファイルが見つかりません: {}", entryName);
                return false;
            }
            
            File dest = new File(destFile);
            File parentDir = dest.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            zip.extractFile(fileHeader, parentDir.getAbsolutePath(), dest.getName());
            
            logger.info("ファイル抽出成功 - ZIPファイル: {}, ソースファイル: {}, ターゲットファイル: {}", 
                zipFilePath, entryName, destFile);
            return true;
            
        } catch (ZipException e) {
            logger.error("ファイル抽出失敗 - ZIPファイル: {}, エラー: {}", zipFilePath, e.getMessage());
            return false;
        }
    }
    
    /**
     * ZIPファイルが有効かどうかをチェック
     * 
     * @param zipFilePath ZIPファイルパス
     * @return 有効なZIPファイルの場合はtrueを返す
     */
    public static boolean isValidZipFile(String zipFilePath) {
        return isValidZipFile(zipFilePath, null);
    }
    
    /**
     * パスワード保護されたZIPファイルが有効かどうかをチェック
     * 
     * @param zipFilePath ZIPファイルパス
     * @param password ZIPファイルのパスワード
     * @return 有効なZIPファイルの場合はtrueを返す
     */
    public static boolean isValidZipFile(String zipFilePath, String password) {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            return false;
        }
        
        try {
            ZipFile zip = new ZipFile(zipFilePath);
            // パスワードを設定
            if (password != null && !password.isEmpty()) {
                zip.setPassword(password.toCharArray());
            }
            
            // 有効なZIPファイルかどうかをチェック
            return zip.isValidZipFile();
            
        } catch (Exception e) {
            logger.debug("無効なZIPファイル: {}, エラー: {}", zipFilePath, e.getMessage());
            return false;
        }
    }
    
    // ========== プライベートヘルパーメソッド ==========
    
    /**
     * ファイルをZIP出力ストリームに追加
     */
    private static void addFileToZip(File file, String entryName, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            // ディレクトリエントリを追加
            if (!entryName.endsWith("/")) {
                entryName += "/";
            }
            zos.putNextEntry(new ZipEntry(entryName));
            zos.closeEntry();
        } else {
            // ファイルエントリを追加
            zos.putNextEntry(new ZipEntry(entryName));
            
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
            }
            
            zos.closeEntry();
        }
    }
    
    /**
     * ディレクトリを再帰的にZIP出力ストリームに追加
     */
    private static int addDirectoryToZip(File dir, String basePath, ZipOutputStream zos, boolean includeSubdirs) throws IOException {
        int fileCount = 0;
        File[] files = dir.listFiles();
        
        if (files != null) {
            for (File file : files) {
                String entryName = basePath + "/" + file.getName();
                
                if (file.isFile()) {
                    addFileToZip(file, entryName, zos);
                    fileCount++;
                } else if (file.isDirectory() && includeSubdirs) {
                    // サブディレクトリを再帰的に処理
                    addFileToZip(file, entryName + "/", zos);
                    fileCount += addDirectoryToZip(file, entryName, zos, includeSubdirs);
                }
            }
        }
        
        return fileCount;
    }
}