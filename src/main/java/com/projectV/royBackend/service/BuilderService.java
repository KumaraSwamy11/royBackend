package com.projectV.royBackend.service;

import com.projectV.royBackend.model.Builder;
import com.projectV.royBackend.repository.BuilderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BuilderService {


    @Autowired
    private BuilderRepository builderRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //Directory where the photos will be saved it should change in prod
    private  static  final  String UPLOAD_DIR="D:\\Uploads";


    public Builder registerBuilder(Builder builder, MultipartFile photo) throws Exception {
        // Check if builder already exists
        if (builderRepository.findByEmail(builder.getEmail()) != null) {
            throw new Exception("Builder with this email already exists.");
        }

        // Encrypt the password before saving
        builder.setPassword(passwordEncoder.encode(builder.getPassword()));

        //If photo is provided, save it to the file system
        if (!photo.isEmpty()){
            String fileName = System.currentTimeMillis() +"_"+photo.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR,fileName);

            try{
                //Create directories if not present
                Files.createDirectories(filePath.getParent());

                //Save the photo to the file System
                Files.write(filePath, photo.getBytes());

                //Set the file path in the Builder entity
                builder.setPhotoPath(fileName);
            }
            catch (IOException e){
                throw new Exception("Could not save file: "+e.getMessage());
            }
        }


        return builderRepository.save(builder); // Save builder to the database

    }
}
