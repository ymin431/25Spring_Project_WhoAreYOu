package com.example.whoareyou.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.whoareyou.component.BottomBar
import com.example.whoareyou.component.BottomTab
import com.example.whoareyou.home.HomeScreen

@Composable
fun MainScreen() {
    var selectedTab by rememberSaveable { mutableStateOf(BottomTab.Home) }

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BottomTab.Home -> HomeScreen()
                BottomTab.Contacts -> {}
                BottomTab.MyPage -> {}
            }
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen()
}