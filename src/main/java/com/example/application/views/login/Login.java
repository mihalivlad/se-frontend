package com.example.application.views.login;

import com.example.application.callApi.RecommendationApi;
import com.example.application.callApi.UserApi;
import com.example.application.data.LoginModel;
import com.example.application.views.main.MainView;
import com.example.application.views.photofeed.PhotoFeedView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Route(value = "login", layout = MainView.class)
@PageTitle("Login")
@CssImport("./styles/views/signupform/sign-up-view.css")
public class Login extends Div {

    public static final String ROUTE = "login";

    private LoginForm login = new LoginForm(); //
    private Anchor anchor = new Anchor("http://localhost:4200/sign-up", "Sign Up");

    public Login() {
        login.setForgotPasswordButtonVisible(false);
        VerticalLayout fl = new VerticalLayout();
        fl.add(login);
        fl.add(anchor);
        fl.setAlignItems(FlexComponent.Alignment.CENTER);
        add(fl); //

        login.addLoginListener(e -> {
            try {
                MainView.authResponse = UserApi.callServiceLogin(new LoginModel(e.getUsername(), e.getPassword()));
                UI.getCurrent().getPage().setLocation("http://localhost:4200/photo-feed");
                RecommendationApi recommendationApi = new RecommendationApi();
                PhotoFeedView.setIndex(recommendationApi.getIndex());
                PhotoFeedView.setMaxIndex(PhotoFeedView.getIndex());
            } catch (HttpClientErrorException ex) {
                if (ex.getRawStatusCode() == 403) {
                    Notification.show("username or password invalid", 2000, Position.MIDDLE);
                    login.setError(true);
                }
            }

        });
    }

}
