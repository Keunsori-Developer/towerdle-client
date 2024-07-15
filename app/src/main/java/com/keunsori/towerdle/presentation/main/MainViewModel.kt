package com.keunsori.towerdle.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keunsori.towerdle.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    private val reducer = MainReducer(MainState.init())

    val uiState get() = reducer.uiState

    private val effectChannel = Channel<MainEffect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    private fun sendEvent(event: MainEvent) {
        reducer.sendEvent(event)
    }

    fun increase(){
        sendEvent(MainEvent.Increase)
    }

    fun decrease(){
        sendEvent(MainEvent.Decrease)
    }

    fun showToastMessage(message: String) {
        viewModelScope.launch {
            effectChannel.send( MainEffect.ShowToast(message) )
        }
    }
}