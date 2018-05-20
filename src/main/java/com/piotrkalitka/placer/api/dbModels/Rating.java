package com.piotrkalitka.placer.api.dbModels;

import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private int placeId;
    @Column(nullable = false)
    @Range(min = 1, max = 5)
    private int rating;

    public Rating() {

    }

    public Rating(int userId, int placeId, @Range(min = 1, max = 5) int rating) {
        this.userId = userId;
        this.placeId = placeId;
        this.rating = rating;
    }

    public Rating(int id, int userId, int placeId, @Range(min = 1, max = 5) int rating) {
        this.id = id;
        this.userId = userId;
        this.placeId = placeId;
        this.rating = rating;
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

    public int getRating() {
        return rating;
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

    public void setRating(int rating) {
        this.rating = rating;
    }
}