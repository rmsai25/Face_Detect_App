////package com.example.face_detect_app.viewmodel
////
////import android.graphics.Bitmap
////import androidx.compose.runtime.mutableStateListOf
////import androidx.compose.runtime.mutableStateOf
////import androidx.lifecycle.ViewModel
////import com.example.face_detect_app.models.User
////import kotlinx.coroutines.flow.MutableStateFlow
////import kotlinx.coroutines.flow.StateFlow
////import java.util.UUID
////
////sealed class RecognitionState {
////    object Idle : RecognitionState()
////    object Recognizing : RecognitionState()
////    data class Success(val user: User) : RecognitionState()
////    data class Error(val message: String) : RecognitionState()
////}
////
////class FaceDetectionViewModel : ViewModel() {
////
////    // Registered users
////    private val _registeredUsers = mutableStateListOf<User>()
////    val registeredUsers: List<User> = _registeredUsers
////
////    // UI States
////    var isCameraActive = mutableStateOf(false)
////        private set
////
////    var capturedImage = mutableStateOf<Bitmap?>(null)
////        private set
////
////    var showCaptureSuccess = mutableStateOf(false)
////        private set
////
////    var isLiveRecognitionActive = mutableStateOf(false)
////        private set
////
////    private val _recognitionState = MutableStateFlow<RecognitionState>(RecognitionState.Idle)
////    val recognitionState: StateFlow<RecognitionState> = _recognitionState
////
////    var showRegistrationSuccess = mutableStateOf(false)
////        private set
////
////    var registeredUserName = mutableStateOf("")
////        private set
////
////    init {
////        // Add dummy users
////        _registeredUsers.addAll(
////            listOf(
////                User("1", "John Doe", "face_001"),
////                User("2", "Jane Smith", "face_002"),
////                User("3", "Mike Johnson", "face_003")
////            )
////        )
////    }
////
////    // Camera Controls
////    fun startCamera() {
////        isCameraActive.value = true
////        capturedImage.value = null
////        showCaptureSuccess.value = false
////    }
////
////    fun stopCamera() {
////        isCameraActive.value = false
////    }
////
////    fun captureImage(bitmap: Bitmap? = null) {
////        // In a real app, bitmap would come from the camera
////        // For now, we'll simulate a capture
////        capturedImage.value = bitmap ?: createDummyBitmap()
////        showCaptureSuccess.value = true
////    }
////
////    private fun createDummyBitmap(): Bitmap {
////        // Create a dummy bitmap for simulation
////        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
////    }
////
////    fun dismissCaptureSuccess() {
////        showCaptureSuccess.value = false
////    }
////
////    // Registration
////    fun registerUser(name: String) {
////        if (name.isBlank()) return
////
////        val faceId = "face_${UUID.randomUUID().toString().take(8)}"
////        val newUser = User(
////            id = UUID.randomUUID().toString(),
////            name = name,
////            faceId = faceId,
////            faceImage = capturedImage.value
////        )
////        _registeredUsers.add(newUser)
////        registeredUserName.value = name
////        showRegistrationSuccess.value = true
////
////        // Reset
////        capturedImage.value = null
////        showCaptureSuccess.value = false
////    }
////
////    fun dismissRegistrationSuccess() {
////        showRegistrationSuccess.value = false
////        registeredUserName.value = ""
////    }
////
////    // Live Recognition
////    fun startLiveRecognition() {
////        isLiveRecognitionActive.value = true
////        _recognitionState.value = RecognitionState.Recognizing
////    }
////
////    fun stopLiveRecognition() {
////        isLiveRecognitionActive.value = false
////        _recognitionState.value = RecognitionState.Idle
////    }
////
////    fun simulateRecognition() {
////        if (!isLiveRecognitionActive.value) return
////
////        // Simulate random recognition
////        if (_registeredUsers.isNotEmpty() && Math.random() > 0.3) {
////            val randomUser = _registeredUsers.random()
////            _recognitionState.value = RecognitionState.Success(randomUser)
////        } else {
////            _recognitionState.value = RecognitionState.Error("No face detected or user not registered")
////        }
////    }
////
////    fun clearRecognitionError() {
////        if (_recognitionState.value is RecognitionState.Error) {
////            _recognitionState.value = RecognitionState.Recognizing
////        }
////    }
////
////    fun resetRecognition() {
////        _recognitionState.value = RecognitionState.Idle
////    }
////}
//
//
//
//package com.example.face_detect_app.viewmodel
//
//import android.app.Application
//import android.graphics.Bitmap
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.face_detect_app.models.User
//import com.example.face_detect_app.repository.FaceRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//sealed class RecognitionState {
//    object Idle : RecognitionState()
//    object Recognizing : RecognitionState()
//    data class Success(val user: User, val confidence: Double) : RecognitionState()
//    data class Error(val message: String) : RecognitionState()
//}
//
//sealed class RegistrationState {
//    object Idle : RegistrationState()
//    object Loading : RegistrationState()
//    data class Success(val user: User) : RegistrationState()
//    data class Error(val message: String) : RegistrationState()
//}
//
//class FaceDetectionViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository = FaceRepository(application)
//
//    // Registered users
//    private val _registeredUsers = mutableStateListOf<User>()
//    val registeredUsers: List<User> = _registeredUsers
//
//    // UI States
//    var isCameraActive = mutableStateOf(false)
//        private set
//
//    var capturedImage = mutableStateOf<Bitmap?>(null)
//        private set
//
//    var showCaptureSuccess = mutableStateOf(false)
//        private set
//
//    var isLiveRecognitionActive = mutableStateOf(false)
//        private set
//
//    private val _recognitionState = MutableStateFlow<RecognitionState>(RecognitionState.Idle)
//    val recognitionState: StateFlow<RecognitionState> = _recognitionState
//
//    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
//    val registrationState: StateFlow<RegistrationState> = _registrationState
//
//    var showRegistrationSuccess = mutableStateOf(false)
//        private set
//
//    var registeredUserName = mutableStateOf("")
//        private set
//
//    // Camera Controls
//    fun startCamera() {
//        isCameraActive.value = true
//        capturedImage.value = null
//        showCaptureSuccess.value = false
//    }
//
//    fun stopCamera() {
//        isCameraActive.value = false
//    }
//
//    fun captureImage(bitmap: Bitmap? = null) {
//        // In a real app, bitmap would come from the camera
//        // For now, we'll create a dummy bitmap
//        capturedImage.value = bitmap ?: createDummyBitmap()
//        showCaptureSuccess.value = true
//    }
//
//    private fun createDummyBitmap(): Bitmap {
//        return Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888)
//    }
//
//    fun dismissCaptureSuccess() {
//        showCaptureSuccess.value = false
//    }
//
//    // Registration with API
//    fun registerUser(name: String) {
//        if (name.isBlank()) return
//
//        val bitmap = capturedImage.value
//        if (bitmap == null) {
//            _registrationState.value = RegistrationState.Error("No image captured")
//            return
//        }
//
//        viewModelScope.launch {
//            _registrationState.value = RegistrationState.Loading
//
//            val result = repository.registerFace(name, bitmap)
//
//            result.onSuccess { response ->
//                val newUser = User(
//                    id = response.userId,
//                    name = name,
//                    faceConfidence = response.faceConfidence,
//                    faceImage = bitmap
//                )
//                _registeredUsers.add(newUser)
//                registeredUserName.value = name
//                showRegistrationSuccess.value = true
//                _registrationState.value = RegistrationState.Success(newUser)
//
//                // Reset
//                capturedImage.value = null
//                showCaptureSuccess.value = false
//            }
//
//            result.onFailure { error ->
//                _registrationState.value = RegistrationState.Error(
//                    error.message ?: "Registration failed"
//                )
//            }
//        }
//    }
//
//    fun dismissRegistrationSuccess() {
//        showRegistrationSuccess.value = false
//        registeredUserName.value = ""
//        _registrationState.value = RegistrationState.Idle
//    }
//
//    // Live Recognition with API
//    fun startLiveRecognition() {
//        isLiveRecognitionActive.value = true
//        _recognitionState.value = RecognitionState.Recognizing
//    }
//
//    fun stopLiveRecognition() {
//        isLiveRecognitionActive.value = false
//        _recognitionState.value = RecognitionState.Idle
//    }
//
//    fun performRecognition(bitmap: Bitmap) {
//        if (!isLiveRecognitionActive.value) return
//
//        viewModelScope.launch {
//            _recognitionState.value = RecognitionState.Recognizing
//
//            val result = repository.recognizeFace(bitmap)
//
//            result.onSuccess { response ->
//                if (response.recognized && response.user != null) {
//                    val user = User(
//                        id = response.user.id,
//                        name = response.user.name,
//                        faceConfidence = response.user.confidence
//                    )
//                    _recognitionState.value = RecognitionState.Success(
//                        user,
//                        response.user.confidence
//                    )
//                } else {
//                    _recognitionState.value = RecognitionState.Error(
//                        response.message ?: "No matching face found"
//                    )
//                }
//            }
//
//            result.onFailure { error ->
//                _recognitionState.value = RecognitionState.Error(
//                    error.message ?: "Recognition failed"
//                )
//            }
//        }
//    }
//
//    // Simulate recognition (for testing without actual camera)
//    fun simulateRecognition() {
//        val bitmap = capturedImage.value ?: createDummyBitmap()
//        performRecognition(bitmap)
//    }
//
//    fun clearRecognitionError() {
//        if (_recognitionState.value is RecognitionState.Error) {
//            _recognitionState.value = RecognitionState.Recognizing
//        }
//    }
//
//    fun resetRecognition() {
//        _recognitionState.value = RecognitionState.Idle
//    }
//}






package com.example.face_detect_app.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.face_detect_app.models.User
import com.example.face_detect_app.repository.FaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RecognitionState {
    object Idle : RecognitionState()
    object Recognizing : RecognitionState()
    data class Success(val user: User, val confidence: Double) : RecognitionState()
    data class Error(val message: String) : RecognitionState()
}

sealed class RegistrationState {
    object Idle : RegistrationState()
    object Loading : RegistrationState()
    data class Success(val user: User) : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}

class FaceDetectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FaceRepository(application)

    // Registered users
    private val _registeredUsers = mutableStateListOf<User>()
    val registeredUsers: List<User> = _registeredUsers

    // UI States
    var isCameraActive = mutableStateOf(false)
        private set

    var capturedImage = mutableStateOf<Bitmap?>(null)
        private set

    var showCaptureSuccess = mutableStateOf(false)
        private set

    var isLiveRecognitionActive = mutableStateOf(false)
        private set

    private val _recognitionState = MutableStateFlow<RecognitionState>(RecognitionState.Idle)
    val recognitionState: StateFlow<RecognitionState> = _recognitionState

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    var showRegistrationSuccess = mutableStateOf(false)
        private set

    var registeredUserName = mutableStateOf("")
        private set

    init {
        // Load users from API on startup
        loadAllUsers()
    }

    // Load all registered users from API
    fun loadAllUsers() {
        viewModelScope.launch {
            val result = repository.getAllUsers()

            result.onSuccess { response ->
                _registeredUsers.clear()
                _registeredUsers.addAll(
                    response.users.map { userInfo ->
                        User(
                            id = userInfo.id,
                            name = userInfo.name,
                            faceConfidence = 0.0
                        )
                    }
                )
            }

            result.onFailure { error ->
                // Silently fail - users list will be empty
                android.util.Log.e("FaceDetectionViewModel", "Failed to load users: ${error.message}")
            }
        }
    }

    // Camera Controls
    fun startCamera() {
        isCameraActive.value = true
        capturedImage.value = null
        showCaptureSuccess.value = false
    }

    fun stopCamera() {
        isCameraActive.value = false
    }

    fun captureImage(bitmap: Bitmap? = null) {
        capturedImage.value = bitmap ?: createDummyBitmap()
        showCaptureSuccess.value = true
    }

    private fun createDummyBitmap(): Bitmap {
        return Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888)
    }

    fun dismissCaptureSuccess() {
        showCaptureSuccess.value = false
    }

    // Registration with API
    fun registerUser(name: String) {
        if (name.isBlank()) return

        val bitmap = capturedImage.value
        if (bitmap == null) {
            _registrationState.value = RegistrationState.Error("No image captured")
            return
        }

        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading

            val result = repository.registerFace(name, bitmap)

            result.onSuccess { response ->
                val newUser = User(
                    id = response.userId,
                    name = name,
                    faceConfidence = response.faceConfidence,
                    faceImage = bitmap
                )
                _registeredUsers.add(newUser)
                registeredUserName.value = name
                showRegistrationSuccess.value = true
                _registrationState.value = RegistrationState.Success(newUser)

                // Reset
                capturedImage.value = null
                showCaptureSuccess.value = false

                // Reload users list from API
                loadAllUsers()
            }

            result.onFailure { error ->
                _registrationState.value = RegistrationState.Error(
                    error.message ?: "Registration failed"
                )
            }
        }
    }

    fun dismissRegistrationSuccess() {
        showRegistrationSuccess.value = false
        registeredUserName.value = ""
        _registrationState.value = RegistrationState.Idle
    }

    // Live Recognition with API
    fun startLiveRecognition() {
        isLiveRecognitionActive.value = true
        _recognitionState.value = RecognitionState.Recognizing
    }

    fun stopLiveRecognition() {
        isLiveRecognitionActive.value = false
        _recognitionState.value = RecognitionState.Idle
    }

    fun performRecognition(bitmap: Bitmap) {
        if (!isLiveRecognitionActive.value) return

        viewModelScope.launch {
            _recognitionState.value = RecognitionState.Recognizing

            val result = repository.recognizeFace(bitmap)

            result.onSuccess { response ->
                if (response.recognized && response.user != null) {
                    val user = User(
                        id = response.user.id,
                        name = response.user.name,
                        faceConfidence = response.user.confidence
                    )
                    _recognitionState.value = RecognitionState.Success(
                        user,
                        response.user.confidence
                    )
                } else {
                    _recognitionState.value = RecognitionState.Error(
                        response.message ?: "No matching face found"
                    )
                }
            }

            result.onFailure { error ->
                _recognitionState.value = RecognitionState.Error(
                    error.message ?: "Recognition failed"
                )
            }
        }
    }

    fun simulateRecognition() {
        val bitmap = capturedImage.value ?: createDummyBitmap()
        performRecognition(bitmap)
    }

    fun clearRecognitionError() {
        if (_recognitionState.value is RecognitionState.Error) {
            _recognitionState.value = RecognitionState.Recognizing
        }
    }

    fun resetRecognition() {
        _recognitionState.value = RecognitionState.Idle
    }
}
