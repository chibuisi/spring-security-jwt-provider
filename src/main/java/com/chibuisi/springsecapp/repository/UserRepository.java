package com.chibuisi.springsecapp.repository;

import com.chibuisi.springsecapp.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserAccount, Long> {
	public UserAccount findByUsername(String username);
}
