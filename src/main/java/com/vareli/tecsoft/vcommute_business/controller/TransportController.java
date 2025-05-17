package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Transport;
import com.vareli.tecsoft.vcommute_business.service.TransportService;

@RestController
@RequestMapping("/transport")
public class TransportController {
    
    @Autowired
    private TransportService transportService;

    @PostMapping(value = "/createTransport")
	public ResponseEntity<?> createTransport(@RequestBody Transport transport) {
		return transportService.createTransport(transport);
	}

     @GetMapping(value = "/findByAll")
	public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId) {
		return transportService.findByAll(companyId);
	}

	@GetMapping(value = "/findByAllActive")
	public ResponseEntity<?> findByAllActive(@RequestParam(required = false) Integer companyId) {
		return transportService.findByAllActive(companyId);
	}

	@GetMapping(value = "/findById")
	public ResponseEntity<?> findById(@RequestParam Integer id) {
		return transportService.findById(id);
	}

    @GetMapping(value = "/activeTransport")
	public ResponseEntity<?> activeTransport(@RequestParam Integer id) {
		return transportService.activeTransport(id);
	}

    @GetMapping(value = "/deactiveTransport")
	public ResponseEntity<?> deactiveTransport(@RequestParam Integer id) {
		return transportService.deactiveTransport(id);
	}
}
