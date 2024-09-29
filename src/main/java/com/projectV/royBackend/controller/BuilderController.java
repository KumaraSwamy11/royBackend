package com.projectV.royBackend.controller;


import com.projectV.royBackend.model.Builder;
import com.projectV.royBackend.service.BuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/builders")
public class BuilderController {

    @Autowired
    private BuilderService builderService;

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
            return ResponseEntity.ok(builder); // Return builder profile on successful login
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Fetch builder profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBuilderProfile(@PathVariable Long id) {
        Builder builder = builderService.getBuilderProfile(id);
        if (builder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(builder);
    }
}
