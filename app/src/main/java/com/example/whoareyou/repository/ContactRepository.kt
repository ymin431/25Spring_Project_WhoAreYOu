package com.example.whoareyou.repository

import android.net.Uri
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

    suspend fun saveContact(contact: Contact, imageUri: Uri?): Result<Unit> = try {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        
        // 1. 이미지가 있으면 Storage에 업로드
        val imageURL = imageUri?.let { uploadImage(it) } ?: ""
        
        // 2. Contact 객체 생성 (이미지 URL 포함)
        val contactWithImage = contact.copy(
            imageURL = imageURL,
            savedAt = com.google.firebase.Timestamp.now()
        )
        
        // 3. Firestore에 저장
        db.collection("users")
            .document(userId)
            .collection("contacts")
            .document(UUID.randomUUID().toString())
            .set(contactWithImage)
            .await()
            
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getRecentContacts(limit: Int = 5): Flow<List<Contact>> = flow {
        try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
            
            val snapshot = db.collection("users")
                .document(userId)
                .collection("contacts")
                .orderBy("savedAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                
            val contacts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Contact::class.java)
            }
            
            emit(contacts)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    fun getAllContacts(): Flow<List<Contact>> = flow {
        try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
            
            val snapshot = db.collection("users")
                .document(userId)
                .collection("contacts")
                .orderBy("savedAt", Query.Direction.DESCENDING)
                .get()
                .await()
                
            val contacts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Contact::class.java)
            }
            
            emit(contacts)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    private suspend fun uploadImage(imageUri: Uri): String {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val imageId = UUID.randomUUID().toString()
        val imageRef = storage.reference.child("users/$userId/contacts/$imageId.jpg")
        
        return imageRef.putFile(imageUri).await()
            .storage.downloadUrl.await().toString()
    }
} 