package com.tecsoft.vcommute.serviceimpl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.AdvanceBalance;
import com.tecsoft.vcommute.model.Cluster;
import com.tecsoft.vcommute.model.CommuteBalance;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.AdvanceBalanceRepository;
import com.tecsoft.vcommute.repository.ClusterRepository;
import com.tecsoft.vcommute.repository.CommuteBalanceRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.service.CommuteBalanceService;

@Service
public class CommuteBalanceServiceImpl implements CommuteBalanceService {

    @Autowired
    private CommuteBalanceRepository commuteBalanceRepository;

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private UserRepositiry userRepositiry;

    @Autowired
    private AdvanceBalanceRepository advanceBalanceRepository;

    @Override
    public ResponseEntity<?> addAdavnceCommute(CommuteBalance commuteBalance) {
        List<Cluster> cls = clusterRepository.findAll();
        Integer clstId = null;
        for (Cluster cl : cls) {
            if (commuteBalance.getAdvanceDate().getDayOfMonth() >= cl.getStrDate()
                    && commuteBalance.getAdvanceDate().getDayOfMonth() <= cl.getEndDate()) {
                clstId = cl.getId();
            }
        }
        Integer advMonth = commuteBalance.getAdvanceDate().getMonthValue();
        
        String batchId = clstId + "" + advMonth + "" + commuteBalance.getAdvanceDate().getYear();
        Integer isPresentSameWithBatch = commuteBalanceRepository.isPresentSameWithBatch(commuteBalance.getUserId(),batchId);
       
        AdvanceBalance ad = new AdvanceBalance();
        ad.setAdvanceBalance(commuteBalance.getAdvanceBalance());
        ad.setAdvanceDate(commuteBalance.getAdvanceDate());
        ad.setPayStatus("ADVANCE");
        ad.setUserId(commuteBalance.getUserId());
        ad.setBatchNo(batchId);
        ad.setPurpose(commuteBalance.getPurpose());
        advanceBalanceRepository.save(ad);
            if (isPresentSameWithBatch!=0) {
                Optional<CommuteBalance> balance = commuteBalanceRepository
                        .fetchPresentBatch(commuteBalance.getUserId(), batchId);
                        if (balance.get().getAdvanceDate()==null) {
                            balance.get().setBalance(null);
                        }
                balance.get().setAdvanceBalance(balance.get().getAdvanceBalance() + commuteBalance.getAdvanceBalance());
                commuteBalanceRepository.save(balance.get());
                return ResponseEntity.status(HttpStatus.OK).body("New Balance Updated Exising balance");
            }
            commuteBalance.setBatchNo(batchId);
            commuteBalance.setClusetrId(clstId);
            commuteBalance.setPayableAmount(0.0);
            commuteBalance.setPayStatus("ADVANCE");
            commuteBalanceRepository.save(commuteBalance);
            return ResponseEntity.status(HttpStatus.OK).body("New Balance Added Successfully");
       
      }

    @Override
    public ResponseEntity<?> fetchAdavnceById(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(advanceBalanceRepository.findById(id.intValue()));
    }

    @Override
    public ResponseEntity<?> editAdavnceCommute(AdvanceBalance advanceBalance) {
        Optional<AdvanceBalance> byId = advanceBalanceRepository.findById(advanceBalance.getId());
        Double advBal = byId.get().getAdvanceBalance();
        List<Cluster> cls = clusterRepository.findAll();
        Integer clstId = null;
        for (Cluster cl : cls) {
            if (advanceBalance.getAdvanceDate().getDayOfMonth() >= cl.getStrDate()
                    && advanceBalance.getAdvanceDate().getDayOfMonth() <= cl.getEndDate()) {
                clstId = cl.getId();
            }
        }
        Integer advMonth = advanceBalance.getAdvanceDate().getMonthValue();
        
        String batchId = clstId + "" + advMonth + "" + advanceBalance.getAdvanceDate().getYear();
       advanceBalanceRepository.save(advanceBalance);
        Optional<CommuteBalance> balance = commuteBalanceRepository
        .fetchPresentBatch(advanceBalance.getUserId(), batchId);
        double calAmt = advBal - advanceBalance.getAdvanceBalance();
        balance.get().setAdvanceBalance(advBal - calAmt);
        balance.get().setBatchNo(batchId);
        balance.get().setAdvanceDate(advanceBalance.getAdvanceDate());
        commuteBalanceRepository.save(balance.get());
        return ResponseEntity.status(HttpStatus.OK).body("Successfuly Updated");
    }

    @Override
    public ResponseEntity<?> fetchAdavnceCommute(String stdate, String enddate) {
        ZoneId asiaKolkata = ZoneId.of("Asia/Kolkata");
        Instant nowUtc1 = Instant.parse(stdate);
        Instant nowUtc2 = Instant.parse(enddate);
        ZonedDateTime zone1 = ZonedDateTime.ofInstant(nowUtc1, asiaKolkata);
        ZonedDateTime zone2 = ZonedDateTime.ofInstant(nowUtc2, asiaKolkata);
        LocalDate stdt = zone1.toLocalDate();
        LocalDate eddt = zone2.toLocalDate();
        List<AdvanceBalance> fetchAdvanceData = advanceBalanceRepository.fetchAdvanceData(stdt,eddt);
        for (AdvanceBalance commuteBalance : fetchAdvanceData) {
            commuteBalance.setName(userRepositiry.findById(commuteBalance.getUserId()).get().getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(fetchAdvanceData);
    }

    @Override
    public ResponseEntity<?> deleteAdavnceCommute(Long id) {
        Optional<CommuteBalance> byId = commuteBalanceRepository.findById(id.intValue());
        if (byId.get().getPayStatus().equals("ADVANCE")) {
            commuteBalanceRepository.deleteById(id.intValue());
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Removed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data already process!");
        }
    }

    @Override
    public ResponseEntity<?> fetchBalanceSheet() {
        List<CommuteBalance> balaceSheet = commuteBalanceRepository.findBalaceSheet();
        if (balaceSheet.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(balaceSheet);
        } else {
            for (CommuteBalance commuteBalance : balaceSheet) {
                User byId = userRepositiry.getById(commuteBalance.getUserId());
                commuteBalance.setName(byId.getName());
                commuteBalance.setEmployeeId(byId.getEmpCode());
                commuteBalance.setBankName(byId.getBankName());
                commuteBalance.setBankAccountNo(byId.getBankACNo());
                commuteBalance.setBankType(byId.getBankType());
                commuteBalance.setIfscCode(byId.getBankIFSCCode());
                commuteBalance.setClusterName(
                        clusterRepository.findById(commuteBalance.getClusetrId()).get().getClusterName());
                commuteBalance.setBalance(commuteBalance.getPayableAmount() - commuteBalance.getAdvanceBalance());
            }
            return ResponseEntity.status(HttpStatus.OK).body(balaceSheet);
        }
    }

    @Override
    public ResponseEntity<?> fetchPaiidSheet(String stdate, String enddate) {
        List<CommuteBalance> byPaisReportDateWise = commuteBalanceRepository.findByPaisReportDateWise();
        if (byPaisReportDateWise.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(byPaisReportDateWise);
        } else {
            for (CommuteBalance commuteBalance : byPaisReportDateWise) {
                User byId = userRepositiry.getById(commuteBalance.getUserId());
                commuteBalance.setName(byId.getName());
                commuteBalance.setEmployeeId(byId.getEmpCode());
                commuteBalance.setBankName(byId.getBankName());
                commuteBalance.setBankAccountNo(byId.getBankACNo());
                commuteBalance.setBankType(byId.getBankType());
                commuteBalance.setIfscCode(byId.getBankIFSCCode());
                commuteBalance.setClusterName(
                        clusterRepository.findById(commuteBalance.getClusetrId()).get().getClusterName());
                commuteBalance.setBalance(commuteBalance.getPayableAmount() - commuteBalance.getAdvanceBalance());
            }
            return ResponseEntity.status(HttpStatus.OK).body(byPaisReportDateWise);
        }
    }

    @Override
    public ResponseEntity<?> markPaidSheet(Integer id) {
        Optional<CommuteBalance> byId = commuteBalanceRepository.findById(id);
        Optional<AdvanceBalance> byBatchNo = advanceBalanceRepository.findByBatchNo(byId.get().getBatchNo());
        if (byId.get().getPayStatus().equals("ADVANCE") || byId.get().getPayStatus().equals("PAYABLE")) {
            byId.get().setPayStatus("PAID");
            if (byId.get().getApprovedDt()==null) {
                byId.get().setApprovedDt(LocalDate.now());
            }
            commuteBalanceRepository.save(byId.get());
            byBatchNo.get().setPayStatus("PAID");
            advanceBalanceRepository.save(byBatchNo.get());
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Marked");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something wrong...");
        }
    }

    @Override
    public ResponseEntity<?> markListOfPaidSheet(List<Integer> ids) {
        for (Integer id : ids) {
            try {
                markPaidSheet(id);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Marked");
    }


    @Override
    public ResponseEntity<?> markPaymentImportedList(List<Integer> ids) {;
        ZoneId kolkataZone = ZoneId.of("Asia/Kolkata");
		LocalDateTime importDate = LocalDateTime.now(kolkataZone);
		List<Integer> ids2 = ids;
		for (Integer long1 : ids2) {
			CommuteBalance balSheet = commuteBalanceRepository.findById(long1).get();
            balSheet.setPayStatus("COMPLETE");
			balSheet.setImportDate(importDate);
			commuteBalanceRepository.save(balSheet);
		}
        return ResponseEntity.status(HttpStatus.OK).body("Successfullly Marked");
    }



}
