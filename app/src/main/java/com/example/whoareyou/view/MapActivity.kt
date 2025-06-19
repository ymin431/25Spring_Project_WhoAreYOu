package com.example.whoareyou.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val address = intent.getStringExtra("address") ?: ""
        setContent {
            MapScreen(address = address, onBack = { finish() })
        }
    }
} 