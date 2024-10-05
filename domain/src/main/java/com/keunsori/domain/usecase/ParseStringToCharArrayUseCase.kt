package com.keunsori.domain.usecase

class ParseStringToCharArrayUseCase {
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

    val separatedMiddleLetter: (Char) -> CharArray = {
        when (it) {
            'ㅘ' -> charArrayOf('ㅗ', 'ㅏ')
            'ㅙ' -> charArrayOf('ㅗ', 'ㅐ')
            'ㅚ' -> charArrayOf('ㅗ', 'ㅣ')
            'ㅝ' -> charArrayOf('ㅜ', 'ㅓ')
            'ㅞ' -> charArrayOf('ㅜ', 'ㅔ')
            'ㅟ' -> charArrayOf('ㅜ', 'ㅣ')
            'ㅢ' -> charArrayOf('ㅡ', 'ㅣ')
            else -> charArrayOf(it)
        }
    }

    /**
    String 형태의 한글 문자열을 초성, 중성, 종성으로 구분하여 CharArray로 만든다.
     */
    operator fun invoke(input: String): CharArray {
        val list = mutableListOf<Char>()
        val length = input.length

        for (i in 0 until length) {
            val ad = String.format("0x%04X", input[i].code) // hex 값으로 변환

            // 한글이 가지는 유니코드 값의 범위는 AC00부터 D7A3까지
            if (ad < "0xAC00" || ad > "0xD7A3") {
                return list.toCharArray()
            }

            val default = Integer.decode("0xAC00")
            val decimal = Integer.decode(ad) - default
            val h1 = decimal / (21 * 28)
            val h2 = (decimal % (21 * 28)) / 28
            val h3 = decimal % 28


            list.add(firstLetter[h1])
            list.addAll(separatedMiddleLetter(middleLetter[h2]).toTypedArray())
            if (h3 != 0) list.add(lastLetter[h3])
        }
        return list.toCharArray()
    }

}
