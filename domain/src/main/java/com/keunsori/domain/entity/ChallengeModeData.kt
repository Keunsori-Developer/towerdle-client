package com.keunsori.domain.entity

sealed class ChallengeModeData(open val date: String, open val quizInfo: QuizInfo?) {
    data class Finished(
        val quizInputResults: List<List<QuizInputResult.Element>>,
        override val date: String
    ) : ChallengeModeData(date, null)

    data class InGoing(
        val quizInputResults: List<QuizInputResult>?,
        override val date: String,
        override val quizInfo: QuizInfo
    ) : ChallengeModeData(date, quizInfo)

    data class NotStarted(override val date: String, override val quizInfo: QuizInfo) :
        ChallengeModeData(date, quizInfo)

    data object FailedToGetModeData : ChallengeModeData("", null)
}
