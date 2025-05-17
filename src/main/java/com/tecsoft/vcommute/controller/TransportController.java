package com.tecsoft.vcommute.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.LevelDescriptions;
import com.tecsoft.vcommute.service.TransportService;

@RestController
public class TransportController {

    @Autowired
    private TransportService transportService;

    @PostMapping(value = "/addLevelDes", consumes = "application/json")
    public ResponseEntity<?> addLevelDes(@RequestBody LevelDescriptions levelDescriptions,
            HttpServletRequest request) throws Exception {
        return transportService.addLevelDes(levelDescriptions);
    }

    @GetMapping("/fetchLevelDescriptions")
    public ResponseEntity<?> fetchLevelDescriptions() {
        return transportService.fetchLevelDescriptions();
    }

    @GetMapping(value = "/editLevelDescriptions")
    public ResponseEntity<?> editLevelDescriptions(@RequestParam Long id) {
        return transportService.editLevelDescriptions(id);
    }

    @GetMapping(value = "/deleteLevelDescriptions")
    public ResponseEntity<?> deleteLevelDescriptions(@RequestParam Long id) {
        return transportService.deleteLevelDescriptions(id);
    }

}
