package com.example.whoareyou

import android.content.Intent
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WhoAreYOuTheme {
                val loginViewModel: LoginViewModel by viewModels()

                val userName by loginViewModel.userName
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    loginViewModel.login(result) {}
                }

                if (userName == null) {
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
//import com.example.whoareyou.ocrcontact.OcrLoadingScreen
//import com.example.whoareyou.ui.theme.WhoAreYOuTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            WhoAreYOuTheme {
//                OcrLoadingScreen(
//                    onBack = {
//                        // 임시로 아무 동작 없음 (혹은 finish() 등도 가능)
//                    }
//                )
//            }
//        }
//    }
//}