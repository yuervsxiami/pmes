package com.cnuip.pmes2.service.impl;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnuip.pmes2.controller.api.request.ModifyPasswordParam;
import com.cnuip.pmes2.domain.core.User;
import com.cnuip.pmes2.exception.BussinessLogicException;
import com.cnuip.pmes2.exception.UserException;
import com.cnuip.pmes2.repository.core.AuthorityMapper;
import com.cnuip.pmes2.repository.core.UserMapper;
import com.cnuip.pmes2.service.UserService;
import com.cnuip.pmes2.util.ResponseEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;

/**
* Create By Crixalis:
* 2017年12月29日 下午2:10:35
*/
@Transactional(readOnly=true)
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthorityMapper authorityMapper;
	
	@Autowired
	private IdentityService identityService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.selectUserByUsername(username);
		if (user == null) {
			user = userMapper.selectUserByPhone(username);
		}
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在");
		}
		user.getRole().setAuthorities(authorityMapper.selectByRoleId(user.getRoleId()));
		return user;
	}

	@Override
	public User selectUserByPhone(String phone) {
		return userMapper.selectUserByPhone(phone);
	}

	@Override
	public User selectUserByUsername(String username) {
		return userMapper.selectUserByPhone(username);
	}

	@Override
	public User selectUserByPrimaryKey(Long userId) {
		return userMapper.selectUserByPrimaryKey(userId);
	}

	@Override
	public PageInfo<User> selectAll(int pageNum, int pageSize) {
		Page<User> page = (Page<User>) userMapper.selectAll(pageNum, pageSize);
		return page.toPageInfo();
	}

	@Override
	public PageInfo<User> selectByRoleId(Long roleId, int pageNum, int pageSize) {
		Page<User> page = (Page<User>) userMapper.selectUserByRole(roleId, pageNum, pageSize);
		return page.toPageInfo();
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public User add(User user) throws UserException {
		checkUserInfo(user);
		checkUniqueInfo(user, 0);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.add(user);
		userMapper.updateUserRole(user.getId(), user.getRoleId());
		if(user.getState() == null) {
			user.setState(0);
		}
		userMapper.updateUserState(user.getId(), user.getState());
		addUserToActiviti(user);
		return userMapper.selectUserByPrimaryKey(user.getId());
	}

	@Override
	public User login(User loginUser) throws UserException {
		User sysUser = null;
		if (sysUser == null && !Strings.isNullOrEmpty(loginUser.getPhone())) {
			sysUser = userMapper.selectUserByUsername(loginUser.getPhone());
		}
		if (sysUser == null && !Strings.isNullOrEmpty(loginUser.getPhone())) {
			sysUser = userMapper.selectUserByPhone(loginUser.getPhone());
		}
		if (sysUser != null && !Strings.isNullOrEmpty(loginUser.getPassword())
				&& this.passwordEncoder.matches(loginUser.getPassword(), sysUser.getPassword())) {
			return sysUser;
		}
		throw new UserException(ResponseEnum.USER_LOGIN_PASSWORD);
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public User regist(User user) throws UserException {
		checkUserInfo(user);
		checkUniqueInfo(user, 0);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.add(user);
		addUserToActiviti(user);
		return userMapper.selectUserByPrimaryKey(user.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public User update(User user) throws UserException {
		checkUniqueInfo(user, 1);
		if(user == null && !Strings.isNullOrEmpty(user.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		userMapper.update(user);
		return userMapper.selectUserByPrimaryKey(user.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public void updatePassword(ModifyPasswordParam param) throws UserException {
		User u = userMapper.selectUserByPrimaryKey(param.getUserId());
		if(u == null) {
			throw new UserException(ResponseEnum.USER_ISNOTEXIST);
		}
		if(!Strings.isNullOrEmpty(param.getOldPassword())
				&& this.passwordEncoder.matches(param.getOldPassword(), u .getPassword())) {
			User user = new User(param.getUserId(),passwordEncoder.encode(param.getNewPassword()));
			userMapper.updatePassword(user);
			return;
		}
		throw new UserException(ResponseEnum.USER_LOGIN_PASSWORD);
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public User updateByPhone(User user) throws UserException {
		checkUserInfo(user);
		checkUniqueInfo(user, 1);
		userMapper.updateByPhone(user);
		return userMapper.selectUserByPrimaryKey(user.getId());
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public int updateUserRole(Long userId, Long roleId) {
		int result = userMapper.updateUserRole(userId, roleId);
		setActivitiMemberShip("user"+userId , "role" + roleId);
		return result;
	}

	@Transactional(rollbackFor={BussinessLogicException.class, Exception.class})
	@Override
	public int updateUserState(Long userId, Integer state) {
		return userMapper.updateUserState(userId, state);
	}

	private void checkUserInfo(User user) throws UserException {
		if(Strings.isNullOrEmpty(user.getUsername())) {
			throw new UserException(ResponseEnum.USER_REGIST_ACCOUNT);
		}
		if(Strings.isNullOrEmpty(user.getPhone())) {
			throw new UserException(ResponseEnum.USER_EMPTYPHONE);
		}
		if(Strings.isNullOrEmpty(user.getPassword())) {
			throw new UserException(ResponseEnum.USER_EMPTYPASSWORD);
		}
	}
	
	private void checkUniqueInfo(User user,int limit) throws UserException {
		if(userMapper.checkPhoneExist(user.getPhone())>limit) {
			throw new UserException(ResponseEnum.USER_PHONEISEXIST);
		}
		if(userMapper.checkUsernameExist(user.getUsername())>limit) {
			throw new UserException(ResponseEnum.USER_USERNAMEISEXIST);
		}
	}

	@Override
	public PageInfo<User> search(User user, int pageNum, int pageSize) {
		Page<User> page = (Page<User>) userMapper.search(user, pageNum, pageSize);
		return page.toPageInfo();
	}
	
	private void addUserToActiviti(User user) {
		String userId = "user" + user.getId();
		String roleId = "role" + user.getRoleId();
		saveActUser(user, userId);  
		setActivitiMemberShip(userId, roleId);
	}

	private void setActivitiMemberShip(String userId, String roleId) {
		deleteAllGroupShip(userId);
		identityService.createMembership(userId, roleId);
	}

	private void deleteAllGroupShip(String userId) {
		List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();  
        for(Group group : activitiGroups) {  
            identityService.deleteMembership(userId, group.getId());  
        }
	}

	private void saveActUser(User user, String userId) {
		org.activiti.engine.identity.User activitiUser = identityService.newUser(userId); 
		activitiUser.setFirstName(user.getName());  
        activitiUser.setEmail(user.getEmail());  
        identityService.saveUser(activitiUser);
	}

}
