package com.example.application.callApi;

import com.example.application.data.PhotoModel;
import com.example.application.data.entity.RequestPhoto;
import com.example.application.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class PhotoApi {
    private RestTemplate restTemplate = new RestTemplate();

    public List<PhotoModel> getAll(){
        PhotoModel[] photoModelsArray = restTemplate.getForObject("http://localhost:8080/photo/", PhotoModel[].class);
        return Arrays.asList(photoModelsArray);
    }

    public PhotoModel addPhoto(byte[] imageData, String description) throws IOException {
        RequestPhoto requestPhoto = new RequestPhoto();
        requestPhoto.setImage(imageData);
        requestPhoto.setUsername(MainView.authResponse.getUserName());
        requestPhoto.setDescription(description);
        return restTemplate.postForObject("http://localhost:8080/photo/post",requestPhoto, PhotoModel.class);
    }


}
