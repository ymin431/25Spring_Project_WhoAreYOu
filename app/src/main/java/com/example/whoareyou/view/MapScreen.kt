package com.example.whoareyou.view

import android.location.Geocoder
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MapScreen(address: String, onBack: (() -> Unit)? = null) {
    val context = LocalContext.current
    var position by remember { mutableStateOf<LatLng?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    // 주소를 위도/경도로 변환
    LaunchedEffect(address) {
        withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context)
                val results = geocoder.getFromLocationName(address, 1)
                if (!results.isNullOrEmpty()) {
                    val loc = results[0]
                    position = LatLng(loc.latitude, loc.longitude)
                } else {
                    error = "주소를 찾을 수 없습니다."
                }
            } catch (e: Exception) {
                error = "지오코딩 오류: ${e.localizedMessage}"
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (position != null) {
            val cameraPosition = com.google.android.gms.maps.model.CameraPosition.Builder()
                .target(position!!)
                .zoom(16f)
                .build()
            val cameraPositionState = rememberCameraPositionState {
                this.position = cameraPosition
            }
            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier.fillMaxSize()
            ) {
                Marker(
                    state = MarkerState(position = position!!),
                    title = address
                )
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                androidx.compose.material3.Text(error!!)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                androidx.compose.material3.Text("지도 로딩 중...")
            }
        }

        // 상단 뒤로가기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp)
                .align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack?.invoke() ?: (context as? android.app.Activity)?.finish() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = Color.Black
                )
            }
        }

        // 하단 주소 표시
        if (position != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    androidx.compose.material3.Text(
                        text = address,
                        color = Color.Black
                    )
                }
            }
        }
    }
} 