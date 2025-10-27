////
////package com.example.face_detect_app.viewmodel
////
////import android.app.Application
////import android.graphics.Bitmap
////import androidx.compose.runtime.mutableStateListOf
////import androidx.compose.runtime.mutableStateOf
////import androidx.lifecycle.AndroidViewModel
////import androidx.lifecycle.viewModelScope
////import com.example.face_detect_app.models.User
////import com.example.face_detect_app.repository.FaceRepository
////import kotlinx.coroutines.flow.MutableStateFlow
////import kotlinx.coroutines.flow.StateFlow
////import kotlinx.coroutines.launch
////
////sealed class RecognitionState {
////    object Idle : RecognitionState()
////    object Recognizing : RecognitionState()
////    data class Success(val user: User, val confidence: Double) : RecognitionState()
////    data class Error(val message: String) : RecognitionState()
////}
////
////sealed class RegistrationState {
////    object Idle : RegistrationState()
////    object Loading : RegistrationState()
////    data class Success(val user: User) : RegistrationState()
////    data class Error(val message: String) : RegistrationState()
////}
////
////class FaceDetectionViewModel(application: Application) : AndroidViewModel(application) {
////
////    private val repository = FaceRepository(application)
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
////    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
////    val registrationState: StateFlow<RegistrationState> = _registrationState
////
////    var showRegistrationSuccess = mutableStateOf(false)
////        private set
////
////    var registeredUserName = mutableStateOf("")
////        private set
////
////    init {
////        // Load users from API on startup
////        loadAllUsers()
////    }
////
////    // Load all registered users from API
////    fun loadAllUsers() {
////        viewModelScope.launch {
////            val result = repository.getAllUsers()
////
////            result.onSuccess { response ->
////                _registeredUsers.clear()
////                _registeredUsers.addAll(
////                    response.users.map { userInfo ->
////                        User(
////                            id = userInfo.id,
////                            name = userInfo.name,
////                            faceConfidence = 0.0
////                        )
////                    }
////                )
////            }
////
////            result.onFailure { error ->
////                // Silently fail - users list will be empty
////                android.util.Log.e("FaceDetectionViewModel", "Failed to load users: ${error.message}")
////            }
////        }
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
////        capturedImage.value = bitmap ?: createDummyBitmap()
////        showCaptureSuccess.value = true
////    }
////
////    private fun createDummyBitmap(): Bitmap {
////        return Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888)
////    }
////
////    fun dismissCaptureSuccess() {
////        showCaptureSuccess.value = false
////    }
////
////    // Registration with API
////    fun registerUser(name: String) {
////        if (name.isBlank()) return
////
////        val bitmap = capturedImage.value
////        if (bitmap == null) {
////            _registrationState.value = RegistrationState.Error("No image captured")
////            return
////        }
////
////        viewModelScope.launch {
////            _registrationState.value = RegistrationState.Loading
////
////            val result = repository.registerFace(name, bitmap)
////
////            result.onSuccess { response ->
////                val newUser = User(
////                    id = response.userId,
////                    name = name,
////                    faceConfidence = response.faceConfidence,
////                    faceImage = bitmap
////                )
////                _registeredUsers.add(newUser)
////                registeredUserName.value = name
////                showRegistrationSuccess.value = true
////                _registrationState.value = RegistrationState.Success(newUser)
////
////                // Reset
////                capturedImage.value = null
////                showCaptureSuccess.value = false
////
////                // Reload users list from API
////                loadAllUsers()
////            }
////
////            result.onFailure { error ->
////                _registrationState.value = RegistrationState.Error(
////                    error.message ?: "Registration failed"
////                )
////            }
////        }
////    }
////
////    fun dismissRegistrationSuccess() {
////        showRegistrationSuccess.value = false
////        registeredUserName.value = ""
////        _registrationState.value = RegistrationState.Idle
////    }
////
////    // Live Recognition with API
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
////    fun performRecognition(bitmap: Bitmap) {
////        if (!isLiveRecognitionActive.value) return
////
////        viewModelScope.launch {
////            _recognitionState.value = RecognitionState.Recognizing
////
////            val result = repository.recognizeFace(bitmap)
////
////            result.onSuccess { response ->
////                if (response.recognized && response.user != null) {
////                    val user = User(
////                        id = response.user.id,
////                        name = response.user.name,
////                        faceConfidence = response.user.confidence
////                    )
////                    _recognitionState.value = RecognitionState.Success(
////                        user,
////                        response.user.confidence
////                    )
////                } else {
////                    _recognitionState.value = RecognitionState.Error(
////                        response.message ?: "No matching face found"
////                    )
////                }
////            }
////
////            result.onFailure { error ->
////                _recognitionState.value = RecognitionState.Error(
////                    error.message ?: "Recognition failed"
////                )
////            }
////        }
////    }
////
////    fun simulateRecognition() {
////        val bitmap = capturedImage.value ?: createDummyBitmap()
////        performRecognition(bitmap)
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
//import com.example.face_detect_app.ui.camera.CameraManager
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
//    private val context = application.applicationContext
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
//    init {
//        loadAllUsers()
//    }
//
//    fun loadAllUsers() {
//        viewModelScope.launch {
//            val result = repository.getAllUsers()
//
//            result.onSuccess { response ->
//                _registeredUsers.clear()
//                _registeredUsers.addAll(
//                    response.users.map { userInfo ->
//                        User(
//                            id = userInfo.id,
//                            name = userInfo.name,
//                            faceConfidence = 0.0
//                        )
//                    }
//                )
//            }
//
//            result.onFailure { error ->
//                android.util.Log.e("FaceDetectionViewModel", "Failed to load users: ${error.message}")
//            }
//        }
//    }
//
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
//    // ✅ UPDATED: Capture real image from camera
//    fun captureImage() {
//        CameraManager.captureImage(context) { bitmap ->
//            capturedImage.value = bitmap
//            showCaptureSuccess.value = true
//            android.util.Log.d("FaceDetectionViewModel", "Image captured: ${bitmap.width}x${bitmap.height}")
//        }
//    }
//
//    fun dismissCaptureSuccess() {
//        showCaptureSuccess.value = false
//    }
//
//    fun registerUser(name: String) {
//        if (name.isBlank()) return
//
//        val bitmap = capturedImage.value
//        if (bitmap == null) {
//            _registrationState.value = RegistrationState.Error("No image captured")
//            return
//        }
//
//        android.util.Log.d("FaceDetectionViewModel", "Registering user: $name with bitmap: ${bitmap.width}x${bitmap.height}")
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
//                capturedImage.value = null
//                showCaptureSuccess.value = false
//                loadAllUsers()
//            }
//
//            result.onFailure { error ->
//                android.util.Log.e("FaceDetectionViewModel", "Registration failed: ${error.message}")
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
//    fun simulateRecognition() {
//        val bitmap = capturedImage.value
//        if (bitmap != null) {
//            performRecognition(bitmap)
//        } else {
//            _recognitionState.value = RecognitionState.Error("No image to recognize")
//        }
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
import com.example.face_detect_app.ui.camera.CameraManager
import kotlinx.coroutines.delay
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
    private val context = application.applicationContext

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
        loadAllUsers()
    }

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
                android.util.Log.e("FaceDetectionViewModel", "Failed to load users: ${error.message}")
            }
        }
    }

    fun startCamera() {
        isCameraActive.value = true
        capturedImage.value = null
        showCaptureSuccess.value = false
    }

    fun stopCamera() {
        isCameraActive.value = false
    }

    // Capture real image from camera (for registration)
    fun captureImage() {
        CameraManager.captureImage(context) { bitmap ->
            capturedImage.value = bitmap
            showCaptureSuccess.value = true
            android.util.Log.d("FaceDetectionViewModel", "Image captured: ${bitmap.width}x${bitmap.height}")
        }
    }

    fun dismissCaptureSuccess() {
        showCaptureSuccess.value = false
    }

    fun registerUser(name: String) {
        if (name.isBlank()) return

        val bitmap = capturedImage.value
        if (bitmap == null) {
            _registrationState.value = RegistrationState.Error("No image captured")
            return
        }

        android.util.Log.d("FaceDetectionViewModel", "Registering user: $name with bitmap: ${bitmap.width}x${bitmap.height}")

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

                capturedImage.value = null
                showCaptureSuccess.value = false
                loadAllUsers()
            }

            result.onFailure { error ->
                android.util.Log.e("FaceDetectionViewModel", "Registration failed: ${error.message}")
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

    // ✅ START LIVE RECOGNITION - Captures frames continuously
    fun startLiveRecognition() {
        isLiveRecognitionActive.value = true
        _recognitionState.value = RecognitionState.Recognizing

        // Start continuous recognition loop
        viewModelScope.launch {
            while (isLiveRecognitionActive.value) {
                captureAndRecognize()
                delay(1000) // Recognition every 1 second
            }
        }
    }

    fun stopLiveRecognition() {
        isLiveRecognitionActive.value = false
        _recognitionState.value = RecognitionState.Idle
    }

    // ✅ CAPTURE AND RECOGNIZE - Captures frame and sends to API
    private fun captureAndRecognize() {
        CameraManager.captureImage(context) { bitmap ->
            android.util.Log.d("FaceDetectionViewModel", "Frame captured for recognition: ${bitmap.width}x${bitmap.height}")
            performRecognitionInternal(bitmap)
        }
    }

    private fun performRecognitionInternal(bitmap: Bitmap) {
        if (!isLiveRecognitionActive.value) return

        viewModelScope.launch {
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

                    // Auto-reset to Recognizing after 3 seconds to continue scanning
                    delay(3000)
                    if (isLiveRecognitionActive.value) {
                        _recognitionState.value = RecognitionState.Recognizing
                    }
                } else {
                    // Keep recognizing state for continuous scanning
                    if (isLiveRecognitionActive.value) {
                        _recognitionState.value = RecognitionState.Recognizing
                    }
                }
            }

            result.onFailure { error ->
                android.util.Log.e("FaceDetectionViewModel", "Recognition error: ${error.message}")
                _recognitionState.value = RecognitionState.Error(
                    error.message ?: "Recognition failed"
                )

                // Auto-retry after error
                delay(2000)
                if (isLiveRecognitionActive.value) {
                    _recognitionState.value = RecognitionState.Recognizing
                }
            }
        }
    }

    // Legacy method for manual recognition (not used in live mode)
    fun performRecognition(bitmap: Bitmap) {
        if (!isLiveRecognitionActive.value) return
        performRecognitionInternal(bitmap)
    }

    fun simulateRecognition() {
        // Not needed for live recognition
        // This was for testing only
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
