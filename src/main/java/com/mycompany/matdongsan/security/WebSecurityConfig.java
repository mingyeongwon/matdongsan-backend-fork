package com.mycompany.matdongsan.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    // 인증 필터 체인을 관리 객체로 등록
    @Bean
    public SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
        // rest API에서 로그인 폼을 제공하지 않으므로 폼을 통한 로그인 인증을 하지 않도록설정
        // 로그인 폼은 front-end에서 제공
        http.formLogin(config -> config.disable());

        // HttpSession을 사용하지 않도록 설정
        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //사이트간 요청 위조 방지 비활성화(GET 방식 이외의 바식 요청은 _csrf 토큰을 요구하기 때문)
        http.csrf(config -> config.disable());

        // CORS 설정(크로스 도메인에서 받은 인증 정보(AccessToken)로 요청s할 경우 허가)
        http.cors(config -> {
        });

        //JWT로 인증이되도록 필터를 등록
        //JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 인증 관리자를 관리 객체로 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 권한 계층을 관리 객체로 등록
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
        return hierarchy;
    }

    //@PreAuthorize 어노테이션의 사용법
    @Bean
    public MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy());
        return handler;
    }

    //다른 도메인(크로스 도메인) 제한설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //요청 사이트 제한
        configuration.addAllowedOrigin("*");
        //요청 방식 제한
        configuration.addAllowedMethod("*");
        //configuration.addAllowedMethod("POST");
        //요청 헤더 제한
        configuration.addAllowedHeader("*");
        //모든 URL에 대해서 위 설정 내용 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}

