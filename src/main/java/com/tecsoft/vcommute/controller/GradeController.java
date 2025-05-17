package com.tecsoft.vcommute.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.Grade;
import com.tecsoft.vcommute.service.GradeService;

@RestController
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @PostMapping(value = "/addGrade", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> addGrade(@RequestBody Grade grade, HttpServletRequest request) throws Exception {
        return gradeService.addGrade(grade);
    }

    @GetMapping(value = "/fetchGrade")
    public ResponseEntity<?> fetchGrade() {
        return gradeService.fetchGrade();
    }

    @GetMapping(value = "/editGrade")
    public ResponseEntity<?> editGrade(@RequestParam Long id) {
        return gradeService.editGrade(id);
    }

    @GetMapping(value = "/deleteGrade")
    public ResponseEntity<?> deleteGrade(@RequestParam Long id) {
        return gradeService.deleteGrade(id);
    }

}
