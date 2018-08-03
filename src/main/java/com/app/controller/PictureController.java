package com.app.controller;

import com.app.module.ApplicationUser;
import com.app.module.Event;
import com.app.module.Picture;
import com.app.repository.ApplicationUserRepository;
import com.app.repository.EventRepository;
import com.app.repository.PictureRepository;
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
@RequestMapping(path = "/picture")
public class PictureController {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private EventRepository eventRepository;

    public PictureController() {
    }

    @GetMapping(path = "/event/{eventId}")
    public @ResponseBody
    Iterable<Picture> getAllPicturesByEventId(@PathVariable(value = "eventId") String eventId) {
        return pictureRepository.findByEvent(eventRepository.findOne(Long.parseLong(eventId)));
    }


    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity addNewPicture(HttpServletRequest request, @RequestParam("eventId") String eventId, @RequestParam("description") String description, @RequestParam("file") MultipartFile file) {
        Principal p = request.getUserPrincipal();
        ApplicationUser user = applicationUserRepository.findByEmail(p.getName());
        Event event = eventRepository.findOne(Long.parseLong(eventId));

        Picture picture = new Picture();
        picture.setOwner(user);
        picture.setEvent(event);
        picture.setDescription(description);

        try {
            picture.setImage(file.getBytes());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("");

        }
        return ResponseEntity.ok(pictureRepository.save(picture));
    }

    @GetMapping(
            value = "/{pictureId}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<byte[]> getImageWithMediaType(@PathVariable(value = "pictureId") String id) throws Exception {

        try {
            byte[] image = pictureRepository.findOne(Long.parseLong(id)).getImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (Exception e) {
            throw new Exception("Picture id is not in the correct format", e);
        }
    }
}