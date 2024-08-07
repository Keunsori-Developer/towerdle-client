package com.keunsori.presentation.intent

import com.keunsori.presentation.utils.Reducer
import com.keunsori.presentation.utils.UiEffect
import com.keunsori.presentation.utils.UiEvent
import com.keunsori.presentation.utils.UiState


data class MainState(val data: Int, val boolean: Boolean, val list: ArrayList<Int>) : // 예시, 필요한 데이터로 변경 예정
    UiState {
    companion object {
        fun init() = MainState(0, false, arrayListOf())
    }
}

sealed class MainEvent : UiEvent {
    data object Increase : MainEvent() // 예시, 삭제 예정
    data object Decrease : MainEvent() // 예시, 삭제 예정
}

sealed class MainEffect : UiEffect {
    data class ShowToast(val message: Int) : MainEffect()
    data class MoveScreen(val route: String) : MainEffect()
}

class MainReducer(state: MainState) : Reducer<MainState, MainEvent>(state) {
    override fun reduce(oldState: MainState, event: MainEvent) {
        when (event) {
            is MainEvent.Increase -> { // 예시, 삭제 예정
                setState(
                    oldState.copy(
                        data = oldState.data + 1,
                        boolean = true
                    )
                )
            }

            is MainEvent.Decrease -> { // 예시, 삭제예정
                setState(
                    oldState.copy(
                        data = oldState.data - 1,
                        boolean = false
                    )
                )
            }
        }
    }
}
