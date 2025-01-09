package com.projectV.royBackend.repository;

import com.projectV.royBackend.model.Builder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuilderRepository extends JpaRepository<Builder, Long> {
   Builder findByEmail(String email); // Used to find a builder by their email //14/12
   // Optional<Builder> findByEmail(String email);

}
