package com.example.application.views.photofeed;

import com.example.application.callApi.PhotoApi;
import com.example.application.callApi.RecommendationApi;
import com.example.application.data.PhotoModel;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Route(value = "photo-feed", layout = MainView.class)
@PageTitle("Photo Feed")
@CssImport(value = "./styles/views/photofeed/photo-feed-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class PhotoFeedView extends Div {

    Grid<PhotoModel> grid = new Grid<>();
    private static int index;
    private static int maxIndex;
    private List<PhotoModel> photoModelList = new ArrayList<>();
    private Button next = new Button(">");
    private Button prev = new Button("<");
    private PhotoApi photoApi = new PhotoApi();
    private RecommendationApi recommendationApi = new RecommendationApi();
    private HorizontalLayout card = new HorizontalLayout();
    private PhotoModel photoModel;

    public PhotoFeedView() {
        if (MainView.authResponse.getUserName().equals("")) {
            add(new Label("You are not login"));
        } else {
            try {
                photoModelList = recommendationApi.getRecom();
                for (PhotoModel photoModel : photoModelList) {
                    photoApi.getUsername(photoModel);
                }
                setId("photo-feed-view");
                addClassName("photo-feed-view");
                setSizeFull();
                grid.setHeight("100%");
                grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
                photoModel = photoModelList.get(index);
                photoModel.setPicture(photoApi.getImage(photoModel.getId()));
                createCard(photoModel);
                add(prev);
                add(card);
                add(next);
                prev.addClickListener(e -> {
                    next.setVisible(true);
                    try {
                        photoModel = photoModelList.get(index - 1);
                        photoModel.setPicture(photoApi.getImage(photoModel.getId()));
                        createCard(photoModel);
                        index--;
                    } catch (IndexOutOfBoundsException ex) {
                        prev.setVisible(false);
                    }
                });
                next.addClickListener(e -> {
                    prev.setVisible(true);
                    try {
                        photoModel = photoModelList.get(index + 1);
                        photoModel.setPicture(photoApi.getImage(photoModel.getId()));
                        createCard(photoModel);
                        index++;
                        if (index > maxIndex) {
                            maxIndex = index;
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        next.setVisible(false);
                    }
                });
            } catch (ArrayIndexOutOfBoundsException ex) {
                add(new Label("No posts available!"));
            }

        }
    }

    public static int getMaxIndex() {
        return maxIndex;
    }

    public static void setMaxIndex(int maxIndex) {
        PhotoFeedView.maxIndex = maxIndex;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        PhotoFeedView.index = index;
    }

    private void createCard(PhotoModel photoModel) {
        photoApi.setDislikes(photoModel);
        photoApi.setLikes(photoModel);
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

        Anchor name = new Anchor("http://localhost:4200/username/" + photoModel.getUser().getUserName()
                , photoModel.getUser().getUserName());
        name.addClassName("name");
        header.add(name);

        Span post = new Span(photoModel.getDescription());
        post.addClassName("post");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");
        Icon likeIcon = new Icon(VaadinIcon.THUMBS_UP);
        if (photoApi.isLike(photoModel)) {
            likeIcon.setColor("green");
        } else {
            likeIcon.setColor("black");
        }
        Span likes = new Span(String.valueOf(photoModel.getLikes()));
        likes.addClassName("likes");
        Icon dislikeIcon = new Icon(VaadinIcon.THUMBS_DOWN);
        if (photoApi.isDislike(photoModel)) {
            dislikeIcon.setColor("red");
        } else {
            dislikeIcon.setColor("black");
        }
        Span dislikes = new Span(String.valueOf(photoModel.getDislikes()));
        dislikes.addClassName("dislikes");
        actions.add(likeIcon, likes, dislikeIcon, dislikes);
        likeIcon.addClickListener(e -> {
            if (likeIcon.getColor().equals("green")) {
                likeIcon.setColor("black");
                photoModel.setLikes(photoModel.getLikes() - 1);
                likes.setText(String.valueOf(photoModel.getLikes()));
                photoApi.updateLike(photoModel);
            } else {
                likeIcon.setColor("green");
                photoModel.setLikes(photoModel.getLikes() + 1);
                likes.setText(String.valueOf(photoModel.getLikes()));
                if (dislikeIcon.getColor().equals("red")) {
                    photoModel.setDislikes(photoModel.getDislikes() - 1);
                    dislikes.setText(String.valueOf(photoModel.getDislikes()));
                    dislikeIcon.setColor("black");
                    photoApi.updateLikeDislike(photoModel);
                } else {
                    photoApi.updateLike(photoModel);
                }
            }
        });

        dislikeIcon.addClickListener(e -> {
            if (dislikeIcon.getColor().equals("red")) {
                dislikeIcon.setColor("black");
                photoModel.setDislikes(photoModel.getDislikes() - 1);
                dislikes.setText(String.valueOf(photoModel.getDislikes()));
                photoApi.updateDislike(photoModel);
            } else {
                dislikeIcon.setColor("red");
                photoModel.setDislikes(photoModel.getDislikes() + 1);
                dislikes.setText(String.valueOf(photoModel.getDislikes()));
                if (likeIcon.getColor().equals("green")) {
                    photoModel.setLikes(photoModel.getLikes() - 1);
                    likes.setText(String.valueOf(photoModel.getLikes()));
                    likeIcon.setColor("black");
                    photoApi.updateLikeDislike(photoModel);
                } else {
                    photoApi.updateDislike(photoModel);
                }
            }
        });

        description.add(header, post, actions);
        card.removeAll();
        card.add(image, description);
    }


}
