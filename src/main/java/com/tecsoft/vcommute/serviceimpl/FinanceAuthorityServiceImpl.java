package com.tecsoft.vcommute.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.FinanceAuthority;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.FinanceAuthorityRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.service.FinanceAuthorityService;

@Service
public class FinanceAuthorityServiceImpl implements FinanceAuthorityService {

    @Autowired
    private FinanceAuthorityRepository financeAuthorityRepository;

    @Autowired
    private UserRepositiry userRepositiry;

    @Override
    public ResponseEntity<?> fetchFinanceAuthority() {
        List<FinanceAuthority> allByStatus = financeAuthorityRepository.findAllByStatus(true);
        for (FinanceAuthority financeAuthority : allByStatus) {
            financeAuthority.setName(userRepositiry.findById(financeAuthority.getUserId()).get().getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(allByStatus);
    }

    @Override
    public ResponseEntity<?> addFinanceAuthority(FinanceAuthority financeAuthority) {
        financeAuthority.setStatus(true);
        financeAuthorityRepository.save(financeAuthority);
        return ResponseEntity.status(HttpStatus.OK).body("Suucessfully Saved");
    }

    @Override
    public ResponseEntity<?> editFinanceAuthority(Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(financeAuthorityRepository.findById(id));
    }

    @Override
    public ResponseEntity<?> deleteFinanceAuthority(Integer id) {
        Optional<FinanceAuthority> byId = financeAuthorityRepository.findById(id);
        byId.get().setStatus(false);
        financeAuthorityRepository.save(byId.get());
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted");
    }

    @Override
    public ResponseEntity<?> fetchAllFinaceRole() {
        List<User> collect = userRepositiry.findAll().stream()
                .filter(user -> user.isEnabled() && user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("FINANCE")))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

}
