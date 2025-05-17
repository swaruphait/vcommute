package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    
   @Autowired
   private UserService userService;

    @PostMapping({ "/creatSuperadmin" })
	public ResponseEntity<?> creatSuperadmin(@RequestBody User user) {
		return userService.creatSuperadmin(user);
	}

	@PostMapping({ "/creatUser" })
	public ResponseEntity<?> creatUser(@RequestBody User user) {
		return userService.creatUser(user);
	}

    @GetMapping({ "/fetchAllSuperadmin" })
	public ResponseEntity<?> fetchAllSuperadmin() {
		return userService.fetchAllSuperadmin();
	}

	@GetMapping({ "/fetchAllUser" })
	public ResponseEntity<?> fetchAllUser(@RequestParam(required = false) Integer companyId) {
		return userService.fetchAllUser(companyId);
	}

    @GetMapping({ "/findById" })
	public ResponseEntity<?> findById(@RequestParam Long id) {
		return userService.findById(id);
	}

    @GetMapping({ "/activeUser" })
	public ResponseEntity<?> activeUser(@RequestParam Long id) {
		return userService.activeUser(id);
	}

    @GetMapping({ "/deactiveUser" })
	public ResponseEntity<?> deactiveUser(@RequestParam Long id) {
		return userService.deactiveUser(id);
	}

    @GetMapping({ "/usernameAvailability" })
	public Boolean usernameAvailability(@RequestParam String username) {
		return userService.usernameAvailability(username);
	}

	@GetMapping({ "/fetchActiveUserList" })
	public ResponseEntity<?> fetchActiveUserList(@RequestParam(required = false) Integer companyId) {
		return userService.fetchActiveUserList(companyId);
	}

	@GetMapping({ "/fetchAuthorityUnderUser" })
	public ResponseEntity<?> fetchAuthorityUnderUser() {
		return userService.fetchAuthorityUnderUser();
	}
    
	@GetMapping("/resetDeviceId")
    public ResponseEntity<?> resetDeviceId(@RequestParam Long userId) throws Exception {
        return userService.resetDeviceId(userId);
    }

    @GetMapping("/resetDeviceIdAll")
    public ResponseEntity<?> resetDeviceIdAll() throws Exception {
        return userService.resetDeviceIdAll();
    }

	@GetMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam Long userId) throws Exception {
        return userService.resetPassword(userId);
    }
}
