package com.piotrkalitka.placer.api.dbModels;

import org.springframework.lang.Nullable;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String website;
    @Column(nullable = false)
    private String phoneNumber;
    private String description;
    @OneToMany
    @JoinColumn(name = "placeId")
    private List<Image> images;

    public Place(int userId, String name, String address, String website, String phoneNumber, @Nullable String description) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }

    public Place(int id, int userId, String name, String address, String website, String phoneNumber, @Nullable String description) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }

    public Place() {

    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
