package com.korit.board.controller;

import com.korit.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/categories")
    public ResponseEntity<?> getCategories() {

        return ResponseEntity.ok(boardService.getBoardCategoriesAll());
    }
}
