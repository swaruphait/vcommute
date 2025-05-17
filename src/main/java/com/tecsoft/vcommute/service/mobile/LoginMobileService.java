package com.tecsoft.vcommute.service.mobile;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.dto.FaceMultipartDto;
import com.tecsoft.vcommute.dto.MultipartImage;

public interface LoginMobileService {
  ResponseEntity<?> loginprocess(String user_id, String user_pw, String deviceId);

    ResponseEntity<?> resetDeviceId(Long userId);

    ResponseEntity<?> resetDeviceIdAll();

    ResponseEntity<?> forgopass(String username, String email, String mobile);

    ResponseEntity<?> changepass(Long id, String password, String newPassword);

    ResponseEntity<?> addImages(MultipartImage file);

    ResponseEntity<?> addEmbadding(Long id, String embeddingJson);

    ResponseEntity<?> fetchEmbaddingById(Long userId);

    ResponseEntity<?> resetEmbadding(Long userId);

    ResponseEntity<?> addFaceEmbadding(FaceMultipartDto file);  
}
