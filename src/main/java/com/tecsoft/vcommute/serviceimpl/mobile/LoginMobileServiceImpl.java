package com.tecsoft.vcommute.serviceimpl.mobile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import com.tecsoft.vcommute.dto.EmabddedDto;
import com.tecsoft.vcommute.dto.EmailEntity;
import com.tecsoft.vcommute.dto.FaceMultipartDto;
import com.tecsoft.vcommute.dto.MultipartImage;
import com.tecsoft.vcommute.model.AttendanceData;
import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.model.TravelDataDetails;
import com.tecsoft.vcommute.model.TravelDataHeader;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.AttendanceDataRepository;
import com.tecsoft.vcommute.repository.CustomerRepository;
import com.tecsoft.vcommute.repository.TravelDataHeaderRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.response.ResponseHandler;
import com.tecsoft.vcommute.service.mobile.LoginMobileService;
import com.tecsoft.vcommute.serviceimpl.EmailServiceImpl;

@Service
public class LoginMobileServiceImpl implements LoginMobileService {
	@Autowired
	private UserRepositiry userRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailServiceImpl emailService;

	@Autowired
	private TravelDataHeaderRepository travelDataHeaderRepository;

	@Autowired
	private AttendanceDataRepository attendanceDataRepository;

	@Autowired
	private EmbeddingConverter embeddingConverter;

	@Value("${file.upload-dir}")
	private String UPLOAD_DIR;

	@Override
	public ResponseEntity<?> loginprocess(String user_id, String user_pw, String deviceId) {
		Optional<User> optionalUser = userRepository.findByUserName(user_id);

		if (optionalUser.isEmpty()) {
			return ResponseHandler.generateResponse("User Not Found", HttpStatus.BAD_REQUEST, null);
		}

		User user = optionalUser.get();

		if (!passwordEncoder.matches(user_pw, user.getPassword())) {
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

		List<AttendanceData> unfinishedAttendance = attendanceDataRepository.fetchLastUnfinishAttendance(userId, today);
		unfinishedAttendance.forEach(ta -> user.setOpenAttendanceId(ta.getId()));

		List<TravelDataHeader> unfinishedCommute = travelDataHeaderRepository.fetchLastUnfinishCommute(userId, today);
		for (TravelDataHeader commute : unfinishedCommute) {
			user.setOpenCommuteHeaderId(commute.getId());

			commute.getTravelDataDetails().stream()
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
			e.printStackTrace(); // Optional: log error
		}
	}

	@Override
	public ResponseEntity<?> resetDeviceId(Long userId) {
		userRepository.resetDeviceId(userId);
		return ResponseHandler.generateResponse("Device ID Reset Done!!", HttpStatus.OK, null);
	}

	@Override
	public ResponseEntity<?> resetDeviceIdAll() {
		userRepository.resetDeviceIdAll();
		return ResponseHandler.generateResponse("All Device ID Reset Done!!", HttpStatus.OK, null);
	}

	@Override
	public ResponseEntity<?> forgopass(String username, String email, String mobile) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String pwd = RandomStringUtils.random(6, characters);
		Optional<User> findBy = userRepository.findByUsernameAndEmailAndMobile(username, email, mobile);
		if (findBy.isPresent()) {
			User ur = new User();
			ur = findBy.get();
			ur.setPassword(passwordEncoder.encode(pwd));
			EmailEntity ee = new EmailEntity();
			ee.setRecipient(ur.getEmail());
			ee.setSubject("New Password");
			ee.setMsgBody("<p style='font-weight: 400; color: rgb(39, 35, 35);'>Dear " + ur.getName()
					+ ",</p><p>Your Temporary password is <strong>" + pwd + " </strong></p>" + // Please login and
																								// change your password
					"<p style='font-weight: 400; color: rgb(39, 35, 35);'>Best regards,<br/>Vareli Tecnac Pvt. Ltd.</p>");
			 emailService.sendSimpleMail(ee);
				userRepository.save(ur);
				return ResponseHandler.generateResponse("New Password Sent Successfully", HttpStatus.OK, null);
	
		} else {
			return ResponseHandler.generateResponse("User Deatils not  Found", HttpStatus.BAD_REQUEST, null);
		}
	}

	@Override
	public ResponseEntity<?> changepass(Long id, String password, String newPassword) {
		Optional<User> user = userRepository.findById(id);
		boolean isMatch = passwordEncoder.matches(password, user.get().getPassword());
		if (isMatch) {
			user.get().setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user.get());
			return ResponseHandler.generateResponse("Password Updated!!", HttpStatus.OK, null);
		} else {
			return ResponseHandler.generateResponse("Password Not Match", HttpStatus.BAD_REQUEST, null);
		}
	}

	@Override
	public ResponseEntity<?> addImages(MultipartImage file) {
		Optional<User> byId = userRepository.findById(file.getId());
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
			userRepository.save(byId.get());
			return ResponseEntity.status(HttpStatus.OK).body("File Uploaded");
		}
	}

	@Override
	public ResponseEntity<?> addEmbadding(Long id, String embedding) {
		Optional<User> byId = userRepository.findById(id);
		if (byId.isPresent()) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(embedding);
				if (!jsonNode.isArray()) {
					return ResponseHandler.generateResponse("Invalid format: Embedding must be a JSON array",
							HttpStatus.BAD_REQUEST, null);
				}
				byId.get().setEmbedding(embedding);
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
				List<TravelDataHeader> fetchLastUnfinishCommute = travelDataHeaderRepository
						.fetchLastUnfinishCommute(user.getId(), LocalDate.now());
				List<AttendanceData> fetchLastUnfinishAttendance = attendanceDataRepository
						.fetchLastUnfinishAttendance(user.getId(), LocalDate.now());
				for (AttendanceData ta : fetchLastUnfinishAttendance) {
					dto.setOpenAttendanceId(ta.getId());
				}
				for (TravelDataHeader tc : fetchLastUnfinishCommute) {
					dto.setOpenCommuteHeaderId(tc.getId());
					try {
						Optional<TravelDataDetails> result = tc.getTravelDataDetails().stream()
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
				dto.setFaceAttendance(byId.get().isFaceAttendance());
				dto.setNormalAttendance(byId.get().isNormalAttendance());
				dto.setFaceUploaded(byId.get().isFaceUploaded());
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

	@Override
	// public ResponseEntity<?> addFaceEmbadding(FaceMultipartDto file) {
	// 	Optional<User> byId = userRepository.findById(file.getId());

	// 	Set<Long> uniqueSet = new HashSet<>();
	// 	uniqueSet.add(byId.get().getReportManager());
	// 	uniqueSet.add(byId.get().getHod());

	// 	UUID uuid = UUID.randomUUID();
	// 	String mulliganDir = UPLOAD_DIR + "/vcommute";
	// 	String thumbnailDir = mulliganDir + "/thumbnail";

	// 	File uploadDir = new File(mulliganDir);
	// 	if (!uploadDir.exists()) {
	// 		uploadDir.mkdirs();
	// 	}

	// 	File thumbDir = new File(thumbnailDir);
	// 	if (!thumbDir.exists()) {
	// 		thumbDir.mkdirs();
	// 	}

	// 	MultipartFile multipartFile = file.getImages();
	// 	String random = uuid.toString();
	// 	String name = multipartFile.getOriginalFilename();
	// 	String[] part = name.split("\\.");
	// 	String extension = part[part.length - 1];
	// 	String filename = random + "." + extension;

	// 	if (byId.isPresent()) {
	// 		try {
	// 			ObjectMapper mapper = new ObjectMapper();
	// 			JsonNode jsonNode = mapper.readTree(file.getEmbedding());
	// 			if (!jsonNode.isArray()) {
	// 				return ResponseHandler.generateResponse("Invalid format: Embedding must be a JSON array",
	// 						HttpStatus.BAD_REQUEST, null);
	// 			}

	// 			byId.get().setEmbedding(file.getEmbedding());

	// 			if (multipartFile.isEmpty()) {
	// 				return new ResponseEntity<String>("file not found", HttpStatus.BAD_REQUEST);
	// 			} else {
	// 				String uploadFilePath = mulliganDir + "/" + filename;
	// 				String relativePath = "uploadfile/vcommute/" + filename;

	// 				try {
	// 					// Save original image
	// 					byte[] bytes = multipartFile.getBytes();
	// 					Path path = Paths.get(uploadFilePath);
	// 					Files.write(path, bytes);

	// 					// Create and save thumbnail
	// 					BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());
	// 					int thumbWidth = 550;
	// 					int thumbHeight = 750;

	// 					Image resizedImage = originalImage.getScaledInstance(thumbWidth, thumbHeight,
	// 							Image.SCALE_SMOOTH);
	// 					BufferedImage thumbnail = new BufferedImage(thumbWidth, thumbHeight,
	// 							BufferedImage.TYPE_INT_RGB);

	// 					Graphics2D g2d = thumbnail.createGraphics();
	// 					g2d.drawImage(resizedImage, 0, 0, null);
	// 					g2d.dispose();

	// 					File thumbnailFile = new File(thumbnailDir + "/" + filename);
	// 					ImageIO.write(thumbnail, extension, thumbnailFile);

	// 				} catch (IOException e) {
	// 					e.printStackTrace();
	// 				}

	// 				byId.get().setImages(relativePath);
	// 			}

	// 			byId.get().setFaceUploaded(true);
	// 			userRepository.save(byId.get());
	// 			User emp = byId.get();
	// 			for (Long ids : uniqueSet) {

	// 				Optional<User> byId2 = userRepository.findById(ids);
	// 				if (byId2.isPresent()) {
	// 					User user = byId2.get();
	// 					EmailEntity ee = new EmailEntity();
	// 					ee.setRecipient(user.getEmail());
	// 					ee.setSubject(emp.getName() + " Face Upload Pending Approval for Face Recognition Attendance");
	// 					ee.setMsgBody("<p style='font-weight: 400; color: rgb(39, 35, 35);'>Dear " + user.getName()
	// 							+ ",</p><p>This is to inform you that a <strong>" + emp.getName()
	// 							+ " </strong> has uploaded their facial image for face recognition attendance in VCommute. Please review the uploaded image and take appropriate action.</p>"
	// 							+"<p>To <strong>enable face recognition attendance </strong> for the user, you must <strong> approve </strong>the uploaded image."
	// 							+
	// 							"If the image is incorrect or inappropriate, you may choose to disapprove it. Please note that face <strong>"
	// 							+ "recognition attendance will remain inactive until the image is approved."
	// 							+ " </strong> </p>" + 
	// 							"<p>In case the user has <strong> uploaded the wrong image </strong>, you can also select the<strong> Reset </strong>option to allow re-upload.</p>"+
	// 							"<p><strong> Steps to review and take action: </strong></p>"+
	// 							"<p>Go to: http://45.64.222.18:8083/vcommute</p>"+
	// 							"<p>Log in with your credentials</p>"+
	// 							"<p>Select to User Management</p>"+
	// 							"<p>Find the relevant user</p>"+
	// 							"<p>Click on the user's image</p>"+
	// 							"<p>Select Approve, Disapprove, or Reset as appropriate</p>"+
	// 							"<p>Please complete this action at your earliest convenience to avoid delays in attendance processing.</p>"+

	// 							"<p style='font-weight: 400; color: rgb(39, 35, 35);'>Best regards,<br/>Vareli Tecnac Pvt. Ltd.</p>");
	// 					emailService.sendSimpleMail(ee);
		
	// 				}

	// 			}
	// 			return ResponseHandler.generateResponse("Embedding saved!", HttpStatus.OK,
	// 					embeddingConverter.convertToArray(file.getEmbedding()));
	// 		} catch (Exception e) {
	// 			return ResponseHandler.generateResponse("Something went wrong..", HttpStatus.BAD_REQUEST, null);
	// 		}
	// 	} else {
	// 		return ResponseHandler.generateResponse("No valid user found", HttpStatus.BAD_REQUEST, null);
	// 	}
	// }
	public ResponseEntity<?> addFaceEmbadding(FaceMultipartDto file) {
		Optional<User> byId = userRepository.findById(file.getId());

		Set<Long> uniqueSet = new HashSet<>();
		uniqueSet.add(byId.get().getReportManager());
		uniqueSet.add(byId.get().getHod());

		UUID uuid = UUID.randomUUID();
		String mulliganDir = UPLOAD_DIR + "/vcommute";
		String thumbnailDir = mulliganDir + "/thumbnail";

		File uploadDir = new File(mulliganDir);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		File thumbDir = new File(thumbnailDir);
		if (!thumbDir.exists()) {
			thumbDir.mkdirs();
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
				JsonNode jsonNode = mapper.readTree(file.getEmbedding());
				if (!jsonNode.isArray()) {
					return ResponseHandler.generateResponse("Invalid format: Embedding must be a JSON array",
							HttpStatus.BAD_REQUEST, null);
				}

				byId.get().setEmbedding(file.getEmbedding());

				if (multipartFile.isEmpty()) {
					return new ResponseEntity<String>("file not found", HttpStatus.BAD_REQUEST);
				} else {
					String uploadFilePath = mulliganDir + "/" + filename;
					String relativePath = "uploadfile/vcommute/" + filename;
						byte[] bytes = multipartFile.getBytes();
						Path path = Paths.get(uploadFilePath);
						Files.write(path, bytes);
					byId.get().setImages(relativePath);
				}

				byId.get().setFaceUploaded(true);
				userRepository.save(byId.get());
				User emp = byId.get();
				for (Long ids : uniqueSet) {

					Optional<User> byId2 = userRepository.findById(ids);
					if (byId2.isPresent()) {
						User user = byId2.get();
						EmailEntity ee = new EmailEntity();
						ee.setRecipient(user.getEmail());
						ee.setSubject(emp.getName() + " Face Upload Pending Approval for Face Recognition Attendance");
						ee.setMsgBody("<p style='font-weight: 400; color: rgb(39, 35, 35);'>Dear " + user.getName()
								+ ",</p><p>This is to inform you that a <strong>" + emp.getName()
								+ " </strong> has uploaded their facial image for face recognition attendance in VCommute. Please review the uploaded image and take appropriate action.</p>"
								+"<p>To <strong>enable face recognition attendance </strong> for the user, you must <strong> approve </strong>the uploaded image."
								+
								"If the image is incorrect or inappropriate, you may choose to disapprove it. Please note that face <strong>"
								+ "recognition attendance will remain inactive until the image is approved."
								+ " </strong> </p>" + 
								"<p>In case the user has <strong> uploaded the wrong image </strong>, you can also select the<strong> Reset </strong>option to allow re-upload.</p>"+
								"<p><strong> Steps to review and take action: </strong></p>"+
								"<p>Go to: https://v-commute.vareli.co.in/vcommute</p>"+
								"<p>Log in with your credentials</p>"+
								"<p>Select to User Management</p>"+
								"<p>Find the relevant user</p>"+
								"<p>Click on the user's image</p>"+
								"<p>Select Approve, Disapprove, or Reset as appropriate</p>"+
								"<p>Please complete this action at your earliest convenience to avoid delays in attendance processing.</p>"+

								"<p style='font-weight: 400; color: rgb(39, 35, 35);'>Best regards,<br/>Vareli Tecnac Pvt. Ltd.</p>");
						emailService.sendSimpleMail(ee);
		
					}

				}
				return ResponseHandler.generateResponse("Embedding saved!", HttpStatus.OK,
						embeddingConverter.convertToArray(file.getEmbedding()));
			} catch (Exception e) {
				return ResponseHandler.generateResponse("Something went wrong..", HttpStatus.BAD_REQUEST, null);
			}
		} else {
			return ResponseHandler.generateResponse("No valid user found", HttpStatus.BAD_REQUEST, null);
		}
	}
}
