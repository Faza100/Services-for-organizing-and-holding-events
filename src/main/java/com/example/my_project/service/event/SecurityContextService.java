package com.example.my_project.service.event;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.my_project.enums.UserRole;
import com.example.my_project.exeption.NoRolesException;

@Service
public class SecurityContextService {

    public String getCurrentUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public UserRole getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(this::convertToUserRole)
                .orElseThrow(() -> new NoRolesException("User has no roles"));
    }

    private UserRole convertToUserRole(String authority) {
        String roleName = authority.startsWith("ROLE_")
                ? authority.substring(5)
                : authority;
        return UserRole.valueOf(roleName);
    }
}
