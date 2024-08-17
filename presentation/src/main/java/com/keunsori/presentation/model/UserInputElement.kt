package com.keunsori.presentation.model

import androidx.compose.ui.graphics.Color

data class UserInput(val elements: List<Element>) {
    data class Element(val letter: Char, val color: Color = Color.DarkGray)
    companion object {
        val empty = UserInput(elements = listOf())
    }
}

