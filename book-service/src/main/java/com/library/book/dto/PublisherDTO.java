package com.library.book.dto;

public class PublisherDTO {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private String website;

    public PublisherDTO() {
    }

    public PublisherDTO(Long id, String name, String address, String email, String phoneNumber, String website) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.website = website;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
}