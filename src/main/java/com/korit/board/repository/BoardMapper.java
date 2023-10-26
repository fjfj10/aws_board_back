package com.korit.board.repository;

import com.korit.board.entity.Board;
import com.korit.board.entity.BoardCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface BoardMapper {
    public List<BoardCategory> getBoardCategories();
    public int saveCategory(BoardCategory boardCategory);
    public int saveBoard(Board board);
    public List<Board> getBoardList(Map<String, Object> paramsMap);
    public int getBoardCount(Map<String, Object> paramsMap);
    public Board getBoardByBoardId(int boardId);
}
