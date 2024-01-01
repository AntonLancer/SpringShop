package com.anton.springeshop.dao;

import com.anton.springeshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findFirstByName(String name);
}
