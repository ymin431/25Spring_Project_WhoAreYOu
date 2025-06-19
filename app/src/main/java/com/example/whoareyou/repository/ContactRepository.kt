package com.example.whoareyou.repository

import android.net.Uri
import android.util.Log
import com.example.whoareyou.ocrcontact.Contact
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ContactRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "ContactRepository"

    suspend fun saveContact(contact: Contact, imageUri: Uri?): Result<Unit> = try {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        Log.d(TAG, "연락처 저장 시작 - 사용자: $userId")
        
        val imageURL = imageUri?.let { uploadImage(it) } ?: ""
        
        val contactWithImage = contact.copy(
            imageURL = imageURL,
            savedAt = com.google.firebase.Timestamp.now()
        )
        
        db.collection("users")
            .document(userId)
            .collection("contacts")
            .document(UUID.randomUUID().toString())
            .set(contactWithImage)
            .await()
            
        Log.d(TAG, "연락처 저장 완료")
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "연락처 저장 실패", e)
        Result.failure(e)
    }

    fun getRecentContacts(limit: Int = 5): Flow<List<Contact>> = flow {
        try {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Log.d(TAG, "사용자가 로그인하지 않은 상태입니다")
                emit(emptyList())
                return@flow
            }
            
            Log.d(TAG, "최근 연락처 로드 시작 - 사용자: ${currentUser.uid}")
            
            val snapshot = db.collection("users")
                .document(currentUser.uid)
                .collection("contacts")
                .orderBy("savedAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                
            val contacts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Contact::class.java)
            }
            
            Log.d(TAG, "최근 연락처 로드 완료 - ${contacts.size}개")
            emit(contacts)
        } catch (e: Exception) {
            Log.e(TAG, "최근 연락처 로드 실패", e)
            emit(emptyList())
        }
    }

    fun getAllContacts(): Flow<List<Contact>> = flow {
        try {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Log.d(TAG, "사용자가 로그인하지 않은 상태입니다")
                emit(emptyList())
                return@flow
            }
            
            Log.d(TAG, "전체 연락처 로드 시작 - 사용자: ${currentUser.uid}")
            
            val snapshot = db.collection("users")
                .document(currentUser.uid)
                .collection("contacts")
                .orderBy("savedAt", Query.Direction.DESCENDING)
                .get()
                .await()
                
            val contacts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Contact::class.java)
            }
            
            Log.d(TAG, "전체 연락처 로드 완료 - ${contacts.size}개")
            emit(contacts)
        } catch (e: Exception) {
            Log.e(TAG, "전체 연락처 로드 실패", e)
            emit(emptyList())
        }
    }

    private suspend fun uploadImage(imageUri: Uri): String {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        Log.d(TAG, "이미지 업로드 시작 - 사용자: $userId")
        val imageId = UUID.randomUUID().toString()
        val imageRef = storage.reference.child("users/$userId/contacts/$imageId.jpg")
        
        val downloadUrl = imageRef.putFile(imageUri).await()
            .storage.downloadUrl.await().toString()
        Log.d(TAG, "이미지 업로드 완료")
        return downloadUrl
    }
} 