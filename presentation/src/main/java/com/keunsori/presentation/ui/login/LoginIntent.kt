package com.keunsori.presentation.ui.login

import com.keunsori.presentation.utils.Reducer
import com.keunsori.presentation.utils.UiEffect
import com.keunsori.presentation.utils.UiEvent
import com.keunsori.presentation.utils.UiState

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
    data class ShowToast(val message: Int) : LoginEffect()
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
