package com.example.whoareyou

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun MainScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    var lastImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uriString = result.data?.getStringExtra("extra_image_uri")
            val uri = uriString?.let { Uri.parse(it) }
            if (uri != null) {
                lastImageUri = uri
                val intent = Intent(context, com.example.whoareyou.ocrcontact.DataConfirmActivity::class.java).apply {
                    putExtra(com.example.whoareyou.ocrcontact.DataConfirmActivity.EXTRA_IMAGE_URI, uri.toString())
                }
                context.startActivity(intent)
            }
        }
    }

    val myPageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val intent = Intent(context, com.example.whoareyou.home.CameraActivity::class.java)
            cameraLauncher.launch(intent)
        }) {
            Text("카메라로 명함 촬영")
        }
        Button(onClick = {
            val intent = Intent(context, com.example.whoareyou.mypage.MyPageActivity::class.java)
            myPageLauncher.launch(intent)
        }) {
            Text("마이페이지 이동")
        }
    }
}