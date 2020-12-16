package com.example.application.data.entity;

import lombok.*;
import org.atmosphere.config.service.Get;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
  private String email;
  private String password;
  private String userName;
}
