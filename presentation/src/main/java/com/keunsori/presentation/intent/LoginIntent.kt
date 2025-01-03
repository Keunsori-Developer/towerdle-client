package com.keunsori.presentation.intent

import android.util.Log
import com.keunsori.presentation.utils.Reducer
import com.keunsori.presentation.utils.UiEffect
import com.keunsori.presentation.utils.UiEvent
import com.keunsori.presentation.utils.UiState

data class LoginState(
    val loading: Boolean,
    val isGoogleLogin: Boolean,
    val email: String,
    val name: String
) : UiState {
    companion object {
        fun init() = LoginState(loading = false, isGoogleLogin = false, email = "", name = "")
    }
}

sealed class LoginEvent : UiEvent {
    data object AutoGoogleLogin : LoginEvent()
    data class GuestLogin(val ssaid: String) : LoginEvent()
    data class GoogleLogin(val idToken: String) : LoginEvent()
    data object Logout : LoginEvent()
    data class SuccessGoogleLogin(val email: String, val name: String) : LoginEvent()
    data object StartLoading : LoginEvent()
    data object FinishLoading : LoginEvent()
}

sealed class LoginEffect : UiEffect {
    data class ShowToastToResource(val message: Int) : LoginEffect()

    data class ShowToast(val message: String) : LoginEffect()

    data object MoveToMain : LoginEffect()
}

class LoginReducer(state: LoginState) : Reducer<LoginState, LoginEvent>(state) {
    override suspend fun reduce(oldState: LoginState, event: LoginEvent) {
        when (event) {
            LoginEvent.StartLoading -> {
                setState(newState = oldState.copy(loading = true))
            }

            LoginEvent.FinishLoading -> {
                setState(newState = oldState.copy(loading = false))
            }

            is LoginEvent.SuccessGoogleLogin -> {
                setState(
                    newState = oldState.copy(
                        isGoogleLogin = true,
                        email = event.email,
                        name = event.name
                    )
                )
            }

            LoginEvent.Logout -> {
                setState(
                    newState = oldState.copy(
                        isGoogleLogin = false,
                    )
                )
            }



            else -> {}
        }
    }
}
