package com.vareli.tecsoft.vcommute_business.controller.android;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.dto.CommuteDto;
import com.vareli.tecsoft.vcommute_business.service.android.MobileCommuteService;

@RestController
@RequestMapping("/android")
public class MobileCommuteController {

    @Autowired
    private MobileCommuteService mobileCommuteService;

    @PostMapping("/startCommuteData")
    public ResponseEntity<?> startCommuteData(@RequestBody CommuteDto commuteData) {
        return mobileCommuteService.startCommuteData(commuteData);
    }

    @PostMapping("/stopCommuteData")
    public ResponseEntity<?> stopCommuteData(@RequestBody CommuteDto commuteData) throws IOException {
        return mobileCommuteService.stopCommuteData(commuteData);
    }

    @PostMapping("/intervalTravelDataStart")
    public ResponseEntity<Object> intervalTravelDataStart(@RequestParam Long id,
            @RequestBody CommuteDto commuteData) {
        return mobileCommuteService.intervalTravelDataStart(id, commuteData);
    }

    @PostMapping("/intervalTravelDataStop")
    public ResponseEntity<Object> intervalTravelDataStop(@RequestBody CommuteDto commuteData) {
        return mobileCommuteService.intervalTravelDataStop(commuteData);
    }

    @GetMapping(value = "/fetchCommuteData")
    public ResponseEntity<Object> fetchCommuteData(@RequestParam Long userId) {
        return mobileCommuteService.fetchCommuteData(userId);
    }


}
