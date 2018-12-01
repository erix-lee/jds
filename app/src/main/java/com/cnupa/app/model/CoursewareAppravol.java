package com.cnupa.app.model;

import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table
@Data

	
public class CoursewareAppravol {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)  
    @JoinColumn()
	private Courseware courseware;

	@ManyToOne(cascade=CascadeType.REFRESH)  
    @JoinColumn()
	private User user;
	
	private Long srcCourseId;
	private Long srcUnitId;

	private Long tgtCourseId;
	private Long tftUnitId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Instant submitDate = Instant.now();
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Instant acceptDate = Instant.now();
	private Boolean acceptance;
}
