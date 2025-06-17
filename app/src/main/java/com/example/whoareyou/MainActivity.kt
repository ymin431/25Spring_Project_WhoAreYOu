package com.example.whoareyou

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.whoareyou.login.LoginScreen
import com.example.whoareyou.login.LoginViewModel
import com.example.whoareyou.ui.theme.WhoAreYOuTheme
import com.example.whoareyou.view.MainScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WhoAreYOuTheme {
                val loginViewModel: LoginViewModel by viewModels()
                val currentUser = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

                // 로그인 상태 변경 감지
                DisposableEffect(Unit) {
                    val authStateListener = FirebaseAuth.AuthStateListener { auth ->
                        currentUser.value = auth.currentUser
                    }
                    FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
                    
                    onDispose {
                        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
                    }
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    loginViewModel.login(result) {}
                }

                if (currentUser.value == null) {
                    LoginScreen(
                        onGoogleLoginClicked = {
                            launcher.launch(loginViewModel.getSignInIntent(this))
                        }
                    )
                } else {
                    MainScreen()
                }
            }
        }
    }
}

//package com.example.whoareyou
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import com.example.whoareyou.home.HomeScreen
//import com.example.whoareyou.ocrcontact.OcrLoadingScreen
//import com.example.whoareyou.ui.theme.WhoAreYOuTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            WhoAreYOuTheme {
//                HomeScreen()
//            }
//        }
//    }
//}