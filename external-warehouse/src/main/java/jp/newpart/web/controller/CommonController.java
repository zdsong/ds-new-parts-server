package jp.newpart.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.newpart.common.biz.domain.bo.OptionsParamBo;
import jp.newpart.common.biz.service.IExternalWarehouseCommonService;
import jp.newpart.common.config.AppConfig;
import jp.newpart.common.config.ServerConfig;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.core.domain.AjaxResult;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.file.FileUploadUtils;
import jp.newpart.common.utils.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 汎用リクエスト処理
 * 
 * @author nextosd
 */
@RestController
@RequestMapping("/api/common")
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    private static final String FILE_DELIMETER = ",";

    @Autowired
    private IExternalWarehouseCommonService commonService;

    /**
     * コンボボックスデータを取得する
     * @param optionsParam
     * @return
     */
    @PostMapping(value = "/options")
    public AjaxResult getCommonOptions(@RequestBody OptionsParamBo optionsParam) {
        return AjaxResult.success(commonService.getCommonOptions(optionsParam));
    }

    /**
     * 都アドレス コンボボックスデータを取得する
     *
     * @return アドレスコンボボックスリスト
     */
    @GetMapping(value = "/ken/options")
    public AjaxResult getKenCommonOptions() {
        OptionsParamBo optionsParam = new OptionsParamBo();
        optionsParam.setCmdOptionName("ken");
        return AjaxResult.success(commonService.selectAddressOptionsList(optionsParam));
    }

    /**
     * 市区アドレス コンボボックスデータを取得する
     *
     * @return アドレスコンボボックスリスト
     */
    @GetMapping(value = "/{kenId}/city/options")
    public AjaxResult getCityCommonOptions(@PathVariable("kenId") String kenId) {
        OptionsParamBo optionsParam = new OptionsParamBo();
        optionsParam.setCmdOptionName("city");
        optionsParam.setConditionValue1(kenId);
        return AjaxResult.success(commonService.selectAddressOptionsList(optionsParam));
    }

    /**
     * 郵便番号を使用して住所を検索します。
     *
     * @return 住所
     */
    @GetMapping(value = "/address/byPostalCode")
    public AjaxResult getAddressByPostalCode(@RequestParam("postalCode") String postalCode) {

        return AjaxResult.success(commonService.selectMastAddressByPostalCode(postalCode));
    }

    /**
     * 汎用ダウンロードリクエスト
     * 
     * @param fileName ファイル名
     * @param delete 削除するかどうか
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("ファイル名（{}）が不正で、ダウンロードは許可されていません。", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = AppConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("ファイルのダウンロードに失敗しました", e);
        }
    }

    /**
     * 汎用アップロードリクエスト（単一）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // ファイルパスのアップロード
            String filePath = AppConfig.getUploadPath();
            // 新しいファイル名をアップロードして戻す
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 汎用アップロードリクエスト（複数）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            // ファイルパスのアップロード
            String filePath = AppConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                // 新しいファイル名をアップロードして戻す
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * ローカルリソースの一般的なダウンロード
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("リソースファイル({})は不正で、ダウンロードは許可されていません。 ", resource));
            }

            String localPath = AppConfig.getProfile();

            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);

            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("ファイルのダウンロードに失敗しました", e);
        }
    }
}
