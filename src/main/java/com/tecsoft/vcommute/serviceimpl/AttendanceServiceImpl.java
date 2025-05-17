package com.tecsoft.vcommute.serviceimpl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.dto.VAttendanceDto;
import com.tecsoft.vcommute.model.AttendanceData;
import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.model.MyUserDetails;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.AttendanceDataRepository;
import com.tecsoft.vcommute.repository.CustomerRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceDataRepository attendanceDataRepository;

    @Autowired
    private UserRepositiry userRepositiry;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseEntity<?> attandanceDataByDate(String stdate, String enddate) {
        ZoneId asiaKolkata = ZoneId.of("Asia/Kolkata");
        Instant nowUtc1 = Instant.parse(stdate);
        Instant nowUtc2 = Instant.parse(enddate);
        ZonedDateTime zone1 = ZonedDateTime.ofInstant(nowUtc1, asiaKolkata);
        ZonedDateTime zone2 = ZonedDateTime.ofInstant(nowUtc2, asiaKolkata);
        LocalDate stdt = zone1.toLocalDate();
        LocalDate eddt = zone2.toLocalDate();
        List<AttendanceData> attandanceDataDataByDate = attendanceDataRepository.attandanceDataDataByDate(stdt, eddt);
        for (AttendanceData attendanceData : attandanceDataDataByDate) {
            Optional<User> byId = userRepositiry.findById(attendanceData.getUserId());
            attendanceData.setUsername(byId.get().getName());
            attendanceData.setEmpcode(byId.get().getEmpCode());
            attendanceData
                    .setStartCustName(customerRepository.findById(attendanceData.getStartCustId()).get().getName());
            attendanceData.setEndCustName(customerRepository.findById(attendanceData.getEndCustId()).get().getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(attandanceDataDataByDate);
    }

    @Override
    public ResponseEntity<?> attendApprovalData() {
        List<AttendanceData> attendanceDatas = attendanceDataRepository.findByIsOpenStatusAndStatus(true, true);
        if (attendanceDatas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(attendanceDatas);
        } else {
            for (AttendanceData attendanceData : attendanceDatas) {
                attendanceData.setUsername(userRepositiry.findById(attendanceData.getUserId()).get().getName());
                attendanceData
                        .setStartCustName(customerRepository.findById(attendanceData.getStartCustId()).get().getName());
                attendanceData
                        .setEndCustName(customerRepository.findById(attendanceData.getEndCustId()).get().getName());
            }
            return ResponseEntity.status(HttpStatus.OK).body(attendanceDatas);
        }
    }

    @Override
    public ResponseEntity<?> approvAttendData(Long id) {
        Optional<AttendanceData> attendData = attendanceDataRepository.findById(id);
        if (attendData.isPresent()) {
            attendData.get().setOpenStatus(false);
            attendanceDataRepository.save(attendData.get());
            return ResponseEntity.status(HttpStatus.OK).body("Suuccessfully Approved");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went worng!");
        }
    }

    @Override
    public ResponseEntity<?> disapprovAttendData(Long id) {
        Optional<AttendanceData> attendData = attendanceDataRepository.findById(id);
        if (attendData.isPresent()) {
            attendData.get().setOpenStatus(false);
            attendData.get().setStatus(false);
            attendanceDataRepository.save(attendData.get());
            return ResponseEntity.status(HttpStatus.OK).body("Suuccessfully Approved");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went worng!");
        }
    }

    @Override
    public ResponseEntity<?> approvAttendDataList(List<Long> ids) {
        for (Long id : ids) {
            try {

                approvAttendData(id);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @Override
    public ResponseEntity<?> disapprovAttendDataList(List<Long> ids) {
        for (Long id : ids) {
            try {
                disapprovAttendData(id);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @Override
    public ResponseEntity<?> addAttendanceByAuthority(AttendanceData attendanceDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());

     attendanceDto.setStatus(true);
     attendanceDto.setGivenBy(loggedUser.get().getUsername());
     attendanceDataRepository.save(attendanceDto);

       return ResponseEntity.status(HttpStatus.OK).body("Successfully Attendance Entry..");
    }

    @Override
    public ResponseEntity<?> fetchAttendApprovalDataAuthorityWise() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());

        List<AttendanceData> fetchAuthorityWiseAttendanceDatas = attendanceDataRepository.fetchAuthorityWiseAttendanceDatas(loggedUser.get().getId(), loggedUser.get().getId());

          if (fetchAuthorityWiseAttendanceDatas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(fetchAuthorityWiseAttendanceDatas);
        } else {
            for (AttendanceData data : fetchAuthorityWiseAttendanceDatas) {
        Optional<User> u = userRepositiry.findById(data.getUserId());
     
          
                // City byId = cityRepository.getById(cust.getLocationId());
                data.setUsername(u.get().getName());
                data.setEmpcode(u.get().getEmpCode());
                if (data.getStartCustId()!=null) {
                    Optional<Customer> startCustomer = customerRepository.findById(data.getStartCustId());
                    data.setStartCustName(startCustomer.get().getName());


                    }
                if (data.getEndCustId()!=null) {
                    Optional<Customer> endCustomer = customerRepository.findById(data.getEndCustId());
                   data.setEndCustName(endCustomer.get().getName());

                    }
    
            }
            return ResponseEntity.status(HttpStatus.OK).body(fetchAuthorityWiseAttendanceDatas);
        }
    }

    @Override
    public ResponseEntity<?> findAttendById(Long id) {
       Optional<AttendanceData> byId = attendanceDataRepository.findById(id);
       if (byId.isPresent()) {
        return ResponseEntity.status(HttpStatus.OK).body(byId);
       } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
       }
    }

    @Override
    public ResponseEntity<?> editAttendance(AttendanceData attendanceData) {
        attendanceDataRepository.save(attendanceData);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Updated...");
    }

    @Override
    public ResponseEntity<?> takeAttendanceData(String startDate) {
        List<VAttendanceDto> fetchAttendanceApi = attendanceDataRepository.fetchAttendanceApi(startDate);
        return ResponseEntity.status(HttpStatus.OK).body(fetchAttendanceApi);
    }

}
