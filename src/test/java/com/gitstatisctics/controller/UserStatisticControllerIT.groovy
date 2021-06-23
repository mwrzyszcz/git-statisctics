package com.gitstatisctics.controller

import com.gitstatisctics.client.GitUserClient
import com.gitstatisctics.config.MockConfiguration
import com.gitstatisctics.fixtures.UserFixtures
import com.gitstatisctics.repository.UserRepository
import com.gitstatisctics.service.UserStatisticService
import feign.FeignException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import java.time.LocalDateTime

import static org.hamcrest.Matchers.equalTo
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Import(MockConfiguration.class)
@AutoConfigureMockMvc
@SpringBootTest
class UserStatisticControllerIT extends Specification {

    private static final String API_URL = "/api/users/"

    @Autowired
    UserRepository repository

    @Autowired
    UserStatisticService statisticService

    @Autowired
    MockMvc mockMvc

    @Autowired
    GitUserClient client

    void cleanup() {
        repository.deleteAll()
    }

    def "should return HTTP 200 with correct data"() {
        given:
            def givenLogin = "octocat"
            def now = LocalDateTime.now()
            def givenRequestCount = 15;
            def gitResponse = UserFixtures.createGitUserDTOResponse(now)
            def entity = UserFixtures.createUserEntity(givenLogin, givenRequestCount)
            repository.save(entity)

        when:
            def result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL + givenLogin))

        then:
            client.fetchUserInfo(givenLogin) >> gitResponse
            def actualEntity = repository.findByLogin(givenLogin).get()
            with(actualEntity) {
                login == givenLogin
                requestCount == 16
            }
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath('$.id', equalTo(1000)))
                    .andExpect(jsonPath('$.login', equalTo(gitResponse.login)))
                    .andExpect(jsonPath('$.name', equalTo(gitResponse.name)))
                    .andExpect(jsonPath('$.type', equalTo(gitResponse.type)))
                    .andExpect(jsonPath('$.createdAt', equalTo(now.toString())))
                    .andExpect(jsonPath('$.avatarUrl', equalTo(gitResponse.avatarUrl)))
                    .andExpect(jsonPath('$.calculations', equalTo("0.02400")))

    }

    def "should return HTTP 404 if user not found"() {
        given:
            def givenLogin = "randomUser"
            def givenRequestCount = 15;
            def entity = UserFixtures.createUserEntity(givenLogin, givenRequestCount)
            repository.save(entity)

        when:
            def result = mockMvc.perform(MockMvcRequestBuilders.get(API_URL + givenLogin))

        then:
            1 * client.fetchUserInfo(_) >> {
                throw new FeignException(404, "Not Found")
            }
            def actualEntity = repository.findByLogin(givenLogin).get()
            with(actualEntity) {
                login == givenLogin
                requestCount == 15
            }
            result
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(APPLICATION_JSON))
    }
}
