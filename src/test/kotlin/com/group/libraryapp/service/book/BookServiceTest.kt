package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
){

    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("책 등록이 정상 동작한다.")
    fun saveBookTest() {
        // Given
        val request = BookRequest("탈무드", "과학")

        // When
        bookService.saveBook(request)

        // Then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("탈무드")
        assertThat(books[0].type).isEqualTo("과학")
    }

    @Test
    @DisplayName("책 대출이 정상 동작한다.")
    fun loanBookTest() {
        // Given
        bookRepository.save(Book.fixture("탈무드"))
        val savedUser = userRepository.save(User("이경호", null))
        val request = BookLoanRequest("이경호", "탈무드")

        // When
        bookService.loanBook(request)

        // Then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].user.id).isEqualTo(savedUser.id)
        assertThat(result[0].bookName).isEqualTo("탈무드")
        assertThat(result[0].isReturn).isFalse
    }

    @Test
    @DisplayName("책이 이미 대출되어 있는 경우, 대출이 실패한다.")
    fun loanBookFailTest() {
        // Given
        val savedBook = bookRepository.save(Book.fixture("탈무드"))
        val savedUser = userRepository.save(User("이경호", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, savedBook.name, false))
        val request = BookLoanRequest("홍길동", "탈무드")

        // When & Then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다.")
    }

    @Test
    @DisplayName("책 반납이 정상 동작한다.")
    fun returnBookTest() {
        // Given
        val savedBook = bookRepository.save(Book.fixture("탈무드"))
        val savedUser = userRepository.save(User("이경호", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, savedBook.name, false))
        val request = BookReturnRequest("이경호", "탈무드")

        // When
        bookService.returnBook(request)

        // Then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].isReturn).isTrue
    }
}