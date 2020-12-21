package com.example.application.callApi;

import com.example.application.data.AuthResponse;
import com.example.application.data.LoginModel;
import com.example.application.data.entity.User;
import com.example.application.views.account.UserUpdateDetails;
import com.example.application.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class UserApi {
    private RestTemplate restTemplate;

    @Autowired
    public UserApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void callServiceSignup(User user) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(restTemplate.postForObject("http://localhost:8080/api/auth/signup", user, String.class));
    }

    public static AuthResponse callServiceLogin(LoginModel loginModel) throws HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        AuthResponse authResponse = restTemplate.postForObject("http://localhost:8080/api/auth/login", loginModel, AuthResponse.class);
        return authResponse;
    }

    public static String callServiceUpdate(UserUpdateDetails userUpdateDetails) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(MainView.authResponse.getAuthenticationToken());
        try {
            return restTemplate.postForObject("http://localhost:8080/api/account/update", new HttpEntity<>(userUpdateDetails, headers), String.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return "Invalid password";
            }
        } catch (HttpServerErrorException ex) {
            return "Internal Server Error";
        }
        return "Unknown error!";
    }

    public static void refreshToken() throws HttpClientErrorException {
        RefreshTokenData refreshTokenData = new RefreshTokenData();
        refreshTokenData.setRefreshToken(MainView.authResponse.getRefreshToken());
        refreshTokenData.setUserName(MainView.authResponse.getUserName());
        RestTemplate restTemplate = new RestTemplate();
        AuthResponse authResponse = restTemplate.postForObject("http://localhost:8080/api/auth/refresh/token", refreshTokenData, AuthResponse.class);
        MainView.authResponse.setAuthenticationToken(authResponse.getAuthenticationToken());
    }
}