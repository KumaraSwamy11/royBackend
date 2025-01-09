    package com.projectV.royBackend.service;

    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.UUID;

    @Service
    public class FileService {
        private final String uploadDir = "D:\\royBackend\\Uploads"; // Change to your upload directory

        public String saveFile(MultipartFile file) throws Exception {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // Save the file to the upload directory
            Files.copy(file.getInputStream(), filePath);

            // Return the URL or file path for saving in the database
            return "/uploads/" + fileName;
        }

    }
