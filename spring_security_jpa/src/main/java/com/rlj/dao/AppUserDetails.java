package com.rlj.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rlj.entity.User;

// should this class be considered a DAO class ?
public class AppUserDetails implements UserDetails
{

	private String username;
	private String password;
	private boolean active;
	private List<GrantedAuthority> authorities;

	public AppUserDetails(User user)
	{
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.active = user.isEnabled();
		this.authorities = Arrays.stream(user.getRoles().split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return this.authorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return this.active;
    }

}
