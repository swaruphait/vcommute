package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.SubscriptionData;
import com.vareli.tecsoft.vcommute_business.model.SuperCompany;
import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.SubscriptionDataRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionDataRepository subscriptionDataRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findByUsername = userRepository.findByUsername(username);
        if (findByUsername.isEmpty()) {
            throw new UsernameNotFoundException("Could not find user");
        } else {
            Optional<SubscriptionData> subscriptionData = subscriptionDataRepository
                    .findByStatusAndSuperCompanyId(true, findByUsername.get().getSuperCompanyId());

            Optional<SuperCompany> company = superCompanyRepository.findById(findByUsername.get().getSuperCompanyId());
            return new MyUserDetails(findByUsername.get(), subscriptionData, company);
        }
    }
}