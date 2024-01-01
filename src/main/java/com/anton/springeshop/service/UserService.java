package com.anton.springeshop.service;

import com.anton.springeshop.domain.User;
import com.anton.springeshop.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService { // security
	boolean save(UserDto userDTO); // сохраняет пользователя
	void save(User user);
	List<UserDto> getAll();

	User findByName(String name);
	void updateProfile(UserDto userDTO);
}
