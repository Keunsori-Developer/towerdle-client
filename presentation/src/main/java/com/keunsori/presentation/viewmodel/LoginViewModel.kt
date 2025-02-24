package com.keunsori.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.domain.entity.ApiResult
import com.keunsori.domain.usecase.GetUserInfoUseCase
import com.keunsori.domain.usecase.UserUseCase
import com.keunsori.presentation.intent.LoginEffect
import com.keunsori.presentation.intent.LoginEvent
import com.keunsori.presentation.intent.LoginReducer
import com.keunsori.presentation.intent.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    // ui 상태
    private val reducer = LoginReducer(LoginState.init())
    val uiState get() = reducer.uiState

    // side effect 채널
    private val effectChannel = Channel<LoginEffect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            userUseCase.invoke()
        }
    }

    // UI 변경
    fun sendEvent(event: LoginEvent) {
        viewModelScope.launch {
            when (event) {
                LoginEvent.AutoGoogleLogin -> {
                    autoGoogleLogin()
                }

                is LoginEvent.GoogleLogin -> {
                    tryGoogleLogin(googleIdToken = event.idToken)
                }

                is LoginEvent.GuestLogin -> {
                    tryGuestLogin()
                }

                LoginEvent.GetUserInfo -> {
                    getUserInfo()
                }

                else -> {
                    reducer.sendEvent(event)
                }
            }
        }
    }

    // Side Effect 처리
    fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            effectChannel.send(effect)
        }
    }

    private fun autoGoogleLogin() {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryGoogleLogin")
            val res =
                userUseCase.autoGoogleLogin() // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
            when (res) {
                is ApiResult.Success -> {
                    sendEvent(LoginEvent.GetUserInfo)
                    sendEvent(
                        LoginEvent.SuccessGoogleLogin(
                            email = res.data.user.email,
                            name = res.data.user.name
                        )
                    )
                    sendEffect(LoginEffect.MoveToMain) // 메인 화면 이동
                }

                is ApiResult.Fail -> {
                    sendEffect(LoginEffect.ShowToast("구글로그인에 실패하여 게스트로그인을 시도합니다."))
                    sendEvent(LoginEvent.GuestLogin)
                }
            }
            sendEvent(LoginEvent.FinishLoading)
        }
    }

    private fun tryGoogleLogin(googleIdToken: String) {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryGoogleLogin")
            val res =
                userUseCase.tryGoogleLogin(googleIdToken) // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
            when (res) {
                is ApiResult.Success -> {
                    sendEvent(LoginEvent.GetUserInfo)
                    sendEvent(
                        LoginEvent.SuccessGoogleLogin(
                            email = res.data.user.email,
                            name = res.data.user.name
                        )
                    )
                }

                is ApiResult.Fail -> {
                    sendEffect(LoginEffect.ShowToast(res.exception.message.toString()))
                }
            }
            sendEvent(LoginEvent.FinishLoading)
        }
    }

    private fun tryGuestLogin() {
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            Log.d("LoginViewModel", "tryGuestLogin")
            val res =
                userUseCase.tryGuestLogin() // 로그인 API -> googleIdToken으로 refresh, access token을 받아옴
            when (res) {
                is ApiResult.Success -> {
                    sendEffect(LoginEffect.MoveToMain) // 메인 화면 이동
                    sendEvent(LoginEvent.GetUserInfo)
                    sendEvent(
                        LoginEvent.SuccessGuestLogin(
                            email = res.data.user.email,
                            name = res.data.user.name
                        )
                    )
                }

                is ApiResult.Fail -> {
                    sendEffect(LoginEffect.ShowToast(res.exception.message.toString()))
                }
            }
            sendEvent(LoginEvent.FinishLoading)
        }
    }

    private fun getUserInfo(){
        viewModelScope.launch {
            sendEvent(LoginEvent.StartLoading)
            when (val res = getUserInfoUseCase.invoke()) {
                is ApiResult.Success -> {
                    sendEvent(LoginEvent.SaveUserInfo(res.data))
                }

                is ApiResult.Fail -> {
                    sendEffect(LoginEffect.ShowToast("정보를 받아오지 못했습니다."))
                }
            }
            sendEvent(LoginEvent.FinishLoading)
        }
    }

    fun getIsGoogleLoggedIn(): Flow<Boolean?> {
        return userUseCase.getIsGoogleLoggedIn()
    }

    fun logout(googleLogout: suspend () -> Unit) {
        viewModelScope.launch {
            // 로그아웃 (토큰 제거)
            userUseCase.logout()
            tryGuestLogin()
            // 구글 계정 앱 로그아웃
            googleLogout.invoke()
        }
    }
}