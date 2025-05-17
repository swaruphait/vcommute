package com.tecsoft.vcommute.serviceimpl.mobile;

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

import com.tecsoft.vcommute.dto.TravelDataDto;
import com.tecsoft.vcommute.model.City;
import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.model.LevelDescriptions;
import com.tecsoft.vcommute.model.TrackingData;
import com.tecsoft.vcommute.model.TravelDataDetails;
import com.tecsoft.vcommute.model.TravelDataHeader;
import com.tecsoft.vcommute.model.User;
import com.tecsoft.vcommute.repository.CityRepository;
import com.tecsoft.vcommute.repository.CustomerRepository;
import com.tecsoft.vcommute.repository.LevelDescriptionsRepository;
import com.tecsoft.vcommute.repository.TrackingDataRepository;
import com.tecsoft.vcommute.repository.TravelDataDetailsRepository;
import com.tecsoft.vcommute.repository.TravelDataHeaderRepository;
import com.tecsoft.vcommute.repository.UserRepositiry;
import com.tecsoft.vcommute.response.ResponseHandler;
import com.tecsoft.vcommute.service.mobile.TravelMobileService;

@Service
public class TravelMobileServiceImpl implements TravelMobileService{
  @Autowired
	private TravelDataHeaderRepository travelDataHeaderRepository;

	@Autowired
	private TravelDataDetailsRepository travelDataDetailsRepository;

    @Autowired
	private UserRepositiry userRepository;

    @Autowired
	private CustomerRepository customerRepository;

    @Autowired
	private LevelDescriptionsRepository levelDescriptionsRepository;

    @Autowired
	private CityRepository cityRepository;

	@Autowired
	private TrackingDataRepository trackingDataRepository;

    @Override
    public ResponseEntity<Object> addStartTravelData(TravelDataDto travelData) {
       	try {
			Optional<User> users = userRepository.findById(travelData.getUserId());
			Optional<LevelDescriptions> isRequired = levelDescriptionsRepository
					.findById(travelData.getModeId().longValue());
			if (travelData.getUserId() != null && travelData.getUserId() != 0 && users.isPresent()
					&& isRequired.isPresent()) {
				TravelDataHeader header = new TravelDataHeader();
				TravelDataDetails details = new TravelDataDetails();
				header.setUserId(travelData.getUserId());
				header.setStatus(false);
				header.setStartDate(LocalDate.now());
				header.setStartTime(LocalTime.now());
				header.setStartLocation(travelData.getStartLocation());
				header.setStartLocationArea(travelData.getStartLocationArea());
				header.setStartLat(travelData.getStartLat());
				header.setStartLong(travelData.getStartLong());
				header.setTotalEstimatePrice(0.0);
				header.setTotalDistance(0.0);
				header.setApprovalLevel(null);
				header.setMultiCommute(travelData.isMultiCommute());
				Optional<City> city = cityRepository.findByCityName(travelData.getCityName());
				if (city.isPresent()) {
					details.setLocationId(city.get().getId().intValue());
				} else {
					details.setLocationId(0);
				}
				details.setModeId(travelData.getModeId());
				details.setStartDate(LocalDate.now());
				details.setStartTime(LocalTime.now());
				details.setStartLat(travelData.getStartLat());
				details.setStartLong(travelData.getStartLong());
				details.setStartLocation(travelData.getStartLocation());
				details.setStartLocationArea(travelData.getStartLocationArea());
				details.setPriceRequired(isRequired.get().getPrice());
				details.setDocumentRequired(isRequired.get().getDocument());
				details.setModeName(isRequired.get().getDes());
				details.setCompanyId(users.get().getCompanyId());
				details.setStatus(false);
				details.setTravelDataHeader(header);
				header = travelDataHeaderRepository.save(header); 
				details = travelDataDetailsRepository.save(details); 
				return ResponseHandler.generateResponse("Start Travel Data saved successfully!", HttpStatus.OK,
						details);
			} else {
				return ResponseHandler.generateResponse("Invalid User ID or Mode ID. Start Travel Data not saved.",
						HttpStatus.BAD_REQUEST, travelData);
			}
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Something Went Wrong!!", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
    }

    @Override
    public ResponseEntity<Object> intervalTravelDataStart(Long id, TravelDataDto travelDataDto) {
        try {
			Optional<TravelDataHeader> header = travelDataHeaderRepository.findById(id);
			if (header.isEmpty()) {
				return ResponseHandler.generateResponse("Header not found", HttpStatus.NOT_FOUND, null);
			}
			TravelDataDetails details = new TravelDataDetails();
			Optional<User> users = userRepository.findById(header.get().getUserId());
			Optional<LevelDescriptions> isRequired = levelDescriptionsRepository
					.findById(travelDataDto.getModeId().longValue());
			Optional<City> city = cityRepository.findByCityName(travelDataDto.getCityName());

			if (city.isPresent()) {
				details.setLocationId(city.get().getId().intValue());
			} else {
				details.setLocationId(0);
			}
			details.setStartDate(LocalDate.now());
			details.setStartTime(LocalTime.now());
			details.setStartLat(travelDataDto.getStartLat());
			details.setStartLong(travelDataDto.getStartLong());
			details.setStartLocation(travelDataDto.getStartLocation());
			details.setStartLocationArea(travelDataDto.getStartLocationArea());
			details.setModeId(travelDataDto.getModeId());
			details.setPriceRequired(isRequired.get().getPrice());
			details.setDocumentRequired(isRequired.get().getDocument());
			details.setModeName(isRequired.get().getDes());
			details.setCompanyId(users.get().getCompanyId());
			details.setStatus(false);
			details.setTravelDataHeader(header.get());
			details = travelDataDetailsRepository.save(details);
			return ResponseHandler.generateResponse("Start Travel Data saved successfully!", HttpStatus.OK, details);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Something Went Wrong!!", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
    }

    @Override
    public ResponseEntity<Object> intervalTravelDataStop(TravelDataDto travelData) {
      Optional<TravelDataDetails> findDetails = travelDataDetailsRepository.findById(travelData.getDeatilsId());
		if (findDetails.isEmpty()) {
			return ResponseHandler.generateResponse("Details not found", HttpStatus.NOT_FOUND, null);
		}
		TravelDataDetails details = findDetails.get();
		Optional<TravelDataHeader> header = travelDataHeaderRepository.findById(travelData.getHeaderId());
		if (header.isEmpty()) {
			return ResponseHandler.generateResponse("Header not found", HttpStatus.NOT_FOUND, null);
		}
		Double totalPrice = header.get().getTotalActualPrice() == null ? 0.0 : header.get().getTotalActualPrice();
		UUID uuid = UUID.randomUUID();
		if (!details.isStatus()) {
			details.setEndDate(LocalDate.now());
			details.setEndTime(LocalTime.now());
			details.setEndLat(travelData.getEndLat());
			details.setEndLong(travelData.getEndLong());
			details.setEndLocation(travelData.getEndLocation());
			details.setEndLocationArea(travelData.getEndLocationArea());
			details.setStatus(true);
			details.getTravelDataHeader().setTotalEstimatePrice(0.0);
			details.getTravelDataHeader().setTotalDistance(0.0);
			details.getTravelDataHeader().setTotalTime("0.0");
			details.setActualPrice(travelData.getActualPrice());
			header.get().setTotalActualPrice(totalPrice + travelData.getActualPrice());
			header.get().setCustId(travelData.getCustId());
			header.get().setPurpose(travelData.getPurpose());
			header.get().setReferencenumber(travelData.getReferencenumber());
			header.get().setIntervelStop(header.get().isIntervelStop());
			header.get().setMultiCommute(header.get().isMultiCommute());
			header.get().setNote(travelData.getNote());
			if (travelData.getImages() != null) {
				String decomeName = uuid + ".jpg";
				try {
					Base64Encoder.decodeBase64ToImage(travelData.getImages(), decomeName);
				} catch (IOException e) {
					e.printStackTrace();
				}
				details.setImages(decomeName);
			}
			header.get().setIntervelStop(travelData.isIntervelStop());
			travelDataHeaderRepository.save(header.get());
			details = travelDataDetailsRepository.save(details);
			return ResponseHandler.generateResponse("Start Travel Data saved successfully!", HttpStatus.OK, details);
		} else {
			return ResponseHandler.generateResponse("Something Went Wrong!!", HttpStatus.INTERNAL_SERVER_ERROR,
					travelData);
		}
    }

    @Override
    public ResponseEntity<Object> addStopTravelData(TravelDataDto travelDataDto) {
        try {
			Optional<TravelDataHeader> findById = travelDataHeaderRepository.findById(travelDataDto.getHeaderId());
			Optional<TravelDataDetails> findDetails = travelDataDetailsRepository
					.findById(travelDataDto.getDeatilsId());
			if (findById.isEmpty() || findDetails.isEmpty()) {
				return ResponseHandler.generateResponse("Header or Details not found", HttpStatus.NOT_FOUND, null);
			}
			TravelDataHeader header = findById.get();
			TravelDataDetails details = findDetails.get();
			Double totalPrice = header.getTotalActualPrice() == null ? 0.0 : header.getTotalActualPrice();
			UUID uuid = UUID.randomUUID();
			if (travelDataDto.getCustId() != null && travelDataDto.getCustId() != 0 && !header.isStatus()) {
				details.setEndDate(LocalDate.now());
				details.setEndTime(LocalTime.now());
				details.setEndLat(travelDataDto.getEndLat());
				details.setEndLong(travelDataDto.getEndLong());
				details.setEndLocation(travelDataDto.getEndLocation());
				details.setEndLocationArea(travelDataDto.getEndLocationArea());
				details.setStatus(true);
				if (details.isPriceRequired()) {
					details.setActualPrice(travelDataDto.getActualPrice());
					header.setTotalActualPrice(totalPrice + travelDataDto.getActualPrice());
				} else {
					details.setActualPrice(0.0);
				}
				if (travelDataDto.getImages() != null) {
					String decomeName = uuid + ".jpg";
					Base64Encoder.decodeBase64ToImage(travelDataDto.getImages(), decomeName);
					details.setImages(decomeName);
				}
				header.setEndDate(LocalDate.now());
				header.setEndTime(LocalTime.now());
				header.setEndLocation(travelDataDto.getEndLocation());
				header.setEndLocationArea(travelDataDto.getEndLocationArea());
				header.setEndLong(travelDataDto.getEndLong());
				header.setEndLat(travelDataDto.getEndLat());
				header.setStatus(true);
				header.setCustId(travelDataDto.getCustId());
				header.setApprovalLevel("A1");
				header.setTotalActualPrice(totalPrice + travelDataDto.getActualPrice());
				header.setTotalTime("0.0");
				header.setTotalDistance(0.0);
				header.setReferencenumber(travelDataDto.getReferencenumber());
				header.setPurpose(travelDataDto.getPurpose());
				header.setNote(travelDataDto.getNote());
				travelDataHeaderRepository.save(header);
				details = travelDataDetailsRepository.save(details);
				return ResponseHandler.generateResponse("Start Travel Data Saved!", HttpStatus.OK, details);
			} else {
				return ResponseHandler.generateResponse("Start Travel Data not Saved!",
						HttpStatus.INTERNAL_SERVER_ERROR, travelDataDto);
			}

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Something Went Wrong!!", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
    }

    @Override
    public ResponseEntity<Object> fetchTravelData(Long id) {
      List<TravelDataHeader> headers = travelDataHeaderRepository.findByUserId(id);
		if (headers.isEmpty()) {
			return ResponseHandler.generateResponse("Start Travel Data not found for this user!",
					HttpStatus.INTERNAL_SERVER_ERROR, headers);
		} else {
			for (TravelDataHeader td : headers) {
				if (td.getCustId() != null) {
					Optional<Customer> findById = customerRepository.findById(td.getCustId());
					td.setCustomerName(findById.get().getName());
				}
                Optional<User> byId = userRepository.findById(td.getUserId());
                if (byId.isPresent()) {
                    td.setUsername(byId.get().getName());
                }	
			}
			return ResponseHandler.generateResponse("Start Travel Data found !", HttpStatus.OK, headers);
		}
    }

	@Override
	public ResponseEntity<Object> addTracking(TrackingData trackingData) {
		trackingDataRepository.save(trackingData);
		return ResponseHandler.generateResponse("Start Travel Data found.", HttpStatus.OK, null);
	}  
}
