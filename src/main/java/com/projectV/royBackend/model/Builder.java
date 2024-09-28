package com.projectV.royBackend.model;
import jakarta.persistence.*;


@Entity
public class Builder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String whatsappNumber;
    private String address;
    private String city;
    private int experience;
    private String password;
    private String photoPath; // Store photo as a byte array

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
// Getters and setters omitted for brevity

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
