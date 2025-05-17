package com.tecsoft.vcommute.autoservice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tecsoft.vcommute.model.TravelDataDetails;
import com.tecsoft.vcommute.model.TravelDataHeader;
import com.tecsoft.vcommute.repository.AttendanceDataRepository;
import com.tecsoft.vcommute.repository.TravelDataDetailsRepository;
import com.tecsoft.vcommute.repository.TravelDataHeaderRepository;

@Component
public class TravelDataScheduler {
    @Autowired
    TravelDataHeaderRepository travelDataHeaderRepository;

    @Autowired
    TravelDataDetailsRepository travelDataDetailsRepository;

    @Autowired
    AttendanceDataRepository attendanceDataRepository;

    @Scheduled(cron = "30 55  23 * * *")
    public void cronJobSch() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        List<TravelDataDetails> fetchLastUnfinish = travelDataDetailsRepository.fetchLastUnfinish(date);
        for (TravelDataDetails details : fetchLastUnfinish) {
            details.setEndDate(date);
            details.setEndTime(time);
            details.setStatus(true);
            details.getTravelDataHeader().setEndDate(date);
            details.getTravelDataHeader().setEndTime(time);
            System.out.println("get header " + details.getTravelDataHeader().getApprovalLevel());
            Optional<TravelDataHeader> travelDataHeader = travelDataHeaderRepository
                    .findById(details.getTravelDataHeader().getId());
            travelDataHeader.get().setApprovalLevel("NS");
            travelDataHeader.get().setStatus(true);
            travelDataHeader.get().setNote("Did Not Stopped Before Timeout");
            travelDataHeader.get().setEndTime(time);
            travelDataHeader.get().setEndDate(date);
            travelDataDetailsRepository.save(details);
            travelDataHeaderRepository.save(travelDataHeader.get());
        }

    }

}
