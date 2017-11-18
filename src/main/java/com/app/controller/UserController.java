package com.app.controller;

import com.app.module.ApplicationUser;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Controller
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping(path = "/sign-up")
    public @ResponseBody String signUp(@RequestBody ApplicationUser user) {
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        return "save";
    }

    @GetMapping(path="/add")
    public @ResponseBody String addNewUser(HttpServletRequest request, Principal principal) {

        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<ApplicationUser> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}