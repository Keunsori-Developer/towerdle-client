package com.keunsori.presentation.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.core.content.ContextCompat.startActivity
import com.keunsori.domain.entity.QuizInputResult
import com.keunsori.presentation.intent.ChallengeEvent
import com.keunsori.presentation.intent.ChallengeState
import com.keunsori.presentation.ui.util.Dialog
import com.keunsori.presentation.ui.util.TopBar
import com.keunsori.presentation.viewmodel.ChallengeViewModel
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.ChallengeEffect
import com.keunsori.presentation.model.UserInput
import com.keunsori.presentation.ui.theme.Color

@Composable
fun ChallengeGuideScreen(
    viewModel: ChallengeViewModel,
    navigateToHome: () -> Unit,
    navigateToInGame: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sendEvent(ChallengeEvent.GetData)
    }
    LaunchedEffect(Unit) {
        viewModel.effectFlow.collect {
            when (it) {
                is ChallengeEffect.OpenIntent -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, it.text)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            }
        }
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
            is ChallengeState.Finished -> FinishedScreen(
                state,
                onClickShareButton = { viewModel.sendEvent(ChallengeEvent.ShareResult(state.quizInputs)) })

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

        Spacer(modifier = Modifier.height(10.dp))

        Text("오늘의 단어 정보", style = MaterialTheme.typography.titleMedium)
        Text(
            "- 글자 수: ${state.wordLength}글자\n" +
                    "- 자모 갯수: ${state.wordCount}개\n" +
                    "- 최대 입력 가능 횟수: ${state.maxAttemptsCount}번",
            style = MaterialTheme.typography.bodyMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            ElevatedButton(
                onClick = onClickStartButton,
            ) {
                Text("시작하기")
            }
        }
    }
}

@Composable
private fun FinishedScreen(state: ChallengeState.Finished, onClickShareButton: () -> Unit) {
    var isResultOn by remember { mutableStateOf(false) }
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
            "챌린지 진행 완료",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "새로운 챌린지 단어는 자정에 갱신됩니다. (KST 기준)",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "나의 풀이 내역",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = { isResultOn = !isResultOn }) {
                Icon(
                    painter = painterResource(id = if (isResultOn) R.drawable.outline_visibility_24 else R.drawable.outline_visibility_off_24),
                    contentDescription = null
                )
            }
        }

        UserInputHistory(isShown = isResultOn, quizInputs = state.quizInputs)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            ElevatedButton(onClick = onClickShareButton) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Share,
                        modifier = Modifier.size(20.dp),
                        contentDescription = "share"
                    )
                    Text("공유하기")
                }
            }
        }
    }
}

@Composable
private fun UserInputHistory(isShown: Boolean, quizInputs: List<List<UserInput.Element>>) {
    if (quizInputs.isEmpty()) {
        Text(
            text = "풀이 기록이 저장되어 있지 않아요.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (input in quizInputs) {
            Row(
                modifier = Modifier
                    .height(35.dp)
                    .width(input.size * 35.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                for (element in input) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isShown) {
                            Text(element.letter.toString(), fontSize = 15.sp)
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ingame_container_filled),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    color = element.color ?: MaterialTheme.colorScheme.outline,
                                ),
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.ingame_container),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                color = if (isShown) element.color
                                    ?: MaterialTheme.colorScheme.outline
                                else MaterialTheme.colorScheme.outline
                            ),
                        )

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChallengeGuideScreen_preview() {
    CanStartScreen(state = ChallengeState.CanStart("2024-11-11", true, 3, 7, 6, listOf()), {})
}

@Preview
@Composable
fun ChallengeGuideScreen_preview2() {
    val quizinputs = listOf(
        listOf(
            UserInput.Element('a', Color.ingameMatched),
            UserInput.Element('b', Color.ingameWrongSpot),
            UserInput.Element('c', Color.ingameMatched)
        ),
        listOf(
            UserInput.Element('ㅁ', Color.ingameWrongSpot),
            UserInput.Element('ㅏ', Color.ingameNotExist),
            UserInput.Element('ㄴ', Color.ingameMatched)
        )
    )
    FinishedScreen(state = ChallengeState.Finished("2024-11-11", quizinputs)) {}
}
