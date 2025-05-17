package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping({ "/createVareliUser" })
    public ResponseEntity<?> createVareliUser(@RequestBody User user) {
        boolean existsByRole = userRepository.existsByRole("VARELI");
        if (!existsByRole) {
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(user.getRawPassword()));
            user.setCompanyId(0);
            user.setSuperCompanyId(0);
            userRepository.save(user);
            return ResponseHandler.generateResponse("Vareli Account Already..", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Opps.. Already Presen..", HttpStatus.BAD_REQUEST, null);
        }
    }
}
