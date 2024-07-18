package com.keunsori.domain.usecase

import com.keunsori.domain.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainUseCase(private val mainRepository: MainRepository) {
    operator fun invoke(
        test: String,
        scope: CoroutineScope,
        onResult: (String) -> Unit = {}
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                mainRepository.test(test)
            }
            onResult(deferred.await())
        }
    }

    fun a(){

    }
}