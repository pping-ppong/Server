package com.app.pingpong.global.config.security;

import com.app.pingpong.global.config.JwtAccessDeniedHandler;
import com.app.pingpong.global.config.JwtAuthenticationEntryPoint;
import com.app.pingpong.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()

                //.exceptionHandling()
                //.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                //.accessDeniedHandler(jwtAccessDeniedHandler)
                //.and()

                // 시큐리티는 기본적으로 세션을 사용하나 여기서는 사용하지 않도록 stateless로 설정한다.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeHttpRequests()
                .requestMatchers("/oauth/**", "/app/users/kakao/**").permitAll() // 여기는 인증 필요 없음
                .anyRequest().authenticated()// 나머지 API는 모두 인증 필요
                .and()

                .apply(new JwtSecurityConfig(jwtTokenProvider));

                //.and()
                //.oauth2ResourceServer();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/assets/**", "/h2-console/**", "/favicon.ico");
    }
}
