package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.Match;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MatchMapper
 *
 * @author: xiongwei
 * Date: 2018/2/5 下午8:53
 */
@Repository
public interface MatchMapper {

    int save(Match match);

    Match findById(Long id);

    int update(Match match);

    int delete(Match match);

    List<Match> find(Match match);
    
    int changeState(@Param("id")Long id, @Param("state")Integer state);
    
}

