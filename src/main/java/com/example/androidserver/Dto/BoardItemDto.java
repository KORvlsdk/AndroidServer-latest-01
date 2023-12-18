package com.example.androidserver.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardItemDto {
    public String title;
    public String subTitle;
    public String content;
    public List<String> images;
}
