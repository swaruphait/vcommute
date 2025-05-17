package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.CommuteCliamAmount;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.AdvanceAmountRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.ClaimSettlementService;

@Service
public class ClaimSettlementServiceImpl implements ClaimSettlementService {

    @Autowired
    private AdvanceAmountRepository advanceAmountRepository;

    @Override
    public ResponseEntity<?> addAdvance(CommuteCliamAmount commuteCliamAmount) {
        if (commuteCliamAmount.getId() == null) {
            commuteCliamAmount.setStatus("C");
            advanceAmountRepository.save(commuteCliamAmount);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Created...");
        } else {
            advanceAmountRepository.save(commuteCliamAmount);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Updated...");
        }
    }

    @Override
    public ResponseEntity<?> fetchAdavnce() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<CommuteCliamAmount> fetchAdvAmount = advanceAmountRepository.fetchAdvAmount(user.getSuperCompanyId(),
                user.getCompanyId());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchAdvAmount);
    }

    @Override
    public ResponseEntity<?> fetchAdavnceById(Long id) {
        Optional<CommuteCliamAmount> byId = advanceAmountRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong...");
        }
    }

}
