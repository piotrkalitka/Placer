package com.piotrkalitka.placer.api.dbModels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "favourites")
public class Favourite {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private int placeId;

    public Favourite(int id, int userId, int placeId) {
        this.id = id;
        this.userId = userId;
        this.placeId = placeId;
    }

    public Favourite(int userId, int placeId) {
        this.userId = userId;
        this.placeId = placeId;
    }

    public Favourite() {

    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }
}