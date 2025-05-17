package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.SubcriptionPlan;
import com.vareli.tecsoft.vcommute_business.repository.SubcriptionPlanRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.PlanService;

@Service
public class PlanServiceImpl implements PlanService{
   
    @Autowired
    private SubcriptionPlanRepository subcriptionPlanRepository;

    @Override
    public ResponseEntity<?> addPlan(SubcriptionPlan plan) {

        boolean existsByPlanName = subcriptionPlanRepository.existsByPlanName(plan.getPlanName());
        
        if (plan.getId()==null) {
            if (!existsByPlanName) {
                plan.setStatus(true);
                subcriptionPlanRepository.save(plan);
                return ResponseHandler.generateResponse("Successfully saved...", HttpStatus.OK, null);
            } else {
                return ResponseHandler.generateResponse("Alreay Same Plan Name Present...", HttpStatus.BAD_REQUEST, null);
            } 
        }else{
            subcriptionPlanRepository.save(plan);
            return ResponseHandler.generateResponse("Successfully Updated...", HttpStatus.OK, null);
        }
    }

    @Override
    public ResponseEntity<?> findByAllActiveList() {
       List<SubcriptionPlan> collect = subcriptionPlanRepository.findAll().stream().filter(c ->  c.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fatched Data...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findByAll() {
        List<SubcriptionPlan> collect = subcriptionPlanRepository.findAll();
        return ResponseHandler.generateResponse("Successfully Fatched Data...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<SubcriptionPlan> byId = subcriptionPlanRepository.findById(id);
        if (byId.isPresent()) {
           return ResponseHandler.generateResponse("Successfully Fatched Data...", HttpStatus.OK, byId);     
        } else {
            return ResponseHandler.generateResponse("No Valid Data present...", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> activePlan(Integer id) {
        Optional<SubcriptionPlan> byId = subcriptionPlanRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            subcriptionPlanRepository.save(byId.get());
           return ResponseHandler.generateResponse("Successfully Actived The Plan...", HttpStatus.OK, byId);     
        } else {
            return ResponseHandler.generateResponse("Opps.. Plan already active...", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> deactivePlan(Integer id) {
        Optional<SubcriptionPlan> byId = subcriptionPlanRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            subcriptionPlanRepository.save(byId.get());
           return ResponseHandler.generateResponse("Successfully DeActived The Plan...", HttpStatus.OK, byId);     
        } else {
            return ResponseHandler.generateResponse("Opps.. Plan already Deactive...", HttpStatus.BAD_REQUEST, null);
        }
    }
    
}
