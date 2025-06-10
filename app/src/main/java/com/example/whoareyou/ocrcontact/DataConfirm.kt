package com.example.whoareyou.ocrcontact

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whoareyou.R
import com.example.whoareyou.view.MainScreen

data class Contact(
    val name: String,
    val phoneNumber: String,
    val email: String,
    val address: String
)

@Composable
fun DataConfirmScreen(onBack: () -> Unit, contact: Contact) {
    var emailValid by remember { mutableStateOf<Boolean?>(null) }
    val isSaveEnabled = emailValid == true

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

        Spacer(Modifier.height(40.dp))

        Text(
            text = "연락처 확인",
            fontSize = 25.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
        )

        Spacer(Modifier.height(50.dp))

        Text(
            text = "명함에서 추출된 정보를 확인해주세요.",
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.pretendard_light)),
        )

        Spacer(Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            ContactBox("이름", contact.name)
            DrawLine()
            ContactBox("전화번호", contact.phoneNumber)
            DrawLine()
            EmailValidationBox(
                email = contact.email,
                onResult = { result -> emailValid = result }
            )
            DrawLine()
            ContactBox("주소", contact.address)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 20.dp)
        ) {
            Button(
                onClick = { if (isSaveEnabled) /* 저장 로직 */ else Unit },
                enabled = isSaveEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)

            ) {
                Text(
                    text = "다시 찍기",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    modifier = Modifier.padding(7.dp)
                )
            }

            Button(
                onClick = { if (isSaveEnabled) /* 저장 로직 */ else Unit },
                enabled = isSaveEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF007AFF),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)

            ) {
                Text(
                    text = "저장하기",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    modifier = Modifier.padding(7.dp)
                )
            }
        }
    }
}

@Composable
fun ContactBox(param: String, value: String) {
    Column(
    ) {
        Text(
            text = param,
            fontSize = 17.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.pretendard_light)),
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        )
    }
}

@Composable
fun EmailValidationBox(
    email: String,
    onResult: (Boolean?) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var isValid by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(email) {
        val result = EmailValidation.validateEmail(email)
        isValid = result?.isValid
        isLoading = false
        onResult(result?.isValid)
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "이메일",
                fontSize = 17.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.pretendard_light))
            )

            Spacer(modifier = Modifier.weight(1f))

            when {
                isLoading -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "검사 중...",
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_light)),
                            color = Color.Gray
                        )
                    }
                }

                isValid == true -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Valid Email",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "유효한 이메일",
                            fontSize = 13.sp,
                            color = Color(0xFF4CAF50),
                            fontFamily = FontFamily(Font(R.font.pretendard_light))
                        )
                    }
                }

                isValid == false -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Invalid Email",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "유효하지 않음",
                            fontSize = 13.sp,
                            color = Color.Red,
                            fontFamily = FontFamily(Font(R.font.pretendard_light))
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = email,
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.pretendard_medium))
        )
    }
}

@Composable
fun DrawLine() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    ) {
        drawLine(
            color = Color(0xAA808080),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 1.dp.toPx()
        )
    }
}

@Composable
fun DataConfirmScreenWrapper(contact: Contact) {
    var goToMain by remember { mutableStateOf(false) }

    if (goToMain) {
        MainScreen()
    } else {
        DataConfirmScreen(
            onBack = { goToMain = true },
            contact = contact
        )
    }
}

@Composable
@Preview
fun DataConfirmScreenPreview() {
    DataConfirmScreenWrapper(Contact("가나디", "010-1234-1234", "abc123@naver.com", "경상남도 양산시"))
}