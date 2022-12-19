package com.cyworld.front.auth;

import com.cyworld.common.util.Sha512PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Controller;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@ComponentScan(basePackages = {"com.cyworld"},
        excludeFilters = @ComponentScan.Filter(value= Controller.class, type = FilterType.ANNOTATION))
public class FrontSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new Sha512PasswordEncoder();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        // 성공할 때 실행되어야 하는 loginSuccessHandler 를 빈으로 등록
        LoginSuccessHandler loginSuccessHandler = new LoginSuccessHandler();
        return loginSuccessHandler;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        AuthenticationFailureHandler authenticationFailureHandler = new FrontAuthenticationFailureHandler();
        return authenticationFailureHandler;
    }

    @Bean
    public SessionRegistry sessionRegistry () {
        return new SessionRegistryImpl();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("q1w2e3r4"))
                .roles("USER");
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring() // spring security 필터 타지 않도록 설정
                .antMatchers("/resources/**"); // 정적 리소스 무시
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 1. URL 별 권한 설정
        // 2. login, logout url 과 성공했을 때, 실패했을 때 설정
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/signup").permitAll()   // /login, /signup 은 인증 안해도 접근 가능하도록 설정
                .antMatchers(
                        "/mypage",
                       "/web/**"
                ).authenticated()             // /mypage 은 인증이 되야함
                .and()
                .formLogin()                                    // form 을 통한 login 활성화
                .loginPage("/login")                            // 로그인 페이지 URL 설정 , 설정하지 않는 경우 default 로그인 페이지 노출
                .usernameParameter("userid").passwordParameter("userpw") //default 'userid', 'userpw'
                .loginProcessingUrl("/core/login")
                .successHandler(loginSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/logout");                // 로그아웃 URL 설정

        http.sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .sessionFixation().migrateSession() //악성 사용자를 막기 위해 security에서 매번 인증 요청시마다 session을 재생성
                .maximumSessions(-1) // 동시 접속 가능수 TODO 5 정도로 변경, -1 무제한
                .maxSessionsPreventsLogin(false) //true=기존에 동일한 사용자가 로그인한 경우  login 불가 / false=로그인이 되고 기존사용자는 세션만료
                .sessionRegistry(sessionRegistry());
    }
}
