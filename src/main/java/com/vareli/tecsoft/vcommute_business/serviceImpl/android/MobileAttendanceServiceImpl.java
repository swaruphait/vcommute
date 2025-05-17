package com.vareli.tecsoft.vcommute_business.serviceImpl.android;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.Attendance;
import com.vareli.tecsoft.vcommute_business.model.Customer;
import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.repository.AttendanceRepository;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.CustomerRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.android.MobileAttendanceService;

@Service
public class MobileAttendanceServiceImpl implements MobileAttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public ResponseEntity<?> startAttandance(Attendance attendance) {
        Optional<User> byId = userRepository.findById(attendance.getUserId());
        if (attendance.getUserId() != null && attendance.getStartLat() != null
                && attendance.getStartLat() != 0
                && attendance.getStartLong() != null && attendance.getStartLong() != 0
                && attendance.getStartLocation() != null) {
                    attendance.setStartDate(LocalDate.now());
                    attendance.setStartTime(LocalTime.now());
            attendance.setCompanyId(byId.get().getCompanyId());
            attendance.setSuperCompanyId(byId.get().getSuperCompanyId());
            attendance.setPurpose("ATTENDANCE");
            attendance.setStatus(false);
            Attendance save = attendanceRepository.save(attendance);
            return ResponseHandler.generateResponse("Start Successfully!", HttpStatus.OK, save);
        } else {
            return ResponseHandler.generateResponse("Opps.. something went wrong..",
                    HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> stopAttandance(Attendance attendance) {
        System.out.println("**********************************************");
        Optional<Attendance> findById = attendanceRepository.findById(attendance.getId());
        if (attendance.getId() != null && attendance.getId() != 0 && attendance.getEndLat() != 0
                && findById.get().isStatus() == false
                && attendance.getCustomerId() != null && attendance.getCustomerId() != 0
                && attendance.getEndLat() != null
                && attendance.getEndLong() != 0 && attendance.getEndLong() != null
                && attendance.getEndLocation() != null) {
                    findById.get().setEndLat(attendance.getEndLat());
                    findById.get().setEndLong(attendance.getEndLong());
                    findById.get().setEndLocation(attendance.getEndLocation());
                    findById.get().setEndLocationArea(attendance.getEndLocationArea());
                    findById.get().setEndDate(LocalDate.now());
                    findById.get().setEndTime(LocalTime.now());
                    findById.get().setCustomerId(attendance.getCustomerId());
                    findById.get().setStatus(true);
            Attendance save = attendanceRepository.save(findById.get());
            return ResponseHandler.generateResponse("Stop Successfully!", HttpStatus.OK, save);
        } else {
            return ResponseHandler.generateResponse("Opps.. something went wrong..",
                    HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> fetchAttandanceData(Long userId) {
        List<Attendance> attendData = attendanceRepository.findAllByUserIdOrderByStartDateDescStartTimeDesc(userId);
        for (Attendance attendance : attendData) {
            if (attendance.getCustomerId() != null) {
                Optional<Customer> byId = customerRepository.findById(attendance.getCustomerId());
                if (byId.isPresent()) {
                    Customer customer = byId.get();
                    Optional<User> byId2 = userRepository.findById(attendance.getUserId());
                    if (byId2.isPresent()) {
                        User user = byId2.get();
                          attendance.setName(user.getName());
                    attendance.setUserName(user.getName());
                    }
                  
                    attendance.setCustomerName(customer.getName());
                    attendance.setCustomerLocation(cityRepository.findById(customer.getLocationId()).get().getCity());
                }
            }
        }
        return ResponseHandler.generateResponse("Fetch Attendance Data...!", HttpStatus.OK, attendData);
    }

    @Override
    public ResponseEntity<?> fetchUnstoppedAttandanceData(Long userId) {
        List<Attendance> attendData = attendanceRepository.findAllByUserIdOrderByStartDateDescStartTimeDesc(userId);
            for (Attendance attendance : attendData) {
                if (attendance.getCustomerId() != null) {
                    Optional<Customer> byId = customerRepository.findById(attendance.getCustomerId());
                if (byId.isPresent()) {
                    Customer customer = byId.get();
                    attendance.setCustomerName(customer.getName());
                    attendance.setCustomerLocation(cityRepository.findById(customer.getLocationId()).get().getCity());
                }
                }
            }
            return ResponseHandler.generateResponse("Start Travel Data found !", HttpStatus.OK, attendData);
        }

}
