package com.example.application.callApi;

import com.example.application.data.PhotoModel;
import com.example.application.data.entity.RequestPhoto;
import com.example.application.data.entity.User;
import com.example.application.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    public List<PhotoModel> getAllByUsername(){
        PhotoModel[] photoModelsArray = restTemplate.getForObject("http://localhost:8080/photo/user/?username="+MainView.authResponse.getUserName(), PhotoModel[].class);
        return Arrays.asList(photoModelsArray);
    }



    public PhotoModel addPhoto(byte[] imageData, String description) throws IOException {
        RequestPhoto requestPhoto = new RequestPhoto();
        requestPhoto.setImage(imageData);
        requestPhoto.setUsername(MainView.authResponse.getUserName());
        requestPhoto.setDescription(description);
        return restTemplate.postForObject("http://localhost:8080/photo/post",requestPhoto, PhotoModel.class);
    }

    public byte[] getImage(Long id){
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return restTemplate.getForObject("http://localhost:8080/photo/get/?id="+id,byte[].class);
    }

    public <T> T getLast(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }

    public PhotoModel updateLike(PhotoModel photoModel){
        HttpEntity<Long> httpEntity = new HttpEntity(photoModel.getId());
        return restTemplate.exchange("http://localhost:8080/photo/like?username="+MainView.authResponse.getUserName(),HttpMethod.PUT, httpEntity, PhotoModel.class).getBody();
    }

    public PhotoModel updateDislike(PhotoModel photoModel){
        HttpEntity<Long> httpEntity = new HttpEntity(photoModel.getId());
        return restTemplate.exchange("http://localhost:8080/photo/dislike?username="+MainView.authResponse.getUserName(),HttpMethod.PUT, httpEntity, PhotoModel.class).getBody();
    }

    public void updateLikeDislike(PhotoModel photoModel){
        HttpEntity<Long> httpEntity = new HttpEntity(photoModel.getId());
        restTemplate.exchange("http://localhost:8080/photo/likedislike?username="+MainView.authResponse.getUserName(),HttpMethod.PUT, httpEntity, Void.class);
    }

    public boolean isLike(PhotoModel photoModel){
        return restTemplate.postForObject("http://localhost:8080/photo/like?username="+MainView.authResponse.getUserName(),photoModel.getId(), Boolean.class);
    }

    public boolean isDislike(PhotoModel photoModel){
        HttpEntity<PhotoModel> httpEntity = new HttpEntity(photoModel);
        return restTemplate.postForObject("http://localhost:8080/photo/dislike?username="+MainView.authResponse.getUserName(),photoModel.getId(), Boolean.class);
    }

    public void getBy(PhotoModel photoModel){
        PhotoModel aux = restTemplate.getForObject("http://localhost:8080/photo/getBy?id="+photoModel.getId(), PhotoModel.class);
        photoModel.setPicture(aux.getPicture());
        photoModel.setDescription(aux.getDescription());
    }

    public void setLikes(PhotoModel photoModel){
        photoModel.setLikes(restTemplate.getForObject("http://localhost:8080/photo/like?id="+photoModel.getId(),Integer.class));
    }

    public void setDislikes(PhotoModel photoModel){
        photoModel.setDislikes(restTemplate.getForObject("http://localhost:8080/photo/dislike/?id="+photoModel.getId(),Integer.class));
    }

    public void getUsername(PhotoModel photoModel){
        String username = restTemplate.getForObject("http://localhost:8080/photo/username?id="+photoModel.getId(),String.class);
        User user = new User();
        user.setUserName(username);
        photoModel.setUser(user);
    }

}
