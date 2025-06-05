package com.example.whoareyou.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.whoareyou.R

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onGoogleLoginClicked: () -> Unit
) {
    val userName by loginViewModel.userName

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.ic_main_logo),
                contentDescription = "메인 로고",
                modifier = Modifier.size(200.dp)
            )

            Text(
                text = "WhoAreYOu",
                fontSize = 34.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.racingsansone)),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "3초만에 시작해보세요!",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
        ) {
            OutlinedButton(
                onClick = onGoogleLoginClicked,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google_login),
                        contentDescription = "Google",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Google로 시작하기",
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium))
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}