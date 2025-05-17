package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.Transport;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.TransportRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.TransportService;

@Service
public class TransportServiceImpl implements TransportService{

    @Autowired
    private TransportRepository transportRepository;
 
    @Autowired
    private SubCompanyRepository subCompanyRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;


    @Override
    public ResponseEntity<?> createTransport(Transport transport) {
           Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (transport.getId() == null) {
            if (transport.getCompanyId() == null) {
                transport.setCompanyId(user.getCompanyId());
            }
            transport.setSuperCompanyId(user.getSuperCompanyId());
            boolean exists = transportRepository
                    .existsByDesAndSuperCompanyIdAndCompanyId(transport.getDes(),
                    transport.getSuperCompanyId(), transport.getCompanyId());
            if (!exists) {
                transport.setStatus(true);
                transportRepository.save(transport);
                return ResponseEntity.status(HttpStatus.OK).body("Transpport Successfully Saved....");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Transpport Saved....");
            }
        } else {
            transportRepository.save(transport);
            return ResponseEntity.status(HttpStatus.OK).body("Transpport Successfully Updated....");
        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Transport> collect = transportRepository.findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(),
                companyId);
                for (Transport transport : collect) {
                    if (transport.getCompanyId()==0) {
                        transport.setCompanyName(superCompanyRepository.findById(transport.getSuperCompanyId()).get().getName());
                    } else {
                        transport.setCompanyName(subCompanyRepository.findById(transport.getCompanyId()).get().getName());
                    }
                }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findByAllActive(Integer companyId) {
          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Transport> collect = transportRepository
                .findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), companyId).stream()
                .filter(c -> c.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<Transport> byId = transportRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong", HttpStatus.OK, null);
        }
    }

    @Override
    public ResponseEntity<?> activeTransport(Integer id) {
        Optional<Transport> byId = transportRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            transportRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("Activated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transpport Already Active....");
        }
    }

    @Override
    public ResponseEntity<?> deactiveTransport(Integer id) {
       Optional<Transport> byId = transportRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            transportRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("DeActivated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transpport Already DeActive....");
        }
    }
    
}
