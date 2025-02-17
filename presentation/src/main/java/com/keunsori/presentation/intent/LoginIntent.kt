package com.keunsori.presentation.intent

import com.keunsori.domain.entity.QuizLevel
import com.keunsori.domain.entity.UserInfo
import com.keunsori.presentation.utils.Reducer
import com.keunsori.presentation.utils.UiEffect
import com.keunsori.presentation.utils.UiEvent
import com.keunsori.presentation.utils.UiState

data class LoginState(
    val loading: Boolean,
    val isGoogleLogin: Boolean,
    val email: String,
    val name: String,
    val userInfo: UserInfo,
    val detailDialogState: Boolean,
    val quizLevelDetailState: QuizLevel
) : UiState {
    companion object {
        fun init() = LoginState(loading = false, isGoogleLogin = false, email = "", name = "", userInfo = UserInfo.Empty, detailDialogState = false, quizLevelDetailState = QuizLevel.EASY)
    }
}

sealed class LoginEvent : UiEvent {
    data object AutoGoogleLogin : LoginEvent()
    data class GuestLogin(val ssaid: String) : LoginEvent()
    data class GoogleLogin(val idToken: String) : LoginEvent()
    data object GetUserInfo : LoginEvent()
    data class SaveUserInfo(val userInfo: UserInfo) : LoginEvent()
    data object Logout : LoginEvent()
    data class SuccessGoogleLogin(val email: String, val name: String) : LoginEvent()
    data class SuccessGuestLogin(val email: String, val name: String) : LoginEvent()
    data object StartLoading : LoginEvent()
    data object FinishLoading : LoginEvent()
    data class SetDetailDialogState(val isVisible: Boolean) : LoginEvent()
    data class ChangeDetailState(val isForward: Boolean) : LoginEvent()
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

            is LoginEvent.SuccessGuestLogin -> {
                setState(
                    newState = oldState.copy(
                        isGoogleLogin = false,
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

            is LoginEvent.SaveUserInfo -> {
                setState(
                    newState = oldState.copy(
                        userInfo = event.userInfo
                    )
                )
            }

            is LoginEvent.SetDetailDialogState -> {
                setState(
                    newState = oldState.copy(
                        detailDialogState = event.isVisible
                    )
                )
            }

            is LoginEvent.ChangeDetailState -> {
                setState(
                    newState = oldState.copy(
                        quizLevelDetailState = when(oldState.quizLevelDetailState){
                            QuizLevel.EASY -> {
                                if(event.isForward){
                                    QuizLevel.MEDIUM
                                } else {
                                    QuizLevel.VERYHARD
                                }

                            }
                            QuizLevel.MEDIUM -> {
                                if(event.isForward){
                                    QuizLevel.HARD
                                } else {
                                    QuizLevel.EASY
                                }
                            }
                            QuizLevel.HARD -> {
                                if(event.isForward){
                                    QuizLevel.VERYHARD
                                } else {
                                    QuizLevel.MEDIUM
                                }
                            }
                            QuizLevel.VERYHARD -> {
                                if(event.isForward){
                                    QuizLevel.EASY
                                } else {
                                    QuizLevel.HARD
                                }
                            }
                        }
                    )
                )

            }

            else -> {}
        }
    }
}
