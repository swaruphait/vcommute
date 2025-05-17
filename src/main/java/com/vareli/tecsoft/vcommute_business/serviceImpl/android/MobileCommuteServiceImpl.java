package com.vareli.tecsoft.vcommute_business.serviceImpl.android;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.City;
import com.vareli.tecsoft.vcommute_business.model.CommuteDetails;
import com.vareli.tecsoft.vcommute_business.model.CommuteHeader;
import com.vareli.tecsoft.vcommute_business.model.Transport;
import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.model.dto.CommuteDto;
import com.vareli.tecsoft.vcommute_business.model.dto.FetchCommuteDto;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.CommuteDetailsRepository;
import com.vareli.tecsoft.vcommute_business.repository.CommuteHeaderRepository;
import com.vareli.tecsoft.vcommute_business.repository.TransportRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.android.MobileCommuteService;

@Service
public class MobileCommuteServiceImpl implements MobileCommuteService {

    @Autowired
    private CommuteHeaderRepository commuteHeaderRepository;

    @Autowired
    private CommuteDetailsRepository commuteDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CityRepository cityRepository;

    // @Autowired
    // private ModeRepository modeRepository;

    // private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public ResponseEntity<?> startCommuteData(CommuteDto commuteData) {
        // System.out.println("commuteData: "+commuteData);
        try {
            Optional<User> userData = userRepository.findById(commuteData.getUserId());
            Optional<Transport> transport = transportRepository.findById(commuteData.getModeId());
            if (userData.isPresent()) {
                CommuteHeader header = new CommuteHeader();
                CommuteDetails details = new CommuteDetails();
                header.setUserId(commuteData.getUserId());
                header.setStatus(false);
                header.setStartDate(LocalDate.now());
                header.setStartTime(LocalTime.now());
                header.setStartLocation(commuteData.getStartLocation());
                header.setStartLocationArea(commuteData.getStartLocationArea());
                header.setStartLat(commuteData.getStartLat());
                header.setStartLong(commuteData.getStartLong());
                header.setCompanyId(commuteData.getCompanyId());
                header.setSuperCompanyId(commuteData.getSuperCompanyId());
                header.setIntervelStop(commuteData.isIntervelStop());
                header.setMultiCommute(commuteData.isMultiCommute());
                Optional<City> city = cityRepository.findByCity(commuteData.getCityName());
                if (city.isPresent()) {
                    details.setLocationId(city.get().getId().intValue());
                } else {
                    details.setLocationId(0);
                }
                details.setModeId(commuteData.getModeId());
                details.setStartDate(LocalDate.now());
                details.setStartTime(LocalTime.now());
                details.setStartLat(commuteData.getStartLat());
                details.setStartLong(commuteData.getStartLong());
                details.setStartLocation(commuteData.getStartLocation());
                details.setStartLocationArea(commuteData.getStartLocationArea());
                details.setPriceRequired(transport.get().isPriceRequired());
                details.setDocumentRequired(transport.get().isDocumentRequired());
                details.setModeName(transport.get().getDes());
                details.setCompanyId(userData.get().getCompanyId());
                details.setStatus(false);
                details.setDistance(0);
                details.setCommuteHeader(header);
                header = commuteHeaderRepository.save(header);
                details = commuteDetailsRepository.save(details);
                return ResponseHandler.generateResponse("Start Travel Commute Data!", HttpStatus.OK, details);
            } else {
                return ResponseHandler.generateResponse("something Went Worng!! Please Try Again",
                        HttpStatus.BAD_REQUEST, null);
            }
        } catch (Exception e) {
            return ResponseHandler.generateResponse("something Went Worng!! Please Try Again", HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }


    @Override
    public ResponseEntity<?> stopCommuteData(CommuteDto commuteData) {
        System.out.println("Data: "+commuteData);
        System.out.println("Cust Data: "+commuteData.getCustId());

commuteData.setHeaderId(1L);
        try {
            Optional<CommuteHeader> findById = commuteHeaderRepository.findById(commuteData.getHeaderId());
            Optional<CommuteDetails> findDetails = commuteDetailsRepository
                    .findById(commuteData.getDeatilsId());
            if (findById.isEmpty() || findDetails.isEmpty()) {
                return ResponseHandler.generateResponse("Header or Details not found!..", HttpStatus.NOT_FOUND, null);
            }
            CommuteHeader header = findById.get();
            CommuteDetails details = findDetails.get();
            Double totalPrice = header.getTotalActualPrice();
            System.out.println("totalPrice"+totalPrice);
            UUID uuid = UUID.randomUUID();
            if (commuteData.getCustId() != null && commuteData.getCustId() != 0) {
                details.setEndDate(LocalDate.now());
                details.setEndTime(LocalTime.now());
                details.setEndLat(commuteData.getEndLat());
                details.setEndLong(commuteData.getEndLong());
                details.setEndLocation(commuteData.getEndLocation());
                if (commuteData.getLocationId() != null) {
                    details.setLocationId(commuteData.getLocationId());
                } else {
                    details.setLocationId(0);
                }
                // Optional<User> userDetails = userRepository.findById(findById.get().getUserId());
                details.setEndLocationArea(commuteData.getEndLocationArea());
                // double calculateDistance = calculateDistance(details.getStartLat(), details.getStartLong(),
                //         details.getEndLat(), details.getEndLong());
                // details.setDistance(0);
                // long calculateMinutesBetween = calculateMinutesBetween(details.getStartDate(), details.getStartTime(),
                //         details.getEndDate(), details.getEndTime());
                // details.setTime(0);
                // Optional<Mode> modeDetails = modeRepository
                //         .findByLevelIdAndLocationIdAndSuperCompanyIdAndCompanyId(details.getModeId(),
                //                 details.getLocationId(), userDetails.get().getSuperCompanyId(),
                //                 userDetails.get().getCompanyId());

                // if (modeDetails.isPresent()) {
                //     Mode mode = modeDetails.get();
                //     if (details.getDistance() <= mode.getBaseKm()) {
                //         details.setEstimatePrice(mode.getBasePrice());
                //     } else {
                //         double finalPrice = mode.getBasePrice()
                //                 + ((details.getDistance() - mode.getBaseKm()) * mode.getPricePerKm());
                //         details.setEstimatePrice(finalPrice);
                //     }
                // }

                details.setStatus(true);
                // if (details.isPriceRequired()) {
                //     details.setActualPrice(commuteData.getActualPrice());
                //     header.setTotalActualPrice(totalPrice + commuteData.getActualPrice());
                // } else {
                //     details.setActualPrice(0.0);
                // }
                if (commuteData.getImages() != null) {
                    String decomeName = uuid + ".jpg";
                    Base64Encoder.decodeBase64ToImage(commuteData.getImages(), decomeName);
                    details.setImages(decomeName);
                }

                // double totalEstimatePrice = header.getTotalEstimatePrice();
                // double totalDistance = header.getTotalDistance();
                // long totalTime = header.getTotalTime();
                header.setEndDate(LocalDate.now());
                header.setEndTime(LocalTime.now());
                header.setEndLocation(commuteData.getEndLocation());
                header.setEndLocationArea(commuteData.getEndLocationArea());
                header.setEndLong(commuteData.getEndLong());
                header.setEndLat(commuteData.getEndLat());
                header.setStatus(true);
                header.setCustId(commuteData.getCustId());
                header.setApprovalLevel("1");
                header.setTotalActualPrice(totalPrice + commuteData.getActualPrice());
                header.setReferencenumber(commuteData.getReferencenumber());
                header.setPurpose(commuteData.getPurpose());
                header.setNote(commuteData.getNote());
                commuteHeaderRepository.save(header);
                details = commuteDetailsRepository.save(details);
                return ResponseHandler.generateResponse("Start Travel Data Saved...", HttpStatus.OK, details);
            } else {
                return ResponseHandler.generateResponse("Start Travel Data not Saved!...",
                        HttpStatus.INTERNAL_SERVER_ERROR, null);
            }
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Something Went Wrong!...", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Override
    public ResponseEntity<Object> intervalTravelDataStart(Long id, CommuteDto commuteData) {
        try {
            Optional<CommuteHeader> header = commuteHeaderRepository.findById(id);
            if (header.isPresent() && !header.get().isStatus()) {
                CommuteDetails details = new CommuteDetails();
                Optional<Transport> isRequired = transportRepository
                        .findById(commuteData.getModeId());
                Optional<User> users = userRepository.findById(header.get().getUserId());
                Optional<City> city = cityRepository.findByCity(commuteData.getCityName());
                if (city.isPresent()) {
                    details.setLocationId(city.get().getId().intValue());
                } else {
                    details.setLocationId(0);
                }
                details.setStartDate(LocalDate.now());
                details.setStartTime(LocalTime.now());
                details.setStartLat(commuteData.getStartLat());
                details.setStartLong(commuteData.getStartLong());
                details.setStartLocation(commuteData.getStartLocation());
                details.setStartLocationArea(commuteData.getStartLocationArea());
                details.setModeId(commuteData.getModeId());
                details.setPriceRequired(isRequired.get().isPriceRequired());
                details.setDocumentRequired(isRequired.get().isDocumentRequired());
                details.setModeName(isRequired.get().getDes());
                details.setCompanyId(users.get().getCompanyId());
                details.setStatus(false);
                details.setCommuteHeader(header.get());
                details = commuteDetailsRepository.save(details);
                return ResponseHandler.generateResponse("Succesfully Intervel Start...", HttpStatus.OK, details);
            } else {
                return ResponseHandler.generateResponse("Something went wrong! please check your details!...",
                        HttpStatus.BAD_REQUEST, null);
            }
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Error!..", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> intervalTravelDataStop(CommuteDto commuteData) {
        try {
            Optional<CommuteDetails> commuteDetailsOpt = commuteDetailsRepository.findById(commuteData.getDeatilsId());
            Optional<CommuteHeader> headerOpt = commuteHeaderRepository.findById(commuteData.getHeaderId());

            if (commuteDetailsOpt.isPresent() && headerOpt.isPresent()) {
                CommuteDetails details = commuteDetailsOpt.get();
                CommuteHeader header = headerOpt.get();

                if (!details.isStatus() && !header.isStatus()) {
                    Double totalPrice = header.getTotalActualPrice();
                    UUID uuid = UUID.randomUUID();
                    details.setEndDate(LocalDate.now());
                    details.setEndTime(LocalTime.now());
                    details.setEndLat(commuteData.getEndLat());
                    details.setEndLong(commuteData.getEndLong());
                    details.setEndLocation(commuteData.getEndLocation());
                    details.setEndLocationArea(commuteData.getEndLocationArea());
                    details.setStatus(true);
                    details.setLocationId(commuteData.getLocationId() != null ? commuteData.getLocationId() : 0);
                    Optional<User> userOpt = userRepository.findById(header.getUserId());
                    if (!userOpt.isPresent()) {
                        return ResponseHandler.generateResponse("User not found!...", HttpStatus.BAD_REQUEST, null);
                    }
                    // User user = userOpt.get();
                    // double calculateDistance = calculateDistance(
                    //         details.getStartLat(), details.getStartLong(),
                    //         details.getEndLat(), details.getEndLong());
                    details.setDistance(0);
                    // LocalTime endTime = details.getEndTime().truncatedTo(ChronoUnit.SECONDS);
                    // long timeInMinutes = calculateMinutesBetween(
                    //         details.getStartDate(), details.getStartTime(),
                    //         details.getEndDate(), endTime);
                    details.setTime(0);

                    // Optional<Mode> modeOpt = modeRepository.findByLevelIdAndLocationIdAndSuperCompanyIdAndCompanyId(
                    //         details.getModeId(), details.getLocationId(),
                    //         user.getSuperCompanyId(), user.getCompanyId());

                    // if (modeOpt.isPresent()) {
                    //     Mode mode = modeOpt.get();
                    //     double finalPrice = (calculateDistance <= mode.getBaseKm())
                    //             ? mode.getBasePrice()
                    //             : mode.getBasePrice() + ((calculateDistance - mode.getBaseKm()) * mode.getPricePerKm());
                    //     details.setEstimatePrice(finalPrice);
                    // }

                    details.setActualPrice(commuteData.getActualPrice());
                    header.setTotalEstimatePrice(0.0);
                    header.setTotalDistance(0.0);
                    header.setTotalTime(0);

                    header.setTotalActualPrice(totalPrice + commuteData.getActualPrice());
                    header.setCustId(commuteData.getCustId());
                    header.setPurpose(commuteData.getPurpose());
                    header.setReferencenumber(commuteData.getReferencenumber());
                    header.setNote(commuteData.getNote());
                    header.setIntervelStop(commuteData.isIntervelStop());
                    header.setMultiCommute(commuteData.isMultiCommute());
                    if (commuteData.getImages() != null) {
                        String imageName = uuid + ".jpg";
                        try {
                            Base64Encoder.decodeBase64ToImage(commuteData.getImages(), imageName);
                            details.setImages(imageName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    commuteHeaderRepository.save(header);
                    CommuteDetails savedDetails = commuteDetailsRepository.save(details);
                    return ResponseHandler.generateResponse("Successfully Interval Stop!...", HttpStatus.OK, savedDetails);
                } else {
                    return ResponseHandler.generateResponse("Invalid operation: already completed or stopped...",
                            HttpStatus.BAD_REQUEST, null);
                }
            } else {
                return ResponseHandler.generateResponse("Commute Details or Header not found!...",
                        HttpStatus.NOT_FOUND, null);
            }
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Internal Server Error...", HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> fetchCommuteData(Long userId) {
        List<FetchCommuteDto> commuteDatas = commuteHeaderRepository
                .fetchCommuteDataMobile(userId);
        if (commuteDatas.isEmpty()) {
            return ResponseHandler.generateResponse("Data not found for this user!...",
                    HttpStatus.INTERNAL_SERVER_ERROR, null);
        } else {
            return ResponseHandler.generateResponse("Start Travel Data found!...", HttpStatus.OK, commuteDatas);

        }

    }

    // public static double calculateDistance(double startLat, double startLong,
    //         double endLat, double endLong) {
    //     double dLat = Math.toRadians(endLat - startLat);
    //     double dLon = Math.toRadians(endLong - startLong);
    //     double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
    //             + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
    //                     * Math.sin(dLon / 2) * Math.sin(dLon / 2);

    //     double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    //     return EARTH_RADIUS_KM * c;
    // }

    // public static long calculateMinutesBetween(LocalDate startDate, LocalTime startTime,
    //         LocalDate endDate, LocalTime endTime) {
    //     String startDateTimeStr = startDate + " " + startTime;
    //     String endDateTimeStr = endDate + " " + endTime;
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //     LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr, formatter);
    //     LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeStr, formatter);
    //     Duration duration = Duration.between(startDateTime, endDateTime);
    //     return duration.toMinutes();
    // }
}
