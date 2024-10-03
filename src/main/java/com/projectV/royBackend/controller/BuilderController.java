package com.projectV.royBackend.controller;


import com.projectV.royBackend.model.Builder;
import com.projectV.royBackend.model.Project;
import com.projectV.royBackend.security.JwtUtil;
import com.projectV.royBackend.service.BuilderService;
import com.projectV.royBackend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/builders")
public class BuilderController {

    @Autowired
    private BuilderService builderService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProjectService projectService;

    //This method now accepts the form data (Builder) and the file (MultipartFile)
    @PostMapping("/register")
    public ResponseEntity<?> registerBuilder(
            @ModelAttribute Builder builder, //Bind form data
            @RequestParam("photo")MultipartFile photo //Accept file upload
            ) {
        try {
            Builder registeredBuilder = builderService.registerBuilder(builder, photo);
            return ResponseEntity.ok(registeredBuilder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<?> loginBuilder(@RequestParam String email, @RequestParam String password) {
        try {
            Builder builder = builderService.loginBuilder(email, password);
            String token = jwtUtil.generateToken(email); //Generate JWT Token
            Map<String,Object> response= new HashMap<>();
            response.put("token",token); //Return token to frontend
            response.put("builder",builder);
            return ResponseEntity.ok(response); // Return builder profile on successful login
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Fetch builder profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBuilderProfile(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        String email=jwtUtil.extractUsername(token.substring(7));

        Builder builder = builderService.getBuilderProfile(id);
        if (builder == null || !builder.getEmail().equals(email)) {
            return ResponseEntity.status(403).body("Unauthorized");
        }
        return ResponseEntity.ok(builder);
    }

    //Update the Builder Profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBuilderProfile(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Builder updatedBuilder) {
        String email = jwtUtil.extractUsername(token.substring(7));

        //check if the builder exists and matches the authencated user
        Builder existingBuilder = builderService.getBuilderProfile(id);
        if(existingBuilder == null || !existingBuilder.getEmail().equals(email)){
            return ResponseEntity.status(403).body("Unauthorized");
        }

        //Update the Profile Details
        existingBuilder.setName(updatedBuilder.getName());
        existingBuilder.setWhatsappNumber(updatedBuilder.getWhatsappNumber());
        existingBuilder.setAddress(updatedBuilder.getAddress());
        existingBuilder.setCity(updatedBuilder.getCity());
        existingBuilder.setExperience(updatedBuilder.getExperience());

        //Save the updated Builder to the Database
        builderService.saveBuilder(existingBuilder);

        return ResponseEntity.ok(existingBuilder);
    }

    //Adding Project API
    @PostMapping("/{builderId}/projects")
    public ResponseEntity<?> addProject(
            @RequestHeader("Authorization") String token,
            @PathVariable Long builderId,
            @RequestBody Project project){

        String email = jwtUtil.extractUsername(token.substring(7)); //Extract email from token

        //Check if the builder exists and matches the authenticated user
        Builder existingBuilder = builderService.getBuilderProfile(builderId);
        if(existingBuilder==null || !existingBuilder.getEmail().equals(email)){
            return ResponseEntity.status(403).body("Unauthorized");
        }

        //Set the builder for the project
        //project.setBuilder(existingBuilder);

        //Ensure the builders set is initialized
        if(project.getBuilders()==null){
            project.setBuilders(new HashSet<>());  //Initialize the Set if null
        }

        //Add the Builder to the Project
        project.getBuilders().add(existingBuilder);

        //Save the project
        projectService.addProject(project);

        return ResponseEntity.ok(project);


    }

    //Get Projects by Builder API
    @GetMapping("/{builderId}/projects")
    public ResponseEntity<?> getProjectsByBuilder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long builderId){

        String email = jwtUtil.extractUsername(token.substring(7)); //Extract email from token

        //Check if the builder exists and matches the authenticated user

        Builder existingBuilder = builderService.getBuilderProfile(builderId);
        if(existingBuilder == null || !existingBuilder.getEmail().equals(email)){
            return ResponseEntity.status(403).body("Unauthorized");
        }

        //Retrieve the builders projects
        List<Project> projects = projectService.getProjectByBuilder(existingBuilder);
        return ResponseEntity.ok(projects);

    }

}
