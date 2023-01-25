package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
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
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
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
        val request = UserUpdateRequest(savedUser.id!!, "B")


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

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다.")
    fun getUserLoanHistoriesTest1() {
        // Given
        userRepository.save(User("A", null))

        // When
        val result = userService.getUserLoanHistories()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("A")
        assertThat(result[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작한다.")
    fun getUserLoanHistoriesTest2() {
        // Given
        val savedUser = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUser, "책1", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser, "책2", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser, "책3", UserLoanStatus.RETURNED),
        ))

        // When
        val result = userService.getUserLoanHistories()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("A")
        assertThat(result[0].books).hasSize(3)
        assertThat(result[0].books).extracting("name")
            .containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(result[0].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)
    }
}