package com.tecsoft.vcommute.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.dto.MultipartImage;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.service.UserService;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/fetchUserDeatils")
    public ResponseEntity<?> fetchUserDeatils() {
        return userService.fetchUserDeatils();

    }

    @GetMapping("/fetchUserListByAuthority")
    public ResponseEntity<?> fetchUserListByAuthority() {
        return userService.fetchUserListByAuthority();

    }

    @GetMapping("/fetchUserDeatilById")
    public ResponseEntity<?> fetchUserDeatilById(@RequestParam Long id) {
        return userService.fetchUserDeatilById(id);

    }

    @PostMapping(value = "/userRegistration", consumes = "application/json")
    public ResponseEntity<?> userRegistration(@RequestBody User user, HttpServletRequest request) throws Exception {
        return userService.userRegistration(user);
    }

    @PutMapping(value = "/userUpdate", consumes = "application/json")
    public ResponseEntity<?> userUpdate(@RequestBody User user, HttpServletRequest request) throws Exception {
        return userService.userUpdate(user);
    }

    @GetMapping(value = "/userActivation")
    public ResponseEntity<?> userActivation(@RequestParam Long id) {
        return userService.userActivation(id);
    }

    @GetMapping(value = "/userDeactivation")
    public ResponseEntity<?> userDeactivation(@RequestParam Long id) {
        return userService.userDeactivation(id);
    }

    @GetMapping(value = "/forgopass")
    public ResponseEntity<?> forgetpass(@RequestParam String username, @RequestParam String email,
            @RequestParam String mobile)
            throws Exception {
        return userService.forgetpass(username, email, mobile);
    }

    @PutMapping(value = "/resetPass")
    public ResponseEntity<?> resetpass(@RequestParam("password") String password,
            @RequestParam("newPassword") String newPassword, HttpServletRequest request) throws Exception {
        return userService.resetpass(password, newPassword);
    }

    @GetMapping("/resetDeviceId")
    public ResponseEntity<?> resetDeviceId(@RequestParam Long id) throws Exception {
        return userService.resetDeviceId(id);
    }

    @GetMapping("/resetDeviceIdAll")
    public ResponseEntity<?> resetDeviceIdAll() throws Exception {
        return userService.resetDeviceIdAll();
    }

    @GetMapping(value = "/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam Long id) throws Exception {
        return userService.resetPassword(id);
    }

    @GetMapping(value = "/fetchWitoutUser")
    public ResponseEntity<?> fetchWitoutUser() {
        return userService.fetchWitoutUser();

    }

    @PostMapping("/addImages")
    public ResponseEntity<?> addImages(@ModelAttribute MultipartImage file) throws IOException {
        return userService.addImages(file);
    }

    @GetMapping("/resetImage")
    public ResponseEntity<?> resetImage(@RequestParam Long id){
        return userService.resetImage(id);
    }

    @GetMapping("/resetEmbadding")
    public ResponseEntity<?> resetEmbadding(@RequestParam Long id){
        return userService.resetEmbadding(id);
    }

    @GetMapping("/faceApproved")
    public ResponseEntity<?> faceApproved(@RequestParam Long id){
        return userService.faceApproved(id);
    }

    @GetMapping("/faceDisApproved")
    public ResponseEntity<?> faceDisApproved(@RequestParam Long id){
        return userService.faceDisApproved(id);
    }


    @GetMapping("/fetchUserUnderManager")
    public ResponseEntity<?> fetchUserUnderManager() {
        return userService.fetchUserUnderManager();

    }

}
