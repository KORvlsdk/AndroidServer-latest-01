package com.example.androidserver.Filter;

import com.example.androidserver.Security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class jwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("doFilterInternal 호출");
        // 요청 헤더에서 Authorization를 얻어옴
        String receiveToken = request.getHeader("Authorization");
        // receiveToken이 Authorization가 jwt 방식이 아닌 경우
        if (receiveToken == null || !receiveToken.startsWith("Bearer ")) {
            System.out.println("filterChain.doFilter(request, response) 직전");
            filterChain.doFilter(request, response);
            System.out.println("filterChain.doFilter(request, response) 직후");
            return;
        }
        // Authentication 객체 얻어와서
        Authentication authentication;
        try {
            System.out.println("try 시작");
            authentication = getAuthentication(request, response, filterChain);
            System.out.println("authentication 생성");
            // SecurityContextHolder 에 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("SecurityContextHolder 등록");
            filterChain.doFilter(request, response);
            System.out.println("filterChain 동작");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws Exception {
        String token = request.getHeader("Authorization");

        return jwtTokenProvider.validateToken(token);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if(request.getServletPath().equals("/user/login")) {
            return true;
        } else if(request.getServletPath().equals("/user/register")) {
            return true;
        }
        return false;
    }
}
