package com.example.whoareyou.nav

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.whoareyou.contactlist.ContactListScreen
import com.example.whoareyou.home.HomeScreen
import com.example.whoareyou.mypage.MyPageScreen
import com.example.whoareyou.ocrcontact.DataConfirmScreenWrapper
import com.example.whoareyou.login.LoginScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.whoareyou.login.LoginViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("login") {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(
                loginViewModel = loginViewModel,
                onGoogleLoginClicked = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("contact_list") {
            ContactListScreen(navController)
        }

        composable("mypage") {
            MyPageScreen(navController)
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