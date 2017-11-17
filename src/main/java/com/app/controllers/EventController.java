package com.app.controllers;

import com.app.module.Event;
import com.app.module.User;
import com.app.repository.EventRepository;
import com.app.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Controller
@RequestMapping(path="/event")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add", consumes = "application/json", produces = "application/json")
    public @ResponseBody String addNewEvent(@RequestBody String requestBody) {

        JSONObject request = new JSONObject(requestBody);
        String title = "";
        Long ownerId = 1L;
        try {
            title = this.getEventTitle(request);
            ownerId = this.getOwnerId(request);
        } catch (Exception e) {}

        User owner = this.userRepository.findOne(ownerId);

        Event event = new Event(title);
        event.addParticipant(owner);
        this.eventRepository.save(event);

        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Event> getAllEvents() {
        // This returns a JSON or XML with the users
        return eventRepository.findAll();
    }

    private Long getOwnerId(JSONObject request) throws Exception {
        try {
            return Long.parseLong(this.getRequestBodyData(request, "owner"));
        } catch (JSONException e) {
            throw new Exception("owner parameter is Invalid or Missing");
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