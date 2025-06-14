package com.example.whoareyou.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imagePath: String,
    val timestamp: Long = System.currentTimeMillis(),
    val ocrText: String? = null
) 