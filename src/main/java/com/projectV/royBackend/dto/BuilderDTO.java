package com.projectV.royBackend.dto;

public class BuilderDTO {
//    private Long id;
    private String name;
    private int experience;
    private String address;
    private String city;
    private String whatsappNumber;
    private String photoPath;


    public BuilderDTO() {
//        this.id=id;
        this.name = name;
        this.experience=experience;
        this.address = address;
        this.city = city;
        this.whatsappNumber=whatsappNumber;
        this.photoPath = photoPath;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }
}
