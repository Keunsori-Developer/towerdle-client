package com.keunsori.towerdle.presentation.main.intent

import com.keunsori.towerdle.utils.Reducer
import com.keunsori.towerdle.utils.UiEffect
import com.keunsori.towerdle.utils.UiEvent
import com.keunsori.towerdle.utils.UiState

data class MainState(val data: Int, val boolean: Boolean, val list: ArrayList<Int>) :
    UiState {
    companion object {
        fun init() = MainState(0, false, arrayListOf())
    }
}

sealed class MainEvent : UiEvent {
    data object Increase : MainEvent()
    data object Decrease : MainEvent()
}

sealed class MainEffect : UiEffect {
    data class ShowToast(val message: String) : MainEffect()
    data object NavigateToDetail : MainEffect()
}

class MainReducer(state: MainState) : Reducer<MainState, MainEvent>(state) {
    override fun reduce(oldState: MainState, event: MainEvent) {
        when (event) {
            is MainEvent.Increase -> {
                setState(
                    oldState.copy(
                        data = oldState.data + 1,
                        boolean = true
                    )
                )
            }

            is MainEvent.Decrease -> {
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
