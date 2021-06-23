package com.gitstatisctics.service;

import com.gitstatisctics.client.GitUserClient;
import com.gitstatisctics.domain.UserEntity;
import com.gitstatisctics.dto.GitUserDTO;
import com.gitstatisctics.dto.UserResponseDTO;
import com.gitstatisctics.exception.GitUserException;
import com.gitstatisctics.repository.UserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserStatisticService {

    private static final BigDecimal DIVIDEND = valueOf(6);
    private final UserRepository userRepository;
    private final GitUserClient gitUserClient;

    public UserResponseDTO fetchUserInfo(final String login) {
        log.debug("Start fetching information for {}", login);
        final GitUserDTO userInfo = getGitUserInformation(login);

        UserEntity user = userRepository.findByLogin(login)
                .orElseGet(() -> createNewUser(login));
        user.increaseRequestCounter();
        userRepository.save(user);

        return createResponseDTO(userInfo);
    }

    private GitUserDTO getGitUserInformation(String login) {
        try {
            return gitUserClient.fetchUserInfo(login);
        } catch (FeignException ex) {
            String responseMessage = extractResponseMessage(login, ex);
            log.error("Cannot fetch information about {} cause of: {}", login, responseMessage);
            throw new GitUserException(responseMessage, ex.status());
        }
    }

    private UserEntity createNewUser(String login) {
        return UserEntity.builder().login(login).requestCount(0L).build();
    }

    private String calculate(final GitUserDTO userInfo) {
        Long followers = userInfo.getFollowers();
        if (Objects.isNull(userInfo.getFollowers()) || Objects.isNull(userInfo.getPublicRepos())) {
            log.error("Cannot calculate this magic number :)");
            return "";
        }
        if (followers == 0) {
            log.error("Cannot divide by ZERO!");
            return "";
        }

        BigDecimal publicReposPlusTwo = valueOf(2 + userInfo.getPublicRepos());
        return DIVIDEND
                .divide(valueOf(followers), 5, HALF_UP)
                .multiply(publicReposPlusTwo)
                .toPlainString();
    }

    private UserResponseDTO createResponseDTO(GitUserDTO userInfo) {
        String calculations = calculate(userInfo);
        return UserResponseDTO.builder()
                .login(userInfo.getLogin())
                .id(userInfo.getId())
                .name(userInfo.getName())
                .type(userInfo.getType())
                .createdAt(userInfo.getCreatedAt())
                .avatarUrl(userInfo.getAvatarUrl())
                .calculations(calculations)
                .build();
    }

    private String extractResponseMessage(String login, FeignException ex) {
        return ex.responseBody()
                .map(StandardCharsets.UTF_8::decode)
                .map(CharBuffer::toString)
                .orElse("Cannot fetch information about " + login);
    }

}
