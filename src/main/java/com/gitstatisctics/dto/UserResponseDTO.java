package com.gitstatisctics.dto;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class UserResponseDTO {

    Long id;
    String login;
    String name;
    String type;
    String avatarUrl;
    LocalDateTime createdAt;
    String calculations;

}
