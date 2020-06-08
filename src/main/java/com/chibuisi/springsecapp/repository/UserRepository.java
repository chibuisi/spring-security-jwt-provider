package com.chibuisi.springsecapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chibuisi.springsecapp.model.MyUser;


public interface UserRepository extends JpaRepository<MyUser, Long> {
	public MyUser findByUsername(String username);
}
