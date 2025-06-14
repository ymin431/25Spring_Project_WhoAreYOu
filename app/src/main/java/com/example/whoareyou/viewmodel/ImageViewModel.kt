package com.example.whoareyou.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whoareyou.data.ImageEntity
import com.example.whoareyou.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {
    private val _images = MutableStateFlow<List<ImageEntity>>(emptyList())
    val images: StateFlow<List<ImageEntity>> = _images.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllImages().collect { images ->
                _images.value = images
            }
        }
    }

    fun saveImage(uri: Uri) {
        viewModelScope.launch {
            repository.saveImage(uri)
        }
    }

    fun performOcr(imageId: Long) {
        viewModelScope.launch {
            repository.performOcr(imageId)
        }
    }
} 