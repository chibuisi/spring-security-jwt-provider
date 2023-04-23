package com.chibuisi.springsecapp.repository;

import com.chibuisi.springsecapp.model.AppRole;
import com.chibuisi.springsecapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    Optional<AppRole> findAppRoleByRoleName(Role roleName);
}
