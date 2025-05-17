package com.tecsoft.vcommute.serviceimpl.mobile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.City;
import com.tecsoft.vcommute.model.LevelDescriptions;
import com.tecsoft.vcommute.model.Mode;
import com.tecsoft.vcommute.repository.CityRepository;
import com.tecsoft.vcommute.repository.LevelDescriptionsRepository;
import com.tecsoft.vcommute.repository.ModeRepository;
import com.tecsoft.vcommute.response.ResponseHandler;
import com.tecsoft.vcommute.service.mobile.TransportMobileService;

@Service
public class TransportMobileServiceImpl implements TransportMobileService{
    
    @Autowired
    private ModeRepository modeRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private LevelDescriptionsRepository levelDescriptionsRepository;

    @Override
    public ResponseEntity<Object> fetchAllCity() {

        List<City> fetchAllCity = cityRepository.findAll().stream().collect(Collectors.toList());
        Optional<List<City>> fetchcity = Optional.ofNullable(fetchAllCity);
        if (fetchcity.get().isEmpty()) {
            return ResponseHandler.generateResponse("City is Not Found!", HttpStatus.NOT_FOUND, fetchAllCity);
        } else {
            return ResponseHandler.generateResponse("Fetching all City!", HttpStatus.OK, fetchAllCity);
        }
    }

    @Override
    public ResponseEntity<Object> fetchTransportPlain(Integer grade_id, Integer location_id) {
        List<Mode> fetchTransportPlainList = modeRepository.fetchModeDataPlainByParameter(grade_id, location_id)
                .stream().collect(Collectors.toList());
        for (Mode mode : fetchTransportPlainList) {
            Optional<LevelDescriptions> findByLvlDesId = levelDescriptionsRepository.findById(mode.getLevelId());
            mode.setName(findByLvlDesId.get().getDes());
        }
        Optional<List<Mode>> fetchTransPlainList = Optional.ofNullable(fetchTransportPlainList);
        if (fetchTransPlainList.get().isEmpty()) {
            return ResponseHandler.generateResponse("Data Not Found!", HttpStatus.NOT_FOUND, fetchTransportPlainList);
        } else {
            return ResponseHandler.generateResponse("Fetching all TransportPlainList!", HttpStatus.OK,
                    fetchTransportPlainList);
        }
    }

    @Override
    public ResponseEntity<Object> fetchAllAreaTypeTransport(Integer grade_id, Integer location_id) {
        List<Mode> fetchTransportHillList = modeRepository.fetchAreaByParameter(grade_id, location_id)
                .stream().collect(Collectors.toList());
        for (Mode mode : fetchTransportHillList) {
            Optional<LevelDescriptions> findByLvlDesId = levelDescriptionsRepository.findById(mode.getLevelId());
            mode.setName(findByLvlDesId.get().getDes());
        }
        Optional<List<Mode>> fetchTransHillList = Optional.ofNullable(fetchTransportHillList);
        if (fetchTransHillList.get().isEmpty()) {
            return ResponseHandler.generateResponse("Data Not Found!", HttpStatus.NOT_FOUND, fetchTransHillList);
        } else {
            return ResponseHandler.generateResponse("Fetching all TransportPlainList!", HttpStatus.OK,
                    fetchTransHillList);
        }
    }

    @Override
    public ResponseEntity<Object> fetchAllAreaTypeTransportWithoutLocation() {
        String ts = "ATTENDANCE";
        List<LevelDescriptions> fetchTransportHillList = levelDescriptionsRepository
                .fetchAreaByParameterWithoutlocation(ts);
        if (fetchTransportHillList.isEmpty()) {
            return ResponseHandler.generateResponse("Data Not Found!", HttpStatus.NOT_FOUND, fetchTransportHillList);
        } else {
            return ResponseHandler.generateResponse("Fetching all TransportPlainList!", HttpStatus.OK,
                    fetchTransportHillList);
        }
    }
}
