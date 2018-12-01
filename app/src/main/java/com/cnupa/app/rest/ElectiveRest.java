package com.cnupa.app.rest;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnupa.app.model.Elective;
import com.cnupa.app.model.User;
import com.cnupa.app.model.enums.Role;
import com.cnupa.app.repo.ElectiveRepo;
import com.cnupa.app.security.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Transactional
@RequestMapping("elective")
@Slf4j
public class ElectiveRest {
	
	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private ElectiveRepo electiveRepo;
	
	@GetMapping(value = "list")
	public Mono<Page<Elective>> list(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
		Long uid=jwtUtil.getUserId(token);
		return Mono.just(electiveRepo.findAllByStudentId(pageable, uid));

	}
	
	@PostMapping(value = "create")
	public Mono<Void> create(@RequestBody Elective elective,@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {

		Long uid=jwtUtil.getUserId(token);
		elective.setStudentId(uid);
		electiveRepo.save(elective);
		return Mono.empty();
	}
	@DeleteMapping(value = "delete/{id}")
	public Mono<String> del(@PathVariable Long id) {
		electiveRepo.deleteById(id);

		return Mono.empty();
	}
}
