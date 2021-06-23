package com.gitstatisctics.client;

import com.gitstatisctics.dto.GitUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "GitUserClient", url = "${git.users.url}", primary = false)
public interface GitUserClient {

    @GetMapping(value = "/{username}")
    GitUserDTO fetchUserInfo(@PathVariable String username);
}
