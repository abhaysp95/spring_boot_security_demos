package com.rlj.service;

import java.util.Arrays;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService
{

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        // return new User("foo", "bar", Arrays.asList());
        return new User("foo", "$2b$11$KCmo1E.gyOTeD7QJkFv/TO/4lvKvqrbPHy1EW9wJ5ZkLqh09jJDeG", Arrays.asList());
    }

}
