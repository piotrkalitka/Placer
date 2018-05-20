package com.piotrkalitka.placer.api.apiModels.getImage;

import com.piotrkalitka.placer.api.dbModels.Image;

public class GetImageResponseBody {

    private Image image;

    public GetImageResponseBody(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

}