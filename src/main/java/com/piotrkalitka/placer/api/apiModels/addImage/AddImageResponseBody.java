package com.piotrkalitka.placer.api.apiModels.addImage;

public class AddImageResponseBody {

    private String image;
    private int id;

    public AddImageResponseBody(int id, String image) {
        this.id = id;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}
