package com.keunsori.towerdle

import com.keunsori.towerdle.data.handler.StringToCharArrayParser
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun parsingWord_isCorrect() {
        val actual = StringToCharArrayParser("안녕")
        val expected = charArrayOf('ㅇ', 'ㅏ','ㄴ','ㄴ','ㅕ','ㅇ')

        assertTrue(actual.contentEquals(expected))
    }
}
