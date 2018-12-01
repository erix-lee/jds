package com.cnupa.app.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cnupa.app.model.ClassGrade;
import com.cnupa.app.model.Course;

public interface ClassGradeRepo extends JpaRepository<ClassGrade, Long> {

	Page<ClassGrade> findAllByAdviserId(Pageable pageable, Long uid);
	List<ClassGrade> findAllByAdviserNotNull();
	@Query("select c from ClassGrade c group by c.adviser")
	Page<?> findClassGradeGroup(Pageable pageable);
}
