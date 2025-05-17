package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vareli.tecsoft.vcommute_business.model.Cluster;
import com.vareli.tecsoft.vcommute_business.model.CommuteApprovalData;
import com.vareli.tecsoft.vcommute_business.model.CommuteDetails;
import com.vareli.tecsoft.vcommute_business.model.CommuteHeader;
import com.vareli.tecsoft.vcommute_business.model.Customer;
import com.vareli.tecsoft.vcommute_business.model.SubscriptionData;
import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.model.dto.MultiApproveDto;
import com.vareli.tecsoft.vcommute_business.model.dto.MultipartImage;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.ClusterRepository;
import com.vareli.tecsoft.vcommute_business.repository.CommuteApprovalDataRepository;
import com.vareli.tecsoft.vcommute_business.repository.CommuteDetailsRepository;
import com.vareli.tecsoft.vcommute_business.repository.CommuteHeaderRepository;
import com.vareli.tecsoft.vcommute_business.repository.CustomerRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubscriptionDataRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.CommuteService;

@Service
public class CommuteServiceImpl implements CommuteService {

    @Autowired
    private CommuteHeaderRepository commuteHeaderRepository;

    @Autowired
    private CommuteDetailsRepository commuteDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommuteApprovalDataRepository commuteApprovalDataRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private SubscriptionDataRepository subscriptionDataRepository;

    @Autowired
    private ClusterRepository clusterRepository;

    @Value("${file.upload-dir}")
    private static String UPLOAD_DIR;

    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    
    @Override
    public ResponseEntity<?> fetchApprovalList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<CommuteHeader> fetchCommuteApprovalData = commuteHeaderRepository
                .fetchCommuteApprovalData(user.getUser().getId(), user.getSuperCompanyId(), user.getCompanyId());
        for (CommuteHeader commuteHeader : fetchCommuteApprovalData) {
            Optional<User> byId = userRepository.findById(commuteHeader.getUserId());
            commuteHeader.setName(byId.get().getName());
            commuteHeader.setEmployeeId(byId.get().getEmployeeId());
            if (commuteHeader.getCustId() != null) {
                commuteHeader.setCustomerName(customerRepository.findById(commuteHeader.getCustId()).get().getName());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchCommuteApprovalData);
    }

    public ResponseEntity<?> approvedCommuteData(Long id, Integer clusterId, Double price) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();

        Optional<CommuteHeader> byId = commuteHeaderRepository.findById(id);
        if (byId.isPresent()) {
            CommuteHeader commuteHeader = byId.get();
            commuteHeader.setTotalActualPrice(price);
            String approvalLevel = commuteHeader.getApprovalLevel();
            List<Integer> fetchApprovalLevel = userRepository.fetchApprovalLevel(commuteHeader.getUserId());
            String currentLevel = commuteHeader.getApprovalLevel();
            int currentLevelInt;
            try {
                currentLevelInt = Integer.parseInt(currentLevel);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid current approval level format.");
            }
            int currentIndex = fetchApprovalLevel.indexOf(currentLevelInt);
            if (currentIndex == -1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Current approval level not found in approval levels list.");
            }

            if (currentIndex < fetchApprovalLevel.size() - 1) {
                String nextLevel = fetchApprovalLevel.get(currentIndex + 1).toString();
                commuteHeader.setApprovalLevel(nextLevel);
            } else {
                Optional<SubscriptionData> byStatusAndSuperCompanyId = subscriptionDataRepository
                        .findByStatusAndSuperCompanyId(true, user.getSuperCompanyId());
                if (byStatusAndSuperCompanyId.isPresent()) {
                    SubscriptionData subscriptionData = byStatusAndSuperCompanyId.get();
                    if (subscriptionData.isFinanceAccess()) {
                        commuteHeader.setApprovalLevel("F");
                    } else {
                        commuteHeader.setApprovalLevel("A");
                    }
                }
            }

            CommuteApprovalData data = new CommuteApprovalData();
            data.setCommuteId(id);
            data.setCurrentAppvlOrder(commuteHeader.getApprovalLevel());
            data.setPrevAppvlOrder(approvalLevel);
            data.setDate(LocalDate.now().toString());
            data.setTime(LocalTime.now().toString());
            data.setApproveBy(user.getUsername());

            commuteApprovalDataRepository.save(data);
            commuteHeaderRepository.save(commuteHeader);
            return ResponseEntity.status(HttpStatus.OK).body("Approval updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Commute data not found.");
        }
    }

    @Override
    public ResponseEntity<?> approvedDataList(List<MultiApproveDto> data) {
        for (MultiApproveDto multiApproveDto : data) {
            System.out.println(data);
            try {
                approvedCommuteData(multiApproveDto.id, null, multiApproveDto.getTotalActualPrice());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Successs");
    }

    @Override
    public ResponseEntity<?> addDocument(MultipartImage file) {
        Optional<CommuteDetails> travelDetails = commuteDetailsRepository.findById(file.getId());
        UUID uuid = UUID.randomUUID();

        File uploadDir = new File(uploadDirectory);
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
            try {
                String uploadFilePath = uploadDirectory + "/" + filename;
                byte[] bytes = multipartFile.getBytes();
                Path path = Paths.get(uploadFilePath);
                Files.write(path, bytes);
                travelDetails.get().setImages(filename);
                commuteDetailsRepository.save(travelDetails.get());
                return ResponseEntity.status(HttpStatus.OK).body("Document uploaded successfully...");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong..");
            }

        }
    }

    @Override
    public ResponseEntity<?> disapprovedCommuteData(Long id, Double price, String reason) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        Optional<CommuteHeader> travelData = commuteHeaderRepository.findById(id);
        Optional<CommuteApprovalData> fetchLatestApprovalData = commuteApprovalDataRepository
                .fetchLatestApprovalDataByCommuteId(id);
        if (fetchLatestApprovalData.isPresent()) {
            CommuteApprovalData commuteApprovalData = fetchLatestApprovalData.get();
            commuteApprovalData.setPrevAppvlOrder(travelData.get().getApprovalLevel());
            commuteApprovalData.setCurrentAppvlOrder("D");
            commuteApprovalData.setRemarks("Disapproved By " + user.getUser().getName());
            commuteApprovalData.setDate(LocalDate.now().toString());
            commuteApprovalData.setTime(LocalTime.now().toString());
            commuteApprovalData.setApproveBy(user.getUsername());
            commuteApprovalDataRepository.save(commuteApprovalData);
        } else {
            CommuteApprovalData commuteApprovalData = new CommuteApprovalData();
            commuteApprovalData.setCommuteId(id);
            commuteApprovalData.setPrevAppvlOrder(travelData.get().getApprovalLevel());
            commuteApprovalData.setCurrentAppvlOrder("D");
            commuteApprovalData.setRemarks("Disapproved By " + user.getUser().getName());
            commuteApprovalData.setDate(LocalDate.now().toString());
            commuteApprovalData.setTime(LocalTime.now().toString());
            commuteApprovalData.setApproveBy(user.getUsername());
            commuteApprovalDataRepository.save(commuteApprovalData);
        }
        travelData.get().setTotalActualPrice(price);
        travelData.get().setApprovalLevel("D");
        travelData.get().setNote(reason);
        commuteHeaderRepository.save(travelData.get());
        return ResponseEntity.status(HttpStatus.OK).body("Approved Successful");
    }

    @Override
    public ResponseEntity<?> disapprovedCommuteDataList(List<MultiApproveDto> data, String reason) {
        for (MultiApproveDto multiApproveDto : data) {
            try {
                disapprovedCommuteData(multiApproveDto.getId(), multiApproveDto.getTotalActualPrice(), reason);
            } catch (Exception e) {
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @Override
    public ResponseEntity<?> fetchDisApprovalList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<CommuteHeader> fetchCommuteApprovalData = commuteHeaderRepository
                .fetchCommuteDisapproveData(user.getUser().getId(), user.getSuperCompanyId(), user.getCompanyId());
        for (CommuteHeader commuteHeader : fetchCommuteApprovalData) {
            Optional<User> byId = userRepository.findById(commuteHeader.getUserId());
            commuteHeader.setName(byId.get().getName());
            commuteHeader.setEmployeeId(byId.get().getEmployeeId());
            if (commuteHeader.getCustId() != null) {
                commuteHeader.setCustomerName(customerRepository.findById(commuteHeader.getCustId()).get().getName());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchCommuteApprovalData);
    }

    @Override
    public ResponseEntity<?> reApprovedData(Long id, Double price) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        Optional<CommuteHeader> byId = commuteHeaderRepository.findById(id);
        if (byId.isPresent()) {
            CommuteHeader commuteHeader = byId.get();
            commuteHeader.setTotalActualPrice(price);
            Optional<CommuteApprovalData> fetchLatestByCommuteId = commuteApprovalDataRepository
                    .fetchLatestByCommuteId(id);
            String approvalLevel = fetchLatestByCommuteId.get().getPrevAppvlOrder();
            List<Integer> fetchApprovalLevel = userRepository.fetchApprovalLevel(commuteHeader.getUserId());
            String currentLevel = fetchLatestByCommuteId.get().getPrevAppvlOrder();
            int currentLevelInt;
            try {
                currentLevelInt = Integer.parseInt(currentLevel);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid current approval level format.");
            }
            int currentIndex = fetchApprovalLevel.indexOf(currentLevelInt);

            if (currentIndex == -1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Current approval level not found in approval levels list.");
            }
            if (currentIndex < fetchApprovalLevel.size() - 1) {
                String nextLevel = fetchApprovalLevel.get(currentIndex + 1).toString();
                commuteHeader.setApprovalLevel(nextLevel);
            } else {
                Optional<SubscriptionData> byStatusAndSuperCompanyId = subscriptionDataRepository
                        .findByStatusAndSuperCompanyId(true, user.getSuperCompanyId());
                if (byStatusAndSuperCompanyId.isPresent()) {
                    SubscriptionData subscriptionData = byStatusAndSuperCompanyId.get();
                    if (subscriptionData.isFinanceAccess()) {
                        commuteHeader.setApprovalLevel("F");

                    } else {
                        commuteHeader.setApprovalLevel("A");
                    }
                }
            }
            CommuteApprovalData data = new CommuteApprovalData();
            data.setCommuteId(id);
            data.setCurrentAppvlOrder(commuteHeader.getApprovalLevel());
            data.setPrevAppvlOrder(approvalLevel);
            data.setDate(LocalDate.now().toString());
            data.setTime(LocalTime.now().toString());
            data.setApproveBy(user.getUsername());

            commuteApprovalDataRepository.save(data);
            commuteHeaderRepository.save(commuteHeader);
            return ResponseEntity.status(HttpStatus.OK).body("Approval updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Commute data not found.");
        }
    }

    @Override
    public ResponseEntity<?> fetchCommuteData(String startDate, String endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<CommuteHeader> fetchCustomerList = commuteHeaderRepository.fetchCustomerList(startDate, endDate,
                user.getSuperCompanyId(), user.getCompanyId());
        for (CommuteHeader commute : fetchCustomerList) {
            Optional<User> byId = userRepository.findById(commute.getUserId());
            commute.setName(byId.get().getName());
            commute.setEmployeeId(byId.get().getEmployeeId());
            if (commute.getCompanyId() != null) {
                Optional<Customer> byId2 = customerRepository.findById(commute.getCustId());
                commute.setCustomerName(byId2.get().getName());
                commute.setCustomerLocation(cityRepository.findById(byId2.get().getLocationId()).get().getCity());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchCustomerList);
    }

    @Override
    public ResponseEntity<?> fetchAuthorityData(String startDate, String endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<CommuteHeader> fetchCommuteDataAuthorityUnderUser = commuteHeaderRepository
                .fetchCommuteDataAuthorityUnderUser(startDate, endDate, user.getUser().getId(),
                        user.getSuperCompanyId(), user.getCompanyId());
        for (CommuteHeader commute : fetchCommuteDataAuthorityUnderUser) {
            Optional<User> byId = userRepository.findById(commute.getUserId());
            commute.setName(byId.get().getName());
            commute.setEmployeeId(byId.get().getEmployeeId());
            if (commute.getCompanyId() != null) {
                Optional<Customer> byId2 = customerRepository.findById(commute.getCustId());
                commute.setCustomerName(byId2.get().getName());
                commute.setCustomerLocation(cityRepository.findById(byId2.get().getLocationId()).get().getCity());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK,
                fetchCommuteDataAuthorityUnderUser);
    }

    @Override
    public ResponseEntity<?> fetchFinaceApprovalList(Integer clusterId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        Optional<Cluster> byId2 = clusterRepository.findById(clusterId);
        List<CommuteHeader> fetchCommuteApprovalData = commuteHeaderRepository
                .fetchFinanceApprovalDataList(byId2.get().getStartDate(), byId2.get().getEndDate(),user.getSuperCompanyId(), user.getCompanyId());
        for (CommuteHeader commuteHeader : fetchCommuteApprovalData) {
            Optional<User> byId = userRepository.findById(commuteHeader.getUserId());
            commuteHeader.setName(byId.get().getName());
            commuteHeader.setEmployeeId(byId.get().getEmployeeId());
            commuteHeader.setClusterName(byId2.get().getClusterName());
            if (commuteHeader.getCustId() != null) {
                commuteHeader.setCustomerName(customerRepository.findById(commuteHeader.getCustId()).get().getName());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchCommuteApprovalData);
    }

    @Override
    public ResponseEntity<?> cliamSettledApproval(Long id, Integer clusetrId, Double price) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cliamSettledApproval'");
    }

}
