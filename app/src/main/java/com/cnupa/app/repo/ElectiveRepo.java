package com.cnupa.app.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cnupa.app.model.Elective;

public interface ElectiveRepo extends JpaRepository<Elective, Long>{

	Page<Elective> findAllByStudentId(Pageable pageable, Long studentId);
}
