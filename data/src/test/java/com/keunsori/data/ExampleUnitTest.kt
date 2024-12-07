package com.keunsori.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keunsori.domain.entity.WordDefinition
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun parseJsonString_isCorrect() {
        val targetString = "[{\"pos\":\"동사\",\"meanings\":[\"한 곳에서 다른 곳으로 장소를 이동하다.\",\"배, 비행기, 자동차 등이 일정한 곳을 다니다.\",\"어떤 목적을 가진 모임에 참석하기 위해 이동하다.\"]},{\"pos\":\"보조 동사\",\"meanings\":[\"앞말이 나타내는 동작이나 상태, 상태 변화가 계속되거나 진행되는 것을 나타내는 말.\",\"앞말이 나타내는 동작을 이따금씩 반복하여 계속하는 것을 나타내는 말.\"]}]"
        val model = Gson().fromJson<List<WordDefinition>>(targetString, object: TypeToken<List<WordDefinition>>() {}.type)

        assertEquals("동사", model.first().pos)
    }
}