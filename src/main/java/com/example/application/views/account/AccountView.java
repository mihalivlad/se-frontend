package com.example.application.views.account;

import com.example.application.callApi.UserApi;
import com.example.application.data.entity.User;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "account", layout = MainView.class)
@PageTitle("Account")
@CssImport(value = "./styles/views/myprofile/myprofile-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class AccountView extends Div {

    private EmailField email = new EmailField("Email address");
    private PasswordField oldPassword = new PasswordField("Old Password");
    private PasswordField newPassword = new PasswordField("New Password");
    private PasswordField verifyPassword = new PasswordField("Verify New Password");
    private Button delete = new Button("Delete account");
    private Button save = new Button("Save changes");
    private Binder<User> binder = new Binder(User.class);

    public AccountView() {
        if (MainView.authResponse.getUserName().equals("")) {
            add(new Label("You are not login!"));
        } else {
            VerticalLayout fl = new VerticalLayout();
            VerticalLayout flD = new VerticalLayout();
            flD.add(delete);
            fl.add(email);
            fl.add(oldPassword);
            fl.add(newPassword);
            fl.add(verifyPassword);
            fl.add(save);
            flD.setAlignItems(FlexComponent.Alignment.END);
            fl.setAlignItems(FlexComponent.Alignment.CENTER);
            add(flD);
            add(fl);

            email.setValue(MainView.authResponse.getEmail());

            setId("account-view");
            addClassName("account-view");

            delete.addClickListener(e -> {
                    //UserDeleteDetails userDelete = new UserDeleteDetails();
                   // userDelete.setUserName();
                    String response = UserApi.callServiceDelete(MainView.authResponse.getUserName());
                    if (!response.isEmpty()) {
                        Notification.show(response, 1000, Notification.Position.MIDDLE);
                    }
                    //Notification.show("Account deleted!", 1000, Notification.Position.MIDDLE);
            });

            save.addClickListener(e -> {
                if (oldPassword.isInvalid() || email.isInvalid() ||
                        oldPassword.getValue().equals("") || email.getValue().equals("")) {
                    Notification.show("Password or email field empty!", 1000, Notification.Position.MIDDLE);
                } else {
                    if (newPassword.getValue().equals("")) {
                        UserUpdateDetails userUpdate = new UserUpdateDetails();
                        userUpdate.setEmail(email.getValue());
                        userUpdate.setUserName(MainView.authResponse.getUserName());
                        String response = UserApi.callServiceUpdate(userUpdate);
                        if (!response.isEmpty()) {
                            Notification.show(response, 1000, Notification.Position.MIDDLE);
                        }
                        Notification.show("Changes saved!", 1000, Notification.Position.MIDDLE);
                    } else {
                        if (newPassword.isInvalid() || verifyPassword.isInvalid() || !newPassword.getValue().equals(verifyPassword.getValue())) {
                            Notification.show("Passwords do not match or are invalid!", 1000, Notification.Position.MIDDLE);
                        } else {
                            UserUpdateDetails userUpdate = new UserUpdateDetails();
                            userUpdate.setEmail(email.getValue());
                            userUpdate.setNewPassword(newPassword.getValue());
                            userUpdate.setOldPassword(oldPassword.getValue());
                            userUpdate.setUserName(MainView.authResponse.getUserName());
                            String response = UserApi.callServiceUpdate(userUpdate);
                            if (!response.isEmpty()) {
                                Notification.show(response, 1000, Notification.Position.MIDDLE);
                            }
                            Notification.show("Changes saved!", 1000, Notification.Position.MIDDLE);
                        }
                    }
                }
            });
        }
    }
}
