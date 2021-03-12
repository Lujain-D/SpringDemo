package com.example.demo.services;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

//    @Autowired
//    RoleRepository roleRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("creating user in CustomUserDetailsService, username: "+username);

//        return new User("foo", "foo", new ArrayList<>());

        Optional<User> opUser =  userRepository.findByUsername(username);
        if(opUser.isPresent())System.out.println("present");
        if(!opUser.isPresent())System.out.println("not present");
//        opUser.orElseThrow(()-> new UsernameNotFoundException("User not found "+ username));
        return opUser.map(CustomUserDetails::new).get();
//        return new CustomUserDetails();
    }

    public boolean createNewUser(String username, String password, String role){
        Optional<User> opUser =  userRepository.findByUsername(username);
        Optional<Role> opRole = roleRepository.findByRole(role);
        if(opUser.isPresent() | !opRole.isPresent()){
            return false;
        }
        Role roleObj = opRole.get();
        User user = new User(username, password,roleObj );
        user.setRole(roleObj);
        userRepository.save(user);

        return true;
    }
}
