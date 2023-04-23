package com.chibuisi.springsecapp.service;

import com.chibuisi.springsecapp.model.AppRole;
import com.chibuisi.springsecapp.model.Role;
import com.chibuisi.springsecapp.repository.AppRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppRoleService {
    @Autowired
    private AppRoleRepository appRoleRepository;

    public AppRole findAppRole(Role roleName){
        Optional<AppRole> optionalAppRole = appRoleRepository.findAppRoleByRoleName(roleName);
        if(optionalAppRole.isPresent())
            return optionalAppRole.get();
        return null;
    }
}
