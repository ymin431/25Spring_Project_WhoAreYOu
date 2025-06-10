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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.whoareyou.ocrcontact.OcrLoadingScreen

data class Contact(
    val image: Int,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val address: String
)

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var showOcrLoading by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraManager by remember { mutableStateOf<CameraManager?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = cameraManager?.photoUri
            showOcrLoading = true
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
            selectedImageUri = it
            showOcrLoading = true
        }
    }

    val tempContact = Contact(
        image = R.drawable.ic_main_logo,
        name = "김영희",
        phoneNumber = "010-3083-7216",
        email = "abc123@naver.com",
        address = "부산광역시"
    )

    if (showOcrLoading && selectedImageUri != null) {
        OcrLoadingScreen(
            onBack = { showOcrLoading = false },
            imageUri = selectedImageUri!!
        )
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F7))
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
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

// ... existing code ...
// ... existing code ...