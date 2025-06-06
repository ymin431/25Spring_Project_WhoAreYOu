package com.example.whoareyou.contactlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.whoareyou.R
import com.example.whoareyou.component.TopTab

data class Contact(
    val name: String,
    val phoneNumber: String,
    val email: String,
    val imageRes: Int
)

fun getInitial(name: String): String {
    if (name.isEmpty()) return ""
    val ch = name.first()
    if (ch in '\uAC00'..'\uD7A3') {
        val syllableIndex = ch.code - 0xAC00
        val choIndex = syllableIndex / (21 * 28)
        val initials = listOf(
            "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ",
            "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
        )
        return initials[choIndex]
    }
    return ch.toString()
}

@Composable
fun ContactListScreen() {
    // 예시 데이터
    val contacts = listOf(
        Contact("가나다", "010-1234-5678", "hgd@abc.com", R.drawable.ic_businesscard_img),
        Contact("고길동", "010-1111-2222", "gildong@abc.com", R.drawable.ic_businesscard_img),
        Contact("나선욱", "010-2345-6789", "na@abc.com", R.drawable.ic_businesscard_img),
        Contact("나다마", "010-9999-1234", "nadami@abc.com", R.drawable.ic_businesscard_img),
        Contact("노진구", "010-1111-2222", "nojingu@abc.com", R.drawable.ic_businesscard_img),
        Contact("다혜림", "010-5678-1234", "dhr@abc.com", R.drawable.ic_businesscard_img),
        Contact("도레미", "010-2468-1357", "doremi@abc.com", R.drawable.ic_businesscard_img),
        Contact("라준수", "010-1111-3333", "laj@abc.com", R.drawable.ic_businesscard_img),
        Contact("마동석", "010-9999-9999", "dong@abc.com", R.drawable.ic_businesscard_img),
        Contact("바다윤", "010-2020-2020", "badayoon@abc.com", R.drawable.ic_businesscard_img)
    )
        .sortedBy { it.name }
        .groupBy { getInitial(it.name) }
        .toSortedMap()

    // 세로 스크롤 상태 생성
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
            .verticalScroll(scrollState) // 화면을 넘어가면 세로 스크롤 가능
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // 초성별 반복
        contacts.forEach { (initial, items) ->
            // 1) 초성 헤더
            Text(
                text = initial,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier = Modifier
                    .padding(start = 24.dp, top = 12.dp, bottom = 4.dp)
            )

            // 2) 같은 초성에 속한 각각의 연락처를 개별 블록으로 표시
            items.forEach { contact ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                ) {
                    ContactItem(contact)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ContactItem(contact: Contact) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = contact.imageRes),
            contentDescription = contact.name,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = contact.name,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold))
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = contact.phoneNumber,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_light))
            )
            Text(
                text = contact.email,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_light))
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_location),
            contentDescription = "위치 보기",
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF007AFF)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListScreenPreview() {
    ContactListScreen()
}
