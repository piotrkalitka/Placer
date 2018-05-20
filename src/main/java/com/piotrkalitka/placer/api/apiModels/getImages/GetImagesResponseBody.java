package com.piotrkalitka.placer.api.apiModels.getImages;

import com.piotrkalitka.placer.api.dbModels.Image;

import java.util.List;

public class GetImagesResponseBody {

    private List<Image> images;

    public GetImagesResponseBody(List<Image> images) {
        this.images = images;
    }

    public List<Image> getImages() {
        return images;
    }
}
