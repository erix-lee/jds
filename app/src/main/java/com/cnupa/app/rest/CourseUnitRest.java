package com.cnupa.app.rest;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnupa.app.model.Course;
import com.cnupa.app.model.CourseUnit;
import com.cnupa.app.repo.CourseRepo;
import com.cnupa.app.repo.CourseUnitRepo;
import com.cnupa.app.security.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@RestController
@Transactional
@RequestMapping("courseunit")
@Slf4j
public class CourseUnitRest {
	

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private CourseUnitRepo courseUnitRepo;
	@PostMapping(value = "{courseId}/create")
	public Mono<Void> create(@RequestBody CourseUnit entity,@PathVariable Long courseId) {

		courseUnitRepo.save(entity);
		Course c=courseRepo.getOne(courseId);
		c.getUnits().add(entity);
		courseRepo.save(c);
		return Mono.empty();

	}

	@PutMapping(value = "update")
	public Mono<ResponseEntity<String>> update(@RequestBody CourseUnit entity) {
		courseUnitRepo.save(entity);

		return Mono.empty();

	}

	@DeleteMapping(value = "delete/{id}")
	public Mono<String> del(@PathVariable Long id) {
		courseUnitRepo.deleteById(id);

		return Mono.empty();
	}

}
