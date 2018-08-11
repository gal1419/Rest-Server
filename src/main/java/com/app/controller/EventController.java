package com.app.controller;

import com.app.module.ApplicationUser;
import com.app.module.Event;
import com.app.repository.ApplicationUserRepository;
import com.app.repository.EventRepository;
import org.hibernate.Hibernate;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;


@Controller
@RequestMapping(path = "/event")
public class EventController {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    public EventController() {
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    Event getEventById(@PathVariable(value = "id") String id) throws Exception {
        try {
            return eventRepository.findOne(Long.parseLong(id));
        } catch (Exception e) {
            throw new Exception("id parameter is invalid");
        }
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    Event deleteEventById(HttpServletRequest request, @PathVariable(value = "id") String id) throws Exception {

        Principal p = request.getUserPrincipal();
        ApplicationUser authenticatedUser = applicationUserRepository.findByEmail(p.getName());

        Event event;
        try {
            Long eventId = Long.parseLong(id);
            event = eventRepository.findOne(eventId);

            if (event == null) {
                throw new Exception("event was not found!");
            }

            if (!event.getOwner().getId().equals(authenticatedUser.getId())) {
                throw new Exception("Forbidden");
            }

            Hibernate.initialize(event.getParticipants());
            eventRepository.delete(eventId);
        } catch (Exception e) {
            throw new Exception("id parameter is invalid");
        }

        return event;
    }


    @PostMapping(path = "/add")
    public @ResponseBody
    Event addNewEvent(HttpServletRequest request, @RequestParam("title") String title, @RequestParam("address") String address, @RequestParam("file") MultipartFile file) {
        Principal p = request.getUserPrincipal();
        ApplicationUser user = applicationUserRepository.findByEmail(p.getName());

        Event event = new Event(title);
        event.setOwner(user);
        event.addParticipant(user);
        event.setAddress(address);
        event.setTitle(title);

        try {
            event.setImage(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.eventRepository.save(event);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    private Long getOwnerId(JSONObject request) throws Exception {
        try {
            return Long.parseLong(this.getRequestBodyData(request, "owner"));
        } catch (JSONException e) {
            throw new Exception("owner parameter is Invalid or Missing");
        }
    }

    @GetMapping(
            value = "/thumbnail/{eventId}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<byte[]> getImageWithMediaType(@PathVariable(value = "eventId") String id) throws Exception {

        try {
            byte[] image = eventRepository.findOne(Long.parseLong(id)).getImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (Exception e) {
            throw new Exception("Event id is not in the correct format", e);
        }
    }

    private String getEventTitle(JSONObject request) throws Exception {
        try {
            return this.getRequestBodyData(request, "title");
        } catch (JSONException e) {
            throw new Exception("title parameter is Invalid or Missing");
        }
    }

    private String getRequestBodyData(JSONObject request, String key) throws JSONException {
        return request.getString(key);
    }
}