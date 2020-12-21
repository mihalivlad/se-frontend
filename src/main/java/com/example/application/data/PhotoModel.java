package com.example.application.data;

import com.example.application.data.entity.User;
import lombok.*;

import javax.persistence.*;

import java.sql.Blob;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PhotoModel {
    private Long id;
    private User user;
    private byte[] picture;
    private Integer likes;
    private Integer dislikes;
    private String description;

}
