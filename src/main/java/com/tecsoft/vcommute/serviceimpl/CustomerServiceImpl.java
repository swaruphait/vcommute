package com.tecsoft.vcommute.serviceimpl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.dto.CustomerLocationDto;
import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.model.MyUserDetails;
import com.tecsoft.vcommute.repository.CityRepository;
import com.tecsoft.vcommute.repository.CustomerRepository;
import com.tecsoft.vcommute.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public ResponseEntity<?> fetchCustomer() {
        return ResponseEntity.status(HttpStatus.OK).body(customerRepository.findAllByEnabled(true));
    }

    @Override
    public ResponseEntity<?> addCustomer(Customer customer) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer companyId = ((MyUserDetails) principal).CompanyId();
        if (customerRepository.existsByNameAndLocationIdAndLatitudeAndLongitude(customer.getName(),
                customer.getLocationId(), customer.getLatitude(), customer.getLongitude())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Same Customer name already present");
        } else {
            customer.setCity(cityRepository.findById(customer.getLocationId()).get().getCity());
            customer.setEnabled(true);
            customer.setApproved(true);
            customer.setCompanyId(companyId);
            customerRepository.save(customer);
            return ResponseEntity.status(HttpStatus.OK).body("Suucessfully Saved");

        }
    }

    @Override
    public ResponseEntity<?> editcustomer(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerRepository.findById(id));
    }

    @Override
    public ResponseEntity<?> deletecustomer(Long id) {
        Optional<Customer> byId = customerRepository.findById(id);
        byId.get().setEnabled(false);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted");
    }

    @Override
    public ResponseEntity<?> getLatLongCutomer(String address) {
        String apiKey = "AIzaSyB19cHYVEOccMGeKOoRzgrVyDtECwWSL5I";
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + encodedAddress + "&key=" + apiKey;

            HttpGet request = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            JSONObject jsonResponse = new JSONObject(responseBody);

            if (jsonResponse.has("results")) {
                JSONArray results = jsonResponse.getJSONArray("results");

                if (results.length() > 0) {
                    JSONObject result = results.getJSONObject(0);
                    JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
                    double latitude = location.getDouble("lat");
                    double longitude = location.getDouble("lng");
                    String fullAddress = result.getString("formatted_address");
                    JSONArray addressComponents = result.getJSONArray("address_components");
                    String postalCode = "";
                    String longName = "";
                    for (int i = 0; i < addressComponents.length(); i++) {
                        JSONObject component = addressComponents.getJSONObject(i);
                        JSONArray types = component.getJSONArray("types");

                        for (int j = 0; j < types.length(); j++) {
                            String type = types.getString(j);

                            if (type.equals("postal_code")) {
                                postalCode = component.getString("long_name");
                            } else if (type.equals("neighborhood") || type.equals("sublocality")
                                    || type.equals("locality")) {
                                longName = component.getString("long_name");
                            }
                        }
                    }
                    CustomerLocationDto coords = new CustomerLocationDto();
                    coords.setAddress(fullAddress);
                    coords.setLatitude(latitude);
                    coords.setLongitude(longitude);
                    coords.setPincode(postalCode);
                    coords.setSortArea(longName);
                    return new ResponseEntity<>(coords, HttpStatus.OK);
                } else {
                    System.out.println("No results found for the address.");
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @Override
    public ResponseEntity<?> fetchCustomerByName(String name) {
        String input= "%"+name+"%";
        System.out.println("input: "+input);
       List<Customer> fetchCustomerList = customerRepository.fetchCustomerList(input);
       return ResponseEntity.status(HttpStatus.OK).body(fetchCustomerList);
    }

}
