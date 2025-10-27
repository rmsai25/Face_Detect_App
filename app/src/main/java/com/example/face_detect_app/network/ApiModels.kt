package com.example.face_detect_app.network

import com.google.gson.annotations.SerializedName

// Register Response
data class RegisterResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("face_confidence") val faceConfidence: Double
)

// Recognize Response
data class RecognizeResponse(
    @SerializedName("status") val status: String,
    @SerializedName("recognized") val recognized: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("user") val user: RecognizedUser? = null
)

data class RecognizedUser(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("distance") val distance: Double,
    @SerializedName("faces_detected") val facesDetected: Int,
    @SerializedName("primary_face_confidence") val primaryFaceConfidence: Double
)

// Users List Response - NEW
data class UsersListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("count") val count: Int,
    @SerializedName("users") val users: List<UserInfo>
)

data class UserInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("created_at") val createdAt: String? = null
)

// Error Response
data class ErrorResponse(
    @SerializedName("detail") val detail: String
)
