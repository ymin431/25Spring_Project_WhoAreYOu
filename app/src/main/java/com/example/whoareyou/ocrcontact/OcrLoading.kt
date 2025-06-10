package com.example.whoareyou.ocrcontact

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.whoareyou.R
import com.example.whoareyou.home.HomeScreen
import com.example.whoareyou.view.MainScreen
import com.example.whoareyou.viewmodel.OcrState
import com.example.whoareyou.viewmodel.OcrViewModel

@Composable
fun OcrLoadingScreen(
    onBack: () -> Unit,
    imageUri: Uri
) {
    val context = LocalContext.current
    val viewModel: OcrViewModel = viewModel()
    val ocrState by viewModel.ocrState.collectAsState()

    LaunchedEffect(imageUri) {
        viewModel.processImage(context, imageUri)
    }

    when (val state = ocrState) {
        is OcrState.Success -> {
            DataConfirmScreen(
                onBack = onBack,
                contact = state.contact
            )
        }
        is OcrState.Error -> {
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
                    text = "오류가 발생했습니다",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 25.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = state.message,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    fontSize = 16.sp,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.weight(2f))
            }
        }
        else -> {
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
    }
}

@Composable
fun OcrLoadingScreenWrapper(imageUri: Uri) {
    var goToMain by remember { mutableStateOf(false) }

    if (goToMain) {
        HomeScreen()
    } else {
        OcrLoadingScreen(
            onBack = { goToMain = true },
            imageUri = imageUri
        )
    }
}

@Composable
@Preview
fun OcrLoadingScreenPreview() {
    // Preview에서는 더미 URI 사용
    val dummyUri = Uri.parse("content://dummy")
    OcrLoadingScreenWrapper(dummyUri)
}