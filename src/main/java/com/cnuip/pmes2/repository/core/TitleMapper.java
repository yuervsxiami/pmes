package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.Title;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleMapper {
    List<Title> selectAll();
}
