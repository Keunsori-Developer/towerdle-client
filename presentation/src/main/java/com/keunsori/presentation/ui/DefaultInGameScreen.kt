package com.keunsori.presentation.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keunsori.presentation.R
import com.keunsori.presentation.intent.InGameEffect
import com.keunsori.presentation.intent.InGameEvent
import com.keunsori.presentation.intent.InGameUiState
import com.keunsori.presentation.ui.ingame.CommonInGameScreen
import com.keunsori.presentation.ui.ingame.GameGuideScreen
import com.keunsori.presentation.ui.ingame.Keyboard
import com.keunsori.presentation.ui.ingame.MenuBar
import com.keunsori.presentation.ui.ingame.ResultScreen
import com.keunsori.presentation.ui.ingame.UserInputScreen
import com.keunsori.presentation.ui.util.Dialog
import com.keunsori.presentation.viewmodel.InGameViewModel

@Composable
fun DefaultInGameScreen(inGameViewModel: InGameViewModel, navigateToMain: () -> Unit) {
    CommonInGameScreen(inGameViewModel = inGameViewModel, navigateToMain = navigateToMain)
}
