package com.projectV.royBackend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectV.royBackend.dto.BuilderDTO;
import com.projectV.royBackend.model.Builder;
import com.projectV.royBackend.model.Project;
import com.projectV.royBackend.security.JwtUtil;
import com.projectV.royBackend.service.BuilderService;
import com.projectV.royBackend.service.FileService;
import com.projectV.royBackend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin
@RequestMapping("/api/builders")
public class BuilderController {

    @Autowired
    private BuilderService builderService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProjectService projectService;
   // private FileService fileService;

    //constructor based
    private final FileService fileService;  // Declare fileService

    @Autowired
    public BuilderController(FileService fileService) {  // Constructor-based injection
        this.fileService = fileService;
    }

//
//   @Autowired
//    private FileService fileService;  // Inject the FileService


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
            //email is changed
            String token = jwtUtil.generateToken(builder.getEmail(), builder.getId()); //Generate JWT Token
            Map<String,Object> response= new HashMap<>();
            response.put("token",token); //Return token to frontend
            response.put("builder",builder);
            return ResponseEntity.ok(response); // Return builder profile on successful login
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Fetch builder profile by ID Before Error
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getBuilderProfile(@RequestHeader("Authorization") String token,@PathVariable Long id) {
//        String email=jwtUtil.extractUsername(token.substring(7));
//
//        Builder builder = builderService.getBuilderProfile(id);
//        if (builder == null || !builder.getEmail().equals(email)) {
//            return ResponseEntity.status(403).body("Unauthorized");
//        }
//        return ResponseEntity.ok(builder);
//    }

    //After Generating token
    @GetMapping("/{id}")
                                                                                          //14/12 changed
    public ResponseEntity<?> getBuilderProfile(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        // Validate if token starts with 'Bearer '
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body("Invalid Token");
        }

        String email;
        try {
            email = jwtUtil.extractUsername(token.substring(7)); // Extract email from token (remove 'Bearer ')
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Token extraction failed: " + e.getMessage());
        }

        try{
            BuilderDTO builderProfile=builderService.getBuilderProfile(id,email);
            return ResponseEntity.ok(builderProfile);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
//        catch (SecurityException e){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//        }



        // Fetch builder profile
//        Builder builder = builderService.getBuilderProfile(id);
//        if (builder == null || !builder.getEmail().equals(email)) {
//            return ResponseEntity.status(403).body("Unauthorized");
//        }
//        return ResponseEntity.ok(builder);
    }



    //Update the Builder Profile
    @PutMapping("/{Id}")

    public ResponseEntity<?> updateBuilderProfile(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Builder updatedBuilder) {
        String email = jwtUtil.extractUsername(token.substring(7));

        //check if the builder exists and matches the authencated user //14/12 changed
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




    //Adding Projects API
    @PostMapping(value = "/{builderId}/projects", consumes = "multipart/form-data")
    public ResponseEntity<?> addProjectWithFormData(
            @RequestHeader("Authorization") String token,
            @PathVariable Long builderId,
            @RequestPart("project") String projectJson,  // Get project JSON as string
            @RequestPart("file") MultipartFile file) throws Exception {

        // Log received data for debugging
        System.out.println("Received Project JSON: " + projectJson);

        // Convert JSON string to Project object
        ObjectMapper objectMapper = new ObjectMapper();
        Project project;
        try {
            project = objectMapper.readValue(projectJson, Project.class);  // Parse the JSON string to Project object
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid Project JSON: " + e.getMessage());
        }

        String email = jwtUtil.extractUsername(token.substring(7));  // Extract email from token

        // Check if the builder exists and matches the authenticated user
        Builder existingBuilder = builderService.getBuilderProfile(builderId);
        if (existingBuilder == null || !existingBuilder.getEmail().equals(email)) {
            return ResponseEntity.status(403).body("Unauthorized");
        }

        // Handle the file upload (save the file and set the URL for the project)
        if (file != null && !file.isEmpty()) {
            System.out.println("Received file: " + file.getOriginalFilename());
            String imageUrl = fileService.saveFile(file);  // Save the file using the injected FileService
            project.setImageUrl(imageUrl);  // Set the image URL for the project
        }

        // Initialize builders set if null and associate builder with project
        if (project.getBuilders() == null) {
            project.setBuilders(new HashSet<>());
        }
        project.getBuilders().add(existingBuilder);

        // Save the project to the database
        projectService.addProject(project, file);

        return ResponseEntity.ok(project);
    }



    //Adding Project API
//    @PostMapping("/{builderId}/projects")
//    public ResponseEntity<?> addProject(
//            @RequestHeader("Authorization") String token,
//            @PathVariable Long builderId,
//            @RequestBody Project project){
//
//        String email = jwtUtil.extractUsername(token.substring(7)); //Extract email from token
//
//        //Check if the builder exists and matches the authenticated user
//        Builder existingBuilder = builderService.getBuilderProfile(builderId);
//        if(existingBuilder==null || !existingBuilder.getEmail().equals(email)){
//            return ResponseEntity.status(403).body("Unauthorized");
//        }
//
//        //Set the builder for the project
//        //project.setBuilder(existingBuilder);
//
//        //Ensure the builders set is initialized
//        if(project.getBuilders()==null){
//            project.setBuilders(new HashSet<>());  //Initialize the Set if null
//        }
//
//        //Add the Builder to the Project
//        project.getBuilders().add(existingBuilder);
//
//        //Save the project
//        projectService.addProject(project);
//
//        return ResponseEntity.ok(project);
//
//
//    }

    //Get Projects by Builder API
    @GetMapping("/{builderId}/projects")
    public ResponseEntity<?> getProjectsByBuilder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long builderId){

        String email = jwtUtil.extractUsername(token.substring(7)); //Extract email from token

        //Check if the builder exists and matches the authenticated user
                                                       //14/12 changes code email
        Builder existingBuilder = builderService.getBuilderProfile(builderId);
        if(existingBuilder == null || !existingBuilder.getEmail().equals(email)){
            return ResponseEntity.status(403).body("Unauthorized");
        }

        //Retrieve the builders projects
        List<Project> projects = projectService.getProjectByBuilder(existingBuilder);
        return ResponseEntity.ok(projects);

    }

}
