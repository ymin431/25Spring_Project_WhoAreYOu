package com.example.whoareyou.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.whoareyou.component.BottomBar
import com.example.whoareyou.component.BottomTab
import com.example.whoareyou.component.TopTab
import com.example.whoareyou.contactlist.ContactListScreen
import com.example.whoareyou.home.HomeScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Contacts : Screen("contacts")
    object MyPage : Screen("mypage")
    object OcrLoading : Screen("ocr_loading/{uri}")
    object DataConfirm : Screen("data_confirm")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // 현재 화면에 따라 BottomTab 결정
    val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry).value?.destination?.route
    val selectedTab = when (currentRoute) {
        Screen.Home.route -> BottomTab.Home
        Screen.Contacts.route -> BottomTab.Contacts
        Screen.MyPage.route -> BottomTab.MyPage
        else -> BottomTab.Home // 기본값
    }

    Scaffold(
        topBar = { TopTab() },
        bottomBar = {
            BottomBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    val route = when (tab) {
                        BottomTab.Home -> Screen.Home.route
                        BottomTab.Contacts -> Screen.Contacts.route
                        BottomTab.MyPage -> Screen.MyPage.route
                    }
                    // 중복 네비게이션 방지
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Contacts.route) { ContactListScreen() }
            composable(Screen.MyPage.route) { /* MyPageScreen() 구현 필요 */ }
            composable(Screen.OcrLoading.route) { backStackEntry ->
                val uri = backStackEntry.arguments?.getString("uri")
                // OcrLoadingScreen(uri)
            }
            composable(Screen.DataConfirm.route) {
                // DataConfirmScreen()
            }
        }
    }
}


@Composable
@Preview
fun MainScreenPreview() {
    MainScreen()
}