package com.cnupa.app.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnupa.app.exception.UnauthorizedException;
import com.cnupa.app.model.User;
import com.cnupa.app.repo.UserRepo;
import com.cnupa.app.security.JWTUtil;
import com.cnupa.app.security.PBKDF2Encoder;
import com.cnupa.app.security.model.AuthRequest;
import com.cnupa.app.security.model.AuthResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * @author ard333
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthRest {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	private UserRepo userRepo;

	@PostMapping(value = "login")
	public Mono<AuthResponse> auth(@RequestBody AuthRequest ar) throws UnauthorizedException {
		log.debug(ar.getUsername());
		User user = userRepo.findOneByUsernameAndPassword(ar.getUsername(), passwordEncoder.encode(ar.getPassword()))
				.orElse(null);

		if (user == null) {

			return Mono.error(new UnauthorizedException("用户名和密码不匹配!"));

		}
		return Mono.just(new AuthResponse(jwtUtil.generateToken(user), user));
	}

	@GetMapping(value = "init")
	public Mono<ResponseEntity<User>> auth2() {
		User user = new User();
		user.setUsername("admin");
		user.setPassword(passwordEncoder.encode("admin"));
		userRepo.save(user);
		log.debug(user.getUsername());
		return Mono.just(ResponseEntity.ok(user));

	}

	@GetMapping(value = "qlogin")
	public Mono<AuthResponse> auth3() throws Exception {
		User user = userRepo.findOneByUsernameAndPassword("admin", passwordEncoder.encode("admin"))
				.orElseThrow(() -> new UnauthorizedException("xxxx"));
	
		return Mono.just(new AuthResponse(jwtUtil.generateToken(user), user));

	}
}
