package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.User;

public interface UserService {

    ResponseEntity<?> creatSuperadmin(User user);

    ResponseEntity<?> fetchAllSuperadmin();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> activeUser(Long id);

    ResponseEntity<?> deactiveUser(Long id);

    Boolean usernameAvailability(String username);

    ResponseEntity<?> creatUser(User user);

    ResponseEntity<?> fetchAllUser(Integer companyId);

    ResponseEntity<?> fetchActiveUserList(Integer companyId);

    ResponseEntity<?> fetchAuthorityUnderUser();

    ResponseEntity<?> resetDeviceId(Long userId);

    ResponseEntity<?> resetDeviceIdAll();

    ResponseEntity<?> resetPassword(Long userId);

    
}
