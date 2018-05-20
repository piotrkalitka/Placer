package com.piotrkalitka.placer.api.dbModels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private int placeId;
    @Column(nullable = false)
    private String path;

    public Image(int placeId, String path) {
        this.placeId = placeId;
        this.path = path;
    }

    public Image(int id, int placeId, String path) {
        this.id = id;
        this.placeId = placeId;
        this.path = path;
    }

    public Image() {

    }

    public int getId() {
        return id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getPath() {
        return path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public void setPath(String path) {
        this.path = path;
    }
}