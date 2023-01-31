package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
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
        val request = BookRequest("탈무드", BookType.COMPUTER)

        // When
        bookService.saveBook(request)

        // Then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("탈무드")
        assertThat(books[0].type).isEqualTo(BookType.COMPUTER)
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
        assertThat(result[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    @DisplayName("책이 이미 대출되어 있는 경우, 대출이 실패한다.")
    fun loanBookFailTest() {
        // Given
        val savedBook = bookRepository.save(Book.fixture("탈무드"))
        val savedUser = userRepository.save(User("이경호", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, savedBook.name))
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
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, savedBook.name))
        val request = BookReturnRequest("이경호", "탈무드")

        // When
        bookService.returnBook(request)

        // Then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }


    @Test
    @DisplayName("대여 중인 책을 카운트한다.")
    fun countLoanedBookTest() {
        // Given
        val user = userRepository.save(User("이경호", null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(user, "A"),
            UserLoanHistory.fixture(user, "B", UserLoanStatus.RETURNED),
        ))

        // When
        val result = bookService.countLoanBook()

        // Then
        assertThat(result).isEqualTo(1)
    }

    @Test
    @DisplayName("분야별로 책을 카운트한다.")
   fun getBookStatisticsTest() {
        // Given
        bookRepository.saveAll(listOf(
            Book.fixture("A", BookType.COMPUTER),
            Book.fixture("B", BookType.COMPUTER),
            Book.fixture("C", BookType.SCIENCE),
        ))

        // When
        val result = bookService.getBookStatistics()

        // Then
        assertThat(result).hasSize(2)
        assertCount(result, BookType.COMPUTER, 2)
        assertCount(result, BookType.SCIENCE, 1)
    }

    private fun assertCount(list: List<BookStatResponse>, type: BookType, count: Int) {
        assertThat(list.first { item -> item.type == type }.count).isEqualTo(count)
    }
}