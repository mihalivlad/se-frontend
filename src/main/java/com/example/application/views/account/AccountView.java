package com.example.application.views.account;

import com.example.application.data.entity.User;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "account", layout = MainView.class)
@PageTitle("Account")
@CssImport(value = "./styles/views/myprofile/myprofile-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class AccountView extends Div implements AfterNavigationObserver {

    private EmailField email = new EmailField("Email address");
    private PasswordField oldPassword = new PasswordField("Old Password");
    private PasswordField newPassword = new PasswordField("New Password");
    private PasswordField verifyPassword = new PasswordField("Verify New Password");

    private Button save = new Button("Save changes");
    private Binder<User> binder = new Binder(User.class);

    Grid<Person> grid = new Grid<>();

    public AccountView() {
        if (MainView.authResponse.getUserName().equals("")) {
            add(new Label("You are not login"));
        } else {

            VerticalLayout fl = new VerticalLayout();
            fl.add(email);
            fl.add(oldPassword);
            fl.add(newPassword);
            fl.add(verifyPassword);
            fl.add(save);
            fl.setAlignItems(FlexComponent.Alignment.CENTER);
            add(fl);

            email.setValue(MainView.authResponse.getEmail());

            setId("account-view");
            addClassName("account-view");
//            setSizeFull();
//            grid.setHeight("100%");
//            grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
//            grid.addComponentColumn(person -> createCard(person));
            add(grid);

            save.addClickListener(e -> {
                if (oldPassword.isInvalid() || email.isInvalid() ||
                        oldPassword.getValue().equals("") || email.getValue().equals("")) {
                    Notification.show("Error", 1000, Notification.Position.MIDDLE);
                } else {
                    if (newPassword.getValue().equals("")) {
                        Notification.show("Changes saved!", 1000, Notification.Position.MIDDLE);
                    } else {
                        if (newPassword.isInvalid() || verifyPassword.isInvalid() || !newPassword.getValue().equals(verifyPassword.getValue())) {
                            Notification.show("Error", 1000, Notification.Position.MIDDLE);
                        } else {
                            Notification.show("Changes saved!", 1000, Notification.Position.MIDDLE);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
    }

    private static Person createPerson(String image, String name, String date, String post, String likes,
                                       String comments, String shares) {
        Person p = new Person();
        p.setImage(image);
        p.setName(name);
        p.setDate(date);
        p.setPost(post);
        p.setLikes(likes);
        p.setComments(comments);
        p.setShares(shares);

        return p;
    }
}
