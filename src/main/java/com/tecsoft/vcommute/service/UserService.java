package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.dto.MultipartImage;
import com.tecsoft.vcommute.model.User;

public interface UserService {

    ResponseEntity<?> fetchUserDeatils();

    ResponseEntity<?> fetchUserDeatilById(Long id);

    ResponseEntity<?> userRegistration(User user);

    ResponseEntity<?> userUpdate(User user);

    ResponseEntity<?> userActivation(Long id);

    ResponseEntity<?> userDeactivation(Long id);

    ResponseEntity<?> forgetpass(String username, String email, String mobile);

    ResponseEntity<?> resetpass(String password, String newPassword);

    ResponseEntity<?> resetDeviceId(Long id);

    ResponseEntity<?> resetDeviceIdAll();

    ResponseEntity<?> resetPassword(Long id);

    ResponseEntity<?> fetchWitoutUser();

    ResponseEntity<?> fetchUserListByAuthority();

    ResponseEntity<?> addImages(MultipartImage file);

    ResponseEntity<?> resetImage(Long id);

    ResponseEntity<?> resetEmbadding(Long id);

    ResponseEntity<?> faceApproved(Long id);

    ResponseEntity<?> fetchUserUnderManager();

    ResponseEntity<?> faceDisApproved(Long id);

}
