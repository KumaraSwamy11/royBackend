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
}
