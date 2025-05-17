package com.vareli.tecsoft.vcommute_business.serviceImpl.android;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.Leave;
import com.vareli.tecsoft.vcommute_business.model.LeaveData;
import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.model.dto.LeaveBalanceDto;
import com.vareli.tecsoft.vcommute_business.model.dto.LeaveDto;
import com.vareli.tecsoft.vcommute_business.repository.LeaveDataRepository;
import com.vareli.tecsoft.vcommute_business.repository.LeaveRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.android.MobileLeaveService;

@Service
public class MobileLeaveServiceImpl implements MobileLeaveService {

    @Autowired
    private LeaveDataRepository leaveDataRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> leaveApply(LeaveData leaveData) {
        Optional<User> byId = userRepository.findById(leaveData.getUserId());
        if (byId.isPresent()) {
            User user = byId.get();
            int count = 0;
            LocalDate start = leaveData.getLeaveStartDate();
            LocalDate end = leaveData.getLeaveEndDate();
            while (!start.isAfter(end)) {
                // DayOfWeek day = start.getDayOfWeek();
                // if (day != DayOfWeek.SUNDAY) {
                //     count++;
                // }
                count++;
                start = start.plusDays(1);
            }
            leaveData.setStatus("O");
            leaveData.setSuperCompanyId(user.getSuperCompanyId());
            leaveData.setCompanyId(user.getCompanyId());
            leaveData.setNoOfDaysLeave(count);
            LeaveData save = leaveDataRepository.save(leaveData);
            return ResponseHandler.generateResponse("Fetching all City!", HttpStatus.OK, save);
        } else {
            return ResponseHandler.generateResponse("City!", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> fetchLeaveData(Long userId) {
        List<LeaveData> details = leaveDataRepository.findAllByUserIdOrderByLeaveStartDateDesc(userId);
        return ResponseHandler.generateResponse("Fetching all City!", HttpStatus.OK, details);
    }

    @Override
    public ResponseEntity<?> fetchLeaveType(Long userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            User user = byId.get();
            List<Leave> bySuperCompanyIdAndCompanyId = leaveRepository
                    .findBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), user.getCompanyId()).stream().filter(t -> t.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Fetching all City!", HttpStatus.OK, bySuperCompanyIdAndCompanyId);
            
        } else {
        return ResponseHandler.generateResponse("No valid user present", HttpStatus.BAD_REQUEST, null);

        }
    }

    @Override
    public ResponseEntity<?> fetchPendingLeave(Long userId) {
        List<LeaveData> details = leaveDataRepository.findAllByUserIdOrderByLeaveStartDateDesc(userId).stream().filter(t -> t.getStatus().equals("O"))
        .collect(Collectors.toList());
        return ResponseHandler.generateResponse("Fetching all Details!", HttpStatus.OK, details);
    }

    @Override
    public ResponseEntity<?> fetchLeaveHistory(Long userId) {
       List<LeaveData> details = leaveDataRepository.findAllByUserIdOrderByLeaveStartDateDesc(userId).stream().filter(t -> !t.getStatus().equals("O"))
        .collect(Collectors.toList());
        return ResponseHandler.generateResponse("Fetching all Details!", HttpStatus.OK, details);
    }

    @Override
    public ResponseEntity<?> fetchLeaveBalance(Long userId) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (!optionalUser.isPresent()) {
        return ResponseHandler.generateResponse("Oops! No valid user found.", HttpStatus.BAD_REQUEST, null);
    }

    User user = optionalUser.get();
    LocalDate today = LocalDate.now();

    // Calculate financial year start and end
    LocalDate financialYearStart = today.getMonthValue() >= 4
            ? LocalDate.of(today.getYear(), 4, 1)
            : LocalDate.of(today.getYear() - 1, 4, 1);

    LocalDate financialYearEnd = today.getMonthValue() >= 4
            ? LocalDate.of(today.getYear() + 1, 3, 31)
            : LocalDate.of(today.getYear(), 3, 31);

    // Fetch active leaves
    List<Leave> activeLeaves = leaveRepository
            .findBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), user.getCompanyId())
            .stream()
            .filter(Leave::isStatus)
            .collect(Collectors.toList());

    Integer totalEarnedLeave = leaveRepository.totalNoOfLeave(user.getSuperCompanyId(), user.getCompanyId());
    Integer totalTakenLeave = leaveDataRepository.noOfTotalLeaveTaken(userId, financialYearStart, financialYearEnd);
    Integer remainingLeave = totalEarnedLeave - totalTakenLeave;

    LeaveDto leaveSummary = new LeaveDto();
    leaveSummary.setTotalLevaeBalance(remainingLeave);
    leaveSummary.setTotalLevaeEarn(totalEarnedLeave);
    leaveSummary.setTotalLevaeUsed(totalTakenLeave);

    List<LeaveBalanceDto> leaveBalanceList = new ArrayList<>();
    for (Leave leave : activeLeaves) {
        LeaveBalanceDto balanceDto = new LeaveBalanceDto();
        Integer leaveTaken = leaveDataRepository.countLeaveBalance(userId, leave.getId(), financialYearStart, financialYearEnd);
        balanceDto.setLeaveType(leave.getTypeName());
        balanceDto.setNoOfLeave(leave.getNoOfLeave());
        balanceDto.setLeaveTaken(leaveTaken);
        leaveBalanceList.add(balanceDto);
    }

    leaveSummary.setLeaveBalanceDto(leaveBalanceList); // Optional: assuming LeaveDto has this field

    return ResponseHandler.generateResponse("Leave balance details fetched successfully.", HttpStatus.OK, leaveSummary);
}


}
