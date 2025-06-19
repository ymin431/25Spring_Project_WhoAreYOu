package com.example.whoareyou.mypage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.example.whoareyou.ui.theme.WhoAreYOuTheme

class MyPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhoAreYOuTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Text("마이페이지 화면입니다.")
                }
            }
        }
    }
} 