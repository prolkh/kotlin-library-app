package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
){

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장이 정상동작한다.")
    fun saveUserTest() {
        // Given
        val request = UserCreateRequest("이경호", null)

        // When
        userService.saveUser(request)

        // Then
        val result = userRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("이경호")
        assertThat(result[0].age).isNull()
    }

    @Test
    @DisplayName("유저 조회가 정상 동작한다.")
    fun getUserTest() {
        // Given
        userRepository.saveAll(listOf(
            User("A", 20),
            User("B", null),
        ))

        // When
        val result = userService.getUsers()

        // Then
        assertThat(result).hasSize(2)
    }

    @Test
    @DisplayName("유저 업데이트가 정상 동작한다.")
    fun updateUserTest() {
        // Given
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id, "B")


        // When
        userService.updateUserName(request)

        // Then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제가 정상 동작한다.")
    fun deleteUserTest() {
        // Given
        userRepository.save(User("A", null))

        // When
        userService.deleteUser("A")

        // Then
        assertThat(userRepository.findAll()).isEmpty()
    }
}