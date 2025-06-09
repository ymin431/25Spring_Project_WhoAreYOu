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

        // 중앙 텍스트 + 로딩 인디케이터
        Text(
            text = "명함 정보 추출 중...",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 25.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(80.dp))

        CircularProgressIndicator(
            color = Color(0xFF007BFF), // 파란색
            strokeWidth = 11.dp,
            modifier = Modifier.size(120.dp)

        )

        Spacer(modifier = Modifier.weight(2f))
    }
}

@Composable
@Preview
fun OcrLoadingScreenPreview() {
    OcrLoadingScreen(
        onBack = {}
    )
}