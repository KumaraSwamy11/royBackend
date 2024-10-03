package com.projectV.royBackend.repository;

import com.projectV.royBackend.model.Builder;
import com.projectV.royBackend.model.Project;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findByBuilders(Builder builder);
}
