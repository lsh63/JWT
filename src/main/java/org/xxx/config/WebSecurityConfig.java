package org.xxx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.xxx.security.jwt.JwtAuthenticationEntryPoint;
import org.xxx.security.jwt.JwtAuthenticationTokenFilter;

//@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                //OPTIONS方法表明请求想得到请求/响应链上关于此请求里的URI（Request-URI）指定资源的通信选项信息
                .antMatchers(
                		HttpMethod.OPTIONS,//OPTIONS方法下路径无需验证
                		"/**"//所有路径
                ).permitAll()

                // allow anonymous resource requests
                .antMatchers(
                        HttpMethod.GET,//GET方法下路径无需用户验证
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/images/defaultUserImg.jpg",
                        "/js/libs/jquery-3.2.1.min.js",
                        //"/user/**",
                        //"/articlecomment/**",
                        //"/articlefloorcomment/**",
                        //"/persons/**",
                        "/article/**"//免用户登录验证,
                ).permitAll()
                
                .antMatchers(
                		HttpMethod.POST
                ).permitAll()

                // Un-secure H2 Database
                .antMatchers("/h2-console/**/**").permitAll()

                .antMatchers("/auth/**").permitAll()//用户登录时无需用户验证
                .antMatchers("/user/create").permitAll()//用户注册时无需用户验证
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching 禁用页面缓存
        httpSecurity
                .headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }
}