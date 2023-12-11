package com.example.androidserver.Entity;

import com.example.androidserver.Constant.Role;
import com.example.androidserver.Dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private String phoneNum;
    @Enumerated(EnumType.STRING)
    private Role role;

    public static UserEntity createUser(UserDto userDto, PasswordEncoder passwordEncoder){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setEmail(userDto.getEmail());
        String password = passwordEncoder.encode(userDto.getPassword());
        userEntity.setPassword(password);
        userEntity.setPhoneNum(userDto.getPhoneNum());
        userEntity.setRole(Role.USER);
        return userEntity;
    }
    public static UserDto changeToDto(UserEntity userEntity) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(userEntity, UserDto.class);
    }
}
