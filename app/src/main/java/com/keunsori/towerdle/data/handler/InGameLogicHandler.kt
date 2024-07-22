package com.keunsori.towerdle.data.handler

class InGameLogicHandler {
    private val firstLetter = arrayOf(
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
        'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )
    private val middleLetter = arrayOf(
        'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
        'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    )
    private val lastLetter = arrayOf(
        ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ',
        'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ',
        'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )

    fun parseStringWordToArray(input: String): CharArray {
        val list = mutableListOf<Char>()
        val length = input.length

        for (i in 0 until length) {
            val ad = String.format("0x%04X", input[i].code)
            if (ad < "0xAC00" || ad > "0xD7A3") {
                return list.toCharArray()
            }

            val default = Integer.decode("0xAC00")
            val decimal = Integer.decode(ad) - default
            val h1 = decimal / (21 * 28)
            val h2 = (decimal % (21 * 28)) / 28
            val h3 = decimal % 28

            list.add(firstLetter[h1])
            list.add(middleLetter[h2])
            if (h3 != 0) list.add(lastLetter[h3])
        }
        return list.toCharArray()
    }
}
