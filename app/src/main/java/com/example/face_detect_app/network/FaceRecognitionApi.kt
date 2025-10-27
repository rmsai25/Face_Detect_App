package com.example.face_detect_app.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface FaceRecognitionApi {

    @Multipart
    @POST("register")
    suspend fun registerFace(
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<RegisterResponse>

    @Multipart
    @POST("recognize")
    suspend fun recognizeFace(
        @Part image: MultipartBody.Part,
        @Query("threshold") threshold: Double = 0.7
    ): Response<RecognizeResponse>

    @GET("users")
    suspend fun getAllUsers(): Response<UsersListResponse>
}
