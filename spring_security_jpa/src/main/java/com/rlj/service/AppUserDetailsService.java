package com.rlj.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rlj.dao.AppUserDetails;
import com.rlj.entity.User;
import com.rlj.repository.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService
{

	@Autowired
	private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
		Optional<User> user = this.repository.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        return user.map(AppUserDetails::new).get();
    }


}
