package com.keunsori.data.data

data class ChallengeModeRawData(val index: Int, val input: List<Element>) {
    data class Element(val letter: Char, val type: Int)
}
