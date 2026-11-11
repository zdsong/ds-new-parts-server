package jp.newpart.quartz.security;

import jp.newpart.common.core.domain.entity.SysUser;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.core.service.SysPasswordService;
import jp.newpart.common.enums.UserStatus;
import jp.newpart.common.exception.ServiceException;
import jp.newpart.common.utils.ConvertUtils;
import jp.newpart.common.utils.MessageUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.quartz.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPasswordService passwordService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = sysUserMapper.selectUserByUserName(username);

        if (StringUtils.isNull(user))
        {
            log.info("ログインユーザ:{}は存在しません.", username);
            throw new ServiceException(MessageUtils.message("E0050", username));
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("ログインユーザー:{}が削除されました。", username);
            throw new ServiceException(MessageUtils.message("E0054"));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("ログインユーザ:{}が停止されました", username);
            throw new ServiceException(MessageUtils.message("E0049"));
        }

        if(ValueUtils.isNotEmpty(user.getRoles())){
            user.setRoleId(user.getRoles().get(0).getRoleId());
        }

        passwordService.validate(user.getPassword());

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.isAdmin(), ConvertUtils.toStr(user.getUserId()), user.getUserName(), user.getNickName(), user.getPassword(), null);
    }
}
