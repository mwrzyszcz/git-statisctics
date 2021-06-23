package com.gitstatisctics.service

import com.gitstatisctics.client.GitUserClient
import com.gitstatisctics.domain.UserEntity
import com.gitstatisctics.exception.GitUserException
import com.gitstatisctics.repository.UserRepository
import feign.FeignException
import spock.lang.Specification

import java.time.LocalDateTime

import static com.gitstatisctics.fixtures.UserFixtures.createGitUserDTOResponse
import static com.gitstatisctics.fixtures.UserFixtures.createUserEntity

class UserStatisticServiceTest extends Specification {

    UserRepository repository = Mock()
    GitUserClient client = Mock()
    UserStatisticService service

    def setup() {
        service = new UserStatisticService(repository, client)
    }

    def "should correctly return response"() {
        given:
            def login = "octocat";
            def now = LocalDateTime.now()
            def gitResponse = createGitUserDTOResponse(now)
            def userEntity = createUserEntity(login, 0)

        when:
            def userInfo = service.fetchUserInfo(login)

        then:
            1 * client.fetchUserInfo(login) >> gitResponse
            1 * repository.findByLogin(login) >> Optional.of(userEntity)
            1 * repository.save(_ as UserEntity)
            0 * _
            with(userInfo) {
                id == gitResponse.id
                login == gitResponse.login
                name == gitResponse.name
                type == gitResponse.type
                avatarUrl == gitResponse.avatarUrl
                createdAt == gitResponse.createdAt
                calculations == "0.02400"
            }

    }

    def "should throw exception if git not responding"() {
        given:
            def expectedMessage = "Cannot fetch information about abc"

        when:
            service.fetchUserInfo("abc")

        then:
            1 * client.fetchUserInfo(_) >> {
                throw new FeignException(404, "Not Found")
            }
            def exception = thrown(GitUserException.class)
            with(exception) {
                errorCode == 404
                message == expectedMessage
            }
            0 * repository.findByLogin(_)
            0 * repository.save()
            0 * _

    }
}
