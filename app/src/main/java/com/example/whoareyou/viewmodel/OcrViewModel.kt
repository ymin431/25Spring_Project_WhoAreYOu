package com.example.whoareyou.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whoareyou.ocrcontact.Contact
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException

class OcrViewModel : ViewModel() {
    private val _ocrState = MutableStateFlow<OcrState>(OcrState.Initial)
    val ocrState: StateFlow<OcrState> = _ocrState

    fun processImage(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            try {
                _ocrState.value = OcrState.Loading

                val bitmap = getBitmapFromUri(context, imageUri)
                val image = InputImage.fromBitmap(bitmap, 0)

                val recognizer =
                    TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
                val result = recognizer.process(image).await()

                val contact = extractContactInfo(result.text)
                _ocrState.value = OcrState.Success(contact)
            } catch (e: Exception) {
                _ocrState.value = OcrState.Error(e.message ?: "OCR 처리 중 오류가 발생했습니다.")
            }
        }
    }

    private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            } ?: throw IOException("이미지를 불러올 수 없습니다.")
        } catch (e: Exception) {
            throw IOException("이미지 처리 중 오류가 발생했습니다: ${e.message}")
        }
    }

    private fun extractContactInfo(text: String): Contact {
        // 1. 이름 추출 (한글 2~4자, 여러 줄 중 첫 번째)
        val namePattern = Regex("(?m)^([가-힣]{2,4})$")
        val nameMatch = namePattern.find(text)
        val name = nameMatch?.groupValues?.get(1) ?: "이름 없음"

        // 2. 전화번호 추출 (010, 011 등 다양한 패턴, 하이픈/공백/점 모두)
        val phonePattern = Regex("(01[016789]|02|0[3-9][0-9])[- .]?\\d{3,4}[- .]?\\d{4}")
        val phoneNumber = phonePattern.find(text)?.value?.replace(" ", "") ?: "전화번호 없음"

        // 3. 이메일 추출
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val email = emailPattern.find(text)?.value ?: "이메일 없음"

        // 4. 주소 추출 (이름이 포함된 라인은 제외)
        val lines = text.lines()
        val addressPattern = Regex("([가-힣]+(시|도|구|군|로|길|동|읍|면)[^\\n]*)")
        val address = lines
            .filter { line -> name.isBlank() || !line.contains(name) } // 이름이 포함된 라인 제외
            .mapNotNull { line -> addressPattern.find(line)?.value }
            .firstOrNull() ?: "주소 없음"

        return Contact(
            name = name,
            phoneNumber = phoneNumber,
            email = email,
            addressText = address
        )
    }
}

sealed class OcrState {
    object Initial : OcrState()
    object Loading : OcrState()
    data class Success(val contact: Contact) : OcrState()
    data class Error(val message: String) : OcrState()
} 