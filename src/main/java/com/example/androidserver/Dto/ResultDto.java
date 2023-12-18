package com.example.androidserver.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ResultDto {
    public String token;
    public Boolean result;
    public String message;
    public UserDto userDto;

    public List<BoardItemDto> boardItemDtos;
}
