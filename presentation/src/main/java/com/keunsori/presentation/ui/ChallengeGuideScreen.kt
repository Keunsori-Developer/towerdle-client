package com.keunsori.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keunsori.presentation.intent.ChallengeGuideEvent
import com.keunsori.presentation.intent.ChallengeGuideState
import com.keunsori.presentation.ui.util.Dialog
import com.keunsori.presentation.ui.util.TopBar
import com.keunsori.presentation.viewmodel.ChallengeGuideViewModel

@Composable
fun ChallengeGuideScreen(
    viewModel: ChallengeGuideViewModel,
    navigateToHome: () -> Unit,
    navigateToInGame: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        val state = viewModel.uiState.collectAsState().value
        TopBar("챌린지", onBackButtonClicked = navigateToHome)
        when (state) {
            is ChallengeGuideState.CanStart -> CanStartScreen(state)
            is ChallengeGuideState.Finished -> FinishedScreen(state)
            ChallengeGuideState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            ChallengeGuideState.FailToLoad -> {
                Dialog(
                    title = "네트워크 에러",
                    message = "챌린지 데이터를 가져오는데 실패했어요.",
                    oneButtonOnly = false,
                    confirmButtonText = "다시 시도하기",
                    cancelButtonText = "돌아가기",
                    onDismissRequest = {},
                    onConfirm = { viewModel.sendEvent(ChallengeGuideEvent.RetryToGetData) },
                    onCancel = navigateToHome
                )
            }
        }
    }
}

@Composable
private fun CanStartScreen(state: ChallengeGuideState.CanStart) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = state.date)
        Text("챌린지 진행 ${if(state.isOnGoing) "중" else "가능"}")

        ElevatedButton(
            onClick = { },
        ) {
            Text("시작하기")
        }

    }
}

@Composable
private fun FinishedScreen(state: ChallengeGuideState.Finished) {

}

@Preview
@Composable
fun ChallengeGuideScreen_preview() {
CanStartScreen(state = ChallengeGuideState.CanStart("2024-11-11", true, 3))
}
