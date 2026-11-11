package jp.newpart.quartz.config;

import jp.newpart.common.config.AppConfig;
import jp.newpart.common.config.properties.PermitAllUrlProperties;
import jp.newpart.common.security.filter.JwtAuthenticationTokenFilter;
import jp.newpart.common.security.handle.AuthenticationEntryPointImpl;
import jp.newpart.common.security.handle.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Securityの設定
 *
 * @author nextosd
 */
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfig {
    /**
     * カスタムユーザー認証ロジック
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 認証失敗処理クラス
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * ログアウト処理クラス
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * トークン認証フィルター
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * クロスドメインフィルター
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * 匿名でアクセス可能なURL
     */
    @Autowired
    private PermitAllUrlProperties permitAllUrl;

    /**
     * 認証実装
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        List<AuthenticationProvider> providerList = new ArrayList<>();
        providerList.add(daoAuthenticationProvider);

        return new ProviderManager(providerList);
    }

    /**
     * anyRequest          |   すべてのリクエストパスをマッチ
     * access              |   SpringEL式の結果がtrueのときにアクセス可能
     * anonymous           |   匿名でアクセス可能
     * denyAll             |   ユーザーはアクセスできない
     * fullyAuthenticated  |   ユーザーが完全に認証されているときアクセス可能（remember-meによる自動ログインを除く）
     * hasAnyAuthority     |   パラメータがある場合、パラメータが権限を表し、その中のいずれかの権限でアクセス可能
     * hasAnyRole          |   パラメータがある場合、パラメータが役割を表し、その中のいずれかの役割でアクセス可能
     * hasAuthority        |   パラメータがある場合、パラメータが権限を表し、その権限でアクセス可能
     * hasIpAddress        |   パラメータがある場合、パラメータがIPアドレスを表し、ユーザーのIPがパラメータと一致するときアクセス可能
     * hasRole             |   パラメータがある場合、パラメータが役割を表し、その役割でアクセス可能
     * permitAll           |   ユーザーは任意にアクセス可能
     * rememberMe          |   remember-meによってログインしたユーザーがアクセス可能
     * authenticated       |   ユーザーがログイン後アクセス可能
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // CSRFを無効化、セッションを使用しないため
                .csrf(csrf -> csrf.disable())
                // HTTPレスポンスヘッダーを無効化
                .headers((headersCustomizer) -> {
                    headersCustomizer.cacheControl(cache -> cache.disable()).frameOptions(options -> options.sameOrigin());
                })
                // 認証失敗処理クラス
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // トークンベースなので、セッションは必要ない
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 許可された匿名アクセスのURLを注解でマーク
                .authorizeHttpRequests((requests) -> {
                    permitAllUrl.getUrls().forEach(url -> requests.requestMatchers(url).permitAll());

                    // ログイン、登録、キャプチャ画像には匿名アクセスを許可
                    List<String> whiteUrlList = AppConfig.getWhiteUrlList();
                    whiteUrlList.forEach(url -> requests.requestMatchers(url).permitAll());

                    // 静的リソースには匿名アクセスを許可
                    requests.requestMatchers(HttpMethod.GET, "/", "/*.html", "/**.html", "/**.css", "/**.js", "/profile/**").permitAll()
                            .requestMatchers("/api/auth/login").permitAll()
                            // 上記以外のすべてのリクエストは認証が必要
                            .anyRequest().authenticated();
                })
                // ログアウトフィルターを追加
                .logout(logout -> logout.logoutUrl("/api/auth/logout").logoutSuccessHandler(logoutSuccessHandler))
                // JWTフィルターを追加
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // CORSフィルターを追加
                .addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class)
                .addFilterBefore(corsFilter, LogoutFilter.class)
                .build();
    }

    /**
     * 強力なハッシュ暗号化実装
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
