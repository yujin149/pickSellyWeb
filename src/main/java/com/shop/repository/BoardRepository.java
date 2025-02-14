package com.shop.repository;

import com.shop.dto.BoardDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final SqlSessionTemplate sql;
    public void writeForm(BoardDTO boardDTO){
        sql.insert("Board.writeForm", boardDTO);
    }
}
