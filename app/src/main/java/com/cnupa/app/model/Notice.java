package com.cnupa.app.model;

import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.cnupa.app.model.enums.NoticeType;

import lombok.Data;

@Entity
@Table
@Data
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(cascade=CascadeType.REFRESH)  
    @JoinColumn()
	private Course course;
	@Enumerated(EnumType.STRING)
	private NoticeType type;
	
	private Instant createdDate = Instant.now();
	@Size(max = 256)
	private String heading;
	private String content;
	
	private Boolean pulished;
	private Instant expiryDate;
}
