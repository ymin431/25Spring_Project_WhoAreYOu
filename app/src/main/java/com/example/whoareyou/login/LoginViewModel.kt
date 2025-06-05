package com.example.whoareyou.login

import androidx.activity.result.ActivityResult
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel : ViewModel() {

    private val _userName = mutableStateOf<String?>(null)
    val userName: State<String?> = _userName

    private val _isFailState = mutableStateOf(false)
    val isFailState: State<Boolean> = _isFailState

    fun login(
        activityResult: ActivityResult,
        onSuccess: () -> Unit
    ) {
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _userName.value = account.displayName
                        onSuccess()
                    } else {
                        _isFailState.value = true
                    }
                }
        } catch (e: Exception) {
            _isFailState.value = true
        }
    }
}