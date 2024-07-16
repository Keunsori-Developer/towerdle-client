package com.keunsori.towerdle.presentation.main.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.keunsori.towerdle.presentation.main.MainViewModel

@Composable
fun InfoScreen(
    viewModel: MainViewModel,
    credentialManager: CredentialManager
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            viewModel.logout {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
            }
        }) {
            Text(text = "로그아웃")
        }
    }
}