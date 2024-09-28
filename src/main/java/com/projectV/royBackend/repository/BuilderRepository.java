package com.projectV.royBackend.repository;

import com.projectV.royBackend.model.Builder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuilderRepository extends JpaRepository<Builder, Long> {
    Builder findByEmail(String email); // Used to find a builder by their email

}
