package com.keunsori.towerdle.presentation.login

import com.keunsori.towerdle.utils.Reducer
import com.keunsori.towerdle.utils.UiEffect
import com.keunsori.towerdle.utils.UiEvent
import com.keunsori.towerdle.utils.UiState

data class LoginState(val loading: Boolean) : UiState {
    companion object {
        fun init() = LoginState(loading = false)
    }
}

sealed class LoginEvent : UiEvent {
    data object StartLoading: LoginEvent()
    data object FinishLoading: LoginEvent()
}

sealed class LoginEffect : UiEffect {
    data class ShowToast(val message: String) : LoginEffect()
    data object MoveToMain : LoginEffect()
}

class LoginReducer(state: LoginState) : Reducer<LoginState, LoginEvent>(state) {
    override fun reduce(oldState: LoginState, event: LoginEvent) {
        when(event){
            is LoginEvent.StartLoading -> {
                setState(newState = oldState.copy(loading = true))
            }
            is LoginEvent.FinishLoading -> {
                setState(newState = oldState.copy(loading = false))
            }
        }
    }
}
