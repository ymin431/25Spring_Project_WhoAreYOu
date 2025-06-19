package com.example.whoareyou.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraManager(
    private val context: Context,
    private val launcher: ActivityResultLauncher<Intent>
) {
    var photoUri: Uri? = null
        private set

    fun launchCamera() {
        try {
            Log.d("CameraManager", "카메라 실행 시작")
            val photoFile = createImageFile(context)
            Log.d("CameraManager", "임시 파일 생성: ${photoFile.absolutePath}")

            photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            Log.d("CameraManager", "FileProvider URI 생성: $photoUri")

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }

            Log.d("CameraManager", "카메라 인텐트 실행")
            launcher.launch(intent)
        } catch (e: Exception) {
            Log.e("CameraManager", "카메라 실행 중 오류 발생", e)
            throw e
        }
    }

    private fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir!!)
        Log.d("CameraManager", "임시 파일 생성 완료: ${file.absolutePath}")
        return file
    }

    fun clearPhotoUri() {
        Log.d("CameraManager", "photoUri 초기화")
        photoUri = null
    }
}