package com.example.application.views.myprofile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.example.application.callApi.PhotoApi;
import com.example.application.data.PhotoModel;
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
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.main.MainView;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

@Route(value = "my-profile", layout = MainView.class)
@PageTitle("My profile")
@CssImport(value = "./styles/views/myprofile/myprofile-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class MyprofileView extends Div implements AfterNavigationObserver {

    Grid<PhotoModel> grid = new Grid<>();
    Button button = new Button("Post");
    Div output = new Div();
    MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    Upload upload = new Upload(buffer);
    //Button showButton = new Button("Show");
    private boolean enableUpload = true;
    private String filename;
    private PhotoApi photoApi = new PhotoApi();
    private List<PhotoModel> photoModels;
    private TextArea description = new TextArea("Description");

    public MyprofileView() {
        if(MainView.authResponse.getUserName().equals("")){
            add(new Label("You are not login"));
        }else {
            add(description);
            upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

            upload.addSucceededListener(event -> {
                if(enableUpload) {
                    filename = event.getFileName();
                    Component component = createComponent(event.getMIMEType(),
                            filename,
                            buffer.getInputStream(filename));
                    showOutput(filename, component, output);
                    enableUpload = false;
                }
            });

            add(upload, output);
            add(button);
            setId("myprofile-view");
            addClassName("myprofile-view");
            setSizeFull();
            grid.setHeight("100%");
            grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
            grid.addComponentColumn(photoModel -> createCard(photoModel));
            add(grid);
            button.addClickListener(e->{
                try {
                    byte[] bytes = IOUtils.toByteArray(buffer.getInputStream(filename));
                    photoApi.addPhoto(bytes, description.getValue());
                    //photoModels.add(photoApi.getLast(photoApi.getAllByUsername()));//
                    //grid.setItems(photoModels);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Notification.show("Post was uploaded!", 2000, Notification.Position.MIDDLE);
                UI.getCurrent().getPage().reload();
            });
        }
    }



//    private HorizontalLayout createCard(Person person) {
//        HorizontalLayout card = new HorizontalLayout();
//        card.addClassName("card");
//        card.setSpacing(false);
//        card.getThemeList().add("spacing-s");
//
//        Image image = new Image();
//        image.setSrc(person.getImage());
//        VerticalLayout description = new VerticalLayout();
//        description.addClassName("description");
//        description.setSpacing(false);
//        description.setPadding(false);
//
//        HorizontalLayout header = new HorizontalLayout();
//        header.addClassName("header");
//        header.setSpacing(false);
//        header.getThemeList().add("spacing-s");
//
//        Span name = new Span(person.getName());
//        name.addClassName("name");
//        Span date = new Span(person.getDate());
//        date.addClassName("date");
//        header.add(name, date);
//
//        Span post = new Span(person.getPost());
//        post.addClassName("post");
//
//        HorizontalLayout actions = new HorizontalLayout();
//        actions.addClassName("actions");
//        actions.setSpacing(false);
//        actions.getThemeList().add("spacing-s");
//
//        IronIcon likeIcon = new IronIcon("vaadin", "heart");
//        Span likes = new Span(person.getLikes());
//        likes.addClassName("likes");
//        IronIcon commentIcon = new IronIcon("vaadin", "comment");
//        Span comments = new Span(person.getComments());
//        comments.addClassName("comments");
//        IronIcon shareIcon = new IronIcon("vaadin", "connect");
//        Span shares = new Span(person.getShares());
//        shares.addClassName("shares");
//
//        actions.add(likeIcon, likes, commentIcon, comments, shareIcon, shares);
//
//        description.add(header, post, actions);
//        card.add(image, description);
//        return card;
//    }

//    @Override
//    public void afterNavigation(AfterNavigationEvent event) {
//
//        // Set some data when this view is displayed.
//        List<Person> persons = Arrays.asList( //
//        createPerson("https://randomuser.me/api/portraits/men/42.jpg", "John Smith", "May 8",
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/women/42.jpg", "Abagail Libbie", "May 3",
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/men/24.jpg", "Alberto Raya", "May 3",
//
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/women/24.jpg", "Emmy Elsner", "Apr 22",
//
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/men/76.jpg", "Alf Huncoot", "Apr 21",
//
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/women/76.jpg", "Lidmila Vilensky", "Apr 17",
//
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/men/94.jpg", "Jarrett Cawsey", "Apr 17",
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/women/94.jpg", "Tania Perfilyeva", "Mar 8",
//
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/men/16.jpg", "Ivan Polo", "Mar 5",
//
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/women/16.jpg", "Emelda Scandroot", "Mar 5",
//
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/men/67.jpg", "Marcos Sá", "Mar 4",
//
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20"),
//        createPerson("https://randomuser.me/api/portraits/women/67.jpg", "Jacqueline Asong", "Mar 2",
//
//                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
//                "1K", "500", "20")
//
//      );
//
//        grid.setItems(persons);
//    }
//
//    private static Person createPerson(String image, String name, String date, String post, String likes,
//            String comments, String shares) {
//        Person p = new Person();
//        p.setImage(image);
//        p.setName(name);
//        p.setDate(date);
//        p.setPost(post);
//        p.setLikes(likes);
//        p.setComments(comments);
//        p.setShares(shares);
//
//        return p;
//    }

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

        Span name = new Span(MainView.authResponse.getUserName());
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
        photoModels = photoApi.getAllByUsername();
        Collections.reverse(photoModels);
        for (PhotoModel photoModel: photoModels) {
            photoModel.setPicture(photoApi.getImage(photoModel.getId()));
        }

        grid.setItems(photoModels);
    }





    private Component createComponent(String mimeType, String fileName,
                                      InputStream stream) {
        if (mimeType.startsWith("text")) {
            return createTextComponent(stream);
        } else if (mimeType.startsWith("image")) {
            Image image = new Image();
            try {

                byte[] bytes = IOUtils.toByteArray(stream);
                image.getElement().setAttribute("src", new StreamResource(
                        fileName, () -> new ByteArrayInputStream(bytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(
                        new ByteArrayInputStream(bytes))) {
                    final Iterator<ImageReader> readers = ImageIO
                            .getImageReaders(in);
                    if (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(in);
                            image.setWidth(reader.getWidth(0) + "px");
                            image.setHeight(reader.getHeight(0) + "px");
                        } finally {
                            reader.dispose();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return image;
        }
        Div content = new Div();
        String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'",
                mimeType, MessageDigestUtil.sha256(stream.toString()));
        content.setText(text);
        return content;

    }

    private Component createTextComponent(InputStream stream) {
        String text;
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        return new Text(text);
    }

    private void showOutput(String text, Component content,
                            HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }

}
