package com.keunsori.towerdle.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.towerdle.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _test = MutableStateFlow("")
    val test: StateFlow<String>
        get() = _test

    init {
        getTestDataSource()
    }

    fun getTestDataSource() {
        viewModelScope.launch {
            userRepository.getTestDataSource().collect { value ->
                _test.value = value ?: ""
            }
        }
    }

    fun setTestDataSource(test: String) {
        viewModelScope.launch {
            userRepository.setTestDataSource(test)
        }
    }
}