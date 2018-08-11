package com.app.controller;

import com.app.module.ApplicationUser;
import com.app.module.Event;
import com.app.repository.ApplicationUserRepository;
import com.app.repository.EventRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody ApplicationUser user) {

        if (StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setFullName(user.getFullName());
        applicationUser.setEmail(user.getEmail());
        applicationUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if (null != applicationUserRepository.findByEmail(applicationUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This email address is taken");
        }

        try {
            ApplicationUser newUser = applicationUserRepository.save(applicationUser);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }
    }

    @PostMapping("/login")
    public @ResponseBody
    ApplicationUser login(@RequestBody ApplicationUser user) {
        return applicationUserRepository.findByEmail(user.getEmail());
    }

    @GetMapping("/user")
    public @ResponseBody
    ApplicationUser getUser(HttpServletRequest request) {
        Principal p = request.getUserPrincipal();
        return applicationUserRepository.findByEmail(p.getName());
    }

    @GetMapping("/events")
    public @ResponseBody
    Iterable<Event> getUserEvents(HttpServletRequest request) {
        Principal p = request.getUserPrincipal();
        ApplicationUser applicationUser = applicationUserRepository.findByEmail(p.getName());

        if (applicationUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return applicationUser.getEvents();
    }

    @PostMapping("/add-event/{eventId}")
    public @ResponseBody
    Iterable<Event> getUserEvents(HttpServletRequest request, @PathVariable(value = "eventId") String id) throws Exception {
        Principal p = request.getUserPrincipal();
        ApplicationUser applicationUser = applicationUserRepository.findByEmail(p.getName());
        Event event = eventRepository.findOne(Long.parseLong(id));

        if (event == null) {
            throw new NotFoundException("event with id" + id + "was not found");
        }
        List<Event> userEvents = applicationUser.getEvents();

        if (userEvents.contains(event)) {
            return userEvents;
        }

        applicationUser.addEvent(event);
        return applicationUserRepository.save(applicationUser).getEvents();
    }

}