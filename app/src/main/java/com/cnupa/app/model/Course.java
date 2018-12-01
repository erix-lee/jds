package com.cnupa.app.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cnupa.app.model.enums.CourseType;

import lombok.Data;

/**
 * 课程
 *
 */
@Entity
@Table

@Data
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String bannerImg;

	private String iconImg;

	private Boolean allowPastView=false;
	
	private Boolean opened=false;
	/**
	 * 访问量
	 */
	private Long pageviews=0L;


	private CourseType type; 

	@ManyToOne(cascade=CascadeType.REFRESH)  
    @JoinColumn(name="chiefId")  
    private User chief;
	
	@OneToMany(cascade=CascadeType.REFRESH)  
	private  List<User> members;
	
	@OneToMany(cascade = CascadeType.REFRESH)
	private List<CourseUnit> units;
	
	@ManyToOne(cascade=CascadeType.REFRESH)  
    @JoinColumn()  
	private Courseware intro;
	@ManyToOne(cascade=CascadeType.REFRESH)  
    @JoinColumn()  
	private Courseware outline;
	@ManyToOne(cascade=CascadeType.REFRESH)  
    @JoinColumn()  
	private Courseware team;
}
