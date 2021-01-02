package com.example.application.views.userView;

import com.example.application.callApi.PhotoApi;
import com.example.application.callApi.RecommendationApi;
import com.example.application.data.PhotoModel;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import org.springframework.web.client.HttpServerErrorException;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

@Route(value = "username", layout = MainView.class)
@PageTitle("user profile")
@CssImport(value = "./styles/views/myprofile/myprofile-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class UserView extends Div
        implements HasUrlParameter<String>, AfterNavigationObserver {

    Grid<PhotoModel> grid = new Grid<>();
    Button button = new Button("post");
    Div output = new Div();
    MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    Upload upload = new Upload(buffer);
    Button followButton = new Button();
    private boolean enableUpload = true;
    private String filename;
    private PhotoApi photoApi = new PhotoApi();
    private RecommendationApi recommendationApi = new RecommendationApi();
    private List<PhotoModel> photoModels;
    private String username;

    @Override
    public void setParameter(BeforeEvent event,
                             String parameter) {
        username = parameter;
        if(MainView.authResponse.getUserName().equals("")){
            add(new Label("You are not login"));
        }else {
            try {
                if (recommendationApi.isFollow(username)) {
                    followButton.setText("Unfollow");
                } else {
                    followButton.setText("Follow");
                }
            }catch (NullPointerException ex){
                UI.getCurrent().navigate("account");
                return;
            }

            followButton.addClickListener(e->{
                recommendationApi.setFollow(username);
                if(followButton.getText().equals("Follow")){
                    followButton.setText("Unfollow");
                }else{
                    followButton.setText("Follow");
                }
            });
            setId("myprofile-view");
            addClassName("myprofile-view");
            setSizeFull();
            grid.setHeight("100%");
            grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
            grid.addComponentColumn(photoModel -> createCard(photoModel));
            add(followButton);
            add(grid);
        }
    }
    private HorizontalLayout createCard(PhotoModel photoModel) {
        photoApi.setDislikes(photoModel);
        photoApi.setLikes(photoModel);
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(photoModel.getPicture()));
        Image image = new Image(resource, "dummy image");
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(username);
        name.addClassName("name");
//        Span date = new Span(person.getDate());
//        date.addClassName("date");
        header.add(name);

        Span post = new Span(photoModel.getDescription());
        post.addClassName("post");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon likeIcon = new Icon(VaadinIcon.THUMBS_UP);
        if(photoApi.isLike(photoModel)){
            likeIcon.setColor("green");
        }else {
            likeIcon.setColor("black");
        }
        Span likes = new Span(String.valueOf(photoModel.getLikes()));
        likes.addClassName("likes");
        Icon dislikeIcon = new Icon(VaadinIcon.THUMBS_DOWN);
        if(photoApi.isDislike(photoModel)){
            dislikeIcon.setColor("red");
        }else {
            dislikeIcon.setColor("black");
        }
        Span dislikes = new Span(String.valueOf(photoModel.getDislikes()));
        dislikes.addClassName("dislikes");
        actions.add(likeIcon, likes, dislikeIcon, dislikes);
        likeIcon.addClickListener(e->{
            if(likeIcon.getColor().equals("green")){
                likeIcon.setColor("black");
                photoModel.setLikes(photoModel.getLikes()-1);
                likes.setText(String.valueOf(photoModel.getLikes()));
                photoApi.updateLike(photoModel);
            }else {
                likeIcon.setColor("green");
                photoModel.setLikes(photoModel.getLikes()+1);
                likes.setText(String.valueOf(photoModel.getLikes()));
                if(dislikeIcon.getColor().equals("red")) {
                    photoModel.setDislikes(photoModel.getDislikes()-1);
                    dislikes.setText(String.valueOf(photoModel.getDislikes()));
                    dislikeIcon.setColor("black");
                    photoApi.updateLikeDislike(photoModel);
                }else{
                    photoApi.updateLike(photoModel);
                }
            }
        });

        dislikeIcon.addClickListener(e->{
            if(dislikeIcon.getColor().equals("red")){
                dislikeIcon.setColor("black");
                photoModel.setDislikes(photoModel.getDislikes()-1);
                dislikes.setText(String.valueOf(photoModel.getDislikes()));
                photoApi.updateDislike(photoModel);
            }else {
                dislikeIcon.setColor("red");
                photoModel.setDislikes(photoModel.getDislikes()+1);
                dislikes.setText(String.valueOf(photoModel.getDislikes()));
                if(likeIcon.getColor().equals("green")) {
                    photoModel.setLikes(photoModel.getLikes()-1);
                    likes.setText(String.valueOf(photoModel.getLikes()));
                    likeIcon.setColor("black");
                    photoApi.updateLikeDislike(photoModel);
                }else{
                    photoApi.updateDislike(photoModel);
                }
            }
        });

        description.add(header, post, actions);
        card.add(image, description);
        return card;
    }
    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Set some data when this view is displayed.
        try {
            photoModels = photoApi.getAllByUsername(username);
            Collections.reverse(photoModels);
        }catch (HttpServerErrorException ex){
            UI.getCurrent().navigate("account");
            return;
        }
        for (PhotoModel photoModel: photoModels) {
            photoModel.setPicture(photoApi.getImage(photoModel.getId()));
        }

        grid.setItems(photoModels);
    }




}