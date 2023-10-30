package com.korit.board.dto;

import com.korit.board.entity.Board;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class EditBoardReqDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    // categoryId가 없으면 추가하는 로직 추가, int는 @NotBlank가 안걸림->@Min(보다 작은값은 안들어온다)
    @Min(0)
    private int categoryId;
    @NotBlank
    private String categoryName;

    public Board toBoardEntity(String email) {
        return Board.builder()
                .boardTitle(title)
                .boardCategoryId(categoryId)
                .boardContent(content)
                .email(email)
                .build();
    }
}
