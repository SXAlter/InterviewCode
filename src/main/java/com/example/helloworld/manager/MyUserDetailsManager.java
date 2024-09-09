package com.example.helloworld.manager;

import com.example.helloworld.domain.LoginUser;
import com.example.helloworld.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
@AllArgsConstructor
public class MyUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public void createUser(UserDetails userDetails) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = User.builder()
                .id(1)
                .userName("test")
                .password(new BCryptPasswordEncoder()
                        .encode("123456"))
                .build();
        return new LoginUser(user, new ArrayList<>());
    }
}
