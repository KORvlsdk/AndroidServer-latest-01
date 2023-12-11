package com.example.androidserver.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
    // 시큐리티 의존성 주입에서 <순환 참조 문제>가 발생하여, PasswordEncoder 설정을 따로 빼둠.
    // 순환 참조 : 객체 생성을 어떤 것부터 해야할지 결정을 못할 때 발생.
    // 예시
    // -> <인증 공급자>는 <시큐리티 설정파일>에서 빈으로 등록된 <패스워드 인코더> 필요
    // -> <스프링 시큐리티>는 설정 파일 내부에서 사용되는 <인증 공급자> 필요.
    // -> <인증 공급자> 객체가 먼저 생성되면, <패스워드 인코더> 때문에 <시큐리티 설정파일>를 참조하려함.
    // -> 반대로 <시큐리티 설정파일>은 객체 생성에 필요한 <인증 공급자> 참조하려 접근하면, <인증 공급자>에서는 다시 <시큐리티 설정파일> 참조하려함.
    // 결국 <인증 공급자>와 <시큐리티 설정파일>간에 참조가 무한 반복됨.
    // <PasswordEncoder> 설정을 따로 빼면 해결 가능!
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
