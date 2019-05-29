package com.cnuip.pmes2.repository.core;


import com.cnuip.pmes2.domain.core.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author: xiongwei
 * Date: 2017/3/5 下午3:31
 */
@Repository
public interface UserMapper {

    User selectUserByPhone(String phone);
    
    User selectUserByUsername(String username);

    User selectUserByPrimaryKey(Long userId);

    @Select("Select * from p_user WHERE id = #{id}")
    @ResultMap("baseSimpleMap")
    User selectSimpleUserByPrimaryKey(Long id);

    List<User> selectAll(@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
    
    List<User> selectUserByRole(@Param("roleId")Long roleId,@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
    
    List<User> selectUserByRoleNoUser(@Param("roleId")Long roleId, @Param("userId") Long userId, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
    
    List<User> search(@Param("user")User user,@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
    
    int add(User user);
    
    int update(User user);
    
    int updateByPhone(User user);
    
    int updateUserRole(@Param("userId")Long userId, @Param("roleId")Long roleId);

    int updateUserState(@Param("userId")Long userId,@Param("state")Integer state);
    
    int checkUsernameExist(String username);
    
    int checkPhoneExist(String phone);
    
    void updatePassword(User user);
    
}
