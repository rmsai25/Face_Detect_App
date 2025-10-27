package com.example.face_detect_app.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.face_detect_app.network.RecognizeResponse
import com.example.face_detect_app.network.RegisterResponse
import com.example.face_detect_app.network.RetrofitClient
import com.example.face_detect_app.network.UsersListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class FaceRepository(private val context: Context) {

    private val api = RetrofitClient.api

    suspend fun registerFace(name: String, bitmap: Bitmap): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val file = bitmapToFile(bitmap, "face_register_${System.currentTimeMillis()}.jpg")
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
                val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = api.registerFace(namePart, imagePart)
                file.delete()

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(Exception(errorBody))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun recognizeFace(bitmap: Bitmap, threshold: Double = 0.7): Result<RecognizeResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val file = bitmapToFile(bitmap, "face_recognize_${System.currentTimeMillis()}.jpg")
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

                val response = api.recognizeFace(imagePart, threshold)
                file.delete()

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(Exception(errorBody))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getAllUsers(): Result<UsersListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getAllUsers()

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(Exception(errorBody))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap, filename: String): File {
        val file = File(context.cacheDir, filename)
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.flush()
        outputStream.close()

        return file
    }
}
