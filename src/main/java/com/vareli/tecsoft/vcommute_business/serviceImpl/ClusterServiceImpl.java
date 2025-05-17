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

import com.vareli.tecsoft.vcommute_business.model.Cluster;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.ClusterRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.ClusterService;

@Service
public class ClusterServiceImpl implements ClusterService{

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private SubCompanyRepository subCompanyRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Override
    public ResponseEntity<?> createCluster(Cluster cluster) {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (cluster.getId() == null) {
            if (cluster.getCompanyId() == null) {
                cluster.setCompanyId(user.getCompanyId());
            }
            cluster.setSuperCompanyId(user.getSuperCompanyId());
            boolean exists = clusterRepository
                    .existsByClusterNameAndSuperCompanyIdAndCompanyId(cluster.getClusterName(),
                    cluster.getSuperCompanyId(), cluster.getCompanyId());
            if (!exists) {
                cluster.setStatus(true);
                clusterRepository.save(cluster);
                return ResponseEntity.status(HttpStatus.OK).body("Cluster Successfully Saved....");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Cluster Saved....");
            }
        } else {
            clusterRepository.save(cluster);
            return ResponseEntity.status(HttpStatus.OK).body("Cluster Successfully Updated....");
        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Cluster> collect = clusterRepository.findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(),
                companyId);
                for (Cluster cluster : collect) {
                    if (cluster.getCompanyId()==0) {
                        cluster.setCompanyName(superCompanyRepository.findById(cluster.getSuperCompanyId()).get().getName());
                    } else {
                        cluster.setCompanyName(subCompanyRepository.findById(cluster.getCompanyId()).get().getName());
                    }
                }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findByAllActive() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();

        List<Cluster> collect = clusterRepository
                .findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), user.getCompanyId()).stream()
                .filter(c -> c.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
         Optional<Cluster> byId = clusterRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong", HttpStatus.OK, null);
        }
    }

    @Override
    public ResponseEntity<?> activeCluster(Integer id) {
        Optional<Cluster> byId = clusterRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            clusterRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("Activated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Grade Already Active....");
        }
    }

    @Override
    public ResponseEntity<?> deactiveCluster(Integer id) {
        Optional<Cluster> byId = clusterRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            clusterRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("DeActivated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cluster Already DeActive....");
        }
    }
    
}
