package com.keunsori.presentation.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keunsori.presentation.ui.theme.AppTypography

@Composable
fun Dialog(
    title: String,
    message: String,
    oneButtonOnly: Boolean,
    confirmButtonText: String,
    cancelButtonText: String? = null,
    onDismissRequest: (() -> Unit),
    onConfirm: () -> Unit,
    onCancel: (() -> Unit)? = null,
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .width(320.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    RoundedCornerShape(20.dp)
                )
                .border(1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(title, style = AppTypography.titleLarge)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                message, style = AppTypography.titleMedium,
                modifier = Modifier.padding(horizontal = 10.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!oneButtonOnly) {
                    Button(
                        onClick = { onCancel?.invoke() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(cancelButtonText ?: "취소", color = MaterialTheme.colorScheme.secondary)
                    }
                    VerticalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.height(55.dp)
                    )
                }
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(confirmButtonText, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}

@Preview
@Composable
private fun Dialog_Preview() {
    Dialog(
        title = "title",
        message = "message",
        oneButtonOnly = false,
        confirmButtonText = "확인",
        onDismissRequest = {},
        onConfirm = { /*TODO*/ })
}