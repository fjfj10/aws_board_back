package com.korit.board.entity;

import com.korit.board.dto.BoardListRespDto;
import com.korit.board.dto.GetBoardRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Board {
    private int boardId;
    private String boardTitle;
    private int boardCategoryId;
    private String boardContent;
    private String email;
    private String nickname;
    private LocalDateTime createDate;
    private int boardHitsCount;
    private int boardLikeCount;

    public BoardListRespDto toBoardListRespDto() {
        return BoardListRespDto.builder()
                .boardId(boardId)
                .title(boardTitle)
                .nickname(nickname)
                .createDate(createDate.format(DateTimeFormatter.ISO_DATE))  // createDate.format(DateTimeFormatter.ISO_DATE): 년, 월, 일 잘라줌
                .hitsCount(boardHitsCount)
                .likeCount(boardLikeCount)
                .build();
    }

    public GetBoardRespDto toBoardDto() {
        return GetBoardRespDto.builder()
                .boardId(boardId)
                .boardTitle(boardTitle)
                .boardCategoryId(boardCategoryId)
                .boardContent(boardContent)
                .email(email)
                .nickname(nickname)
                // GetBoardRespDto의 createDate가 String으로 잡혀있음. ofLocalizedDate를 사용하면 그 지역의 날짜에 맞게 바꿔줌? FULL/LONG/등등
                .createDate(createDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)))
                .boardHitsCount(boardHitsCount)
                .boardLikeCount(boardLikeCount)
                .build();
    }
}
