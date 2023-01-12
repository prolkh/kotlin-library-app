package com.group.libraryapp.calculator

fun main() {
    val calculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multiplyTest()
    calculatorTest.divideTest()
    calculatorTest.divideExceptionTest()
}


class CalculatorTest {

    fun addTest() {
        // Given: A calculator with initial value of 5
        val calculator = Calculator(10)

        // When: The calculator adds 3
        calculator.add(18)

        // Then: The calculator's value should be 8
        if(! calculator.isExpectedNumber(28)) {
            throw IllegalStateException()
        }
    }

    fun minusTest() {
        // Given
        val calculator = Calculator(10)

        // When
        calculator.minus(18)

        // Then
        if(! calculator.isExpectedNumber(-8)) {
            throw IllegalStateException()
        }
    }

    fun multiplyTest() {
        // Given
        val calculator = Calculator(10)

        // When
        calculator.multiply(18)

        // Then
        if(! calculator.isExpectedNumber(180)) {
            throw IllegalStateException()
        }
    }

    fun divideTest() {
        // Given
        val calculator = Calculator(5)

        // When
        calculator.divide(2)

        // Then
        if(! calculator.isExpectedNumber(2)) {
            throw IllegalStateException()
        }
    }

    fun divideExceptionTest() {
        // Given
        val calculator = Calculator(5)

        // When
        try {
            calculator.divide(0)
        } catch (e: ArithmeticException) {
            // 테스트 성공!!
            return
        }

        throw IllegalStateException("기대하는 예외가 발생하지 않았습니다.")
    }
}