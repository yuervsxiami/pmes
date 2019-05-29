package com.cnuip.pmes2.service;

import com.cnuip.pmes2.domain.core.Match;
import com.cnuip.pmes2.exception.BussinessLogicException;

import java.util.List;

/**
 * MatchService
 *
 * @author: crixalis
 * Date: 2018/2/6 下午3:12
 */
public interface MatchService {

    Match save(Match match) throws BussinessLogicException;

    Match findById(Long id);

    Match update(Match match) throws BussinessLogicException;

    int delete(Match match) throws BussinessLogicException;

    List<Match> find(Match match);

}
