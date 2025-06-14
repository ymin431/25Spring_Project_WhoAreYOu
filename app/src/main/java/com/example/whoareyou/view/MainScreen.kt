package com.example.whoareyou.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.whoareyou.component.BottomBar
import com.example.whoareyou.component.BottomTab
import com.example.whoareyou.component.TopTab
import com.example.whoareyou.nav.AppNavGraph
import com.example.whoareyou.nav.NavRoutes
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.whoareyou.component.routers

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val arguments = navBackStackEntry?.arguments

    println("currentRoute: $currentRoute, arguments: $arguments")

    fun shouldHideBars(route: String?): Boolean {
        if (route == null) return false
        return routers.subScreens.any { sub ->
            if (sub.contains("{")) {
                val prefix = sub.substringBefore("{")
                route.startsWith(prefix)
            } else {
                route == sub
            }
        }
    }

    val hideBars = shouldHideBars(currentRoute)

    Scaffold(
        topBar = {
            if (!hideBars) TopTab()
        },
        bottomBar = {
            if (!hideBars) {
                val selectedTab = when (currentRoute) {
                    NavRoutes.HOME -> BottomTab.Home
                    NavRoutes.CONTACTS -> BottomTab.Contacts
                    NavRoutes.MYPAGE -> BottomTab.MyPage
                    else -> null
                }
                if (selectedTab != null) {
                    BottomBar(
                        selectedTab = selectedTab,
                        onTabSelected = {
                            val route = when (it) {
                                BottomTab.Home -> NavRoutes.HOME
                                BottomTab.Contacts -> NavRoutes.CONTACTS
                                BottomTab.MyPage -> NavRoutes.MYPAGE
                            }
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo(NavRoutes.HOME) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppNavGraph(navController = navController)
        }
    }
}