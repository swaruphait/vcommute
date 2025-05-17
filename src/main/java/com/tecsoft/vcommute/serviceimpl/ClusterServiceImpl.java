package com.tecsoft.vcommute.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.dto.MultiApproveDto;
import com.tecsoft.vcommute.model.ApprovedData;
import com.tecsoft.vcommute.model.City;
import com.tecsoft.vcommute.model.Cluster;
import com.tecsoft.vcommute.model.CommuteBalance;
import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.model.TravelDataHeader;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.ApprovedDataRepository;
import com.tecsoft.vcommute.repository.CityRepository;
import com.tecsoft.vcommute.repository.ClusterRepository;
import com.tecsoft.vcommute.repository.CommuteBalanceRepository;
import com.tecsoft.vcommute.repository.CustomerRepository;
import com.tecsoft.vcommute.repository.TravelDataHeaderRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.service.ClusterService;

@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private UserRepositiry userRepositiry;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TravelDataHeaderRepository travelDataHeaderRepository;

    @Autowired
    private ApprovedDataRepository approvedDataRepository;

    @Autowired
    private CommuteBalanceRepository commuteBalanceRepository;

    @Override
    public ResponseEntity<?> addCluster(Cluster cluster) {
        clusterRepository.save(cluster);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Save");
    }

    @Override
    public ResponseEntity<?> editCluster(Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(clusterRepository.findById(id));
    }

    @Override
    public ResponseEntity<?> fetchCluster() {
        return ResponseEntity.status(HttpStatus.OK).body(clusterRepository.findAll());
    }

    @Override
    public ResponseEntity<?> deleteCluster(Integer id) {
        clusterRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
    }

    @Override
    public ResponseEntity<?> travelDataCluster(Integer id) {

        // User byUsername = userRepositiry.findByUsername(currentPrincipalName);

        Optional<Cluster> findById = clusterRepository.findById(id);
        System.out.println("Entry:");
        if (findById.isPresent()) {
            LocalDate dt = LocalDate.now();
            Integer startDate;
            Integer endDate;
            try {
                startDate = findById.get().getStrDate();

            } catch (Exception e) {
                startDate = findById.get().getStrDate() - 1;
            }

            try {
                endDate = findById.get().getEndDate();

            } catch (Exception e) {
                if (dt.getMonthValue() == 2) {
                    if (dt.isLeapYear()) {
                        endDate = 29;
                    } else {
                        endDate = 28;
                    }

                } else {
                    endDate = findById.get().getEndDate() - 1;
                }

            }
            List<TravelDataHeader> travelDataApproveList = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User ur = userRepositiry.findByUsername(currentPrincipalName);

            if (ur.getFinanceAccessArea().equals("A")) {
                System.out.println("FETCH ALL TYPE DATA");
                travelDataApproveList = travelDataHeaderRepository.fetchAllFinanceApprovalData(startDate,
                        endDate);
            } else if (ur.getFinanceAccessArea().equals("S")) {
                System.out.println("FETCH ONLY SPECIFIC TYPE DATA");
                travelDataApproveList = travelDataHeaderRepository.fetchFinanceApprovalData(ur.getId(), startDate,
                        endDate);
            }

            if (travelDataApproveList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(travelDataApproveList);
            } else {
                for (TravelDataHeader data : travelDataApproveList) {
                    User user = userRepositiry.getById(data.getUserId());
                    Customer cust = customerRepository.getById(data.getCustId());
                    City byId = cityRepository.getById(cust.getLocationId());
                    data.setUsername(user.getName());
                    data.setEmployeeId(user.getEmpCode());
                    data.setCustomerName(cust.getName());
                    data.setCustomerLocation(byId.getCity());

                }
                return ResponseEntity.status(HttpStatus.OK).body(travelDataApproveList);
            }

        }
        return null;
    }

    @Override
    public ResponseEntity<?> approveClusterData(Long id, Integer clusetrId, Double price) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        TravelDataHeader travelData = travelDataHeaderRepository.getById(id);
        travelData.setTotalActualPrice(price);
        travelData.setApprovalLevel("A");
        travelDataHeaderRepository.save(travelData);
        Optional<ApprovedData> byId = approvedDataRepository.findByTdId(id);
        if (byId.isPresent()) {
            byId.get().setApprvOrder(travelData.getApprovalLevel());
            byId.get().setApprvBy(currentPrincipalName);
            byId.get().setDate(LocalDate.now());
            byId.get().setTime(LocalTime.now());
            byId.get().setRmks("Final Approved");
            approvedDataRepository.save(byId.get());
        } else {
            ApprovedData ad = new ApprovedData();
            ad.setApprvBy(currentPrincipalName);
            ad.setApprvOrder(travelData.getApprovalLevel());
            ad.setTdId(travelData.getId());
            ad.setDate(LocalDate.now());
            ad.setTime(LocalTime.now());
            ad.setRmks("Final Approved");
            approvedDataRepository.save(ad);
        }

        // for balance sheet code here
        LocalDateTime.now();
        Integer trvMonth = travelData.getStartDate().getMonthValue();
        Integer trvYear = travelData.getStartDate().getYear();
        List<Cluster> cls = clusterRepository.findAll();
        Integer clstId = null;
        for (Cluster cl : cls) {
            if (travelData.getStartDate().getDayOfMonth() >= cl.getStrDate()
                    && travelData.getStartDate().getDayOfMonth() <= cl.getEndDate()) {
                clstId = cl.getId();
            }
        }
        String batchNo = clstId + "" + trvMonth + "" + trvYear;
        if (commuteBalanceRepository.existsByUserIdAndBatchNo(travelData.getUserId(), batchNo)) {
            Optional<CommuteBalance> balance = commuteBalanceRepository.findByUserIdAndBatchNo(travelData.getUserId(),
                    batchNo);
            if (balance.get().getPayableAmount() >= 0) {
                balance.get().setPayableAmount(balance.get().getPayableAmount() + price);
            } else {
                balance.get().setPayableAmount(price);
            }

            balance.get().setApprovedDt(LocalDate.now());
            balance.get().setPayStatus("PAYABLE");
            commuteBalanceRepository.save(balance.get());
        } else {
            CommuteBalance commuteBalance = new CommuteBalance();
            commuteBalance.setBatchNo(batchNo);
            commuteBalance.setPayableAmount(price);
            commuteBalance.setAdvanceBalance(0.0);
            commuteBalance.setPayStatus("PAYABLE");
            commuteBalance.setApprovedDt(LocalDate.now());
            commuteBalance.setUserId(travelData.getUserId());
            commuteBalance.setClusetrId(clstId);
            commuteBalanceRepository.save(commuteBalance);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Approved");
    }

    @Override
    public ResponseEntity<?> approveClusterDataList(List<MultiApproveDto> data) {
        for (MultiApproveDto multiApproveDto : data) {
            System.out.println(data);
            try {
                approveClusterData(multiApproveDto.id, null, multiApproveDto.getTotalActualPrice());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

}
