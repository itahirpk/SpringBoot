package com.spring.springjwt.repo;

import com.spring.springjwt.entities.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}