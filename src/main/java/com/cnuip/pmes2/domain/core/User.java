package com.cnuip.pmes2.domain.core;

import com.cnuip.pmes2.domain.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
* Create By Crixalis:
* 2017年12月28日 上午10:18:34
*/
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
@NoArgsConstructor
public class User extends BaseModel implements UserDetails {

	private Long id;
	private String name;
	private String username;
	@JsonProperty(access = WRITE_ONLY)
	private String password;
	private String email;
	private String phone;
	private String pic;
	private Integer state;
	private Date createTime;
	private Date updateTime;
	private Long roleId;
	private Role role;
	private Long organizationId;
	private String organizationName;
	private Organization organization;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	@Override
	@JsonProperty(access = WRITE_ONLY)
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		Role role = this.getRole();
		if (role != null) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			if(role.getAuthorities()==null) {
				return authorities;
			}
			for (Authority authority : role.getAuthorities()) {
				authorities.add(new SimpleGrantedAuthority(authority.getName()));
			}
		}
		return authorities;
	}

	public User(Long id, String password) {
		super();
		this.id = id;
		this.password = password;
	}

	public User(String name, String email, String phone, Integer state, Long roleId, String organizationName) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.state = state;
		this.roleId = roleId;
		this.organizationName = organizationName;
	}

}
