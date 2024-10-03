package com.projectV.royBackend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String imageUrl; // Store Image URL
    private String status; // Completed or Ongoing

//    @ManyToMany
//    @JoinColumn(name = "builder_id")
//    private Builder builder; //Link to the builder

    @ManyToMany
    @JoinTable(
            name = "project_builder", //Name of the Join Table
            joinColumns = @JoinColumn(name = "project_id"), // Foreign key for Project
            inverseJoinColumns = @JoinColumn(name = "builder_id") //Foreign key for Builder
    )

    private Set<Builder> builders;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public Builder getBuilder() {
//        return builder;
//    }
//
//    public void setBuilder(Builder builder) {
//        this.builder = builder;
//    }

    public Set<Builder> getBuilders() {
        return builders;
    }

    public void setBuilders(Set<Builder> builders) {
        this.builders = builders;
    }
}
