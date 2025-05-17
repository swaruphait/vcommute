package com.vareli.tecsoft.vcommute_business.serviceImpl.android;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vareli.tecsoft.vcommute_business.model.Attendance;
import com.vareli.tecsoft.vcommute_business.model.CommuteDetails;
import com.vareli.tecsoft.vcommute_business.model.CommuteHeader;
import com.vareli.tecsoft.vcommute_business.model.Customer;
import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.model.dto.EmabddedDto;
import com.vareli.tecsoft.vcommute_business.model.dto.EmailEntity;
import com.vareli.tecsoft.vcommute_business.model.dto.MultipartImage;
import com.vareli.tecsoft.vcommute_business.repository.AttendanceRepository;
import com.vareli.tecsoft.vcommute_business.repository.CommuteHeaderRepository;
import com.vareli.tecsoft.vcommute_business.repository.CustomerRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.android.MobileLoginService;
import com.vareli.tecsoft.vcommute_business.serviceImpl.EmailServiceImpl;

@Service
public class MobileLoginServiceImpl implements MobileLoginService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CommuteHeaderRepository commuteHeaderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
	private EmbeddingConverter embeddingConverter;

    @Autowired
    EmailServiceImpl emailService;

    @Value("${file.upload-dir}")
	private String UPLOAD_DIR;

    @Override
    public ResponseEntity<Object> signin(String username, String password, String deviceId) {
      Optional<User> optionalUser = userRepository.findByUsername(username);
	
		if (optionalUser.isEmpty()) {
			return ResponseHandler.generateResponse("User Not Found", HttpStatus.BAD_REQUEST, null);
		}
	
		User user = optionalUser.get();
	
		if (!passwordEncoder.matches(password, user.getPassword())) {
			return ResponseHandler.generateResponse("Wrong Password!", HttpStatus.BAD_REQUEST, null);
		}
	
		if (user.getDeviceId() == null) {
			user.setDeviceId(deviceId);
			user = userRepository.save(user);
		} else if (!user.getDeviceId().equals(deviceId)) {
			return ResponseHandler.generateResponse("Device Mismatch!", HttpStatus.FORBIDDEN, null);
		}
	
		populateUserData(user);
	
		return ResponseHandler.generateResponse("Login successfully!", HttpStatus.OK, user);
    }

    private void populateUserData(User user) {
		Long userId = user.getId();
		LocalDate today = LocalDate.now();
	
     List<Attendance> unfinishedAttendance = attendanceRepository.fetchLastUnfinishAttendance(userId, user.getSuperCompanyId(), user.getCompanyId(), today);
     unfinishedAttendance.forEach(ta -> user.setOpenAttendanceId(ta.getId()));

     List<CommuteHeader> unfinishedCommute = commuteHeaderRepository.fetchLastUnfinishCommute(userId, user.getSuperCompanyId(), user.getCompanyId(), today);

		for (CommuteHeader commute : unfinishedCommute) {
			user.setOpenCommuteHeaderId(commute.getId());
	
			commute.getCommuteDetails().stream()
				.filter(t -> !t.isStatus())
				.findFirst()
				.ifPresent(details -> {
					user.setOpenCommuteDetailsId(details.getId());
					user.setDocumentRequired(details.isDocumentRequired());
					user.setPriceRequired(details.isPriceRequired());
				});
	
			if (commute.getCustId() != null) {
				customerRepository.findById(commute.getCustId())
					.ifPresent(cust -> commute.setCustomerName(cust.getName()));
			}
		}
	
		user.setAttendanceData(unfinishedAttendance);
		user.setCommuteData(unfinishedCommute);
	
		try {
			if (user.getEmbedding() != null) {
				user.setEmbeddingData(embeddingConverter.convertToArray(user.getEmbedding()));
				user.setCheckEmbedding(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Override
    public ResponseEntity<?> resetDeviceId(Long userId) {
        try {
            userRepository.resetDeviceId(userId);
            return ResponseEntity.ok("Successfully Reset the Device Id");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> resetDeviceIdAll(Integer superCompanyId, Integer companyId) {
        try {
            userRepository.resetDeviceIdAll(superCompanyId, companyId);
            return ResponseEntity.ok("Successfully Reset the All Device Id");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> forgopass(String username, String email, String mobile) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@_0123456789";
        String password = RandomStringUtils.random(6, characters);
        System.out.println(password);
        Optional<User> user = userRepository.findByUsernameAndEmailAndMobile(username, email, mobile);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(password));
            EmailEntity ee = new EmailEntity();
            ee.setRecipient(user.get().getEmail());
            ee.setSubject("New Password VCommute Apllication");
            ee.setMsgBody("<p style='font-weight: 400; color: rgb(39, 35, 35);'>Dear " + user.get().getName()
                    + ",</p><p>Your Temporary password is <strong>" + password + " </strong></p>" + // Please login and
                    // change your password
                    "<p style='font-weight: 400; color: rgb(39, 35, 35);'>Best regards,<br/>Vareli Tecnac Pvt. Ltd.</p>");
            Boolean sendSimpleMail = emailService.sendSimpleMail(ee);
            if (sendSimpleMail) {
                userRepository.save(user.get());
                return ResponseHandler.generateResponse("New Password Sent Successfully", HttpStatus.OK, null);
            } else {
                return ResponseHandler.generateResponse(
                        "Error while sending mail!!!, try later or contact administrator",
                        HttpStatus.INTERNAL_SERVER_ERROR, null);
            }
        } else {
            return ResponseHandler.generateResponse("As per Deatils not Found any users", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<Object> changepass(Long userId, String password, String newPassword) {
        Optional<User> users = userRepository.findById(userId);
        boolean isMatch = passwordEncoder.matches(password, users.get().getPassword());
        if (isMatch) {
            users.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(users.get());
            return ResponseHandler.generateResponse("Password Updated!!", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Password Not Match", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> addFaceEmbadding(Long id,  String embedding, MultipartImage file) {
    	Optional<User> byId = userRepository.findById(id);
		UUID uuid = UUID.randomUUID();
		String mulliganDir = UPLOAD_DIR + "/vcommute";
		File uploadDir = new File(mulliganDir);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		MultipartFile multipartFile = file.getImages();
		String random = uuid.toString();
		String name = multipartFile.getOriginalFilename();
		String[] part = name.split("\\.");
		String extension = part[part.length - 1];
		String filename = random + "." + extension;

		if (byId.isPresent()) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(embedding);
				if (!jsonNode.isArray()) {
					return ResponseHandler.generateResponse("Invalid format: Embedding must be a JSON array", HttpStatus.BAD_REQUEST, null);
				}
	
				byId.get().setEmbedding(embedding);
				if (multipartFile.isEmpty()) {
					return new ResponseEntity<String>("file not found", HttpStatus.BAD_REQUEST);
				} else {
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
				}
				userRepository.save(byId.get());
				return ResponseHandler.generateResponse("Embedding saved!", HttpStatus.OK,
						embeddingConverter.convertToArray(embedding));
			} catch (Exception e) {
				return ResponseHandler.generateResponse("Something went wrong..", HttpStatus.BAD_REQUEST, null);
			}
		} else {
			return ResponseHandler.generateResponse("No valid user found", HttpStatus.BAD_REQUEST, null);
		}
    }

    @Override
    public ResponseEntity<?> fetchEmbaddingById(Long userId) {
      Optional<User> byId = userRepository.findById(userId);
		if (byId.isPresent()) {
			User user = byId.get();
			try {
				EmabddedDto dto = new EmabddedDto();
				List<CommuteHeader> fetchLastUnfinishCommute = commuteHeaderRepository
						.fetchLastUnfinishCommute(user.getId(), user.getSuperCompanyId(), user.getCompanyId(), LocalDate.now());
				List<Attendance> fetchLastUnfinishAttendance = attendanceRepository
						.fetchLastUnfinishAttendance(user.getId(), user.getSuperCompanyId(), user.getCompanyId(), LocalDate.now());
				for (Attendance ta : fetchLastUnfinishAttendance) {
					dto.setOpenAttendanceId(ta.getId());
				}
				for (CommuteHeader tc : fetchLastUnfinishCommute) {
					dto.setOpenCommuteHeaderId(tc.getId());
					try {
						Optional<CommuteDetails> result = tc.getCommuteDetails().stream()
								.filter(t -> !t.isStatus()).findFirst();
						dto.setOpenCommuteDetailsId(result.get().getId());
						dto.setDocumentRequired(result.get().isDocumentRequired());
						dto.setPriceRequired(result.get().isPriceRequired());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (tc.getCustId() != null) {
						Optional<Customer> cust = customerRepository.findById(tc.getCustId());
						tc.setCustomerName(cust.get().getName());
					}
				}
				dto.setAttendanceData(fetchLastUnfinishAttendance);
				dto.setCommuteData(fetchLastUnfinishCommute);
				if (byId.get().getEmbedding() != null) {
					double[] convertToArray = embeddingConverter.convertToArray(byId.get().getEmbedding());
					dto.setEmbeddingData(convertToArray);
					dto.setCheckEmbedding(true);
				}

				return ResponseHandler.generateResponse("Embadding Details!!", HttpStatus.OK, dto);
			} catch (IOException e) {
				return ResponseHandler.generateResponse("No Valid user found", HttpStatus.BAD_REQUEST, e.getMessage());
			}
		} else {
			return ResponseHandler.generateResponse("No Valid user found", HttpStatus.BAD_REQUEST, null);
		}
    }

    @Override
    public ResponseEntity<?> resetEmbadding(Long userId) {
        Optional<User> byId = userRepository.findById(userId);
		if (byId.isPresent()) {
			userRepository.resetEmabddingById(userId);
			return ResponseHandler.generateResponse("Embadding Reset Done!!", HttpStatus.OK, null);
		} else {
			return ResponseHandler.generateResponse("Opps No valid user present!!", HttpStatus.BAD_REQUEST, null);
		}
    }
    
}
