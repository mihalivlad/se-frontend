package com.example.application.callApi;

import com.example.application.data.PhotoModel;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.UI;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    public Integer getIndex(){
        return restTemplate.getForObject("http://localhost:8080/recom/index?username="+ MainView.authResponse.getUserName(), Integer.class);
    }

    public Integer setIndex(Integer value){
        return restTemplate.postForObject("http://localhost:8080/recom/index?username="+ MainView.authResponse.getUserName(), value, Integer.class);
    }

    public List<String> getFollow(){
        return Arrays.asList(restTemplate.getForObject("http://localhost:8080/recom/follow?username="+MainView.authResponse.getUserName(),String[].class));
    }

    public List<String> setFollow(String username){
        return Arrays.asList(restTemplate.postForObject("http://localhost:8080/recom/follow/?username="+MainView.authResponse.getUserName(),username,String[].class));
    }

    public Boolean isFollow(String username) {
        try {
            return restTemplate.getForObject("http://localhost:8080/recom/follow/" + MainView.authResponse.getUserName() + "/" + username, Boolean.class);

        } catch (HttpServerErrorException ex) {
            UI.getCurrent().navigate("account");
            return null;
        }
    }

}
