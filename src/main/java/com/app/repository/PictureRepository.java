package com.app.repository;

import com.app.module.Event;
import com.app.module.Picture;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called pictureRepository
// CRUD refers Create, Read, Update, Delete

public interface PictureRepository extends CrudRepository<Picture, Long> {
    Iterable<Picture> findByEvent(Event event);
}