package com.keunsori.towerdle.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialCustomException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.keunsori.towerdle.R

suspend fun googleLogin(credentialManager: CredentialManager, context: Context, onSuccess: (String) -> Unit) {
    val tag = "GOOGLE_LOGIN"
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false) // 로그인했던 계정만 표시 여부 -> true일 때 계정이 없으면 NoCredentialException 발생
        .setServerClientId(context.getString(R.string.google_cloud_server_client_id))
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val result = credentialManager.getCredential(
            request = request,
            context = context,
        )
        val credential = result.credential

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

        val googleIdToken = googleIdTokenCredential.idToken
        Log.d(tag, "idToken: $googleIdToken")
        onSuccess.invoke(googleIdToken)
    } catch (e: GetCredentialCustomException) {
        Log.d(tag, e.toString())
    } catch (e: GetCredentialCancellationException){
        // 계정 선택 중 취소를 누를 때
        Log.d(tag, e.toString())
    } catch (e: NoCredentialException){
        // 계정이 없을 때 -> setFilterByAuthorizedAccounts(true) 일 때 발생
        Log.d(tag, e.toString())
    }

}