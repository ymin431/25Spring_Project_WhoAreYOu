package com.example.whoareyou

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.whoareyou.login.LoginScreen
import com.example.whoareyou.login.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : ComponentActivity() {

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Google 로그인 결과 처리
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            loginViewModel.login(result) {
                // 로그인 성공 시 처리 → ViewModel 내부에서 상태 업데이트함
            }
        }

        // Google 로그인 Intent
        val signInIntent = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // firebase client id
                .requestEmail()
                .build()
        ).signInIntent

        setContent {
            var isLoggedIn by remember { mutableStateOf(false) }

            // 유저 이름 상태 관찰
            val userName by loginViewModel.userName

            // 로그인 성공 감지
            LaunchedEffect(userName) {
                if (userName != null) {
                    isLoggedIn = true
                }
            }

            if (!isLoggedIn) {
                LoginScreen(
                    onGoogleLoginClicked = {
                        launcher.launch(signInIntent)
                    },
                    onLoginSuccess = {
                        isLoggedIn = true
                    }
                )
            } else {
//                Home() // 로그인 성공 시 메인 홈 화면
            }
        }
    }
}