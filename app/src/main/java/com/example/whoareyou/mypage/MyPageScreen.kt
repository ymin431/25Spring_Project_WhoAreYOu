package com.example.whoareyou.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whoareyou.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyPageScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
            .padding(16.dp)
    ) {
        // 프로필 섹션
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_main_logo),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(50.dp))
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "프로필",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold))
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // 설정 메뉴
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Text(
                text = "설정",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 설정 메뉴 아이템들
            MenuItem(
                text = "로그아웃",
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun MenuItem(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            color = Color(0xFF007AFF),
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}