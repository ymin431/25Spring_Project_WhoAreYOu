package com.example.whoareyou.home

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.whoareyou.R
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.whoareyou.ocrcontact.Contact
import com.example.whoareyou.repository.ContactRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class Contact(
    val image: Int,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val address: String
)

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { ContactRepository() }
    var recentContacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        repository.getRecentContacts().collectLatest { contacts ->
            recentContacts = contacts
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        repository.getRecentContacts().collectLatest { contacts ->
            recentContacts = contacts
        }
    }

    var cameraManager by remember { mutableStateOf<CameraManager?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = cameraManager?.photoUri
            if (uri != null) {
                navController.navigate("data_confirm?imageUri=${Uri.encode(uri.toString())}")
            }
        }
    }

    LaunchedEffect(cameraLauncher) {
        cameraManager = CameraManager(context, cameraLauncher)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraManager?.launchCamera()
        } else {
            Toast.makeText(context, "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            navController.navigate("data_confirm?imageUri=${Uri.encode(it.toString())}")
        }
    }

    val tempContact = Contact(
        image = R.drawable.ic_main_logo,
        name = "김영희",
        phoneNumber = "010-3083-7216",
        email = "abc123@naver.com",
        address = "부산광역시"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Spacer(Modifier.width(20.dp))
            ButtonWithLogo(
                logo = R.drawable.btn_camera,
                description = "카메라",
                label = "명함 사진 찍기",
                onClick = {
                    val permission = android.Manifest.permission.CAMERA
                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        cameraManager?.launchCamera()
                    } else {
                        permissionLauncher.launch(permission)
                    }
                }
            )
            ButtonWithLogo(
                logo = R.drawable.btn_gallery,
                description = "갤러리",
                label = "갤러리에서 선택",
                onClick = {
                    galleryLauncher.launch("image/*")
                }
            )
            Spacer(Modifier.width(20.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "최근 연락처",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (recentContacts.isEmpty()) {
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
                items(recentContacts) { contact ->
                    RecentContactItem(contact = contact)
                }
            }
        }
    }
}

@Composable
fun ButtonWithLogo(
    logo: Int,
    description: String,
    label: String,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF007AFF))
            .clickable { onClick() }
            .padding(vertical = 20.dp)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(logo),
            contentDescription = description,
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun RecentContactItem(contact: Contact) {
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
            Column {
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
        }
    }
}

//@Composable
//@Preview
//fun HomeScreenPreview() {
//    HomeScreen(navController = null)
//}