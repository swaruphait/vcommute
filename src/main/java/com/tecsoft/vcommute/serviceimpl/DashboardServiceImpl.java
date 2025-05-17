package com.tecsoft.vcommute.serviceimpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.dto.Dashdata;
import com.tecsoft.vcommute.model.MyUserDetails;
import com.tecsoft.vcommute.model.Role;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.AttendanceDataRepository;
import com.tecsoft.vcommute.repository.RoleRepository;
import com.tecsoft.vcommute.repository.TravelDataHeaderRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private TravelDataHeaderRepository travelDataHeaderRepository;

    @Autowired
    private AttendanceDataRepository attendanceDataRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepositiry userRepositiry;

    @Override
    public ResponseEntity<?> adminBarchartdata() {
        LocalDate todaydate = LocalDate.now();
        List<Integer> bar = new ArrayList<Integer>();
        int monthValue = todaydate.getMonthValue();
        LocalDate todaydate1 = todaydate.minusMonths(monthValue);
        for (int i = 1; i <= 12; i++) {
            LocalDate earlier = todaydate1.plusMonths(i);
            LocalDate stdt = earlier.withDayOfMonth(1);
            LocalDate eddt = earlier.withDayOfMonth(earlier.lengthOfMonth());
            Integer presentmnt = travelDataHeaderRepository.monthlyvisit(stdt, eddt);
            bar.add(presentmnt);

        }
        return ResponseEntity.status(HttpStatus.OK).body(bar);
    }

    @Override
    public ResponseEntity<?> adminWeekchartdata() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }

        LocalDate preweek = today.minusWeeks(1);
        // Go backward to get Monday
        LocalDate monday1 = preweek;
        while (monday1.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday1 = monday1.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday1 = preweek;
        while (sunday1.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday1 = sunday1.plusDays(1);
        }
        List<Integer> bar = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            Integer dayTra = travelDataHeaderRepository.monthlyvisit(monday.plusDays(i),
                    monday.plusDays(i));
            bar.add(dayTra);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bar);
    }

    @Override
    public ResponseEntity<?> adminMntAttndchartdata() {
        LocalDate todaydate = LocalDate.now();
        List<Integer> bar = new ArrayList<Integer>();
        int monthValue = todaydate.getMonthValue();
        LocalDate todaydate1 = todaydate.minusMonths(monthValue);
        for (int i = 1; i <= 12; i++) {
            LocalDate earlier = todaydate1.plusMonths(i);
            LocalDate stdt = earlier.withDayOfMonth(1);
            LocalDate eddt = earlier.withDayOfMonth(earlier.lengthOfMonth());
            Integer presentmnt = attendanceDataRepository.attendanceMonthCount(stdt, eddt);
            bar.add(presentmnt);
        }

        return ResponseEntity.status(HttpStatus.OK).body(bar);
    }

    @Override
    public ResponseEntity<?> adminMntUsesApplication() {
        LocalDate todaydate = LocalDate.now();
        List<Integer> bar = new ArrayList<Integer>();
        int monthValue = todaydate.getMonthValue();
        LocalDate todaydate1 = todaydate.minusMonths(monthValue);
        for (int i = 1; i <= 12; i++) {
            LocalDate earlier = todaydate1.plusMonths(i);
            LocalDate stdt = earlier.withDayOfMonth(1);
            LocalDate eddt = earlier.withDayOfMonth(earlier.lengthOfMonth());
            Integer presentmnt = travelDataHeaderRepository.commuteTrafic(stdt, eddt);
            bar.add(presentmnt);
        }

        return ResponseEntity.status(HttpStatus.OK).body(bar);
    }

    @Override
    public ResponseEntity<?> adminUserDistributiondata() {
        Map<String, Integer> hm = new HashMap<String, Integer>();
        List<Role> role = roleRepository.findAll();
        for (Role r : role) {
            Integer count = roleRepository.roleCount(r.getName());
            hm.put(r.getName(), count);
        }
        return ResponseEntity.status(HttpStatus.OK).body(hm);

    }

    @Override
    public ResponseEntity<?> hodBarchartdata() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        LocalDate todaydate = LocalDate.now();
        List<Integer> bar = new ArrayList<Integer>();
        int monthValue = todaydate.getMonthValue();
        LocalDate todaydate1 = todaydate.minusMonths(monthValue);
        for (int i = 1; i <= 12; i++) {
            LocalDate earlier = todaydate1.plusMonths(i);
            LocalDate stdt = earlier.withDayOfMonth(1);
            LocalDate eddt = earlier.withDayOfMonth(earlier.lengthOfMonth());
            Integer presentmnt = travelDataHeaderRepository.monthlyvisitByAuthority(user.get().getId().intValue(),
                    user.get().getId().intValue(), stdt, eddt);
            bar.add(presentmnt);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bar);
    }

    @Override
    public ResponseEntity<?> hodWeekchartdata() {
        LocalDate today = LocalDate.now();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }

        LocalDate preweek = today.minusWeeks(1);
        // Go backward to get Monday
        LocalDate monday1 = preweek;
        while (monday1.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday1 = monday1.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday1 = preweek;
        while (sunday1.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday1 = sunday1.plusDays(1);
        }
        List<Integer> bar = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            Integer dayTra = travelDataHeaderRepository.monthlyvisitByAuthority(user.get().getId().intValue(),
                    user.get().getId().intValue(),
                    monday.plusDays(i), monday.plusDays(i));
            bar.add(dayTra);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bar);
    }

    @Override
    public ResponseEntity<?> hodMntAttndchartdata() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        LocalDate todaydate = LocalDate.now();
        List<Integer> bar = new ArrayList<Integer>();
        int monthValue = todaydate.getMonthValue();
        LocalDate todaydate1 = todaydate.minusMonths(monthValue);
        for (int i = 1; i <= 12; i++) {
            LocalDate earlier = todaydate1.plusMonths(i);
            LocalDate stdt = earlier.withDayOfMonth(1);
            LocalDate eddt = earlier.withDayOfMonth(earlier.lengthOfMonth());
            Integer presentmnt = attendanceDataRepository.attendanceMonthCount(stdt,
                    eddt);
            bar.add(presentmnt);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bar);
    }

    @Override
    public ResponseEntity<?> hodMntUsesApplication() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        LocalDate todaydate = LocalDate.now();
        List<Integer> bar = new ArrayList<Integer>();
        int monthValue = todaydate.getMonthValue();
        LocalDate todaydate1 = todaydate.minusMonths(monthValue);
        for (int i = 1; i <= 12; i++) {
            LocalDate earlier = todaydate1.plusMonths(i);
            LocalDate stdt = earlier.withDayOfMonth(1);
            LocalDate eddt = earlier.withDayOfMonth(earlier.lengthOfMonth());
            Integer presentmnt = travelDataHeaderRepository.monthlyvisitByAuthority(user.get().getId().intValue(),
                    user.get().getId().intValue(),
                    stdt,
                    eddt);
            bar.add(presentmnt);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bar);
    }

    @Override
    public ResponseEntity<?> hodMonthlyvisit() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        LocalDate today = LocalDate.now();

        today.withDayOfMonth(1);
        today.withDayOfMonth(today.lengthOfMonth());

        LocalDate earlier = today.minusMonths(1);
        earlier.withDayOfMonth(1);
        earlier.withDayOfMonth(earlier.lengthOfMonth());

        // Go backward to get Monday
        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }

        LocalDate preweek = today.minusWeeks(1);
        // Go backward to get Monday
        LocalDate monday1 = preweek;
        while (monday1.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday1 = monday1.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday1 = preweek;
        while (sunday1.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday1 = sunday1.plusDays(1);
        }

        Dashdata dd = new Dashdata();
        return ResponseEntity.status(HttpStatus.OK).body(dd);
    }

    @Override
    public ResponseEntity<?> monthlyvisit() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        LocalDate today = LocalDate.now();

        today.withDayOfMonth(1);
        today.withDayOfMonth(today.lengthOfMonth());

        LocalDate earlier = today.minusMonths(1);
        earlier.withDayOfMonth(1);
        earlier.withDayOfMonth(earlier.lengthOfMonth());

        // Go backward to get Monday
        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }

        LocalDate preweek = today.minusWeeks(1);
        // Go backward to get Monday
        LocalDate monday1 = preweek;
        while (monday1.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday1 = monday1.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday1 = preweek;
        while (sunday1.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday1 = sunday1.plusDays(1);
        }

        Dashdata dd = new Dashdata();
        return ResponseEntity.status(HttpStatus.OK).body(dd);
    }

}
