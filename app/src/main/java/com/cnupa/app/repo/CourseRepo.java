package com.cnupa.app.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cnupa.app.model.Course;
import com.cnupa.app.model.enums.CourseType;

public interface CourseRepo extends JpaRepository<Course, Long> {

	Page<Course>  findAllByTypeAndChiefId(Pageable pageable, CourseType type, Long userId);
//	Page<Course> findAllByTypeAndOwner(Pageable pageable, Type type,String owner);
	
	Page<Course>  findAllByTypeAndMembersId(Pageable pageable, CourseType type, Long userId);

	Page<Course>  findAllByType(Pageable pageable, CourseType valueOf);

	Page<Course>  findAllByTypeAndOpenedTrue(Pageable pageable, CourseType team);
}
