package com.example.androidserver.Controller;

import com.example.androidserver.Dto.ResultDto;
import com.example.androidserver.Dto.UserDto;
import com.example.androidserver.Entity.UserEntity;
import com.example.androidserver.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDto userForm) {
        UserEntity user = UserEntity.createUser(userForm, passwordEncoder);

        if(userService.saveUser(user)) {
            return "회원가입이 완료되었습니다.";
        }

        return "회원가입 실패..";
    }
    @PostMapping("/login")
    public ResultDto loginUser(@RequestBody UserDto userForm) {
        UserEntity user = UserDto.changeToEntity(userForm);

        return userService.loginUser(user);
    }

    @PostMapping("/auth")
    public ResultDto Test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResultDto resultDto = new ResultDto();
        resultDto.setResult(true);
        resultDto.setToken(authentication.getCredentials().toString());
        resultDto.setMessage("로그인 성공!");
        return resultDto;
    }
}
