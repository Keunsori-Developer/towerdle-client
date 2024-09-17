package com.keunsori.presentation.model


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class UserInput(val elements: List<Element>) {
    data class Element(val letter: Char, @get:Composable val color: Color? = null)
    companion object {
        val empty = UserInput(elements = listOf())
    }
}

