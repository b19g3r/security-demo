package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * demo3
     * 经测试, 此方法无需编写
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getBcryptPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getBcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 自定义登录页面
        http.formLogin()
                // 登录页面
                .loginPage("/login.html")
                // 鉴权的 controller 接口
                .loginProcessingUrl("/user/login")
                // 登录成功后默认的跳转地址
                .defaultSuccessUrl("/test/index").permitAll()
                .and()
                .authorizeRequests()
                // 设置直接访问的路径
                .antMatchers("/", "/test/t", "/user/login").permitAll()
                // 需要权限才能访问的路径
                .antMatchers("/test/index").hasAnyAuthority("admins")
                .antMatchers("/test/t").hasAnyAuthority("admins")
                .anyRequest().authenticated()
                // 关闭 csrf 防护
                .and().csrf().disable();
    }
}
