package com.keunsori.presentation.utils

import androidx.credentials.CredentialManager
import androidx.compose.runtime.staticCompositionLocalOf

val LocalCredentialManagerController = staticCompositionLocalOf {
    MyCredentialManagerController()
}

class MyCredentialManagerController(val credentialManager: CredentialManager? = null)