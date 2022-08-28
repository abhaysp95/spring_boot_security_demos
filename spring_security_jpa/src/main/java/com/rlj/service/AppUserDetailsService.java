package com.rlj.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rlj.dao.AppUserDetails;
import com.rlj.entity.User;
import com.rlj.repository.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService
{

	@Autowired
	private UserRepository repository;

	// since a password encoder will be used to encode the raw password before
	// storing, this is the suitable place for this bean (and also avoid
	// circular dependency)
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder(11);
	}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
		Optional<User> user = this.repository.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        return user.map(AppUserDetails::new).get();
    }


}
