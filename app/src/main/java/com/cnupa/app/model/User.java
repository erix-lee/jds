package com.cnupa.app.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cnupa.app.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table

@Data

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn( discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("user")

public class User  implements UserDetails  {
	public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

	/**
	 * 
	 */
	private static final long serialVersionUID = 5402081046666860620L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@OrderColumn
	@NotNull
	@Pattern(regexp = LOGIN_REGEX)
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String username;

	@OrderColumn
	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = "password", length = 60)
	private String password;

	@OrderColumn
	@Size(max = 50)
	@Column(length = 50)
	private String fullname;

	@OrderColumn
	@Size(min = 5, max = 100)
	@Column(length = 100, unique = true)
	private String email;

	@OrderColumn
	@Column(name = "created_date")
	private Instant createdDate = Instant.now();

	@Size(max = 256)
	@Column(name = "image_url", length = 256)
	private String imageUrl;

	@Size(min = 7, max = 15)
	@Column(name = "phone", length = 15)
	String phone;

	@Enumerated(EnumType.STRING)
	private Role role = Role.TEACHER;

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	@JsonIgnore
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();

		auths.add(new SimpleGrantedAuthority(role.name()));

		return auths;

	}
}
