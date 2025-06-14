package com.example.whoareyou.ocrcontact

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Contact(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val addressText: String = "",
    val addressGeopoint: GeoPoint? = null,
    val imageURL: String = "",
    @ServerTimestamp
    val savedAt: Timestamp? = null
) 