package com.example.application.data.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequestPhoto {
    private String username;
    private byte[] image;
    private String description;
}
