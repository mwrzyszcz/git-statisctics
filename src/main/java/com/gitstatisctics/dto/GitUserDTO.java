package com.gitstatisctics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class GitUserDTO {

    String login;

    Long id;

    @JsonProperty("avatar_url")
    String avatarUrl;

    String name;

    String type;

    Long followers;

    @JsonProperty("public_repos")
    Long publicRepos;

    @JsonProperty("created_at")
    LocalDateTime createdAt;

}
