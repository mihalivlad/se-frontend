package com.example.application.callApi;

import com.example.application.data.AuthResponse;
import com.example.application.data.LoginModel;
import com.example.application.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


public class UserApi{
    private RestTemplate restTemplate;
    @Autowired
    public UserApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void callServiceSignup (User user){
    RestTemplate restTemplate = new RestTemplate();
    System.out.println(restTemplate.postForObject("http://localhost:8080/api/auth/signup", user, String.class));
}
public static AuthResponse callServiceLogin(LoginModel loginModel) throws HttpClientErrorException {
    RestTemplate restTemplate = new RestTemplate();
    AuthResponse authResponse = restTemplate.postForObject("http://localhost:8080/api/auth/login", loginModel, AuthResponse.class);
    return authResponse;
}
       }