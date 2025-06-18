package com.example.whoareyou.ocrcontact

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object EmailValidation {
    suspend fun validateEmail(email: String): EmailValidationResult? {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = "dc7153a56b684e4faae9e56f28276df4"
                val apiUrl = "https://emailvalidation.abstractapi.com/v1/?api_key=$apiKey&email=$email"

                val connection = URL(apiUrl).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val response = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(response)

                return@withContext EmailValidationResult(
                    isValid = json.getString("deliverability") == "DELIVERABLE",
                    qualityScore = json.getDouble("quality_score")
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    data class EmailValidationResult(
        val isValid: Boolean,
        val qualityScore: Double
    )
}