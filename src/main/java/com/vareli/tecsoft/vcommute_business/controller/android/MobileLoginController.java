package com.vareli.tecsoft.vcommute_business.controller.android;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.dto.MultipartImage;
import com.vareli.tecsoft.vcommute_business.service.android.MobileLoginService;

@RestController
@RequestMapping("/android")
public class MobileLoginController {

    @Autowired
    private MobileLoginService loginService;

    @GetMapping("/signin")
    public ResponseEntity<?> signin(@RequestParam String username,
            @RequestParam String password, @RequestParam String deviceId) throws Exception {
        return loginService.signin(username, password, deviceId);
    }

    @GetMapping("/resetDeviceId")
    public ResponseEntity<?> resetDeviceId(@RequestParam Long userId) throws Exception {
        return loginService.resetDeviceId(userId);
    }

    @GetMapping("/resetDeviceIdAll")
    public ResponseEntity<?> resetDeviceIdAll(Integer superCompanyId, Integer companyId) throws Exception {
        return loginService.resetDeviceIdAll(superCompanyId, companyId);
    }

    @GetMapping(value = "/forgotpass")
    public ResponseEntity<?> forgopass(@RequestParam String username, @RequestParam String email,
            @RequestParam String mobile) throws Exception {
        return loginService.forgopass(username, email, mobile);
    }

    @PutMapping(value = "/changepass")
    public ResponseEntity<?> changepass(@RequestParam Long userId, @RequestParam String password,
            @RequestParam String newPassword) throws Exception {
        return loginService.changepass(userId, password, newPassword);
    }

    @PutMapping(value = "/addFaceEmbadding")
    public ResponseEntity<?> addFaceEmbadding(@RequestParam Long id, @RequestBody String embeddingJson,
            @ModelAttribute MultipartImage file) throws Exception {
        return loginService.addFaceEmbadding(id, embeddingJson, file);
    }

    @GetMapping(value = "/fetchEmbaddingById")
	public ResponseEntity<?> fetchEmbaddingById(@RequestParam Long userId) throws Exception {
		return loginService.fetchEmbaddingById(userId);
	}

	@GetMapping("/resetEmbadding")
	public ResponseEntity<?> resetEmbadding(@RequestParam Long userId) throws Exception {
		return loginService.resetEmbadding(userId);
	}
}
