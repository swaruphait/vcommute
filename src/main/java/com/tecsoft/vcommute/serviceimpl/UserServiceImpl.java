package com.tecsoft.vcommute.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tecsoft.vcommute.dto.EmailEntity;
import com.tecsoft.vcommute.dto.MultipartImage;
import com.tecsoft.vcommute.model.City;
import com.tecsoft.vcommute.model.Company;
import com.tecsoft.vcommute.model.MyUserDetails;
import com.tecsoft.vcommute.model.Role;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.CityRepository;
import com.tecsoft.vcommute.repository.CompanyRepository;
import com.tecsoft.vcommute.repository.FinanceAuthorityRepository;
import com.tecsoft.vcommute.repository.RoleRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.service.UserDetailsServiceImpl;
import com.tecsoft.vcommute.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;
    @Autowired
    private UserRepositiry userRepositiry;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    FinanceAuthorityRepository financeAuthorityRepository;

    private static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public ResponseEntity<?> fetchUserDeatils() {
        List<User> all = userRepositiry.findAll().stream().filter(t -> t.isEnabled()).collect(Collectors.toList());
        for (User user : all) {
            Optional<City> findById = cityRepository.findById(user.getLocation_id());
            user.setLocation(findById.get().getCity());
            if (user.getCompanyId() != null) {
                Optional<Company> findById2 = companyRepository.findById(user.getCompanyId());
                user.setCompanyName(findById2.get().getName());
            }
            if (user.getReportManager() != null) {
                user.setNameReportManager(userRepositiry.findById(user.getReportManager()).get().getName());
            }
            if (user.getFinanceAuth() != null) {
                user.setNameFinanceAuth(userRepositiry.findById(user.getFinanceAuth()).get().getName());
            }
            if (user.getHod() != null) {
                user.setNameHod(userRepositiry.findById(user.getHod()).get().getName());
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(all);
    }

    @Override
    public ResponseEntity<?> fetchUserDeatilById(Long id) {
        Optional<User> findById = userRepositiry.findById(id);
        Set<Role> roles = findById.get().getRoles();
        for (Role role : roles) {
            findById.get().setRole(role.getName());
        }
        logger.info(findById.toString());
        findById.get().setPassword("");
        return ResponseEntity.status(HttpStatus.OK).body(findById.get());
    }

    @Override
    public ResponseEntity<?> userRegistration(User user) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer companyId = ((MyUserDetails) principal).CompanyId();
        if (userRepositiry.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username Already Exists");
        } else {
            Set<Role> findByName = roleRepository.findByName(user.getRole());
            user.setCompanyId(companyId);
            user.setRoles(findByName);
            user.setNormalAttendance(true);
            user.setEnabled(true);
            user.setAppvBy("O");
            user.setPassword(passwordEncoder.encode(user.getRawPassword()));
            User save = userRepositiry.save(user);
            if (user.getRole().equals("HOD") || user.getRole().equals("HR") ||
                    user.getRole().equals("FINANCE")) {

                try {
                    EmailEntity ee = new EmailEntity();
                    ee.setRecipient(save.getEmail());
                    ee.setSubject("New Account Creat For VCOMMUTE");
                    ee.setMsgBody(
                            "<p>Dear <strong>" + save.getName() + ",</strong></p>" +
                                    "<p>Thank you for joining as a member of our organization! Your registration is complete. "
                                    +
                                    "You can now access all the features based on your role.</p>" +
                                    "<p><strong>Username:</strong> " + save.getUsername() + "<br/>" +
                                    "<strong>Password:</strong> " + user.getRawPassword() + "</p>" +
                                    "<p>Please click the link below for the user guidelines, which also includes a download option:<br/>"
                                    +
                                    "<a href='https://vareli-my.sharepoint.com/personal/debdeeps_vareli_co_in/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Fdebdeeps%5Fvareli%5Fco%5Fin%2FDocuments%2FV%20Commute&ct=1724238479275&or=Teams%2DHL&ga=1&LOF=1'>https://vareli-vcommute.com/</a></p>"
                                    +
                                    "<p>You can now log in to the web application here:<br/>" +
                                    "<a href='https://v-commute.vareli.co.in/vcommute/'>https://v-commute.vareli.co.in/vcommute/</a></p>"
                                    +
                                    "<p>To give your commute and attendance, please download and use our mobile application:<br/>"
                                    +
                                    "<a href='https://play.google.com/store/apps/details?id=com.vcommute.traveltracking&pcampaignid=web_share'>"
                                    +
                                    "Download VCommute App</a></p>" +
                                    "<p>We value your privacy and security, so please be assured that all the information you provide during registration will be kept confidential and secure.</p>"
                                    +
                                    "<p style='font-weight: 400; color: rgb(39, 35, 35);'>Best regards,<br/>Vareli Tecnac Pvt. Ltd.</p>");

                    Boolean sendSimpleMail = emailServiceImpl.sendSimpleMail(ee);
                    if (sendSimpleMail) {
                        return ResponseEntity.status(HttpStatus.OK)
                                .body("User successfully created!! New user id is ->" + save.getUsername()
                                        + "<- and password is -> " + user.getRawPassword() + "<-  ");
                    } else {
                        return ResponseEntity.status(HttpStatus.OK)
                                .body("Error while sending mail!!! New user id is ->" + save.getUsername()
                                        + "<- and password is -> "
                                        + user.getRawPassword() + "<-  ");
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else {
                try {
                    EmailEntity ee = new EmailEntity();
                    ee.setRecipient(save.getEmail());
                    ee.setSubject("New Account Creat For VCOMMUTE");
                    ee.setMsgBody(
                            "<p>Dear <strong>" + save.getName() + ",</strong></p>" +
                                    "<p>Thank you for joining as a member of our organization! Your registration is complete. "
                                    +
                                    "You can now access all the features based on your role.</p>" +
                                    "<p><strong>Username:</strong> " + save.getUsername() + "<br/>" +
                                    "<strong>Password:</strong> " + user.getRawPassword() + "</p>" +
                                    "<p>Please click the link below for the user guidelines, which also includes a download option:<br/>"
                                    +
                                    "<a href='https://vareli-my.sharepoint.com/personal/debdeeps_vareli_co_in/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Fdebdeeps%5Fvareli%5Fco%5Fin%2FDocuments%2FV%20Commute&ct=1724238479275&or=Teams%2DHL&ga=1&LOF=1'>https://vareli-vcommute.com/</a></p>"

                                    +
                                    "<p>To give your commute and attendance, please download and use our mobile application:<br/>"
                                    +
                                    "<a href='https://play.google.com/store/apps/details?id=com.vcommute.traveltracking&pcampaignid=web_share'>"
                                    +
                                    "Download VCommute App</a></p>" +
                                    "<p>We value your privacy and security, so please be assured that all the information you provide during registration will be kept confidential and secure.</p>"
                                    +
                                    "<p style='font-weight: 400; color: rgb(39, 35, 35);'>Best regards,<br/>Vareli Tecnac Pvt. Ltd.</p>");

                    Boolean sendSimpleMail = emailServiceImpl.sendSimpleMail(ee);
                    if (sendSimpleMail) {
                        return ResponseEntity.status(HttpStatus.OK)
                                .body("User successfully created!! New user id is ->" + save.getUsername()
                                        + "<- and password is -> " + user.getRawPassword() + "<-  ");
                    } else {
                        return ResponseEntity.status(HttpStatus.OK)
                                .body("Error while sending mail!!! New user id is ->" + save.getUsername()
                                        + "<- and password is -> "
                                        + user.getRawPassword() + "<-  ");
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Registered");
        }
    }

    @Override
    public ResponseEntity<?> userUpdate(User user) {
        Optional<User> findById = userRepositiry.findById(user.getId());
        if (user.getRawPassword() == null) {
            user.setPassword(findById.get().getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getRawPassword().strip()));
        }
        Set<Role> findByName = roleRepository.findByName(user.getRole());
        user.setRoles(findByName);
        user.setEnabled(true);
        userRepositiry.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Update.");
    }

    @Override
    public ResponseEntity<?> userActivation(Long id) {
        Optional<User> findById = userRepositiry.findById(id);
        if (!findById.get().isEnabled()) {
            findById.get().setEnabled(true);
            userRepositiry.save(findById.get());
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Activated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uesr Already Active");
        }
    }

    @Override
    public ResponseEntity<?> userDeactivation(Long id) {
        Optional<User> findById = userRepositiry.findById(id);
        if (findById.get().isEnabled()) {
            findById.get().setEnabled(false);
            userRepositiry.save(findById.get());
            logger.info("Successfully Deactive");
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Deactivated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uesr Already Deactivated");
        }
    }

    @Override
    public ResponseEntity<?> forgetpass(String username, String email, String mobile) {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pwd = RandomStringUtils.random(6, characters);
        Optional<User> findBy = userRepositiry.findByUsernameAndEmailAndMobile(username, email, mobile);
        System.out.println("Condition: " + findBy);
        if (findBy.isPresent()) {
            User ur = findBy.get();
            System.out.println(ur);
            ur.setPassword(passwordEncoder.encode(pwd));
            System.out.println(ur.getEmail());
            try {
                EmailEntity ee = new EmailEntity();
                ee.setRecipient(ur.getEmail());
                ee.setSubject("New Password");
                ee.setMsgBody("Dear " + ur.getName() + ",\n" +
                        "Your temporary password is: " + pwd + "\n" +
                        "Please login and change your password.\n" +
                        "Best regards,\n" +
                        "Vareli Tecnac Pvt. Ltd.");

                emailServiceImpl.sendSimpleMail(ee);
                userRepositiry.save(ur);
                return new ResponseEntity<>(
                        "Password '" + pwd + "' set successfully! also sent email new password.", HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>("Unable to send password reset email. Please contact the administrator.",
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("No user found with the provided information.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> resetpass(String password, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepositiry.findByUserName(currentPrincipalName);
        System.out.println(user);
        boolean isMatch = passwordEncoder.matches(password, user.get().getPassword());
        System.out.println("Match: " + isMatch);
        if (isMatch) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepositiry.save(user.get());
            return new ResponseEntity<String>("Password Updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Password Not Match with old password", HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<?> resetDeviceId(Long id) {
        User userDetails = userRepositiry.findById(id).get();
        String deviceId = userDetails.getDeviceId();
        System.out.println(userDetails.getDeviceId());
        if (deviceId != null) {
            userRepositiry.resetDeviceId(id);
            return ResponseEntity.status(HttpStatus.OK).body("Succesfully Reset Device Id");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }

    @Override
    public ResponseEntity<?> resetDeviceIdAll() {
        userRepositiry.resetDeviceIdAll();
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resetPassword(Long id) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pwd = RandomStringUtils.random(6, characters);
        Optional<User> findById = userRepositiry.findById(id);
        if (findById.get().isEnabled()) {
            User ur = new User();
            ur = findById.get();
            ur.setPassword(passwordEncoder.encode(pwd));
            try {
                EmailEntity ee = new EmailEntity();
                ee.setRecipient(ur.getEmail());
                ee.setSubject("New Password");
                ee.setMsgBody("<p style='font-weight: 400; color: rgb(39, 35, 35);'>Dear " + ur.getName()
                        + ",</p><p>Your Temporary password is <strong>" +
                        pwd + " </strong></p>" + // Please login and change your password
                        "<p style='font-weight: 400; color: rgb(39, 35, 35);'>Best regards,<br/>Vareli Tecnac Pvt. Ltd.</p>");
                Boolean sendSimpleMail = emailServiceImpl.sendSimpleMail(ee);
                if (sendSimpleMail) {
                    userRepositiry.save(ur);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("New Password Sent Successfully!!New password is ->" + pwd + "<-  ");
                } else {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Error while sending mail!!!, New password is ->" + pwd + "<-  ");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("User is NOT active!! Please Active Than Try for Password Reset");
        }
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @Override
    public ResponseEntity<?> fetchWitoutUser() {
        List<User> collect = userRepositiry.findAll().stream()
                .filter(user -> user.isEnabled() && user.getRoles().stream()
                        .noneMatch(role -> role.getName().equals("USER") || role.getName().equals("SUPERADMIN")))
                .collect(Collectors.toList());
        return new ResponseEntity<List<User>>(collect, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> fetchUserListByAuthority() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        List<User> fetchEmployeeAuthorityWise = userRepositiry.fetchEmployeeAuthorityWise(loggedUser.get().getId(),
                loggedUser.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(fetchEmployeeAuthorityWise);
    }

    @Override
    public ResponseEntity<?> addImages(MultipartImage file) {
        Optional<User> byId = userRepositiry.findById(file.getId());
        UUID uuid = UUID.randomUUID();

        // Define the directory to include the 'mulligan' folder
        String mulliganDir = UPLOAD_DIR + "/vcommute";
        File uploadDir = new File(mulliganDir);

        // Create the directory if it doesn't exist
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        MultipartFile multipartFile = file.getImages();
        String random = uuid.toString();
        String name = multipartFile.getOriginalFilename();

        // System.out.println(name);
        String[] part = name.split("\\.");
        String extension = part[part.length - 1];
        // System.out.println(extension);
        String filename = random + "." + extension;

        if (multipartFile.isEmpty()) {
            return new ResponseEntity<String>("file not found", HttpStatus.BAD_REQUEST);
        } else {
            // Update the upload file path to include the 'mulligan' folder
            String uploadFilePath = mulliganDir + "/" + filename;
            String realtivePath = "uploadfile/vcommute/" + filename;
            try {
                byte[] bytes = multipartFile.getBytes();
                Path path = Paths.get(uploadFilePath);
                Files.write(path, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byId.get().setImages(realtivePath);
            userRepositiry.save(byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("File Uploaded");
        }
    }

    @Override
    public ResponseEntity<?> resetImage(Long id) {
        userRepositiry.resetImage(id);
        return ResponseEntity.status(HttpStatus.OK).body("Reset Image Successfully");

    }

    @Override
    public ResponseEntity<?> resetEmbadding(Long id) {
        userRepositiry.resetEmbadding(id);
        return ResponseEntity.status(HttpStatus.OK).body("Reset Embadd Successfully");
    }

    @Override
    public ResponseEntity<?> faceApproved(Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        String roleName = null;
        for (Role role : loggedUser.get().getRoles()) {
            roleName = role.getName();
        }
        Optional<User> byId = userRepositiry.findById(id);
        if (!byId.get().isFaceAttendance()) {
            byId.get().setFaceAttendance(true);
            byId.get().setNormalAttendance(false);
            if (roleName.equals("ADMIN")) {
                byId.get().setAppvBy("A");
            }
            if (byId.get().getReportManager().equals(loggedUser.get().getId())) {
                byId.get().setAppvBy("R");
            }

            if (byId.get().getHod().equals(loggedUser.get().getId())) {
                byId.get().setAppvBy("H");
            }
            userRepositiry.save(byId.get());
            try {
            User user = byId.get();
            EmailEntity ee = new EmailEntity();
            ee.setRecipient(user.getEmail());
            ee.setSubject("Face Upload Approved -Face Recognition Attendance Activated in VCommute");
            ee.setMsgBody("<p>Dear " + user.getName() + "</p><p>"
            + "We’re pleased to inform you that your uploaded facial image has been reviewed and approved."
            + "Your face recognition attendance feature is now active in VCommute. You can start using it for seamless attendance</p>"
            + "<p>&nbsp;</p><p>Best regards,</p><p>Vareli Tecnac Pvt. Ltd.</p>");
            emailServiceImpl.sendSimpleMail(ee);
            } catch (Exception e) {
            System.out.println(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).body("Approved Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Approved The Face");
        }
    }

    @Override
    public ResponseEntity<?> fetchUserUnderManager() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        List<User> fetchEmployeeAuthorityWise = userRepositiry.fetchEmployeeAuthorityWise(loggedUser.get().getId(),
                loggedUser.get().getId());
        for (User user : fetchEmployeeAuthorityWise) {
            if (user.getCompanyId() != null) {
                Optional<Company> findById2 = companyRepository.findById(user.getCompanyId());
                user.setCompanyName(findById2.get().getName());
            }
            if (user.getReportManager() != null) {
                user.setNameReportManager(userRepositiry.findById(user.getReportManager()).get().getName());
            }
            if (user.getFinanceAuth() != null) {
                user.setNameFinanceAuth(userRepositiry.findById(user.getFinanceAuth()).get().getName());
            }
            if (user.getHod() != null) {
                user.setNameHod(userRepositiry.findById(user.getHod()).get().getName());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(fetchEmployeeAuthorityWise);
    }

    @Override
    public ResponseEntity<?> faceDisApproved(Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        String roleName = null;
        for (Role role : loggedUser.get().getRoles()) {
            roleName = role.getName();
        }
        Optional<User> byId = userRepositiry.findById(id);
        byId.get().setFaceAttendance(false);
        byId.get().setNormalAttendance(true);
        if (roleName.equals("ADMIN")) {
            byId.get().setAppvBy("A");
        }
        if (byId.get().getReportManager().equals(loggedUser.get().getId())) {
            byId.get().setAppvBy("R");
        }

        if (byId.get().getHod().equals(loggedUser.get().getId())) {
            byId.get().setAppvBy("H");
        }
        userRepositiry.save(byId.get());
        try {
            User user = byId.get();
            EmailEntity ee = new EmailEntity();
            ee.setRecipient(user.getEmail());
            ee.setSubject("Your Normal Attendance Activate - Face Recognition Disapproved in VCommute");
            ee.setMsgBody("<p>Dear " + user.getName() + "</p><p>"
                    + "We’re pleased to inform you that your uploaded facial image has been disapproved."
                    + "Your face recognition attendance feature is not active in VCommute. You can continue using it normal attendance</p>"
                    + "<p>&nbsp;</p><p>Best regards,</p><p>Vareli Tecnac Pvt. Ltd.</p>");
            emailServiceImpl.sendSimpleMail(ee);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Face Attendance Revoked Successfully");

    }

}
