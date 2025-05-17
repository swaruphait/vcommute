package com.tecsoft.vcommute.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tecsoft.vcommute.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByEnabled(Boolean enabled, Pageable pageable);

    List<Customer> findAllByEnabled(Boolean enabled);

    boolean existsByNameAndLocationIdAndBranchArea(String name, Long locationId, String branchArea);

    boolean existsByNameAndLocationIdAndLatitudeAndLongitude(String name, Long locationId, Double latitude,
            Double longitude);

    @Query(value = "SELECT * FROM mst_customer WHERE name LIKE ?1  AND enabled IS TRUE", nativeQuery = true)
    List<Customer> fetchCustomerList(@Param("name") String name);

    @Query(value = "SELECT a.id, CASE WHEN a.branch_area IS NULL OR TRIM(a.branch_area) = '' THEN CONCAT(a.name, ' (', b.city, ')') " + 
       "ELSE CONCAT(a.name, ' (', a.branch_area, ')') END AS name, a.type, a.location_id AS locationId, a.enabled, a.company_id AS companyId, a.created_by, a.created_date," +
       "a.last_modified_by,a.branch_area,a.company_id,a.location_id, a.user_id, a.last_modified_date, a.approved, a.latitude, a.longitude, a.radius, a.branch_area AS branchArea, a.user_id AS userId, b.city AS city " + 
       " FROM mst_customer a LEFT JOIN mst_city b ON a.location_id = b.id WHERE a.enabled = true " +
       "ORDER BY name ASC", nativeQuery = true)
    List<Customer> findCustomerWithLoc();
}
