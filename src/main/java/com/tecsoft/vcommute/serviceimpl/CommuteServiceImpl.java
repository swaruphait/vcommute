package com.tecsoft.vcommute.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.tecsoft.vcommute.dto.EmailEntity;
import com.tecsoft.vcommute.dto.MultiApproveDto;
import com.tecsoft.vcommute.dto.MultipartImage;
import com.tecsoft.vcommute.model.ApprovedData;
import com.tecsoft.vcommute.model.City;
import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.model.Mode;
import com.tecsoft.vcommute.model.MyUserDetails;
import com.tecsoft.vcommute.model.TravelDataDetails;
import com.tecsoft.vcommute.model.TravelDataHeader;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.ApprovedDataRepository;
import com.tecsoft.vcommute.repository.CityRepository;
import com.tecsoft.vcommute.repository.CustomerRepository;
import com.tecsoft.vcommute.repository.ModeRepository;
import com.tecsoft.vcommute.repository.TravelDataDetailsRepository;
import com.tecsoft.vcommute.repository.TravelDataHeaderRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.service.CommuteService;

@Service
public class CommuteServiceImpl implements CommuteService {

    // private static final double EARTH_RADIUS_KM = 6371; // Radius of the Earth in km

    @Autowired
    private TravelDataHeaderRepository travelDataHeaderRepository;

    @Autowired
    private TravelDataDetailsRepository travelDataDetailsRepository;

    @Autowired
    private UserRepositiry userRepositiry;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ApprovedDataRepository approvedDataRepository;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private ModeRepository modeRepository;

    @Value("${file.upload-dir}")
    private static String UPLOAD_DIR;

    @Value("${api-key}")
    private String API_KEY;

    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    private static final Map<String, Function<String, Double>> converters = new HashMap<>();

    static {
        converters.put("km", str -> Double.parseDouble(str));
        converters.put("ft", str -> Double.parseDouble(str) * 0.0003048); // 1 ft = 0.0003048 km
        converters.put("mi", str -> Double.parseDouble(str) * 1.60934); // 1 mi = 1.60934 km
        converters.put("m", str -> Double.parseDouble(str) * 0.001); // 1 m = 0.001 km
    }

    public static double convertToKilometers(String distanceString) {
        for (Map.Entry<String, Function<String, Double>> entry : converters.entrySet()) {
            if (distanceString.contains(entry.getKey())) {
                Matcher matcher = Pattern.compile("[0-9.]+").matcher(distanceString);
                if (matcher.find()) {
                    return entry.getValue().apply(matcher.group());
                }
            }
        }
        return 0.0; // Default value if no conversion is found
    }

    public static double extractTimeValue(String input) {
        // Use regular expression to extract the numeric value
        String numericValueStr = input.replaceAll("[^0-9.]", "");
        try {
            return Double.parseDouble(numericValueStr);
        } catch (NumberFormatException e) {
            // Handle parsing error as needed
            return 0.0; // Default value if parsing fails
        }
    }

    @Override
    public ResponseEntity<?> fetchApprovalData() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        List<TravelDataHeader> fetchApprovalData = travelDataHeaderRepository
                .fetchApprovalData(loggedUser.get().getId(), loggedUser.get().getId());
        if (fetchApprovalData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        } else {
            for (TravelDataHeader data : fetchApprovalData) {
                User user = userRepositiry.getById(data.getUserId());
                Customer cust = customerRepository.getById(data.getCustId());
                City byId = cityRepository.getById(cust.getLocationId());
                data.setUsername(user.getName());
                data.setEmployeeId(user.getEmpCode());
                data.setCustomerName(cust.getName());
                data.setCustomerLocation(byId.getCity());
            }
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        }
    }

    @Override
    public ResponseEntity<?> fetchAllCommuteData(String stdate, String enddate) {
        ZoneId asiaKolkata = ZoneId.of("Asia/Kolkata");
        Instant nowUtc1 = Instant.parse(stdate);
        Instant nowUtc2 = Instant.parse(enddate);
        ZonedDateTime zone1 = ZonedDateTime.ofInstant(nowUtc1, asiaKolkata);
        ZonedDateTime zone2 = ZonedDateTime.ofInstant(nowUtc2, asiaKolkata);
        LocalDate stdt = zone1.toLocalDate();
        LocalDate eddt = zone2.toLocalDate();
        List<TravelDataHeader> fetchApprovalData = travelDataHeaderRepository
                .fetchAllCommuteDataByDate(stdt, eddt);
        if (fetchApprovalData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        } else {
            for (TravelDataHeader data : fetchApprovalData) {
                User user = userRepositiry.getById(data.getUserId());
                Customer cust = customerRepository.getById(data.getCustId());
                City byId = cityRepository.getById(cust.getLocationId());
                data.setUsername(user.getName());
                data.setEmployeeId(user.getEmpCode());
                data.setCustomerName(cust.getName());
                data.setCustomerLocation(byId.getCity());
            }
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        }
    }

    @Override
    public ResponseEntity<?> disapprovalTravelData(Long id, Double price, String reason) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        TravelDataHeader travelData = travelDataHeaderRepository.getById(id);
        String lastApproveLevel = travelData.getApprovalLevel();
        Optional<User> user = userRepositiry.findById(travelData.getUserId());
        travelData.setTotalActualPrice(price);
        travelData.setApprovalLevel("D");
        travelData.setNote(reason);
        TravelDataHeader save = travelDataHeaderRepository.save(travelData);
        Optional<ApprovedData> byTdId = approvedDataRepository.findByTdId(save.getId());
        if (byTdId.isEmpty()) {
            ApprovedData ad = new ApprovedData();
            ad.setApprvBy(currentPrincipalName);
            ad.setApprvOrder(lastApproveLevel);
            ad.setTdId(travelData.getId());
            ad.setDate(LocalDate.now());
            ad.setTime(LocalTime.now());
            ad.setRmks("Disapproved By " + currentPrincipalName);
            approvedDataRepository.save(ad);
        } else {
            byTdId.get().setDate(LocalDate.now());
            byTdId.get().setApprvBy(currentPrincipalName);
            byTdId.get().setApprvOrder(lastApproveLevel);
            byTdId.get().setTdId(travelData.getId());
            byTdId.get().setTime(LocalTime.now());
            byTdId.get().setRmks("Disapproved By " + currentPrincipalName);
            approvedDataRepository.save(byTdId.get());
        }

        try {
            EmailEntity ee = new EmailEntity();
            ee.setRecipient(user.get().getEmail());
            ee.setSubject("Disapproved Your Travel Claim");
            ee.setMsgBody("<p>Dear " + user.get().getName() + "</p><p>"
                    + "I want to inform you that your travel allowance is disapproval by <strong>"
                    + currentPrincipalName + "</strong> for <strong>" + reason + "</strong>.<br/>"
                    + "*If you have any queary plaese contact to your approval authority</p>"
                    + "<p><strong>Your Disapproved Travel Details: </strong>: </p>"
                    + "<p><strong>Start DateTime: </strong>:" + travelData.getStartDate() +
                    travelData.getStartTime()
                    + " </p>"
                    + "<p><strong>Start Location: </strong>:" + travelData.getStartLocation() +
                    "</p>"
                    + "<p><strong>End DateTime: </strong>:" + travelData.getEndDate() +
                    travelData.getEndTime()
                    + " </p>"
                    + "<p><strong>Stop Location: </strong>: " + travelData.getEndLocation()
                    + "<p><strong>Purpose: </strong>: " + travelData.getPurpose() +
                    "<p><strong>Ref. Code: </strong>: "
                    + travelData.getReferencenumber() + "</p><p><strong>Approx Distance: </strong>:"
                    + travelData.getTotalDistance() + "</p>"
                    + "<p><strong>Price: </strong>:" + travelData.getTotalActualPrice() + "</p>"
                    + "<p>&nbsp;</p><p>Best regards,</p><p>Vareli Tecnac Pvt. Ltd.</p>");
            Boolean sendSimpleMail = emailServiceImpl.sendSimpleMail(ee);
            if (sendSimpleMail) {
                return ResponseEntity.status(HttpStatus.OK).body("Approved Successful");
            }

            else {
                return ResponseEntity.status(HttpStatus.OK).body("Failed !! Approved Error Try later");

            }
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Approved Successful");
    }

    @Override
    public ResponseEntity<?> approvalTravelData(Long id, Integer clusetrId, Double price) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        TravelDataHeader travelData = travelDataHeaderRepository.getById(id);
        travelData.setTotalActualPrice(price);
        travelData.setApprovalLevel("A2");
        travelDataHeaderRepository.save(travelData);
        Optional<ApprovedData> byTdId = approvedDataRepository.findByTdId(id);
        if (byTdId.isEmpty()) {
            ApprovedData ad = new ApprovedData();
            ad.setApprvBy(currentPrincipalName);
            ad.setApprvOrder(travelData.getApprovalLevel());
            ad.setTdId(travelData.getId());
            ad.setDate(LocalDate.now());
            ad.setTime(LocalTime.now());
            ad.setRmks("Approved");
            approvedDataRepository.save(ad);
        } else {
            byTdId.get().setDate(LocalDate.now());
            byTdId.get().setApprvBy(currentPrincipalName);
            byTdId.get().setApprvOrder(travelData.getApprovalLevel());
            byTdId.get().setTdId(travelData.getId());
            byTdId.get().setTime(LocalTime.now());
            byTdId.get().setRmks("Approved");
            approvedDataRepository.save(byTdId.get());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Approved Data");

    }

    @Override
    public ResponseEntity<?> approvalTravelDataList(List<MultiApproveDto> data) {
        for (MultiApproveDto multiApproveDto : data) {
            System.out.println(data);
            try {
                approvalTravelData(multiApproveDto.id, null, multiApproveDto.getTotalActualPrice());
            } catch (Exception e) {
                // System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @Override
    public ResponseEntity<?> disapprovalTravelDataList(List<MultiApproveDto> data, String reason) {
        for (MultiApproveDto multiApproveDto : data) {
            try {
                disapprovalTravelData(multiApproveDto.id, multiApproveDto.getTotalActualPrice(), reason);
            } catch (Exception e) {
                // System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @Override
    public ResponseEntity<?> updateDataBygoogleapi() {
        List<TravelDataDetails> findAll = travelDataDetailsRepository.fetchTravelDatagoogleapi();
        GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
        for (TravelDataDetails travelDataDetails : findAll) {
            try {

                DistanceMatrixRow[] rows = DistanceMatrixApi.newRequest(context)
                        .origins(new LatLng(travelDataDetails.getStartLat(), travelDataDetails.getStartLong()))
                        .destinations(new LatLng(travelDataDetails.getEndLat(), travelDataDetails.getEndLong()))
                        .awaitIgnoreError().rows;

                if (rows.length != 0) {
                    for (int i = 0; i < rows.length; i++) {
                        for (int j = 0; j < rows[i].elements.length; j++) {
                            // System.out.println("Distance: " + rows[i].elements[j].distance.humanReadable);
                            // System.out.println("Time: " + rows[i].elements[j].duration);

                            try {
                                travelDataDetails.setDistance(rows[i].elements[j].distance.humanReadable);
                                travelDataDetails.setTime(rows[i].elements[j].duration.humanReadable);
                                double distance = convertToKilometers(rows[i].elements[j].distance.humanReadable);
                                double time = extractTimeValue(rows[i].elements[j].duration.humanReadable);
                                Optional<Mode> mode = null;
                                try {
                                    mode = modeRepository.findByLevelIdAndLocationId(
                                            travelDataDetails.getModeId().longValue(),
                                            travelDataDetails.getLocationId().longValue());
                                } catch (Exception e) {
                                    // System.out.println("Discripancy Id: " + travelDataDetails.getId());
                                }
                                double basePrice = 0;
                                double baseKm = 0;
                                double pricePerKm = 0;

                                if (mode.isPresent()) {
                                    basePrice = mode.get().getBasePrice();
                                    baseKm = mode.get().getBasekm();
                                    pricePerKm = mode.get().getPriceperkm();
                                }
                                // System.out.println("basePrice: " + basePrice);
                                // System.out.println("baseKm: " + baseKm);
                                // System.out.println("pricePerKm: " + pricePerKm);

                                if (distance <= baseKm) {
                                    travelDataDetails.setEstimatePrice(basePrice);
                                } else {
                                    double finalPrice = basePrice + ((distance - baseKm) * pricePerKm);
                                    travelDataDetails.setEstimatePrice(finalPrice);
                                }
                                double prevTotalDistance = travelDataDetails.getTravelDataHeader().getTotalDistance();
                                double prevTotalTime = extractTimeValue(
                                        travelDataDetails.getTravelDataHeader().getTotalTime());
                                double newTotalDistance = prevTotalDistance + distance;
                                double newTotalTime = prevTotalTime + time;
                                travelDataDetails.getTravelDataHeader().setTotalDistance(newTotalDistance);
                                travelDataDetails.getTravelDataHeader()
                                        .setTotalTime(Double.toString(newTotalTime) + " min");

                                // Update the total estimate price
                                double prevTotalEstimatePrice = travelDataDetails.getTravelDataHeader()
                                        .getTotalEstimatePrice();
                                double newTotalEstimatePrice = prevTotalEstimatePrice
                                        + travelDataDetails.getEstimatePrice();
                                travelDataDetails.setApiStatus(true);
                                travelDataDetails.getTravelDataHeader().setTotalEstimatePrice(newTotalEstimatePrice);
                                travelDataDetailsRepository.save(travelDataDetails);
                            } catch (Exception e) {
                                // System.out.println("Failed as distance is zero with id: " + travelDataDetails.getId());
                                travelDataDetails.setDistance("0.0");
                                travelDataDetails.setTime("0.0");
                                travelDataDetails.setEstimatePrice(0.0);
                            }
                        }
                    }

                }

            } catch (Exception e) {
                // System.out.println("Data Incorrect for:" + travelDataDetails.getId());
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }


    //  public ResponseEntity<?> updateDataBygoogleapi() {
    //     List<TravelDataDetails> findAll = travelDataDetailsRepository.fetchTravelDatagoogleapi();
    //     // GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
    //     for (TravelDataDetails travelDataDetails : findAll) {
    //         try {

    //             double latDistance = Math.toRadians(travelDataDetails.getEndLat() - travelDataDetails.getStartLat());
    //             double lonDistance = Math.toRadians(travelDataDetails.getEndLong() - travelDataDetails.getStartLong());
    //             // DistanceMatrixRow[] rows = DistanceMatrixApi.newRequest(context)
    //             //         .origins(new LatLng(travelDataDetails.getStartLat(), travelDataDetails.getStartLong()))
    //             //         .destinations(new LatLng(travelDataDetails.getEndLat(), travelDataDetails.getEndLong()))
    //             //         .awaitIgnoreError().rows;

    //             double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
    //             + Math.cos(Math.toRadians(travelDataDetails.getStartLat())) * Math.cos(Math.toRadians(travelDataDetails.getEndLong()))
    //             * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

    //     double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    //     travelDataDetails.setDistance((EARTH_RADIUS_KM * c) + "km");

    //     double distance = EARTH_RADIUS_KM * c;
    //     Optional<Mode> mode = null;

    //     try {
    //         mode = modeRepository.findByLevelIdAndLocationId(
    //                 travelDataDetails.getModeId().longValue(),
    //                 travelDataDetails.getLocationId().longValue());
    //     } catch (Exception e) {
    //         // System.out.println("Discripancy Id: " + travelDataDetails.getId());
    //     }
    //     double basePrice = 0;
    //     double baseKm = 0;
    //     double pricePerKm = 0;

    //     if (mode.isPresent()) {
    //         basePrice = mode.get().getBasePrice();
    //         baseKm = mode.get().getBasekm();
    //         pricePerKm = mode.get().getPriceperkm();
    //     }
    //     // System.out.println("basePrice: " + basePrice);
    //     // System.out.println("baseKm: " + baseKm);
    //     // System.out.println("pricePerKm: " + pricePerKm);

    //     if (distance <= baseKm) {
    //         travelDataDetails.setEstimatePrice(basePrice);
    //     } else {
    //         double finalPrice = basePrice + ((distance - baseKm) * pricePerKm);
    //         travelDataDetails.setEstimatePrice(finalPrice);
    //     }
    //     double prevTotalDistance = travelDataDetails.getTravelDataHeader().getTotalDistance();
    //     double prevTotalTime = extractTimeValue(
    //             travelDataDetails.getTravelDataHeader().getTotalTime());
    //     double newTotalDistance = prevTotalDistance + distance;
    //     double newTotalTime = prevTotalTime ;
    //     travelDataDetails.getTravelDataHeader().setTotalDistance(newTotalDistance);
    //     travelDataDetails.getTravelDataHeader()
    //             .setTotalTime(Double.toString(newTotalTime) + " min");

    //     // Update the total estimate price
    //     double prevTotalEstimatePrice = travelDataDetails.getTravelDataHeader()
    //             .getTotalEstimatePrice();
    //     double newTotalEstimatePrice = prevTotalEstimatePrice
    //             + travelDataDetails.getEstimatePrice();
    //     travelDataDetails.setApiStatus(true);
    //     travelDataDetails.getTravelDataHeader().setTotalEstimatePrice(newTotalEstimatePrice);
    //     travelDataDetailsRepository.save(travelDataDetails);

    //         } catch (Exception e) {
    //         }

    //     }
    //     return ResponseEntity.status(HttpStatus.OK).body("Success");
    // }


    @Override
    public ResponseEntity<?> fetchFirstLevelDisapprovData() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        List<TravelDataHeader> findAll = travelDataHeaderRepository.fetchDisapprovedData(loggedUser.get().getId(),
                loggedUser.get().getId());
        if (findAll.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(findAll);
        } else {
            for (TravelDataHeader data : findAll) {
                User user = userRepositiry.getById(data.getUserId());
                Customer cust = customerRepository.getById(data.getCustId());
                City byId = cityRepository.getById(cust.getLocationId());
                data.setUsername(user.getName());
                data.setEmployeeId(user.getEmpCode());
                data.setCustomerName(cust.getName());
                data.setCustomerLocation(byId.getCity());
            }
            return ResponseEntity.status(HttpStatus.OK).body(findAll);
        }
    }

    @Override
    public ResponseEntity<?> fetchFinanceLevelDisapprovData() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());

        List<TravelDataHeader> findAll = null;
        if (loggedUser.get().getFinanceAccessArea().equals("A")) {
            findAll = travelDataHeaderRepository.fetchAllFinanceDisapprovedData();
        } else if (loggedUser.get().getFinanceAccessArea().equals("S")) {
            findAll = travelDataHeaderRepository.fetchFinanceDisapprovedData(
                    loggedUser.get().getId());
        }

        if (findAll.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(findAll);
        } else {
            for (TravelDataHeader data : findAll) {
                User user = userRepositiry.getById(data.getUserId());
                Customer cust = customerRepository.getById(data.getCustId());
                City byId = cityRepository.getById(cust.getLocationId());
                data.setUsername(user.getName());
                data.setEmployeeId(user.getEmpCode());
                data.setCustomerName(cust.getName());
                data.setCustomerLocation(byId.getCity());
            }
            return ResponseEntity.status(HttpStatus.OK).body(findAll);
        }
    }

    @Override
    public ResponseEntity<?> firstLevelReApprove(Long id, Double price) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<TravelDataHeader> byId = travelDataHeaderRepository.findById(id);
        byId.get().getTotalActualPrice();
        approvedDataRepository.findByTdIdAndAppr(id, currentPrincipalName);
        byId.get().setStatus(true);
        byId.get().setApprovalLevel("A2");
        travelDataHeaderRepository.save(byId.get());
        return ResponseEntity.status(HttpStatus.OK).body("Success");

    }

    @Override
    public ResponseEntity<?> financeLevelReApprove(Long id, Double price) {

        Optional<TravelDataHeader> byId = travelDataHeaderRepository.findById(id);
        byId.get().getTotalActualPrice();
        // Optional<ApprovedData> disData = approvedDataRepository.findByTdIdAndAppr(id, currentPrincipalName);
        // // System.out.println(disData);
        byId.get().setStatus(true);
        byId.get().setApprovalLevel("A");
        travelDataHeaderRepository.save(byId.get());

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @Override
    public ResponseEntity<?> fetchCommuteDatabyDate(String stdate, String enddate) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        ZoneId asiaKolkata = ZoneId.of("Asia/Kolkata");
        Instant nowUtc1 = Instant.parse(stdate);
        Instant nowUtc2 = Instant.parse(enddate);
        ZonedDateTime zone1 = ZonedDateTime.ofInstant(nowUtc1, asiaKolkata);
        ZonedDateTime zone2 = ZonedDateTime.ofInstant(nowUtc2, asiaKolkata);
        LocalDate stdt = zone1.toLocalDate();
        LocalDate eddt = zone2.toLocalDate();
        List<TravelDataHeader> fetchApprovalData = travelDataHeaderRepository
                .fetchApprovalDataByDate(loggedUser.get().getId(), loggedUser.get().getId(), stdt, eddt);
        if (fetchApprovalData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        } else {
            for (TravelDataHeader data : fetchApprovalData) {
                User user = userRepositiry.getById(data.getUserId());
                Customer cust = customerRepository.getById(data.getCustId());
                City byId = cityRepository.getById(cust.getLocationId());
                data.setUsername(user.getName());
                data.setEmployeeId(user.getEmpCode());
                data.setCustomerName(cust.getName());
                data.setCustomerLocation(byId.getCity());
            }
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        }
    }

    @Override
    public ResponseEntity<?> unfinishCommuteAuthority(String stdate, String enddate) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        ZoneId asiaKolkata = ZoneId.of("Asia/Kolkata");
        LocalDate stdt = null;
        LocalDate eddt = null;
        Instant nowUtc1 = Instant.parse(stdate);
        Instant nowUtc2 = Instant.parse(enddate);
        ZonedDateTime zone1 = ZonedDateTime.ofInstant(nowUtc1, asiaKolkata);
        ZonedDateTime zone2 = ZonedDateTime.ofInstant(nowUtc2, asiaKolkata);
        stdt = zone1.toLocalDate();
        eddt = zone2.toLocalDate();

        List<TravelDataHeader> fetchApprovalData = travelDataHeaderRepository
                .unfinishTravelDataAuthority(loggedUser.get().getId(), loggedUser.get().getId(), stdt, eddt);
        if (fetchApprovalData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        } else {
            for (TravelDataHeader data : fetchApprovalData) {
                User user = userRepositiry.getById(data.getUserId());
                data.setUsername(user.getName());
                data.setEmployeeId(user.getEmpCode());
            }
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        }
    }

    @Override
    public ResponseEntity<?> reportGenAutority(String stdate, String enddate, String active) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedUser = userRepositiry.findByUserName(((MyUserDetails) principal).getUsername());
        ZoneId asiaKolkata = ZoneId.of("Asia/Kolkata");
        Instant nowUtc1 = Instant.parse(stdate);
        Instant nowUtc2 = Instant.parse(enddate);
        ZonedDateTime zone1 = ZonedDateTime.ofInstant(nowUtc1, asiaKolkata);
        ZonedDateTime zone2 = ZonedDateTime.ofInstant(nowUtc2, asiaKolkata);
        LocalDate stdt = zone1.toLocalDate();
        LocalDate eddt = zone2.toLocalDate();
        if (active.equals("ALL")) {
            List<TravelDataHeader> reportGenAuthAllData = travelDataHeaderRepository
                    .reportGenAuthAllData(loggedUser.get().getId(), loggedUser.get().getId(), stdt, eddt);
            if (reportGenAuthAllData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            } else {
                for (TravelDataHeader data : reportGenAuthAllData) {
                    User user = userRepositiry.getById(data.getUserId());
                    Customer cust = customerRepository.getById(data.getCustId());
                    City byId = cityRepository.getById(cust.getLocationId());
                    data.setUsername(user.getName());
                    data.setEmployeeId(user.getEmpCode());
                    data.setCustomerName(cust.getName());
                    data.setCustomerLocation(byId.getCity());
                }
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            }
        } else if (active.equals("A") || active.equals("D")) {
            List<TravelDataHeader> reportGenAuthAllData = travelDataHeaderRepository
                    .reportGenAuthTypeWiseData(loggedUser.get().getId(), loggedUser.get().getId(), stdt, eddt, active);
            if (reportGenAuthAllData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            } else {
                for (TravelDataHeader data : reportGenAuthAllData) {
                    User user = userRepositiry.getById(data.getUserId());
                    Customer cust = customerRepository.getById(data.getCustId());
                    City byId = cityRepository.getById(cust.getLocationId());
                    data.setUsername(user.getName());
                    data.setEmployeeId(user.getEmpCode());
                    data.setCustomerName(cust.getName());
                    data.setCustomerLocation(byId.getCity());
                }
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            }
        } else {
            List<TravelDataHeader> reportGenAuthAllData = travelDataHeaderRepository
                    .reportGenAuthPendingData(loggedUser.get().getId(), loggedUser.get().getId(), stdt, eddt);
            if (reportGenAuthAllData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            } else {
                for (TravelDataHeader data : reportGenAuthAllData) {
                    User user = userRepositiry.getById(data.getUserId());
                    Customer cust = customerRepository.getById(data.getCustId());
                    City byId = cityRepository.getById(cust.getLocationId());
                    data.setUsername(user.getName());
                    data.setEmployeeId(user.getEmpCode());
                    data.setCustomerName(cust.getName());
                    data.setCustomerLocation(byId.getCity());
                }
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            }
        }
    }

    @Override
    public ResponseEntity<?> unfinishAllCommuteData(String stdate, String enddate) {
        ZoneId asiaKolkata = ZoneId.of("Asia/Kolkata");
        Instant nowUtc1 = Instant.parse(stdate);
        Instant nowUtc2 = Instant.parse(enddate);
        ZonedDateTime zone1 = ZonedDateTime.ofInstant(nowUtc1, asiaKolkata);
        ZonedDateTime zone2 = ZonedDateTime.ofInstant(nowUtc2, asiaKolkata);
        LocalDate stdt = zone1.toLocalDate();
        LocalDate eddt = zone2.toLocalDate();
        List<TravelDataHeader> fetchApprovalData = travelDataHeaderRepository
                .unfinishTravelData(stdt, eddt);
        if (fetchApprovalData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        } else {
            for (TravelDataHeader data : fetchApprovalData) {
                User user = userRepositiry.getById(data.getUserId());
                data.setUsername(user.getName());
                data.setEmployeeId(user.getEmpCode());
            }
            return ResponseEntity.status(HttpStatus.OK).body(fetchApprovalData);
        }
    }

    @Override
    public ResponseEntity<?> reportGen(String stdate, String enddate, String active) {
        ZoneId asiaKolkata = ZoneId.of("Asia/Kolkata");
        Instant nowUtc1 = Instant.parse(stdate);
        Instant nowUtc2 = Instant.parse(enddate);
        ZonedDateTime zone1 = ZonedDateTime.ofInstant(nowUtc1, asiaKolkata);
        ZonedDateTime zone2 = ZonedDateTime.ofInstant(nowUtc2, asiaKolkata);
        LocalDate stdt = zone1.toLocalDate();
        LocalDate eddt = zone2.toLocalDate();
        if (active.equals("ALL")) {
            List<TravelDataHeader> reportGenAuthAllData = travelDataHeaderRepository
                    .reportGenAllData(stdt, eddt);
            if (reportGenAuthAllData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            } else {
                for (TravelDataHeader data : reportGenAuthAllData) {
                    User user = userRepositiry.getById(data.getUserId());
                    Customer cust = customerRepository.getById(data.getCustId());
                    City byId = cityRepository.getById(cust.getLocationId());
                    data.setUsername(user.getName());
                    data.setEmployeeId(user.getEmpCode());
                    data.setCustomerName(cust.getName());
                    data.setCustomerLocation(byId.getCity());
                }
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            }
        } else if (active.equals("A") || active.equals("D")) {
            List<TravelDataHeader> reportGenAuthAllData = travelDataHeaderRepository
                    .reportGenTypeWiseData(stdt, eddt, active);
            if (reportGenAuthAllData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            } else {
                for (TravelDataHeader data : reportGenAuthAllData) {
                    User user = userRepositiry.getById(data.getUserId());
                    Customer cust = customerRepository.getById(data.getCustId());
                    City byId = cityRepository.getById(cust.getLocationId());
                    data.setUsername(user.getName());
                    data.setEmployeeId(user.getEmpCode());
                    data.setCustomerName(cust.getName());
                    data.setCustomerLocation(byId.getCity());
                }
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            }
        } else {
            List<TravelDataHeader> reportGenAuthAllData = travelDataHeaderRepository
                    .reportGenPendingData(stdt, eddt);
            if (reportGenAuthAllData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            } else {
                for (TravelDataHeader data : reportGenAuthAllData) {
                    User user = userRepositiry.getById(data.getUserId());
                    Customer cust = customerRepository.getById(data.getCustId());
                    City byId = cityRepository.getById(cust.getLocationId());
                    data.setUsername(user.getName());
                    data.setEmployeeId(user.getEmpCode());
                    data.setCustomerName(cust.getName());
                    data.setCustomerLocation(byId.getCity());
                }
                return ResponseEntity.status(HttpStatus.OK).body(reportGenAuthAllData);
            }
        }

    }

    @Override
    public ResponseEntity<?> addDocument(MultipartImage file) throws IOException {
        Optional<TravelDataDetails> travelDetails = travelDataDetailsRepository.findById(file.getId());
        UUID uuid = UUID.randomUUID();

        File uploadDir = new File(uploadDirectory);
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
            String uploadFilePath = uploadDirectory + "/" + filename;
            byte[] bytes = multipartFile.getBytes(); 
            Path path = Paths.get(uploadFilePath);  
            Files.write(path, bytes);
            travelDetails.get().setImages(filename);
            travelDataDetailsRepository.save(travelDetails.get());
            return new ResponseEntity<String>("file Saved", HttpStatus.OK);
        }
    }
}
