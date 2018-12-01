package com.cnupa.app.rest;

import java.util.List;

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

import com.cnupa.app.model.Course;
import com.cnupa.app.model.TRS;
import com.cnupa.app.repo.TRSRepo;
import com.cnupa.app.security.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Transactional
@RequestMapping("trs")
@Slf4j
public class TRSRest {
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private TRSRepo trsRepo;

	@PostMapping(value = "create")
	public Mono<Void> create(@RequestBody TRS trs) {

		trsRepo.save(trs);
		return Mono.empty();

	}

	@PutMapping(value = "update")
	public Mono<ResponseEntity<String>> update(TRS trs) {
		trsRepo.save(trs);

		return Mono.empty();

	}

	// @PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "delete/{id}")
	public Mono<String> del(@PathVariable Long id) {
		trsRepo.deleteById(id);

		return Mono.empty();
	}

	@GetMapping(value = "current")
	public Mono<Page<TRS>> listByCurrent(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
		Long  userId = jwtUtil.getUserId(token);
		System.out.println("list trs userId=" +userId);
		return Mono.just(trsRepo.findByChiefId(pageable, userId));

	}
	
	@GetMapping(value = "{id}")
	public Mono<TRS> get(@PathVariable("id") Long id) {
		System.out.println("get");
		return Mono.just(trsRepo.findById(id).get());

	}
 
	@GetMapping(value = "list")
	public Mono<Page<TRS>> list(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

		System.out.println("list trs");
		return Mono.just(trsRepo.findAll(pageable));

	}
	@GetMapping(value = "courses/{trsId}")
	public Mono<List<Course>> listByTrs(@PathVariable Long trsId) {


		return Mono.just(trsRepo.getOne(trsId).getCourses());

	}
}