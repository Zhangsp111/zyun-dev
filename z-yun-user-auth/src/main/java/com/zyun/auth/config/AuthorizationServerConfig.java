package com.zyun.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;


@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    //jwt令牌转换器
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    // 访问token的过期时间
    @Value("${auth.tokenValiditySeconds}")
    private Integer tokenValiditySeconds;
    // 刷新token的过期时间
    @Value("${auth.RefreshTokenValiditySeconds}")
    private Integer refreshTokenValiditySeconds;
    @Autowired
    @Qualifier("UserDetailServiceImpl")
    UserDetailsService userDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenStore tokenStore;
    @Autowired
    private CustomUserAuthenticationConverter customUserAuthenticationConverter;

    //读取密钥的配置
    @Bean("keyProp")
    public KeyProperties keyProperties(){
        return new KeyProperties();
    }

    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    @Autowired
    DefaultTokenServices defaultTokenServices;


    //配置客户端校验类，Jdbc校验数据库或内存
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(this.dataSource).clients(this.clientDetails());
        // 向内存中保存 配合下面使用
       /* clients.inMemory()
                .withClient("XcWebApp")//客户端id
                .secret("XcWebApp")//密码，要保密
                .accessTokenValiditySeconds(60)//访问令牌有效期
                .refreshTokenValiditySeconds(60)//刷新令牌有效期
                //授权客户端请求认证服务的类型authorization_code：根据授权码生成令牌，
                // client_credentials:客户端认证，refresh_token：刷新令牌，password：密码方式认证
                .authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token", "password")
                .scopes("app");//客户端范围，名称自定义，必填*/
    }

    //token的存储方法
    //@Bean
    //public InMemoryTokenStore tokenStore() {
    //    //将令牌存储到内存
    //    return new InMemoryTokenStore();
    //}
    //@Bean
    //public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory){
    //    RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
    //    return redisTokenStore;
    //}
    @Bean
    @Autowired
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(@Autowired CustomUserAuthenticationConverter customUserAuthenticationConverter) {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory
                (keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getSecret().toCharArray())
                .getKeyPair(keyProperties.getKeyStore().getAlias(),keyProperties.getKeyStore().getPassword().toCharArray());
        converter.setKeyPair(keyPair);
        //配置自定义的CustomUserAuthenticationConverter,获取自定义的当前登陆用户信息
        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter);
        return converter;
    }

    /**
     * 授权服务器端点配置
     * 配置令牌增强器等信息
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 令牌增强器，实现TokenEnhancer并重写enhance向授权中额外添加其他信息
        // Collection<TokenEnhancer> tokenEnhancers = applicationContext.getBeansOfType(TokenEnhancer.class).values();
        // TokenEnhancerChain tokenEnhancerChain=new TokenEnhancerChain();
        // tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));


        endpoints.authenticationManager(authenticationManager)// 认证管理器
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .userDetailsService(userDetailsService)// 配置userDetailsService实现类
                .accessTokenConverter(jwtAccessTokenConverter) // jwt token解析
                //.tokenStore(tokenStore);
                .tokenServices(defaultTokenServices);
        // 自定义确认授权页面
        // endpoints.pathMapping("/oauth/confirm_access", "/oauth/confirm_access");
        // 自定义错误页
        // endpoints.pathMapping("/oauth/error", "/oauth/error");
        // 自定义异常转换类
        // endpoints.exceptionTranslator(new OpenOAuth2WebResponseExceptionTranslator());
    }

    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        // 令牌存储的持久性策略。
        tokenServices.setTokenStore(tokenStore);
        // 支持token刷新
        tokenServices.setSupportRefreshToken(true);
        // tokenServices.setClientDetailsService(customClientDetailsService);
        // token有效期自定义设置
        tokenServices.setAccessTokenValiditySeconds(tokenValiditySeconds);
        // refresh_token
        tokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        // 配置jwt token增强，这个东西要和tokenStore一起配置
        tokenServices.setTokenEnhancer(jwtAccessTokenConverter);

        return tokenServices;
    }

    //授权服务器的安全配置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//        oauthServer.checkTokenAccess("isAuthenticated()");//校验token需要认证通过，可采用http basic认证
        oauthServer.allowFormAuthenticationForClients()
                .passwordEncoder(new BCryptPasswordEncoder())
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

}

