package com.cnupa.app.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table
@Data
public class Courseware {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String filename;

	private String filetype;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Instant createdDate = Instant.now();

	private Long hit;

	private String filepath;
	private String url;
	private Long unitId;
	private Long courseId;
	
	private Boolean hidden=false;
	
}
