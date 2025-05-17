package com.vareli.tecsoft.vcommute_business.service.android;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.dto.MultipartImage;

public interface MobileLoginService {

    ResponseEntity<Object> signin(String username, String password, String deviceId);

    ResponseEntity<?> resetDeviceId(Long userId);

    ResponseEntity<?> resetDeviceIdAll(Integer superCompanyId, Integer companyId);

    ResponseEntity<Object> forgopass(String username, String email, String mobile);

    ResponseEntity<Object> changepass(Long username, String password, String newPassword);

    ResponseEntity<?> addFaceEmbadding(Long id, String embeddingJson, MultipartImage file);

    ResponseEntity<?> fetchEmbaddingById(Long userId);

    ResponseEntity<?> resetEmbadding(Long userId);
    
}
