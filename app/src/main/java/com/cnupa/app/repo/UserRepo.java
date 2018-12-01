package com.cnupa.app.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cnupa.app.model.User;
import com.cnupa.app.model.enums.Role;

public interface UserRepo extends JpaRepository<User, Long> {
	
	Optional<User> findOneById(String userId);

	Optional<User> findByUsername(String username);

	Optional<User> findOneByUsernameAndPassword(String username, String password);

	Page<User> findByRole(Pageable pageable, Role role);
}
