package com.example.androidserver.Dto;

import com.example.androidserver.Entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private String password;
    private String phoneNum;

    public static UserEntity changeToEntity(UserDto userDto) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(userDto, UserEntity.class);
    }
}
