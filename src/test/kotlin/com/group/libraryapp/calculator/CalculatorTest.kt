package com.group.libraryapp.calculator

fun main() {
    val calculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multiplyTest()
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
}