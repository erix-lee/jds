package com.cnupa.app.rest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
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

import com.cnupa.app.model.ClassGrade;
import com.cnupa.app.model.User;
import com.cnupa.app.repo.ClassGradeRepo;
import com.cnupa.app.repo.UserRepo;
import com.cnupa.app.security.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Transactional
@RequestMapping("class-grade")
@Slf4j
public class ClassGradeRest {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private ClassGradeRepo classGradeRepo;
	@Autowired
	private UserRepo userRepo;

	/**
	 * 创建课程
	 * 
	 * @param course
	 * @param token
	 * @return
	 */
	@PostMapping(value = "create")
	public Mono<Void> createPublic(@RequestBody ClassGrade classGrade) {

		classGradeRepo.save(classGrade);
		return Mono.empty();

	}

	@PutMapping(value = "unappoint/{id}")
	public Mono<ResponseEntity<String>> unappoint(@PathVariable Long id,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
		Long uid = jwtUtil.getUserId(token);

		ClassGrade clazz = classGradeRepo.getOne(id);
		if (clazz.getAdviser().getId().equals(uid)) {
			clazz.setAdviser(null);
			classGradeRepo.save(clazz);

		} else {
			// return Mono.error(new RuntimeException("!!!!"));

		}

		return Mono.empty();

	}

	@PutMapping(value = "appoint/{id}")
	public Mono<ResponseEntity<String>> appoint(@PathVariable Long id,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
		Long uid = jwtUtil.getUserId(token);
		User u = userRepo.getOne(uid);

		ClassGrade clazz = classGradeRepo.getOne(id);
		clazz.setAdviser(u);
		classGradeRepo.save(clazz);
		return Mono.empty();

	}

//	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@DeleteMapping(value = "delete/{id}")
	public Mono<String> del(@PathVariable Long id) {
		classGradeRepo.deleteById(id);

		return Mono.empty();
	}

	@GetMapping(value = "{id}")
	public Mono<ClassGrade> get(@PathVariable("id") Long id) {
		System.out.println("get");
		return Mono.just(classGradeRepo.findById(id).get());

	}

	@GetMapping(value = "list")
	public Mono<Page<ClassGrade>> list(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

		return Mono.just(classGradeRepo.findAll(pageable));

	}
	@GetMapping(value = "list/{adviserId}")
	public Mono<Page<ClassGrade>> listByAdviserId(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
			@PathVariable Long  adviserId) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

		return Mono.just(classGradeRepo.findAllByAdviserId(pageable, adviserId));
	}
	@GetMapping(value = "listcurrent")
	public Mono<Page<ClassGrade>> listcurrent(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
		Long uid = jwtUtil.getUserId(token);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

		return Mono.just(classGradeRepo.findAllByAdviserId(pageable, uid));

	}

	@GetMapping(value = "listgroup")
	public Mono<Map<User, List<ClassGrade>>> listgroup( ) {

		Map<User, List<ClassGrade>> groupBy = classGradeRepo.findAllByAdviserNotNull().stream()
				.collect(Collectors.groupingBy(ClassGrade::getAdviser));

		return Mono.just(groupBy);

	}

}
