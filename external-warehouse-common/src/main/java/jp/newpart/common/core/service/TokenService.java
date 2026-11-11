package jp.newpart.common.core.service;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jp.newpart.common.cache.AppCache;
import jp.newpart.common.constant.CacheConstants;
import jp.newpart.common.constant.Constants;
import jp.newpart.common.constant.IdPrefixConstants;
import jp.newpart.common.core.domain.model.LoginUser;
import jp.newpart.common.utils.ServletUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.common.utils.ip.AddressUtils;
import jp.newpart.common.utils.ip.IpUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * CRM token トークン検証処理
 *
 * @author nextosd
 */
@Component
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    // トークンカスタムID
    @Value("${token.header}")
    private String header;

    // トークンキー
    @Value("${token.secret}")
    private String secret;

    // トークン有効期間（デフォルト30分）
    @Value("${token.expireTime}")
    private int expireTime;

    @Value("${token.tempExpireTime}")
    private long tokenTempExpireTime = 10;

    // トークンの自動更新トークン有効時間（分）
    @Value("${token.automaticRenewalTokenMin}")
    private Integer automaticRenewalTokenMin;

    // ログインユーザー cache key
    @Value("${token.tokenCacheKey}")
    private String tokenCacheKey;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    @Autowired
    private AppCache appCache;

    /**
     * ユーザーID情報の取得
     *
     * @return ユーザー情報
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // リクエスト付きトークンの取得
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            try
            {
                Claims claims = parseToken(token);
                // 対応する権限とユーザー情報の解析
                String uuid = (String) claims.get(tokenCacheKey);
                String userKey = getTokenKey(uuid);
                LoginUser user = appCache.getCacheObject(userKey, LoginUser.class);
                return user;
            }
            catch (Exception e)
            {
                log.error("ユーザー情報の取得例外Token={}, message='{}'", token, e.getMessage());
            }
        }
        return null;
    }

    /**
     * ユーザー識別情報の設定
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * ユーザー識別情報の削除
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
            appCache.deleteObject(userKey);
        }
    }

    /**
     * トークンの作成
     *
     * @param loginUser ユーザー情報
     * @return トークン
     */
    public String createToken(LoginUser loginUser)
    {
        // ユーザートークンの削除
        if(ValueUtils.isNotEmpty(loginUser.getToken())){
            delLoginUser(loginUser.getToken());
        }
        String token = IdUtils.nextIdStr(IdPrefixConstants.TOKEN_ID);
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(tokenCacheKey, token);
        return createToken(claims);
    }

    /**
     * トークンの有効期限を検証し、残り時間が20分未満の場合、自動的にキャッシュを更新
     *
     * @param loginUser
     * @return トークン
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= automaticRenewalTokenMin * MILLIS_MINUTE)  {
            refreshToken(loginUser);
        }
    }

    /**
     * トークン有効期間のリフレッシュ
     *
     * @param loginUser ログイン情報
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // loginUserをキャッシュに保存
        String userKey = getTokenKey(loginUser.getToken());
        appCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * ユーザーエージェント情報の設定
     *
     * @param loginUser ログイン情報
     */
    public void setUserAgent(LoginUser loginUser)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * データ宣言からトークンを生成するには
     *
     * @param claims データ宣言
     * @return トークン
     */
    private String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * トークンからのデータ宣言の取得
     *
     * @param token トークン
     * @return データ宣言
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateTempToken(String username) {
        return generateToken(username, tokenTempExpireTime);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * トークンからのユーザー名の取得
     *
     * @param token トークン
     * @return ユーザー名
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 取得リクエストtoken
     *
     * @param request リクエスト
     * @return token トークン
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            return token.replace(Constants.TOKEN_PREFIX, "");
        }
        return null;
    }

    private String getTokenKey(String uuid)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }

    private String generateToken(String username, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
