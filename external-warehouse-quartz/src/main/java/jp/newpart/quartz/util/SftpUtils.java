package jp.newpart.quartz.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTPユーティリティクラス - SFTP接続管理、ファイルダウンロード、アップロード、存在確認機能を提供
 * 
 * @author nextosd
 * @version 1.0
 */
public class SftpUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(SftpUtils.class);
    
    /**
     * デフォルトSFTPポート
     */
    private static final int DEFAULT_SFTP_PORT = 22;
    
    /**
     * デフォルト接続タイムアウト時間（ミリ秒）
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    
    /**
     * デフォルトセッションタイムアウト時間（ミリ秒）
     */
    private static final int DEFAULT_SESSION_TIMEOUT = 30000;
    
    /**
     * SFTP接続を確立
     * 
     * @param host ホストアドレス
     * @param port ポート番号
     * @param username ユーザー名
     * @param password パスワード
     * @return ChannelSftpオブジェクト
     * @throws JSchException SFTP接続例外
     */
    public static ChannelSftp connect(String host, int port, String username, String password) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);
        session.setPassword(password);
        
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        
        session.setTimeout(DEFAULT_SESSION_TIMEOUT);
        session.connect(DEFAULT_CONNECT_TIMEOUT);
        
        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        
        logger.info("SFTP接続成功 - ホスト: {}, ポート: {}, ユーザー: {}", host, port, username);
        return channel;
    }

    /**
     * SFTP接続を確立
     *
     * @param host ホストアドレス
     * @param port ポート番号
     * @param username ユーザー名
     * @param privateKeyPath 秘密鍵パス
     * @return ChannelSftpオブジェクト
     * @throws JSchException SFTP接続例外
     */
    public static ChannelSftp connectByPrivateKey(String host, int port, String username, String privateKeyPath) throws JSchException {
        JSch jsch = new JSch();
        jsch.addIdentity(privateKeyPath);
        Session session = jsch.getSession(username, host, port);


        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.setTimeout(DEFAULT_SESSION_TIMEOUT);
        session.connect(DEFAULT_CONNECT_TIMEOUT);

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        logger.info("SFTP Private Key - 接続成功 - ホスト: {}, ポート: {}, ユーザー: {}", host, port, username);
        return channel;
    }
    
    /**
     * SFTP接続を確立（デフォルトポート22を使用）
     * 
     * @param host ホストアドレス
     * @param username ユーザー名
     * @param password パスワード
     * @return ChannelSftpオブジェクト
     * @throws JSchException SFTP接続例外
     */
    public static ChannelSftp connect(String host, String username, String password) throws JSchException {
        return connect(host, DEFAULT_SFTP_PORT, username, password);
    }
    
    /**
     * SFTP接続を閉じる
     * 
     * @param channel SFTPチャネル
     */
    public static void disconnect(ChannelSftp channel) {
        if (channel != null) {
            Session session = null;
            try {
                // 先获取session，再断开通道
                session = channel.getSession();
                channel.disconnect();
            } catch (Exception e) {
                logger.warn("SFTPチャネルを閉じる際に例外が発生: {}", e.getMessage());
            }
            
            // セッション接続を切断
            if (session != null && session.isConnected()) {
                try {
                    session.disconnect();
                } catch (Exception e) {
                    logger.warn("SFTPセッションを閉じる際に例外が発生: {}", e.getMessage());
                }
            }
            
            logger.info("SFTP接続が閉じられました");
        }
    }
    
    /**
     * SFTPサーバー上のファイルをダウンロード
     * 
     * @param channel SFTPチャネル
     * @param remotePath リモートファイルパス
     * @param localPath ローカル保存パス
     * @return ダウンロードが成功したかどうか
     */
    public static boolean downloadFile(ChannelSftp channel, String remotePath, String localPath) {
        if (channel == null || !channel.isConnected()) {
            logger.error("SFTPチャネルが接続されていないか、切断されています");
            return false;
        }
        
        try {
            // ローカルディレクトリを作成（存在しない場合）
            File localFile = new File(localPath);
            File parentDir = localFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    logger.info("ローカルディレクトリを作成しました: {}", parentDir.getAbsolutePath());
                } else {
                    logger.error("ローカルディレクトリの作成に失敗しました: {}", parentDir.getAbsolutePath());
                    return false;
                }
            }
            
            try (OutputStream outputStream = new FileOutputStream(localPath)) {
                channel.get(remotePath, outputStream);
                logger.info("ファイルダウンロード成功 - リモートパス: {}, ローカルパス: {}", remotePath, localPath);
                return true;
            }
        } catch (SftpException | IOException e) {
            logger.error("ファイルダウンロード失敗 - リモートパス: {}, ローカルパス: {}, エラー: {}", 
                remotePath, localPath, e.getMessage());
            return false;
        }
    }
    
    /**
     * SFTPサーバー上のファイルをバイト配列としてダウンロード
     * 
     * @param channel SFTPチャネル
     * @param remotePath リモートファイルパス
     * @return ファイル内容のバイト配列、失敗した場合はnullを返す
     */
    public static byte[] downloadFileToBytes(ChannelSftp channel, String remotePath) {
        if (channel == null || !channel.isConnected()) {
            logger.error("SFTPチャネルが接続されていないか、切断されています");
            return null;
        }
        
        try (InputStream inputStream = channel.get(remotePath)) {
            byte[] fileData = IOUtils.toByteArray(inputStream);
            logger.info("ファイルをバイト配列としてダウンロード成功 - リモートパス: {}, ファイルサイズ: {} バイト", 
                remotePath, fileData.length);
            return fileData;
        } catch (SftpException | IOException e) {
            logger.error("ファイルをバイト配列としてダウンロード失敗 - リモートパス: {}, エラー: {}", 
                remotePath, e.getMessage());
            return null;
        }
    }
    
    /**
     * ファイルをSFTPサーバーにアップロード
     * 
     * @param channel SFTPチャネル
     * @param localPath ローカルファイルパス
     * @param remotePath リモート保存パス
     * @return アップロードが成功したかどうか
     */
    public static boolean uploadFile(ChannelSftp channel, String localPath, String remotePath) {
        if (channel == null || !channel.isConnected()) {
            logger.error("SFTPチャネルが接続されていないか、切断されています");
            return false;
        }
        
        try (InputStream inputStream = new FileInputStream(localPath)) {
            // リモートディレクトリが存在することを確認
            createRemoteDirectory(channel, remotePath);
            
            channel.put(inputStream, remotePath);
            logger.info("ファイルアップロード成功 - ローカルパス: {}, リモートパス: {}", localPath, remotePath);
            return true;
        } catch (SftpException | IOException e) {
            logger.error("ファイルアップロード失敗 - ローカルパス: {}, リモートパス: {}, エラー: {}", 
                localPath, remotePath, e.getMessage());
            return false;
        }
    }
    
    /**
     * バイト配列をSFTPサーバーにアップロード
     * 
     * @param channel SFTPチャネル
     * @param fileData ファイルデータのバイト配列
     * @param remotePath リモート保存パス
     * @return アップロードが成功したかどうか
     */
    public static boolean uploadBytesToFile(ChannelSftp channel, byte[] fileData, String remotePath) {
        if (channel == null || !channel.isConnected()) {
            logger.error("SFTPチャネルが接続されていないか、切断されています");
            return false;
        }
        
        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            // リモートディレクトリが存在することを確認
            createRemoteDirectory(channel, remotePath);
            
            channel.put(inputStream, remotePath);
            logger.info("バイト配列のアップロード成功 - リモートパス: {}, データサイズ: {} バイト", 
                remotePath, fileData.length);
            return true;
        } catch (SftpException | IOException e) {
            logger.error("バイト配列のアップロード失敗 - リモートパス: {}, エラー: {}", 
                remotePath, e.getMessage());
            return false;
        }
    }
    
    /**
     * SFTPサーバー上のファイルが存在するか判断
     * 
     * @param channel SFTPチャネル
     * @param remotePath リモートファイルパス
     * @return ファイルが存在するかどうか
     */
    public static boolean fileExists(ChannelSftp channel, String remotePath) {
        if (channel == null || !channel.isConnected()) {
            logger.error("SFTPチャネルが接続されていないか、切断されています");
            return false;
        }
        
        try {
            channel.lstat(remotePath);
            logger.debug("ファイルが存在します - リモートパス: {}", remotePath);
            return true;
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                logger.debug("ファイルが存在しません - リモートパス: {}", remotePath);
                return false;
            } else {
                logger.error("ファイルの存在確認中に例外が発生しました - リモートパス: {}, エラー: {}", 
                    remotePath, e.getMessage());
                return false;
            }
        }
    }
    
    /**
     * SFTPサーバー上のファイルを削除
     * 
     * @param channel SFTPチャネル
     * @param remotePath リモートファイルパス
     * @return 削除が成功したかどうか
     */
    public static boolean deleteFile(ChannelSftp channel, String remotePath) {
        if (channel == null || !channel.isConnected()) {
            logger.error("SFTPチャネルが接続されていないか、切断されています");
            return false;
        }
        
        try {
            channel.rm(remotePath);
            logger.info("ファイル削除成功 - リモートパス: {}", remotePath);
            return true;
        } catch (SftpException e) {
            logger.error("ファイル削除失敗 - リモートパス: {}, エラー: {}", remotePath, e.getMessage());
            return false;
        }
    }
    
    /**
     * リモートディレクトリ内のファイルをリスト表示
     * 
     * @param channel SFTPチャネル
     * @param remoteDir リモートディレクトリパス
     * @return ファイルリスト，失敗した場合はnull
     */
    public static Vector<ChannelSftp.LsEntry> listFiles(ChannelSftp channel, String remoteDir) {
        if (channel == null || !channel.isConnected()) {
            logger.error("SFTPチャネルが接続されていないか、切断されています");
            return null;
        }
        
        try {
            Vector<ChannelSftp.LsEntry> files = channel.ls(remoteDir);
            logger.debug("ファイルのリスト表示に成功 - リモートディレクトリ: {}, ファイル数: {}", remoteDir, files.size());
            return files;
        } catch (SftpException e) {
            logger.error("ファイルのリスト表示に失敗 - リモートディレクトリ: {}, エラー: {}", remoteDir, e.getMessage());
            return null;
        }
    }
    
    /**
     * リモートディレクトリを作成（存在しない場合）
     * 
     * @param channel SFTPチャネル
     * @param remotePath リモートパス
     */
    private static void createRemoteDirectory(ChannelSftp channel, String remotePath) {
        try {
            String directory = remotePath.substring(0, remotePath.lastIndexOf('/'));
            if (!directory.isEmpty()) {
                // 再帰的にディレクトリを作成
                String[] dirs = directory.split("/");
                StringBuilder currentPath = new StringBuilder();
                
                for (String dir : dirs) {
                    if (!dir.isEmpty()) {
                        currentPath.append("/").append(dir);
                        try {
                            channel.mkdir(currentPath.toString());
                        } catch (SftpException e) {
                            // ディレクトリが既に存在する場合、例外を無視
                            if (e.id != ChannelSftp.SSH_FX_FAILURE) {
                                throw e;
                            }
                        }
                    }
                }
            }
        } catch (SftpException e) {
            logger.warn("リモートディレクトリの作成に失敗: {}", e.getMessage());
        }
    }
    
    /**
     * tempディレクトリ下のすべてのファイルを削除
     * 
     * @param localPathTemplate ローカルパステンプレート
     * @return 削除が成功したかどうか
     */
    public static boolean cleanTempDirectory(String localPathTemplate) {
        File tempDir = new File(localPathTemplate);
        if (!tempDir.exists() || !tempDir.isDirectory()) {
            logger.info("tempディレクトリが存在しません: {}", tempDir.getAbsolutePath());
            return true;
        }
        
        File[] files = tempDir.listFiles();
        if (files == null) {
            logger.info("tempディレクトリのファイルを取得できません: {}", tempDir.getAbsolutePath());
            return true;
        }
        
        boolean allDeleted = true;
        for (File file : files) {
            if (file.isDirectory()) {
                if (!deleteDirectory(file)) {
                    allDeleted = false;
                }
            } else {
                if (!file.delete()) {
                    logger.warn("ファイルの削除に失敗しました: {}", file.getAbsolutePath());
                    allDeleted = false;
                } else {
                    logger.debug("ファイルを削除しました: {}", file.getAbsolutePath());
                }
            }
        }
        
        if (allDeleted) {
            logger.info("tempディレクトリのクリーンアップが完了しました");
        } else {
            logger.warn("tempディレクトリのクリーンアップに一部失敗しました");
        }
        
        return allDeleted;
    }
    
    /**
     * ディレクトリを再帰的に削除
     * 
     * @param directory 削除するディレクトリ
     * @return 削除が成功したかどうか
     */
    private static boolean deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (!deleteDirectory(file)) {
                        return false;
                    }
                } else {
                    if (!file.delete()) {
                        logger.warn("ファイルの削除に失敗しました: {}", file.getAbsolutePath());
                        return false;
                    }
                }
            }
        }
        return directory.delete();
    }
    
}