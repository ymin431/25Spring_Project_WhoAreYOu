package com.example.whoareyou.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.whoareyou.data.ImageDao
import com.example.whoareyou.data.ImageEntity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageRepository(private val imageDao: ImageDao, private val context: Context) {
    
    fun getAllImages(): Flow<List<ImageEntity>> = imageDao.getAllImages()

    suspend fun saveImage(uri: Uri): Long {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("Image_Save", "Starting to save image from URI: $uri")
                val inputStream = context.contentResolver.openInputStream(uri)
                val fileName = "image_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)
                
                inputStream?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }

                Log.d("Image_Save", "Image saved successfully at: ${file.absolutePath}")
                val imageEntity = ImageEntity(imagePath = file.absolutePath)
                imageDao.insertImage(imageEntity)
            } catch (e: Exception) {
                Log.e("Image_Save", "Error saving image", e)
                throw e
            }
        }
    }

    suspend fun performOcr(imageId: Long) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("OCR_Process", "Starting OCR process for image ID: $imageId")
                val images = imageDao.getAllImages().collect { images ->
                    val image = images.find { it.id == imageId } ?: run {
                        Log.e("OCR_Error", "Image not found with ID: $imageId")
                        return@collect
                    }
                    
                    val file = File(image.imagePath)
                    if (!file.exists()) {
                        Log.e("OCR_Error", "Image file does not exist: ${image.imagePath}")
                        return@collect
                    }

                    try {
                        Log.d("OCR_Process", "Decoding bitmap from file: ${image.imagePath}")
                        val bitmap = android.graphics.BitmapFactory.decodeFile(file.absolutePath)
                        if (bitmap == null) {
                            Log.e("OCR_Error", "Failed to decode bitmap from file: ${image.imagePath}")
                            return@collect
                        }

                        Log.d("OCR_Process", "Creating input image from bitmap")
                        val inputImage = InputImage.fromBitmap(bitmap, 0)
                        
                        Log.d("OCR_Process", "Initializing Korean text recognizer")
                        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
                        
                        Log.d("OCR_Process", "Starting text recognition")
                        val result = recognizer.process(inputImage).await()
                        
                        val ocrText = result.text
                        Log.d("OCR_Result", "Image ID: $imageId")
                        Log.d("OCR_Result", "Recognized Text: $ocrText")
                        
                        if (ocrText.isBlank()) {
                            Log.w("OCR_Result", "No text was recognized in the image")
                        }
                        
                        result.textBlocks.forEachIndexed { index, block ->
                            Log.d("OCR_Result", "Block $index Text: ${block.text}")
                            block.lines.forEachIndexed { lineIndex, line ->
                                Log.d("OCR_Result", "Block $index, Line $lineIndex: ${line.text}")
                            }
                        }
                        
                        imageDao.updateImage(image.copy(ocrText = ocrText))
                        Log.d("OCR_Success", "OCR completed successfully for image ID: $imageId")
                    } catch (e: Exception) {
                        Log.e("OCR_Error", "Error during OCR processing: ${e.message}", e)
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                Log.e("OCR_Error", "Error in performOcr: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }
} 