package com.cnupa.app.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cnupa.app.model.Course;
import com.cnupa.app.model.Notice;
import com.cnupa.app.model.enums.NoticeType;

public interface NoticeRepo  extends JpaRepository<Notice, Long> {
	Page<Course>  findAllByCourseIsNull(Pageable pageable, NoticeType valueOf);
	Page<Course>  findAllByCourseIsNullAndType(Pageable pageable, NoticeType valueOf);
	Page<Course>  findAllByCourseId(Pageable pageable, Long id);


}
