package com.cnupa.app.rest;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnupa.app.model.User;
import com.cnupa.app.model.enums.Role;
import com.cnupa.app.repo.UserRepo;
import com.cnupa.app.security.PBKDF2Encoder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Transactional
@RequestMapping("user")
@Slf4j
public class UserRest {


	@Autowired
	private PBKDF2Encoder passwordEncoder;
	@Autowired
	private UserRepo userRepo;

	@PostMapping(value = "create")
	public Mono<Void> create(@RequestBody User user,
			@RequestParam(value = "role",  required = true) String role) {
		log.debug(user.getUsername());
		user.setRole(Role.valueOf(role));
		if(user.getRole().equals(Role.STUDENT)) {
			user.setAccountNonLocked(false);
		}
		user.setPassword(passwordEncoder.encode("888888"));
		userRepo.save(user);
		return Mono.empty();
	}

	@PutMapping(value = "update")
	public Mono<ResponseEntity<String>> update(User user) {
		userRepo.save(user);

		return Mono.empty();

	}

//	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@DeleteMapping(value = "delete")
	public Mono<String> del(Long id, @RequestHeader(value = "x-token") String token) {
		userRepo.deleteById(id);

		return Mono.empty();
	}

	@GetMapping(value = "{id}")
	public Mono<User> get(@PathVariable("id") Long id) {
		return Mono.just(userRepo.findById(id).get());

	}

	@GetMapping(value = "list")
	public Mono<Page<User>> list(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "1000", required = false) Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "fullname");

		return Mono.just(userRepo.findAll(pageable));

	}
	@GetMapping(value = "teachers")
	public Mono<Page<User>> listTeacher(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "1000", required = false) Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "fullname");


		return Mono.just(userRepo.findByRole(pageable,Role.TEACHER));

	}
	@GetMapping(value = "students")
	public Mono<Page<User>> listStudents(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "1000", required = false) Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "fullname");


		return Mono.just(userRepo.findByRole(pageable,Role.STUDENT));

	}
}