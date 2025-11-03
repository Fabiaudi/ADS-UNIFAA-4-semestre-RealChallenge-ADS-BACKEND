package com.unifaa.bookexam.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserPrincipal implements UserDetails {
    private String id;
    private String role;
    
    public boolean hasRole(String role) {
        return this.role.equalsIgnoreCase(role);
    }

    public String getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        throw new UnsupportedOperationException("Não implementado método 'getAuthorities'");
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("Não implementado método 'getPassword'");
    }

    @Override
    public String getUsername() {
        throw new UnsupportedOperationException("Não implementado método 'getUsername'");
    }
}
