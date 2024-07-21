package com.keunsori.towerdle.data.handler

class InGameLogicHandler {
    fun parseStringWordToArray(input: String) : CharArray {
        val list = mutableListOf<Char>()
        val length = input.length

        val df= input.toByteArray()
        val dfd= df.map { it.toh }

        println("!!!!! ${input.toByteArray()}")
        for (i in 0 until length) {
            val oneSyllable = input[i].digitToInt()
            println(oneSyllable)
            if (oneSyllable >= 0xAC00) {

            }
        }
        return list.toCharArray()
    }
}
