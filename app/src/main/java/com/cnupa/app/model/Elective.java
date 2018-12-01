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

import lombok.Data;

@Entity
@Table
@Data
public class Elective {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long studentId;
	
	@ManyToOne(cascade=CascadeType.REFRESH)  
    @JoinColumn()
	private ClassGrade classGrade;
	
	@ManyToOne(cascade=CascadeType.REFRESH)  
    @JoinColumn()
	private Course course;
}
