package com.group.libraryapp.calculator

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class JunitCalculatorTest {

    @Test
    fun addTest() {
        // Given
        val calculator = Calculator(5)

        // When
        calculator.add(3)

        // Then
        assertThat(calculator.getNumber()).isEqualTo(8)
    }

    @Test
    fun minusTest() {
        //..
    }

    @Test
    fun multiplyTest() {
        //..
    }

    @Test
    fun divideExceptionTest() {
        // Given
        val calculator = Calculator(5)

        // When & Then
        assertThatThrownBy {
            calculator.divide(0)
        }.isInstanceOf(ArithmeticException::class.java)
            .hasMessage("0으로 나눌 수 없습니다.")
    }
}