package com.example.face_detect_app.models

import android.graphics.Bitmap

data class User(
    val id: Int,  // Changed from String to Int to match API
    val name: String,
    val faceConfidence: Double = 0.0,
    val faceImage: Bitmap? = null,
    val registeredAt: Long = System.currentTimeMillis()
)
