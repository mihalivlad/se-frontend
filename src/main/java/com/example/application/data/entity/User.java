package com.example.application.data.entity;

import lombok.*;
import org.atmosphere.config.service.Get;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {
  private String email;
  private String password;
  private String userName;
}
