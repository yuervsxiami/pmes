package com.cnuip.pmes2.service.impl;

import com.cnuip.pmes2.domain.core.Match;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.repository.core.MatchMapper;
import com.cnuip.pmes2.service.MatchService;
import com.cnuip.pmes2.util.ResponseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * MatchServiceImpl
 *
 * @author: crixalis
 * Date: 2018/2/6 下午3:13
 */
@Service
@Transactional(readOnly = true)
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchMapper matchMapper;

    @Transactional(rollbackFor = BussinessLogicException.class)
    @Override
    public Match save(Match requirement) throws BussinessLogicException {
        try {
            this.matchMapper.save(requirement);
        } catch (Exception e) {
            throw new BussinessLogicException(e, ResponseEnum.MATCH_SAVE_ERROR);
        }
        return this.matchMapper.findById(requirement.getId());
    }

    @Override
    public Match findById(Long id) {
        return this.matchMapper.findById(id);
    }

    @Transactional(rollbackFor = BussinessLogicException.class)
    @Override
    public Match update(Match requirement) throws BussinessLogicException {
        try {
            this.matchMapper.update(requirement);
        } catch (Exception e) {
            throw new BussinessLogicException(e, ResponseEnum.MATCH_UPDATE_ERROR);
        }
        return this.matchMapper.findById(requirement.getId());
    }

    @Transactional(rollbackFor = BussinessLogicException.class)
    @Override
    public int delete(Match requirement) throws BussinessLogicException {
        int result;
        try {
            result = this.matchMapper.delete(requirement);
        } catch (Exception e) {
            throw new BussinessLogicException(e, ResponseEnum.MATCH_DELETE_ERROR);
        }
        return result;
    }

    @Override
    public List<Match> find(Match requirement) {
        return this.matchMapper.find(requirement);
    }
}
