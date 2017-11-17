package com.app.repository;

import com.app.module.Picture;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PictureRepository extends CrudRepository<Picture, Long> {

}