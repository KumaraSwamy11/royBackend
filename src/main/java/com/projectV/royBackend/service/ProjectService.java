package com.projectV.royBackend.service;

import com.projectV.royBackend.model.Builder;
import com.projectV.royBackend.model.Project;
import com.projectV.royBackend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public  void addProject(Project project){
        projectRepository.save(project);
    }

    public List<Project> getProjectByBuilder(Builder builder){
        return projectRepository.findByBuilders(builder);
    }

}
