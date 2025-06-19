package com.example.whoareyou.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whoareyou.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    private val _userName = mutableStateOf<String?>(null)
    val userName: State<String?> = _userName
    private val TAG = "LoginViewModel"

    fun getSignInIntent(context: Context): Intent {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, options).signInIntent
    }

    fun login(result: ActivityResult, onSuccess: () -> Unit) {
        if (result.resultCode != Activity.RESULT_OK) {
            Log.e(TAG, "로그인 취소됨: resultCode = ${result.resultCode}")
            return
        }

        viewModelScope.launch {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (!task.isSuccessful) {
                    Log.e(TAG, "Google 로그인 태스크 실패: ${task.exception}")
                    throw task.exception ?: Exception("Unknown error")
                }
                
                val account = task.getResult(Exception::class.java)
                if (account == null) {
                    Log.e(TAG, "Google 계정 정보가 null입니다")
                    throw Exception("Google account is null")
                }
                Log.d(TAG, "Google 계정 가져오기 성공: ${account.email}")

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val authResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()

                if (authResult.user != null) {
                    Log.d(TAG, "Firebase 로그인 성공: ${authResult.user?.uid}")
                    _userName.value = account.displayName
                    onSuccess()
                } else {
                    Log.e(TAG, "Firebase 로그인 실패: 사용자 정보가 null입니다")
                    throw Exception("Firebase login failed: user is null")
                }
            } catch (e: Exception) {
                Log.e(TAG, "로그인 실패", e)
                _userName.value = null
                FirebaseAuth.getInstance().signOut()
                throw e
            }
        }
    }
}