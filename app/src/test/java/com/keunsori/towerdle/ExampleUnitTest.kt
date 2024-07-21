package com.keunsori.towerdle

import com.keunsori.towerdle.data.handler.InGameLogicHandler
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun parsingWord_isCorrect() {
        val handler = InGameLogicHandler()
        val actual = handler.parseStringWordToArray("안녕")
        val expected = charArrayOf('ㅇ', 'ㅏ','ㄴ','ㄴ','ㅕ','ㅇ')

        assertTrue(actual.contentEquals(expected))
    }
}
