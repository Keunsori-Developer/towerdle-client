package com.keunsori.presentation.intent

import com.keunsori.presentation.utils.UiEvent

sealed interface InGameEvent : UiEvent {
    data class SelectLetter(val letter :Char): InGameEvent
    data object ClickAnswerButton : InGameEvent
}