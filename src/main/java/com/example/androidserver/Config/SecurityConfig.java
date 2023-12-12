package com.example.androidserver.Config;

import com.example.androidserver.Filter.jwtAuthenticationFilter;
import com.example.androidserver.Security.JwtTokenProvider;
import com.example.androidserver.Service.AuthenticationProviderService;
import com.example.androidserver.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProviderService customProvider;
    private final JwtTokenProvider jwtTokenProvider;
    // 새로운 시큐리티는 컴포넌트 방식의 설정과
    // lamda DSL(Domain Specific Language)을 권장한다.
    // lamda DSL을 사용한 설정으로 가독성이 향상되었고, 모호했던 설정이 좀 더 명확해졌다고 한다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 보안 구성은 <Spring Security>에서 하며, <HttpSecurity>는 <보안 설정 객체>를 제공해주는 객체이다.
        // <HttpSecurity>는 <Spring Security>에서 설정하는 <보안 설정 객체>를 반환하는 <메서드>를 제공해준다.
        // <메서드>를 호출해서 <보안 설정 객체>를 얻고, <해당 객체>를 설정하는 것이다.
        // 실제로는 <보안 설정 객체>에 건네준 각종 <메서드>, <인자>는 해당 객체에 내부적으로 저장되며,
        // <저장된 상태값>들은 <Spring Security>에서 내부적으로 처리되어 보안 구성이 완성된다.
        http
                // csrf()를 호출하면, 메서드 내부에서 CsrfConfigurer(csrf 설정 객체)를 생성, 반환한다.
                // 정확히는 CsrfConfigurer<HttpSecurity>()
                // <CsrfConfigurer>는 <AbstractHttpConfigurer>를 확장하는데,
                // <CsrfConfigurer>는 단순 <설정 객체>이고,
                // csrf 보호 기능을 제공하는건 <AbstractHttpConfigurer>이다.
                // 아래 코드의 의미는 CsrfConfigurer에서 AbstractHttpConfigurer의
                // disable()의 참조 주소를 건네주고 있다.
                // 나중에 <Spring Security>의 보안 구성 단계에서 <설정 객체(CsrfConfigurer 포함)>들을 읽는데,
                // CsrfConfigurer에 저장된 <disable 참조 주소>를 통해 <csrf 보호 기능> 비활성화가 이루어진다.
                .csrf(AbstractHttpConfigurer::disable)
                // <authorizeHttpRequests>는 <HttpSecurity> 를 반환한다.
                // 특정 URL 패턴에 대한 권한 설정을 하거나,
                // 인증 여부를 확인하는 등의 보안 관련 설정을 정의할 수 있다.
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/login", "/user/register").permitAll()
                        .anyRequest().authenticated()
                )
                // 세션 설정 비활성화
                .sessionManagement(ssetion -> ssetion
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new jwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
                // 예외 설정, 문제) 로그인 하지 않고, 인증 필요한 페이지 접근하면 오류 코드 반환이 아니라,
                // 로그인 페이지로 리다이렉션 됨. 이유는 아직 모르겠음.
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            // 403 Forbidden 코드를 반환
//                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
//                        })
//                );
        return http.build();
    }

    // <스프링 시큐리티>에는 <다양한 필터>들이 있고, 각 필터마다 기능들이 다르다.
    // <인증을 담당하는 필터>도 여러개 있는데, 각 필터는 <인증 관리자> 객체가 존재한다(AuthenticationManager).
    // <시큐리티 설정 파일>에서 AuthenticationManager를 설정하면, 기본적으로는 UsernamePasswordAuthenticationFilter에 적용된다.
    // 아래 코드의 <ProviderManager>는 AuthenticationManager 인터페이스의 구현체로, 시큐리티에서 아무런 설정을 하지 않았을 경우
    // AuthenticationManager의 구현체로 사용되는 객체이다.
    // AuthenticationManager를 직접 구현할 수 있지만, 보통은 ProviderManager 만으로도 충분하다.
    @Bean
    public ProviderManager authenticationManager() {
        // ProviderManager를 초기화 하고 있다. 초기화 하지 않으면 <스프링 시큐리티>에서 제공되는 <인증 공급자>로 초기화된다.
        // ProviderManager는 <인증 공급자>로 초기화를 진행하고, <인증 공급자>를 쓰는 <인증 관리자>는 로그인 방식에 따라
        // 어떤 <인증 공급자>를 쓸지 결정하고, 인증의 책임을 <인증 공급자>에 주게 된다.
        return new ProviderManager(Collections.singletonList(customProvider));
    }
}
