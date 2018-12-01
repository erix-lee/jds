package com.cnupa.app.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cnupa.app.model.Courseware;

public interface CoursewareRepo extends JpaRepository<Courseware, Long>{

	Page<Courseware> findAllByUnitId(Pageable pageable, Long unitId);

}
