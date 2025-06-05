package com.example.whoareyou.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whoareyou.R
import com.example.whoareyou.component.TopTab

data class Contact(
    val image: Int,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val address: String
)

@Composable
fun HomeScreen() {

    val tempContact = Contact(
        image = R.drawable.ic_main_logo,
        name = "김영희",
        phoneNumber = "010-3083-7216",
        email = "abc123@naver.com",
        address = "부산광역시"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
    ) {
        TopTab()

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Spacer(Modifier.width(20.dp))
            ButtonWithLogo(R.drawable.btn_camera, "카메라", "명함 사진 찍기")
            ButtonWithLogo(R.drawable.btn_gallery, "갤러리", "갤러리에서 선택")
            Spacer(Modifier.width(20.dp))

        }

        Spacer(Modifier.height(20.dp))

        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Text(
                text = "최근 추가된 연락처",
                fontSize = 17.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                modifier = Modifier.padding(top = 25.dp, start = 30.dp, bottom = 10.dp)
            )
            RecentlyAddedContact(tempContact)
            RecentlyAddedContact(tempContact)
            RecentlyAddedContact(tempContact)
            RecentlyAddedContact(tempContact)
            RecentlyAddedContact(tempContact)
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun ButtonWithLogo(logo: Int, description: String, label: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF007AFF))
    ) {
        Image(
            imageVector = ImageVector.vectorResource(logo),
            contentDescription = description,
            modifier = Modifier.padding(0.dp, 20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "명함 사진 찍기",
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun RecentlyAddedContact(contact: Contact) {
    Row(
        modifier = Modifier
            .padding(30.dp, 0.dp)
    ) {
        Image(
            painter = painterResource(contact.image),
            contentDescription = contact.name,
            modifier = Modifier
                .size(50.dp)
                .border(0.1.dp, Color(0xFF999999), shape = RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.width(10.dp))
        Column (
            modifier = Modifier.padding(0.dp)
        ) {
            Text(
                text = contact.name,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            )
            Text(
                text = contact.phoneNumber,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_light)),
                modifier = Modifier.padding(0.dp)
            )
            Text(
                text = contact.email,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_light)),
                modifier = Modifier.padding(0.dp)
            )
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}