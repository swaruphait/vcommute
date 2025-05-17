package com.tecsoft.vcommute.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsoft.vcommute.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    List<Department> findAllByStatus(boolean status);

    boolean existsByName(String name);
}
