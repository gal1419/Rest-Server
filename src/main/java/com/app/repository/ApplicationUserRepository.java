package com.app.repository;

import com.app.module.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
    ApplicationUser findByEmail(String email);
}