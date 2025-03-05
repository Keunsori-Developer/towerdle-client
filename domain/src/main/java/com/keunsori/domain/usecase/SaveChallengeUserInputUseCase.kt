package com.keunsori.domain.usecase

import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.domain.repository.InGameRepository

class SaveChallengeUserInputUseCase(private val inGameRepository: InGameRepository) {
    suspend operator fun invoke(trialCount: Int, input: List<QuizInputResult.Element>) =
        inGameRepository.saveChallengeData(trialCount, input)
}
