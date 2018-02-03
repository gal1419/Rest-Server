package com.app.controller;

import com.app.module.ApplicationUser;
import com.app.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/users")
public class UserController {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody ApplicationUser user) {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setFirstName(user.getFirstName());
        applicationUser.setLastName(user.getLastName());
        applicationUser.setEmail(user.getEmail());
        applicationUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(applicationUser);
        return "saved";
    }
}