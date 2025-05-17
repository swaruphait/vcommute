package com.tecsoft.vcommute.serviceimpl.mobile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.AttendanceData;
import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.AttendanceDataRepository;
import com.tecsoft.vcommute.repository.CustomerRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.response.ResponseHandler;
import com.tecsoft.vcommute.service.mobile.AttendanceMobileService;

@Service
public class AttendanceMobileServiceImpl implements AttendanceMobileService{
    @Autowired
	private AttendanceDataRepository attendanceDataRepository;

    @Autowired
	private CustomerRepository customerRepository;

    @Autowired
	private UserRepositiry userRepository;

    @Override
    public ResponseEntity<Object> startAttandance(AttendanceData attendanceData) {
      try {
			Optional<User> findById = userRepository.findById(attendanceData.getUserId());
			if (attendanceData.getStartLat() != null && attendanceData.getStartLat() != 0
					&& attendanceData.getStartLong() != null && attendanceData.getStartLong() != 0
					&& attendanceData.getStartLocation() != null && findById.isPresent()
					&& attendanceData.getStartCustId() != null && attendanceData.getStartCustId() != 0) {
				attendanceData.setStartDate(LocalDate.now());
				attendanceData.setStartTime(LocalTime.now());
				attendanceData.setCompanyId(findById.get().getCompanyId());
				attendanceData.setPurpose("ATTENDANCE");
				attendanceData.setStatus(false);
				if (findById.get().isFaceAttendance()) {
					attendanceData.setGivenBy("F");
				}else{
					attendanceData.setGivenBy("N");

				}
				attendanceData = attendanceDataRepository.save(attendanceData);
				return ResponseHandler.generateResponse("Start Travel Data Saved!", HttpStatus.OK, attendanceData);
			} else {
				return ResponseHandler.generateResponse("Start Travel Data not Saved!",
						HttpStatus.INTERNAL_SERVER_ERROR, null);
			}
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
    }

    @Override
    public ResponseEntity<Object> stopAttandance(AttendanceData attendanceData) {
        try {
			Optional<AttendanceData> findById = attendanceDataRepository.findById(attendanceData.getId());
			if (findById.isEmpty()) {
				return ResponseHandler.generateResponse("Attendance Data not found", HttpStatus.NOT_FOUND, null);
			}
			AttendanceData atendData = findById.get();
			if (attendanceData.getId() != null && attendanceData.getId() != 0 && attendanceData.getEndLat() != 0
					&& !atendData.isStatus()
					&& attendanceData.getEndLat() != null && attendanceData.getEndLong() != 0
					&& attendanceData.getEndLong() != null
					&& attendanceData.getEndLocation() != null
					&& attendanceData.getEndCustId() != null
					&& attendanceData.getEndCustId() != 0) {
				atendData.setEndLat(attendanceData.getEndLat());
				atendData.setEndLong(attendanceData.getEndLong());
				atendData.setEndLocation(attendanceData.getEndLocation());
				atendData.setEndDate(LocalDate.now());
				atendData.setEndTime(LocalTime.now());
				atendData.setOverDesc(attendanceData.getOverDesc());
				atendData.setEndCustId(attendanceData.getEndCustId());
				atendData.setOverDutyIndi(attendanceData.getOverDutyIndi());
				atendData.setEndLocationArea(attendanceData.getEndLocationArea());
				atendData.setStatus(true);
				atendData.setRemarks(attendanceData.getRemarks());
				attendanceData = attendanceDataRepository.save(atendData);
				return ResponseHandler.generateResponse("Stop Attendance Data Saved!", HttpStatus.OK, attendanceData);
			} else {
				return ResponseHandler.generateResponse("Proper Details Not Filled!", HttpStatus.INTERNAL_SERVER_ERROR,
						null);
			}
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
    }

    @Override
    public ResponseEntity<Object> fetchAttandanceData(Long id) {
       	List<AttendanceData> find = attendanceDataRepository.findByUserIdOrderByStartDateDescStartTimeDesc(id);
		if (find.isEmpty()) {
			return ResponseHandler.generateResponse("ATTENDANCE Data not found for this user!",
					HttpStatus.INTERNAL_SERVER_ERROR, find);
		} else {
			for (AttendanceData td : find) {
				if (td.getStartCustId() != null) {
					Optional<Customer> findById = customerRepository.findById(td.getStartCustId());
					td.setStartCustName(findById.get().getName());
				}
				if (td.getEndCustId() != null) {
					Optional<Customer> findById = customerRepository.findById(td.getEndCustId());
					td.setEndCustName(findById.get().getName());
				}
		    Optional<User> byId = userRepository.findById(td.getUserId());
            if (byId.isPresent()) {
				td.setUsername(byId.get().getName());
            }
			}
			return ResponseHandler.generateResponse("Start ATTENDANCE Data found !", HttpStatus.OK, find);
		}
    }
}
