package com.cnupa.app.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cnupa.app.model.User;
import com.cnupa.app.repo.UserRepo;
import com.cnupa.app.security.PBKDF2Encoder;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(value = 1)
@Slf4j
public class InitAdminAccountRunner implements ApplicationRunner {

	@Autowired

	private UserRepo userRepo;
	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Override
	public void run(ApplicationArguments var1) throws Exception {

		User u = userRepo.findByUsername("admin").get();
		if (u == null) {
			log.info("init admin count");
			User user = new User();
			user.setUsername("admin");
			user.setPassword(passwordEncoder.encode("admin"));
			userRepo.save(user);
		}

	}
}
