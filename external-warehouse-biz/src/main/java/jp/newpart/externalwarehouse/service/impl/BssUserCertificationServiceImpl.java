package jp.newpart.externalwarehouse.biz.service.impl;

import jp.newpart.common.biz.constants.ExternalWarehouseConstants;
import jp.newpart.common.biz.domain.entity.BssUserCertificationEntity;
import jp.newpart.common.biz.enums.BssUserCertificationFlag;
import jp.newpart.common.biz.service.IBssUserCertificationBaseService;
import jp.newpart.common.biz.service.ISysConfigService;
import jp.newpart.common.config.AppConfig;
import jp.newpart.common.core.components.EMailService;
import jp.newpart.common.core.enums.DelFlgEnum;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.externalwarehouse.biz.service.IBssUserCertificationService;
import jp.newpart.common.utils.*;
import jp.newpart.common.utils.uuid.IdUtils;
import jp.newpart.externalwarehouse.biz.domain.bo.BssUserAuthCodeMailBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * ユーザー認証情報Serviceインタフェース
 *
 * @author nextosd
 */
@Service
@Slf4j
public class BssUserCertificationServiceImpl implements IBssUserCertificationService {

    @Autowired
    private IBssUserCertificationBaseService bssUserCertificationBaseService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private EMailService emailService;

    /**
     * ユーザIDに認証コードを取得する
     * @param userId
     * @return 認証コード
     */
    @Override
    public BssUserCertificationEntity getAuthCodeByUserId(String userId) {
        BssUserCertificationEntity bssUserCertificationEntityQuery = new BssUserCertificationEntity();
        bssUserCertificationEntityQuery.setFarmerId(userId);
        bssUserCertificationEntityQuery.setDiscontinuedFlg(DelFlgEnum.OFF.getCode());

        List<BssUserCertificationEntity> bssUserCertificationList = bssUserCertificationBaseService.selectList(bssUserCertificationEntityQuery);

        if(ValueUtils.isEmpty(bssUserCertificationList) || ValueUtils.isEmpty(bssUserCertificationList.get(0).getCertificationCode())){
            // E0011 ユーザーが送信した認証情報に存在ありません。
            throw new ServiceException(MessageUtils.message("E0011"));
        }

        return bssUserCertificationList.get(0);
    }

    /**
     * 認証コード - EMail送信する
     *
     * @param params 送信
     * @return ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String sendAuthCodeByEmail(BssUserAuthCodeMailBo params) {

        // ユーザーID E0003={0}は必須入力です。
        if(ValueUtils.isEmpty(params.getUserId())){
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.userName")));
        }

        // メールアドレス E0003={0}は必須入力です。
        if(ValueUtils.isEmpty(params.getMailAddress())){
            throw new ServiceException(MessageUtils.message("E0003", MessageUtils.message("label.mailAddress")));
        }

        String authCode = null;

        if(ValueUtils.isNotEqual(AppConfig.isMailEnabled(), false)) {
            authCode = this.generateCode();
        } else {
            authCode = "888888";//DateUtils.dateTimeNow("yyMMdd");
        }

        // 前回送信は廃止する
        BssUserCertificationEntity bssUserCertificationEntityDelete = new BssUserCertificationEntity();
        bssUserCertificationEntityDelete.setUpdatePgId(params.getPgId());
        bssUserCertificationEntityDelete.setFarmerId(params.getUserId());
        bssUserCertificationBaseService.deleteLogicByFarmId(bssUserCertificationEntityDelete);

        BssUserCertificationEntity bssUserCertificationEntity = new BssUserCertificationEntity();
        bssUserCertificationEntity.setId(IdUtils.nextIdStr());
        bssUserCertificationEntity.setType(params.getType().getCode());
        bssUserCertificationEntity.setCertificationFlg(BssUserCertificationFlag.MAIL.getCode());
        bssUserCertificationEntity.setMailAddress(params.getMailAddress());
        bssUserCertificationEntity.setFarmerId(params.getUserId());
        bssUserCertificationEntity.setCreatePgId(params.getPgId());
        bssUserCertificationEntity.setVersion(1);
        bssUserCertificationEntity.setCertificationCode(authCode);
        bssUserCertificationEntity.setDiscontinuedFlg(DelFlgEnum.OFF.getCode());

        // 分間有効
        int minutes = ConvertUtils.toInt(sysConfigService.selectConfigByKey(ExternalWarehouseConstants.SANCHOKU_AUTH_CODE_EXPIRE_MINUTES), 10);
        // 開始時間
        long startTime = System.currentTimeMillis();
        // 有効終了時間
        long endTime = startTime + ConvertUtils.toInt(sysConfigService.selectConfigByKey(ExternalWarehouseConstants.SANCHOKU_AUTH_CODE_EXPIRE_MINUTES), 10) * 60 * 1000;

        bssUserCertificationEntity.setStartTime(DateUtils.getTime(startTime));
        bssUserCertificationEntity.setEndTime(DateUtils.getTime(endTime));

        bssUserCertificationBaseService.insert(bssUserCertificationEntity);

        // Email 送信
        String mailInfo =StringUtils.format(sysConfigService.selectConfigByKey(ExternalWarehouseConstants.SANCHOKU_AUTH_CODE_LOGIN_EMAIL_CONTENT)
                , params.getUserName()
                , bssUserCertificationEntity.getCertificationCode()
                , minutes);
        String mailTitle = sysConfigService.selectConfigByKey(ExternalWarehouseConstants.SANCHOKU_AUTH_CODE_LOGIN_EMAIL_TITLE);
        String mailAddress = DecryptUtils.decryptAES(params.getMailAddress());

        log.debug("DEV 認証コード = " + bssUserCertificationEntity.getCertificationCode());

        if(ValueUtils.isNotEqual(AppConfig.isMailEnabled(), false)) {
            // BCCメールアドレス
            String bcc = sysConfigService.selectConfigByKey(ExternalWarehouseConstants.SANCHOKU_AUTH_CODE_BCC_EMAIL_ADDRESS);

            if(ValueUtils.isNotEmpty(bcc)){
                // Email 送信 & Bcc
                emailService.sendHtmlMail(mailAddress, null, bcc, mailTitle, mailInfo);
            } else {
                // Email 送信
                emailService.sendHtmlMail(mailAddress, mailTitle, mailInfo);
            }
        }

        return bssUserCertificationEntity.getId();
    }

    /**
     * 農家IDによる認証記録を廃止
     *
     * @param bssUserCertification
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteLogicById(BssUserCertificationEntity bssUserCertification) {
        return bssUserCertificationBaseService.deleteLogicById(bssUserCertification);
    }

    /**
     * 認証コードの作成
     * @return
     */
    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        int codeLength = ConvertUtils.toInt(sysConfigService.selectConfigByKey(ExternalWarehouseConstants.SANCHOKU_AUTH_CODE_LENGTH), 6);
        for (int i = 0; i < codeLength; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
