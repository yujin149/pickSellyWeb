package com.shop.service;

import com.shop.dto.BoardDTO;
import com.shop.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void writeForm(BoardDTO boardDTO) {
        boardRepository.writeForm(boardDTO);

    }
}
