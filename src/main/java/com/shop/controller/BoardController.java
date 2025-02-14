package com.shop.controller;

import ch.qos.logback.core.model.Model;
import com.shop.dto.BoardDTO;
import com.shop.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping(value = "community/notice")
    public String noticeList(){
        return "community/notice";
    }

    @GetMapping(value = "community/noticeWrite")
    public String writeForm() {
        return "community/noticeWrite";
    }

    @PostMapping(value = "community/noticeWrite")
    public String writeForm(BoardDTO boardDTO){
        System.out.println("boardDTO = " + boardDTO);
        boardService.writeForm(boardDTO);
        return "community/notice";
    }

}
