package com.gitstatisctics.controller;

import com.gitstatisctics.dto.UserResponseDTO;
import com.gitstatisctics.service.UserStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
@RestController
public class UserStatisticController {

    private final UserStatisticService userStatisticService;

    @GetMapping(value = "/{login}")
    ResponseEntity<UserResponseDTO> getUserInfo(@PathVariable String login) {
        return ResponseEntity.ok(userStatisticService.fetchUserInfo(login));
    }

}
