package com.tecsoft.vcommute.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tecsoft.vcommute.model.MyUserDetails;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.UserRepositiry;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepositiry userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user);
    }
}
