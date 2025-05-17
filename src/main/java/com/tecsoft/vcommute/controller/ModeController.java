package com.tecsoft.vcommute.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.Mode;
import com.tecsoft.vcommute.service.ModeService;

@RestController
public class ModeController {

    @Autowired
    private ModeService modeService;

    @GetMapping("/fetchMode")
    public ResponseEntity<?> fetchMode() {
        return modeService.fetchMode();
    }

    @PostMapping(value = "/addMode", consumes = "application/json")
    public ResponseEntity<?> addMode(@RequestBody Mode mode, HttpServletRequest request) throws Exception {
        return modeService.addMode(mode);
    }

    @GetMapping(value = "/editMode")
    public ResponseEntity<?> editMode(@RequestParam Integer id) {
        return modeService.editMode(id);
    }

    @GetMapping(value = "/deleteMode")
    public ResponseEntity<?> deleteMode(@RequestParam Integer id) {
        return modeService.deleteMode(id);
    }

}
