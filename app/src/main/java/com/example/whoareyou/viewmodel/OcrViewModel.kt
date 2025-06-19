package com.example.whoareyou.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
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

    fun resetOcrState() {
        Log.d("OcrViewModel", "=== OCR 상태 초기화 ===")
        _ocrState.value = OcrState.Initial
        Log.d("OcrViewModel", "OCR 상태가 Initial로 초기화되었습니다")
    }

    fun processImage(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            try {
                Log.d("OcrViewModel", "=== OCR 처리 시작 ===")
                Log.d("OcrViewModel", "이미지 URI: $imageUri")
                _ocrState.value = OcrState.Loading

                val bitmap = getBitmapFromUri(context, imageUri)
                Log.d("OcrViewModel", "이미지 로드 완료: ${bitmap.width}x${bitmap.height}")
                Log.d("OcrViewModel", "이미지 크기: ${bitmap.byteCount} bytes")
                
                val image = InputImage.fromBitmap(bitmap, 0)
                Log.d("OcrViewModel", "InputImage 생성 완료")

                val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
                Log.d("OcrViewModel", "텍스트 인식기 생성 완료")
                
                Log.d("OcrViewModel", "OCR 처리 시작...")
                val result = recognizer.process(image).await()
                
                Log.d("OcrViewModel", "=== OCR 결과 ===")
                Log.d("OcrViewModel", "전체 텍스트: ${result.text}")
                Log.d("OcrViewModel", "텍스트 블록 수: ${result.textBlocks.size}")
                
                result.textBlocks.forEachIndexed { index, block ->
                    Log.d("OcrViewModel", "블록 $index: '${block.text}'")
                    Log.d("OcrViewModel", "  - 라인 수: ${block.lines.size}")
                    block.lines.forEachIndexed { lineIndex, line ->
                        Log.d("OcrViewModel", "    라인 $lineIndex: '${line.text}'")
                        Log.d("OcrViewModel", "    - 요소 수: ${line.elements.size}")
                        line.elements.forEachIndexed { elementIndex, element ->
                            Log.d("OcrViewModel", "      요소 $elementIndex: '${element.text}'")
                        }
                    }
                }

                if (result.text.isBlank()) {
                    Log.w("OcrViewModel", "OCR 결과가 비어있습니다")
                    _ocrState.value = OcrState.Error("이미지에서 텍스트를 인식할 수 없습니다. 더 선명한 이미지를 촬영해주세요.")
                    return@launch
                }

                Log.d("OcrViewModel", "=== 연락처 정보 추출 시작 ===")
                val contact = extractContactInfo(result.text)
                Log.d("OcrViewModel", "연락처 정보 추출 완료: $contact")
                Log.d("OcrViewModel", "=== OCR 처리 완료 ===")
                _ocrState.value = OcrState.Success(contact)
            } catch (e: Exception) {
                Log.e("OcrViewModel", "OCR 처리 중 오류 발생", e)
                Log.e("OcrViewModel", "오류 메시지: ${e.message}")
                Log.e("OcrViewModel", "오류 스택 트레이스: ${e.stackTraceToString()}")
                _ocrState.value = OcrState.Error("OCR 처리 중 오류가 발생했습니다: ${e.message}")
            }
        }
    }

    private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap {
        try {
            Log.d("OcrViewModel", "=== 이미지 로드 시작 ===")
            Log.d("OcrViewModel", "URI: $uri")

            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            if (bitmap == null) throw IOException("비트맵 디코딩 실패")

            val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
            val exif = fileDescriptor?.let { ExifInterface(it) }
            val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            Log.d("OcrViewModel", "EXIF 회전 적용: $orientation")
            Log.d("OcrViewModel", "최종 비트맵 크기: ${rotatedBitmap.width}x${rotatedBitmap.height}")
            Log.d("OcrViewModel", "=== 이미지 로드 완료 ===")
            return rotatedBitmap
        } catch (e: Exception) {
            Log.e("OcrViewModel", "이미지 로드 중 오류", e)
            Log.e("OcrViewModel", "이미지 로드 오류 메시지: ${e.message}")
            throw IOException("이미지 처리 중 오류가 발생했습니다: ${e.message}")
        }
    }

    private fun extractContactInfo(text: String): Contact {
        Log.d("OcrViewModel", "=== 텍스트 추출 시작 ===")
        Log.d("OcrViewModel", "입력 텍스트: '$text'")
        
        val namePattern = Regex("(?m)^([가-힣]{2,5})\\s*$")
        val nameMatch = namePattern.find(text)
        val name = nameMatch?.groupValues?.get(1)?.trim() ?: "이름 없음"
        Log.d("OcrViewModel", "이름 패턴 매칭 결과: ${nameMatch?.groupValues}")
        Log.d("OcrViewModel", "추출된 이름: '$name'")

        val phonePattern = Regex("(01[016789]|02|0[3-9][0-9])[- .]?\\d{3,4}[- .]?\\d{4}")
        val phoneMatch = phonePattern.find(text)
        val phoneNumber = phoneMatch?.value?.replace(" ", "") ?: "전화번호 없음"
        Log.d("OcrViewModel", "전화번호 패턴 매칭 결과: ${phoneMatch?.value}")
        Log.d("OcrViewModel", "추출된 전화번호: '$phoneNumber'")

        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val emailMatch = emailPattern.find(text)
        val email = emailMatch?.value ?: "이메일 없음"
        Log.d("OcrViewModel", "이메일 패턴 매칭 결과: ${emailMatch?.value}")
        Log.d("OcrViewModel", "추출된 이메일: '$email'")

        val lines = text.lines().filter { it.trim().isNotEmpty() }
        Log.d("OcrViewModel", "텍스트 라인들: $lines")
        
        val addressPattern = Regex("([가-힣]+(시|도|구|군|로|길|동|읍|면)[^\\n]*)")
        val addressCandidates = lines
            .filter { line -> 
                val trimmedLine = line.trim()
                trimmedLine.isNotEmpty() && 
                (name == "이름 없음" || !trimmedLine.contains(name))
            }
            .mapNotNull { line -> 
                val match = addressPattern.find(line.trim())
                match?.value?.trim()
            }
        
        Log.d("OcrViewModel", "주소 후보들: $addressCandidates")
        val address = addressCandidates.firstOrNull() ?: "주소 없음"
        Log.d("OcrViewModel", "최종 추출된 주소: '$address'")

        val contact = Contact(
            name = name,
            phoneNumber = phoneNumber,
            email = email,
            addressText = address
        )
        
        Log.d("OcrViewModel", "=== 최종 연락처 정보 ===")
        Log.d("OcrViewModel", "이름: '${contact.name}'")
        Log.d("OcrViewModel", "전화번호: '${contact.phoneNumber}'")
        Log.d("OcrViewModel", "이메일: '${contact.email}'")
        Log.d("OcrViewModel", "주소: '${contact.addressText}'")
        Log.d("OcrViewModel", "=== 텍스트 추출 완료 ===")
        
        return contact
    }
}

sealed class OcrState {
    object Initial : OcrState()
    object Loading : OcrState()
    data class Success(val contact: Contact) : OcrState()
    data class Error(val message: String) : OcrState()
} 