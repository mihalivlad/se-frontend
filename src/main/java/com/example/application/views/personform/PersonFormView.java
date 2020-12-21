package com.example.application.views.personform;

import com.example.application.callApi.RecommendationApi;
import com.example.application.callApi.UserApi;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;

@Route(value = "sign-up", layout = MainView.class)
@PageTitle("Sign up")
@CssImport("./styles/views/signupform/sign-up-view.css")
public class PersonFormView extends Div {

    private TextField username = new TextField("Username");
    private EmailField email = new EmailField("Email address");
    private PasswordField password = new PasswordField("Password");
    private PasswordField verifyPassword = new PasswordField("Verify Password");
    private Anchor anchor = new Anchor("http://localhost:4200/login", "Log In");

    private Button signup = new Button("Sign up");

    private Binder<User> binder = new Binder(User.class);
    private RecommendationApi recommendationApi = new RecommendationApi();

    public PersonFormView() {
        setId("sign-up-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        //binder.bindInstanceFields(this);

        clearForm();
        signup.addClickListener(e -> {
            if(username.isInvalid() || email.isInvalid() || password.isInvalid() || verifyPassword.isInvalid() ||
               username.getValue().equals("") || email.getValue().equals("") || password.getValue().equals("")) {
                Notification.show("Error",1000,Position.MIDDLE);
            }else{
                    UserApi.callServiceSignup(new User(email.getValue(), password.getValue(), username.getValue()));
                    Notification.show("User registered!",1000,Position.MIDDLE);
                    //recommendationApi.initRecom(username.getValue());
                    clearForm();
            }
        });
    }

    private void clearForm() {
        binder.setBean(new User());
    }

    private Component createTitle() {
        return new H3("User registration");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Please enter a valid email address");
        password.setErrorMessage("Please verify your password");
        username.setErrorMessage("Name must contain at least three characters");
        allBinder();
        formLayout.add(username, email, password, verifyPassword);
        return formLayout;
    }

    private void allBinder() {
        binder.forField(email)
                .withValidator(new EmailValidator(""))
                .withValidationStatusHandler(status -> {
                    email.setInvalid(status.isError());
                })
                .bind(User::getEmail, User::setEmail);


        binder.forField(username)
                // Define the validator
                .withValidator(
                        name -> name.length() >= 3, username.getErrorMessage())
                // Define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    username.setInvalid(status.isError());
                })
                // Finalize the binding
                .bind(User::getUserName, User::setUserName);
        binder.forField(password)
                // Define the validator
                .withValidator(
                        password -> password.equals(verifyPassword.getValue()), password.getErrorMessage())
                // Define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    password.setInvalid(status.isError());
                })
                // Finalize the binding
                .bind(User::getPassword, User::setPassword);
        binder.forField(verifyPassword)
                // Define the validator
                .withValidator(
                        verifyPassword -> verifyPassword.equals(password.getValue()), password.getErrorMessage())
                // Define how the validation status is displayed
                .withValidationStatusHandler(status -> {
                    password.setInvalid(status.isError());
                })
                // Finalize the binding
                .bind(User::getPassword, User::setPassword);
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        signup.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(signup);
        buttonLayout.add(anchor);
        buttonLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return buttonLayout;
    }

}
