package com.cnupa.app.security.model;

import com.cnupa.app.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author ard333
 */
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class AuthResponse {
	
	private String token;
	private User user;
}
