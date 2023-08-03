package com.wuhobin.config.security;


import com.wuhobin.config.security.AuthenticationEnryPoint;
import com.wuhobin.config.security.AuthenticationLogout;
import com.wuhobin.config.security.CustomizeAccessDeniedHandler;
import com.wuhobin.config.security.PermitAllUrlProperties;
import com.wuhobin.config.security.mobile.JwtMobileAuthenticationTokenFilter;
import com.wuhobin.config.security.mobile.MobileCodeAuthenticationFilter;
import com.wuhobin.config.security.mobile.MobileCodeAuthenticationProvider;
import com.wuhobin.service.user.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author wuhongbin
 * @description: spring security 配置类
 * @datetime 2023/01/12 15:48
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private PermitAllUrlProperties permitAllUrlProperties;

    /**
     * 未登录时 自定义处理逻辑
     */
    @Autowired
    private AuthenticationEnryPoint authenticationEnryPoint;

    /**
     * 退出登录时 自定义处理逻辑
     */
    @Autowired
    private AuthenticationLogout authenticationLogout;

    /**
     * 没有权限时 自定义处理逻辑
     */
    @Autowired
    private CustomizeAccessDeniedHandler customizeAccessDeniedHandler;


    /**
     * 从数据库中获取用户
     */
    @Autowired
    private UserDetailService userDetailService;



    /**
     * 自定义token拦截过滤器
     * 用于手机号验证码登录
     */
    @Autowired
    private JwtMobileAuthenticationTokenFilter jwtMobileAuthenticationTokenFilter;


    /**
     * 注入加密处理类
     * @return
     */

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 跨域配置
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }



    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/configuration/ui",
                "/swagger-resources",
                "/swagger-resources/configuration/security",
                "/swagger-ui.html");
    }

    /**
     * 配置认证处理器
     * @param auth AuthenticationManagerBuilder
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(mobileCodeAuthenticationProvider());
    }


    /**
     * 需要手动将AuthenticationManager暴露出来
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public MobileCodeAuthenticationProvider mobileCodeAuthenticationProvider() {
        return new MobileCodeAuthenticationProvider(userDetailService);
    }


    /**
     *  mobileCodeAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
     *  java.lang.IllegalArgumentException: authenticationManager must be specified 上面这一行 用来解决以下报错
     * @return
     * @throws Exception
     */
    @Bean
    public MobileCodeAuthenticationFilter mobileCodeAuthenticationFilter() throws Exception {
        MobileCodeAuthenticationFilter mobileCodeAuthenticationFilter = new MobileCodeAuthenticationFilter();
        mobileCodeAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return mobileCodeAuthenticationFilter;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 注解标记允许匿名访问的url
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        permitAllUrlProperties.getUrls().forEach(url -> registry.antMatchers(url).permitAll());

        //登录前进行过滤
        http
                .csrf().disable()
                .headers().cacheControl().disable().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtMobileAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/login/send","/api/login/mobile-login").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEnryPoint)
                .accessDeniedHandler(customizeAccessDeniedHandler)
                .and().cors();
        http.logout().logoutUrl("/api/user/logout").logoutSuccessHandler(authenticationLogout);
        http.addFilterBefore(corsFilter(), JwtMobileAuthenticationTokenFilter.class);
        http.addFilterBefore(corsFilter(), LogoutFilter.class);
    }
}

/*
* 踩坑记录
  1. .formLogin().loginProcessingUrl().successHandler()
                .failureHandler()
   如果spring security配置了formLogin，controller里面写的登录接口就会失效
   登录成功的逻辑和失败的逻辑都是在successHandler和failureHandler中配置的，两者缺一不可
   如果不配置则都会进入到.authenticationEntryPoint(authenticationEnryPoint)

  2. 如果项目中自定义了过滤器并且继承自OncePerRequestFilter，那么你所有的请求都会进入到该过滤器中，就算在配置中permitAll了也会进入到过滤器
  3. 如果项目中自定义了过滤器继承自OncePerRequestFilter，并且该类上面加了类似于@Component的注解，不在SpringSecurityConfig配置文件中配置该过滤器也可以生效
* */