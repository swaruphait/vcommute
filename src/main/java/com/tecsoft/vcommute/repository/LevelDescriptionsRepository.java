package com.tecsoft.vcommute.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tecsoft.vcommute.model.LevelDescriptions;

public interface LevelDescriptionsRepository extends JpaRepository<LevelDescriptions, Long> {

    List<LevelDescriptions> findAllByStatus(boolean status);

    // Optional<LevelDescriptions> findByLvlDesId(Long lvlDesId);

    boolean existsByDes(String des);

    @Query(value = "select * FROM level_des where des NOT IN(?1) ORDER BY des ASC", nativeQuery = true)
	List<LevelDescriptions> fetchAreaByParameterWithoutlocation(String levelDes);

}
