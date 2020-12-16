package com.example.application.views.main;

import java.time.Instant;
import java.util.Optional;

import com.example.application.data.AuthResponse;
import com.example.application.views.account.AccountView;
import com.example.application.views.login.Login;
import com.example.application.views.myprofile.MyprofileView;
import com.example.application.views.photofeed.PhotoFeedView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.example.application.views.about.AboutView;
import com.example.application.views.personform.PersonFormView;
import lombok.Getter;
import lombok.Setter;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@CssImport(value = "./styles/views/main/main-view.css", themeFor = "vaadin-app-layout")
@CssImport("./styles/views/main/main-view.css")
@PWA(name = "VAD Social", shortName = "VAD Social", enableInstallPrompt = false)

public class MainView extends AppLayout {

    public static AuthResponse authResponse = new AuthResponse("","", Instant.MIN, "", "");
    private final Tabs menu;
    Button logout = new Button("Logout");
    Anchor userNameLabel = new Anchor();

    public MainView() {
        HorizontalLayout header = createHeader();
        menu = createMenuTabs();
        if(!authResponse.getUserName().equals("")) {
            userNameLabel.setHref("http://localhost:4200/my-profile");
            userNameLabel.setText(authResponse.getUserName());
            header.add(userNameLabel);
            header.add(logout);
            logout.addClickListener(e -> {
                authResponse = new AuthResponse("","", Instant.MIN, "", "");
                //To Do
                //Delete Token from user

                UI.getCurrent().getPage().setLocation("http://localhost:4200/sign-up");
            });
        }
        addToNavbar(createTopBar(header, menu));
    }

    private VerticalLayout createTopBar(HorizontalLayout header, Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.getThemeList().add("dark");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(header, menu);
        return layout;
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setPadding(false);
        header.setSpacing(false);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setId("header");
        Image logo = new Image("images/logo.png", "My Project logo");
        logo.setId("logo");
        header.add(logo);
        Image avatar = new Image("images/user.svg", "Avatar");
        avatar.setId("avatar");
        header.add(new H1("VAD Social"));
        header.add(avatar);
        return header;
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.getStyle().set("max-width", "100%");
        tabs.add(getAvailableTabs());
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
                if(authResponse.getUserName().equals("")) {
                    return new Tab[] {
                    createTab("About", AboutView.class),
                            createTab("Sign up", PersonFormView.class),
                            createTab("Login", Login.class)};
                }
                    return new Tab[] {
                            createTab("About", AboutView.class),
                            createTab("Photo feed", PhotoFeedView.class),
                            createTab("My profile", MyprofileView.class),
                            createTab("Account", AccountView.class)};

    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class)
                        .equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }
}
