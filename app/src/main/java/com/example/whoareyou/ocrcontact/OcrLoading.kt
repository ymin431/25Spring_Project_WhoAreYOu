package com.example.whoareyou.ocrcontact

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whoareyou.R
import com.example.whoareyou.view.MainScreen

@Composable
fun OcrLoadingScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(24.dp)
                    .clickable { onBack() },
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.weight(1.4f))

        Text(
            text = "명함 정보 추출 중...",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 25.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(80.dp))

        CircularProgressIndicator(
            color = Color(0xFF007BFF),
            strokeWidth = 11.dp,
            modifier = Modifier.size(120.dp)

        )

        Spacer(modifier = Modifier.weight(2f))
    }
}

@Composable
fun OcrLoadingScreenWrapper() {
    var goToMain by remember { mutableStateOf(false) }

    if (goToMain) {
        MainScreen()
    } else {
        OcrLoadingScreen(
            onBack = { goToMain = true }
        )
    }
}

@Composable
@Preview
fun OcrLoadingScreenPreview() {
    OcrLoadingScreenWrapper()
}