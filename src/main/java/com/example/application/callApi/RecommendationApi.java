package com.example.application.callApi;

import com.example.application.data.PhotoModel;
import com.example.application.views.main.MainView;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class RecommendationApi {

    private RestTemplate restTemplate = new RestTemplate();

    public List<PhotoModel> getRecom(){
        PhotoModel[] photoModelsArray = restTemplate.getForObject("http://localhost:8080/recom/?username="+ MainView.authResponse.getUserName(), PhotoModel[].class);
        return Arrays.asList(photoModelsArray);
    }

    //public List<PhotoModel> initRecom(String username){
    //    PhotoModel[] photoModelsArray = restTemplate.postForObject("http://localhost:8080/recom/",username, PhotoModel[].class);
    //    return Arrays.asList(photoModelsArray);
   // }
}
