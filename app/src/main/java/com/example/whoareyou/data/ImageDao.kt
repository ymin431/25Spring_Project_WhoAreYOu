package com.example.whoareyou.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM images ORDER BY timestamp DESC")
    fun getAllImages(): Flow<List<ImageEntity>>

    @Insert
    suspend fun insertImage(image: ImageEntity): Long

    @Update
    suspend fun updateImage(image: ImageEntity)

    @Delete
    suspend fun deleteImage(image: ImageEntity)
} 