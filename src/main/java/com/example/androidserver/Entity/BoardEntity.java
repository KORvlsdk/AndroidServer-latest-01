package com.example.androidserver.Entity;

import com.example.androidserver.Constant.Role;
import com.example.androidserver.Dto.BoardItemDto;
import com.example.androidserver.Dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public String title;
    public String sub_title;
    public String content;

    public static BoardEntity createUser(BoardItemDto boardItemDto){
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(boardItemDto.getTitle());
        boardEntity.setSub_title(boardItemDto.getSubTitle());
        boardEntity.setContent(boardItemDto.getSubTitle());
        return boardEntity;
    }
    public static BoardItemDto changeToDto(BoardEntity boardEntity) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(boardEntity, BoardItemDto.class);
    }
}
