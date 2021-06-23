package com.gitstatisctics.config

import com.gitstatisctics.client.GitUserClient
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import spock.mock.DetachedMockFactory

@TestConfiguration
class MockConfiguration {

    DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

    @Bean
    @Primary
    GitUserClient gitUserClient() {
        return detachedMockFactory.Mock(GitUserClient)
    }
}
