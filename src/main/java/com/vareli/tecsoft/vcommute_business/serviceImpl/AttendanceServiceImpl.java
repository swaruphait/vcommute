package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.Attendance;
import com.vareli.tecsoft.vcommute_business.model.Customer;
import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.AttendanceRepository;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.CustomerRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public ResponseEntity<?> findByAll(String stdate, String enddate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<Attendance> fetchAttendance = attendanceRepository.fetchAttendance(stdate, enddate,
                user.getSuperCompanyId(), user.getCompanyId());
        for (Attendance attendance : fetchAttendance) {
            Optional<User> byId = userRepository.findById(attendance.getUserId());
            attendance.setName(byId.get().getName());
            attendance.setEmployeeId(byId.get().getEmployeeId());
            if (attendance.getCompanyId() != null) {
                Optional<Customer> byId2 = customerRepository.findById(attendance.getCustomerId());
                attendance.setCustomerName(byId2.get().getName());
                attendance.setCustomerLocation(cityRepository.findById(byId2.get().getLocationId()).get().getCity());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchAttendance);
    }

    @Override
    public ResponseEntity<?> fetchAttendLevelAuthority(String stdate, String enddate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<Attendance> fetchAttendanceAthorityUnder = attendanceRepository.fetchAttendanceAthorityUnder(stdate,
                enddate, user.getUser().getId(), user.getSuperCompanyId(), user.getCompanyId());
        for (Attendance attendance : fetchAttendanceAthorityUnder) {
            Optional<User> byId = userRepository.findById(attendance.getUserId());
            attendance.setName(byId.get().getName());
            attendance.setEmployeeId(byId.get().getEmployeeId());
            if (attendance.getCompanyId() != null) {
                Optional<Customer> byId2 = customerRepository.findById(attendance.getCustomerId());
                attendance.setCustomerName(byId2.get().getName());
                attendance.setCustomerLocation(cityRepository.findById(byId2.get().getLocationId()).get().getCity());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchAttendanceAthorityUnder);
    }

}
