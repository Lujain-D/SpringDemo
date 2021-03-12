package com.example.demo.model;

import com.example.demo.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;
//
//
    public CustomUserDetails(){
        this.username = "lujain";
        this.password = "pass";
        this.active = true;
        this.authorities =  Arrays.asList(new SimpleGrantedAuthority("USER")) ;
    }

    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = user.isActive();
        System.out.println("in custom user details: "+user);
        System.out.println("in custom user details: "+user.getRole());
//        this.authorities =  Arrays.asList(new SimpleGrantedAuthority(user.getRole())) ;
        this.authorities =  Arrays.asList(new SimpleGrantedAuthority(user.getRole().getRole())) ;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
