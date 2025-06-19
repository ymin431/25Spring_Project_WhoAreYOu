package com.example.whoareyou.ocrcontact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.whoareyou.ui.theme.WhoAreYOuTheme

class DataConfirmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val imageUri = imageUriString?.let { Uri.parse(it) }
        setContent {
            WhoAreYOuTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DataConfirmScreenWrapper(
                        imageUri = imageUri,
                        navController = null
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
} 