package com.example.whoareyou.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

object NetworkUtils {
    private const val TAG = "NetworkUtils"
    
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        return try {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            
            val result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            
            Log.d(TAG, "네트워크 상태: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "네트워크 상태 확인 실패", e)
            false
        }
    }
    
    fun getNetworkType(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        return try {
            val network = connectivityManager.activeNetwork ?: return "없음"
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return "없음"
            
            when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "모바일 데이터"
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "이더넷"
                else -> "알 수 없음"
            }
        } catch (e: Exception) {
            Log.e(TAG, "네트워크 타입 확인 실패", e)
            "오류"
        }
    }
} 