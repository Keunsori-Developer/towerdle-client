package com.keunsori.presentation.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.keunsori.domain.entity.QuizInputResult

data class UserInput(val elements: List<Element>) {
    data class Element(val letter: Char, val color: Color? = null) {
        companion object {
            fun QuizInputResult.Element.toPresentationModel() = Element(
                letter = this.letter, color = when (this.type) {
                    QuizInputResult.Type.MATCHED -> com.keunsori.presentation.ui.theme.Color.ingameMatched
                    QuizInputResult.Type.WRONG_SPOT -> com.keunsori.presentation.ui.theme.Color.ingameWrongSpot
                    QuizInputResult.Type.NOT_EXIST -> com.keunsori.presentation.ui.theme.Color.ingameNotExist
                }
            )
        }
    }

    companion object {
        val empty = UserInput(elements = listOf())
    }
}

