package jp.newpart.externalwarehouse.auth.service;

import jp.newpart.common.biz.domain.entity.BssUserCertificationEntity;
import jp.newpart.common.biz.enums.ExternalWarehousePgIdEnum;
import jp.newpart.common.core.domain.entity.FarmerUserInfo;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.enums.UserStatus;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.exception.user.AuthVerifyCodeNotMatchException;
import jp.newpart.common.security.context.AuthenticationContextHolder;
import jp.newpart.externalwarehouse.biz.mapper.FarmerUserMapper;
import jp.newpart.externalwarehouse.biz.service.IBssUserCertificationService;
import jp.newpart.common.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * ユーザ認証処理
 *
 * @author nextosd
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private FarmerUserMapper farmerUserMapper;

    @Autowired
    private IBssUserCertificationService bssUserCertificationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        FarmerUserInfo user = farmerUserMapper.selectUserByUserName(username);

        if (StringUtils.isNull(user))
        {
            log.info("ログインユーザ:{}は存在しません.", username);
            throw new ServiceException(MessageUtils.message("E0050", username));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getUsageStatus()))
        {
            log.info("ログインユーザ:{}が停止されました", username);
            throw new ServiceException(MessageUtils.message("E0049"));
        }

        // 認証コード
        String authCode = validate();

        return createLoginUser(user, authCode);
    }

    public String validate() {

        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String authCode = usernamePasswordAuthenticationToken.getCredentials().toString();

        // ユーザIDに認証を取得する
        BssUserCertificationEntity bssUserCertificationEntity = bssUserCertificationService.getAuthCodeByUserId(username);

        Date nowTime = DateUtils.getNowDate();
        Date endTime = DateUtils.parseDate(bssUserCertificationEntity.getEndTime());

        if(ValueUtils.isNotEqual(bssUserCertificationEntity.getCertificationCode(), authCode)){
            log.info("ログインユーザ:{}が認証コードが無効です。", username);
            throw new AuthVerifyCodeNotMatchException();
        }

        // E0013=認証コードの有効期限が切れました。
        if(nowTime.compareTo(endTime) > 0){
            log.info("ログインユーザ:{}が認証コードの有効期限が切れました。", username);
            throw new ServiceException(MessageUtils.message("E0013"));
        }

        bssUserCertificationEntity.setUpdatePgId(ExternalWarehousePgIdEnum.N003.getCode());
        bssUserCertificationEntity.setUpdateBy(username);
        bssUserCertificationEntity.setUpdateBy(DateUtils.getTime());

        // IDによる認証記録を廃止
        bssUserCertificationService.deleteLogicById(bssUserCertificationEntity);

        return bssUserCertificationEntity.getCertificationCode();
    }

    public UserDetails createLoginUser(FarmerUserInfo user, String authCode)
    {
        return new LoginUser(false, user.getId(), user.getFarmerId(), user.getFarmerName(), SecurityUtils.encryptPassword(authCode), null);
    }
}
