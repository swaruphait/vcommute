package com.tecsoft.vcommute.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsoft.vcommute.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    boolean existsByNameAndLevelAndStatus(String name, Integer level, boolean status);

    List<Grade> findAllByStatus(boolean status);

}
