package com.tecsoft.vcommute.controller.mobile;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.dto.FaceMultipartDto;
import com.tecsoft.vcommute.dto.MultipartImage;
import com.tecsoft.vcommute.service.mobile.LoginMobileService;


@RestController
@RequestMapping("/mobile")
public class MobileLoginController {

    @Autowired
	private LoginMobileService loginService;

    @GetMapping("/signin")
	public ResponseEntity<?> userLogin(@RequestParam String username,
	@RequestParam String password, @RequestParam String deviceId) throws Exception {
		return loginService.loginprocess(username, password, deviceId);
	}

	@GetMapping("/resetDeviceId")
	public ResponseEntity<?> resetDeviceId(@RequestParam Long userId) throws Exception {
		return loginService.resetDeviceId(userId);
	}

	@GetMapping("/resetEmbadding")
	public ResponseEntity<?> resetEmbadding(@RequestParam Long userId) throws Exception {
		return loginService.resetEmbadding(userId);
	}

	@GetMapping("/resetDeviceIdAll")
	public ResponseEntity<?> resetDeviceIdAll() throws Exception {
		return loginService.resetDeviceIdAll();
	}

	@GetMapping(value = "/forgotpass")
	public ResponseEntity<?> forgopass(@RequestParam String username, @RequestParam String email,
			@RequestParam String mobile) throws Exception {
		return loginService.forgopass(username, email, mobile);
	}

	@PutMapping(value = "/addEmbadding")
	public ResponseEntity<?> addEmbadding(@RequestParam Long id, @RequestBody String embeddingJson) throws Exception {
		return loginService.addEmbadding(id, embeddingJson);
	}


	@PostMapping(value = "/changepass")
	public ResponseEntity<?> changepass(@RequestParam Long id, @RequestParam String password,
			@RequestParam String newPassword) throws Exception {
		return loginService.changepass(id, password, newPassword);
	}

	@PostMapping("/addImages")
    public ResponseEntity<?> addImages(@ModelAttribute MultipartImage file) throws IOException {
        return loginService.addImages(file);
    }

	@GetMapping(value = "/fetchEmbaddingById")
	public ResponseEntity<?> fetchEmbaddingById(@RequestParam Long userId) throws Exception {
		return loginService.fetchEmbaddingById(userId);
	}

	@PutMapping(value = "/addFaceEmbadding")
	public ResponseEntity<?> addFaceEmbadding(@ModelAttribute FaceMultipartDto file) throws Exception {
		return loginService.addFaceEmbadding(file);
	}


    
}
