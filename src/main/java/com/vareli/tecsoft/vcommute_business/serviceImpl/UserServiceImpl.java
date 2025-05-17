package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.SuperCompany;
import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.model.dto.EmailEntity;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.DepartmentRepository;
import com.vareli.tecsoft.vcommute_business.repository.DesignationRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubscriptionDataRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SuperCompanyRepository superCompanyRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private SubscriptionDataRepository subscriptionDataRepository;

  @Autowired
  private SubCompanyRepository subCompanyRepository;

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private DesignationRepository designationRepository;

  @Autowired
  private EmailServiceImpl emailServiceImpl;

  @Override
  public ResponseEntity<?> creatSuperadmin(User user) {
    if (user.getId() == null) {
      user.setEnabled(true);
      user.setRole("SUPERADMIN");
      user.setCompanyId(0);
      user.setAccountNotExpired(true);
      user.setPassword(passwordEncoder.encode(user.getRawPassword()));
      userRepository.save(user);
      return ResponseHandler.generateResponse("Successfully User Created..", HttpStatus.OK, null);
    } else {
      Optional<User> byId = userRepository.findById(user.getId());
      if (user.getRawPassword() != null) {
        user.setPassword(passwordEncoder.encode(user.getRawPassword()));
      } else {
        user.setPassword(byId.get().getPassword());
      }
      userRepository.save(user);
      return ResponseHandler.generateResponse("Successfully User Updated..", HttpStatus.OK, null);
    }
  }

  @Override
  public ResponseEntity<?> fetchAllSuperadmin() {
    List<User> collect = userRepository.findAll().stream().filter(u -> u.getRole().equals("SUPERADMIN"))
        .collect(Collectors.toList());
    for (User user : collect) {
      user.setCompanyName(superCompanyRepository.findById(user.getSuperCompanyId()).get().getName());
    }
    return ResponseHandler.generateResponse("Successfully User Created..", HttpStatus.OK, collect);
  }

  @Override
  public ResponseEntity<?> findById(Long id) {
    Optional<User> byId = userRepository.findById(id);
    if (byId.isPresent()) {
      return ResponseHandler.generateResponse("Successfully Fetched Dateails..", HttpStatus.OK, byId);
    } else {
      return ResponseHandler.generateResponse("Something went wrong...", HttpStatus.BAD_REQUEST, null);
    }
  }

  @Override
  public ResponseEntity<?> activeUser(Long id) {
    Optional<User> byId = userRepository.findById(id);
    if (!byId.get().isEnabled()) {
      byId.get().setEnabled(true);
      userRepository.save(byId.get());
      return ResponseEntity.status(HttpStatus.OK).body("Successfully User Activated..");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Successfully Aleady Activated..");
    }
  }

  @Override
  public ResponseEntity<?> deactiveUser(Long id) {
    Optional<User> byId = userRepository.findById(id);
    if (byId.get().isEnabled()) {
      byId.get().setEnabled(false);
      userRepository.save(byId.get());
      return ResponseEntity.status(HttpStatus.OK).body("Successfully User DeActivated..");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Aleady DeActived..");
    }
  }

  @Override
  public Boolean usernameAvailability(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public ResponseEntity<?> creatUser(User user) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
    if (user.getId() == null) {
      Integer fetchNoOfCompanyLimit = subscriptionDataRepository.fetchNoOfUserLimit(userDetails.getSuperCompanyId());
      Integer countNoOfUser = userRepository.countNoOfUser(userDetails.getSuperCompanyId());
      if (user.getCompanyId() == null) {
        user.setCompanyId(userDetails.getCompanyId());
      }
      if (fetchNoOfCompanyLimit > countNoOfUser) {
        user.setEnabled(true);
        user.setSuperCompanyId(userDetails.getSuperCompanyId());
        user.setAccountNotExpired(true);
        user.setPassword(passwordEncoder.encode(user.getRawPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully User Created..");
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("No of Limit Reached!! Limit: " + fetchNoOfCompanyLimit + " Present User: " + countNoOfUser);

      }
    } else {
      Optional<User> byId = userRepository.findById(user.getId());
      if (byId.isPresent()) {
        if (user.getRawPassword() != null) {
          user.setPassword(passwordEncoder.encode(user.getRawPassword()));
        } else {
          user.setPassword(byId.get().getPassword()); // keep existing password
        }
        userRepository.save(user);
      }
      return ResponseEntity.status(HttpStatus.OK).body("Successfully User Updated..");
    }

  }

  @Override
  public ResponseEntity<?> fetchAllUser(Integer companyId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails user = (MyUserDetails) auth.getPrincipal();

    if (companyId == null) {
      companyId = user.getCompanyId();
    }

    List<User> fetchUserList = userRepository.fetchUserList(user.getSuperCompanyId(), companyId);

    for (User u : fetchUserList) {
      if (u.getCompanyId() == 0) {
        superCompanyRepository.findById(u.getSuperCompanyId())
            .ifPresent(sc -> u.setCompanyName(sc.getName()));
      } else {
        subCompanyRepository.findById(u.getCompanyId())
            .ifPresent(sub -> u.setCompanyName(sub.getName()));
      }
      if (u.getLocationId() != null) {
        cityRepository.findById(u.getLocationId())
            .ifPresent(city -> u.setLocation(city.getCity()));
      }
      if (u.getDeptId() != null) {
        departmentRepository.findById(u.getDeptId())
            .ifPresent(dept -> u.setDepartment(dept.getName()));
      }
      if (u.getDesignationId() != null) {
        designationRepository.findById(u.getDesignationId())
            .ifPresent(des -> u.setDesignation(des.getName()));
      }
    }
    return ResponseHandler.generateResponse("Successfully fetched user list.", HttpStatus.OK, fetchUserList);
  }

  @Override
  public ResponseEntity<?> fetchActiveUserList(Integer companyId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails user = (MyUserDetails) auth.getPrincipal();
    if (companyId == null) {
      companyId = user.getCompanyId();
    }
    List<User> fetchUserList = userRepository.fetchUserList(user.getSuperCompanyId(), companyId).stream()
        .filter(t -> t.isEnabled()).collect(Collectors.toList());
    return ResponseHandler.generateResponse("Successfully fetched user list.", HttpStatus.OK, fetchUserList);

  }

  @Override
  public ResponseEntity<?> fetchAuthorityUnderUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails user = (MyUserDetails) auth.getPrincipal();
    List<User> fetchAuthorityUnderUserList = userRepository.fetchAuthorityUnderUserList(user.getUser().getId());
    return ResponseHandler.generateResponse("Successfully fetched user list.", HttpStatus.OK,
        fetchAuthorityUnderUserList);
  }

  @Override
  public ResponseEntity<?> resetDeviceId(Long userId) {
    userRepository.resetDeviceId(userId);
    return ResponseEntity.status(HttpStatus.OK).body("Reset Device Id Successfully");
  }

  @Override
  public ResponseEntity<?> resetDeviceIdAll() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails user = (MyUserDetails) auth.getPrincipal();
    userRepository.resetDeviceIdAll(user.getSuperCompanyId(), user.getCompanyId());
    return ResponseEntity.status(HttpStatus.OK).body("Reset All Device Successfully");
  }

  @Override
  public ResponseEntity<?> resetPassword(Long userId) {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    String pwd = RandomStringUtils.random(6, characters);
    Optional<User> byId = userRepository.findById(userId);

    if (byId.isPresent() && byId.get().isEnabled()) {
      User user = byId.get();
      Optional<SuperCompany> byId2 = superCompanyRepository.findById(user.getSuperCompanyId());
      user.setPassword(passwordEncoder.encode(pwd));
      try {
        EmailEntity ee = new EmailEntity();
        ee.setRecipient(user.getEmail());
        ee.setSubject("V-Commute Business New Password");
        ee.setMsgBody("<p style='font-weight: 400; color: rgb(39, 35, 35);'>Dear " + user.getName()
            + ",</p><p>Your Temporary password is <strong>" + pwd
            + " </strong></p><p style='font-weight: 400; color: rgb(39, 35, 35);'>Best regards,<br/>"
            + byId2.get().getName() + ".</p>");
        Boolean sendSimpleMail = this.emailServiceImpl.sendSimpleMail(ee);
        if (sendSimpleMail) {
          this.userRepository.save(user);
          if (byId.isPresent()) {
            this.userRepository.save((User) byId.get());
          }
          return ResponseEntity.status(HttpStatus.OK)
              .body("Succesfully! New password is -> " + pwd + " <-  ");
        } else {
          return ResponseEntity.status(HttpStatus.OK)
              .body("Error Sending Mail! New password is -> " + pwd + " <-  ");
        }
      } catch (Exception var9) {
        return ResponseEntity.status(HttpStatus.OK).body("Succesfully! New password is -> " + pwd + " <-  ");
      }
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("User is NOT active!! Please Active Than Try for Password Reset");
    }
  }

}
