package com.example.whoareyou.nav

import androidx.compose.runtime.Composable
import android.net.Uri
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.whoareyou.contactlist.ContactListScreen
import com.example.whoareyou.home.HomeScreen
import com.example.whoareyou.ocrcontact.Contact
import com.example.whoareyou.ocrcontact.DataConfirmScreenWrapper

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(navController)
        }
        composable(NavRoutes.CONTACTS) {
            ContactListScreen()
        }
        composable(NavRoutes.MYPAGE) {
            // TODO: 마이페이지 화면 추가
        }
        composable(
            "data_confirm?imageUri={imageUri}",
            arguments = listOf(navArgument("imageUri") { nullable = true; type = NavType.StringType })
        ) { backStackEntry ->
            val imageUriString = backStackEntry.arguments?.getString("imageUri")
            val imageUri = imageUriString?.let { Uri.parse(it) }
            DataConfirmScreenWrapper(
                imageUri = imageUri,
                navController = navController
            )
        }
    }
}