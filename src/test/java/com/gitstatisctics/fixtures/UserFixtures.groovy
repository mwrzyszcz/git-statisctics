package com.gitstatisctics.fixtures

import com.gitstatisctics.domain.UserEntity
import com.gitstatisctics.dto.GitUserDTO

import java.time.LocalDateTime

class UserFixtures {

    static GitUserDTO createGitUserDTOResponse(LocalDateTime now) {
        GitUserDTO.builder()
                .login("octocat")
                .name("The Octocat")
                .type("User")
                .avatarUrl("https://avatars.githubusercontent.com/u/583231?v=4")
                .createdAt(now)
                .followers(3000)
                .publicRepos(10)
                .id(1000)
                .build()
    }

    static UserEntity createUserEntity(String login, Long requestCount) {
        return UserEntity.builder()
                .login(login)
                .requestCount(requestCount)
                .build()
    }
}
