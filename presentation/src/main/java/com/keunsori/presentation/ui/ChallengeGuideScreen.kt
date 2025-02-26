package com.keunsori.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keunsori.presentation.intent.ChallengeEvent
import com.keunsori.presentation.intent.ChallengeState
import com.keunsori.presentation.ui.util.Dialog
import com.keunsori.presentation.ui.util.TopBar
import com.keunsori.presentation.viewmodel.ChallengeViewModel

@Composable
fun ChallengeGuideScreen(
    viewModel: ChallengeViewModel,
    navigateToHome: () -> Unit,
    navigateToInGame: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.sendEvent(ChallengeEvent.GetData)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        val state = viewModel.uiState.collectAsState().value
        TopBar("챌린지", onBackButtonClicked = navigateToHome)
        when (state) {
            is ChallengeState.CanStart -> CanStartScreen(state, navigateToInGame)
            is ChallengeState.Finished -> FinishedScreen(state)
            ChallengeState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            ChallengeState.FailToLoad -> {
                Dialog(
                    title = "네트워크 에러",
                    message = "챌린지 데이터를 가져오는데 실패했어요.",
                    oneButtonOnly = false,
                    confirmButtonText = "다시 시도하기",
                    cancelButtonText = "돌아가기",
                    onDismissRequest = {},
                    onConfirm = { viewModel.sendEvent(ChallengeEvent.GetData) },
                    onCancel = navigateToHome
                )
            }
        }
    }
}

@Composable
private fun CanStartScreen(state: ChallengeState.CanStart, onClickStartButton: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val date = state.date.split("-")
        Text(
            text = "${date[0]}년 ${date[1]}월 ${date[2]}일",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "챌린지 진행 ${if (state.isOnGoing) "중" else "가능"}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("챌린지 모드란?", style = MaterialTheme.typography.titleMedium)
        Text(
            "하루에 한 번 모두가 같은 문제를 풀어 경쟁할 수 있습니다.\n" +
                    "새로운 챌린지 단어는 자정에 갱신됩니다. (KST 기준)", style = MaterialTheme.typography.bodyMedium
        )
        ElevatedButton(
            onClick = onClickStartButton,
        ) {
            Text("시작하기")
        }

    }
}

@Composable
private fun FinishedScreen(state: ChallengeState.Finished) {

}

@Preview
@Composable
fun ChallengeGuideScreen_preview() {
    CanStartScreen(state = ChallengeState.CanStart("2024-11-11", true, 3, listOf()), {})
}
