package com.projectV.royBackend.service;

import com.projectV.royBackend.model.Builder;
import com.projectV.royBackend.model.Project;
import com.projectV.royBackend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private final FileService fileService;
    @Autowired
    public ProjectService(FileService fileService) {
        this.fileService = fileService;
    }
// Inject FileService for file uploads


    //Method to add a project with an optional file upload
    public  void addProject(Project project, MultipartFile file){
        try{
            //Handle file upload if the file is not empty
            if (file != null && !file.isEmpty()){
                String imageUrl = fileService.saveFile(file); //Save the file and get the URL or path
                project.setImageUrl(imageUrl);
            }

            //Save the project details to the database
            projectRepository.save(project);
        } catch (Exception e){
            throw new RuntimeException("Failed to save project of file : "+e.getMessage(),e);
        }
    }

    //Fetch Projects by builder
    public List<Project> getProjectByBuilder(Builder builder){
        return projectRepository.findByBuilders(builder);
    }

}
