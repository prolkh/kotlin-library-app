package com.group.libraryapp

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// Junit5 에서 사용되는 5가지 어노테이션
// @Test, @BeforeEach, @AfterEach, @BeforeAll, @AfterAll

class JunitTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            println("> Before all test")
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            println("> After all Test")
        }
    }

    @BeforeEach
    fun beforeEach() {
        println(">> Before each test")
    }

    @AfterEach
    fun afterEach() {
        println(">> After each test")
    }

    @Test
    fun test1() {
        println(">>> Test1")
    }

    @Test
    fun test2() {
        println(">>> Test2")
    }
}