package com.example.whoareyou.contactlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.whoareyou.R
import com.example.whoareyou.ocrcontact.Contact
import com.example.whoareyou.repository.ContactRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
fun ContactListScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { ContactRepository() }
    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        repository.getAllContacts().collectLatest { contactList ->
            contacts = contactList
        }
    }

    // 연락처를 초성별로 그룹화
    val groupedContacts = contacts
        .sortedBy { it.name }
        .groupBy { getInitial(it.name) }
        .toSortedMap()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
            .padding(16.dp)
    ) {
        // 상단 바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "연락처 목록",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold))
            )
        }

        if (contacts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "저장된 연락처가 없습니다",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium))
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                groupedContacts.forEach { (initial, items) ->
                    item {
                        Text(
                            text = initial,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 4.dp)
                        )
                    }
                    items(items) { contact ->
                        ContactItem(contact = contact)
                    }
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지
            if (contact.imageURL.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(contact.imageURL),
                    contentDescription = "Contact Image",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF007AFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = contact.name.firstOrNull()?.toString() ?: "",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold))
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = contact.name,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold))
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

            Spacer(modifier = Modifier.width(10.dp))
            
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_location),
                contentDescription = "위치 보기",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF007AFF)
            )
        }
    }
} 