package com.cnuip.pmes2.service;


import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.cnuip.pmes2.controller.api.request.ModifyPasswordParam;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.UserException;
import com.github.pagehelper.PageInfo;

/**
* Create By Crixalis:
* 2017年12月29日 下午1:58:41
*/
public interface UserService extends UserDetailsService {

    User selectUserByPhone(String phone);
    
    User selectUserByUsername(String username);

    User selectUserByPrimaryKey(Long userId);
    
    PageInfo<User> selectAll(int pageNum, int pageSize);
    
    PageInfo<User> selectByRoleId(Long roleId, int pageNum, int pageSize);
    
    User add(User user) throws UserException;
    
    User login(User user) throws UserException;
    
    User regist(User user) throws UserException;
    
    User update(User user) throws UserException;
    
    void updatePassword(ModifyPasswordParam param) throws UserException;
    
    User updateByPhone(User user) throws UserException;
    
    int updateUserRole(Long userId, Long roleId);

    int updateUserState(Long userId,Integer state);

	PageInfo<User> search(User user, int pageNum, int pageSize);
    
}
