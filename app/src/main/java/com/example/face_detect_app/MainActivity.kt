//
//
//package com.example.face_detect_app
//
//import android.Manifest
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.viewModels
//import androidx.compose.animation.*
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.example.face_detect_app.ui.camera.CameraPreview
//import com.example.face_detect_app.ui.theme.Face_Detect_AppTheme
//import com.example.face_detect_app.viewmodel.FaceDetectionViewModel
//import com.example.face_detect_app.viewmodel.RecognitionState
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.isGranted
//import com.google.accompanist.permissions.rememberPermissionState
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class MainActivity : ComponentActivity() {
//    private val viewModel: FaceDetectionViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            Face_Detect_AppTheme {
//                FaceDetectionApp(viewModel)
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun FaceDetectionApp(viewModel: FaceDetectionViewModel) {
//    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
//
//    LaunchedEffect(Unit) {
//        if (!cameraPermissionState.status.isGranted) {
//            cameraPermissionState.launchPermissionRequest()
//        }
//    }
//
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        if (cameraPermissionState.status.isGranted) {
//            FaceRecognitionSystem(viewModel)
//        } else {
//            PermissionDeniedScreen {
//                cameraPermissionState.launchPermissionRequest()
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FaceRecognitionSystem(viewModel: FaceDetectionViewModel) {
//    var selectedTab by remember { mutableStateOf(0) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "Face Recognition",
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                actions = {
//                    IconButton(onClick = { /* Show users list */ }) {
//                        Badge(
//                            containerColor = MaterialTheme.colorScheme.primary,
//                            contentColor = MaterialTheme.colorScheme.onPrimary
//                        ) {
//                            Text("${viewModel.registeredUsers.size}")
//                        }
//                    }
//                    Spacer(modifier = Modifier.width(8.dp))
//                    IconButton(onClick = { /* Show users list */ }) {
//                        Icon(Icons.Default.People, contentDescription = "Users")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
//                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            // Tab Row
//            TabRow(
//                selectedTabIndex = selectedTab,
//                containerColor = MaterialTheme.colorScheme.surface,
//                contentColor = MaterialTheme.colorScheme.primary
//            ) {
//                Tab(
//                    selected = selectedTab == 0,
//                    onClick = { selectedTab = 0 },
//                    text = {
//                        Text(
//                            "Register",
//                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
//                        )
//                    },
//                    icon = {
//                        Icon(Icons.Default.PersonAdd, contentDescription = null)
//                    }
//                )
//                Tab(
//                    selected = selectedTab == 1,
//                    onClick = { selectedTab = 1 },
//                    text = {
//                        Text(
//                            "Recognize",
//                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
//                        )
//                    },
//                    icon = {
//                        Icon(Icons.Default.Face, contentDescription = null)
//                    }
//                )
//            }
//
//            // Tab Content
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.background)
//            ) {
//                when (selectedTab) {
//                    0 -> RegisterScreen(viewModel)
//                    1 -> RecognizeScreen(viewModel)
//                }
//            }
//        }
//    }
//
//    // Registration Success Dialog
//    if (viewModel.showRegistrationSuccess.value) {
//        RegistrationSuccessDialog(
//            name = viewModel.registeredUserName.value,
//            onDismiss = { viewModel.dismissRegistrationSuccess() }
//        )
//    }
//}
//
//
//
//
//@Composable
//fun RegisterScreen(viewModel: FaceDetectionViewModel) {
//    var nameInput by remember { mutableStateOf("") }
//    val coroutineScope = rememberCoroutineScope()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Camera Preview Card
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            shape = RoundedCornerShape(16.dp),
//            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black)
//            ) {
//                if (viewModel.isCameraActive.value) {
//                    CameraPreview(modifier = Modifier.fillMaxSize())
//
//                    // Face detection overlay
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(32.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        FaceDetectionOverlay()
//                    }
//
//                    // Capture Success Indicator
//                    if (viewModel.showCaptureSuccess.value) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Color.Black.copy(alpha = 0.7f)),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                Icon(
//                                    Icons.Default.CheckCircle,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(64.dp),
//                                    tint = Color(0xFF4CAF50)
//                                )
//                                Spacer(modifier = Modifier.height(16.dp))
//                                Text(
//                                    text = "Image Captured!",
//                                    style = MaterialTheme.typography.titleLarge,
//                                    color = Color.White,
//                                    fontWeight = FontWeight.Bold
//                                )
//                                Spacer(modifier = Modifier.height(8.dp))
//                                Text(
//                                    text = "Ready to register",
//                                    style = MaterialTheme.typography.bodyMedium,
//                                    color = Color.White.copy(alpha = 0.8f)
//                                )
//                            }
//                        }
//                    }
//                } else {
//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Icon(
//                            Icons.Default.Videocam,
//                            contentDescription = null,
//                            modifier = Modifier.size(64.dp),
//                            tint = Color.White.copy(alpha = 0.5f)
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text(
//                            text = "Camera is off",
//                            color = Color.White.copy(alpha = 0.7f),
//                            style = MaterialTheme.typography.bodyLarge
//                        )
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Camera Control Buttons
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Button(
//                onClick = {
//                    if (viewModel.isCameraActive.value) {
//                        viewModel.stopCamera()
//                    } else {
//                        viewModel.startCamera()
//                    }
//                },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(56.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (viewModel.isCameraActive.value)
//                        MaterialTheme.colorScheme.error
//                    else
//                        MaterialTheme.colorScheme.primary
//                ),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Icon(
//                    if (viewModel.isCameraActive.value) Icons.Default.Stop else Icons.Default.Videocam,
//                    contentDescription = null
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    if (viewModel.isCameraActive.value) "Stop" else "Start",
//                    style = MaterialTheme.typography.titleMedium
//                )
//            }
//
//            Button(
//                onClick = {
//                    viewModel.captureImage()
//                    // Auto-dismiss capture success after 2 seconds
//                    coroutineScope.launch {
//                        delay(2000)
//                        viewModel.dismissCaptureSuccess()
//                    }
//                },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(56.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.tertiary
//                ),
//                shape = RoundedCornerShape(12.dp),
//                enabled = viewModel.isCameraActive.value
//            ) {
//                Icon(Icons.Default.CameraAlt, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Capture", style = MaterialTheme.typography.titleMedium)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Registration Form
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.surfaceVariant
//            )
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(20.dp)
//            ) {
//                Text(
//                    text = "Register New User",
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                OutlinedTextField(
//                    value = nameInput,
//                    onValueChange = { nameInput = it },
//                    label = { Text("Full Name") },
//                    leadingIcon = {
//                        Icon(Icons.Default.Person, contentDescription = null)
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true,
//                    shape = RoundedCornerShape(12.dp)
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        if (nameInput.isNotBlank()) {
//                            viewModel.registerUser(nameInput)
//                            nameInput = ""
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    enabled = nameInput.isNotBlank() && viewModel.capturedImage.value != null,
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Icon(Icons.Default.PersonAdd, contentDescription = null)
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Register Face", style = MaterialTheme.typography.titleMedium)
//                }
//
//                AnimatedVisibility(
//                    visible = viewModel.capturedImage.value == null,
//                    enter = fadeIn() + expandVertically(),
//                    exit = fadeOut() + shrinkVertically()
//                ) {
//                    Column {
//                        Spacer(modifier = Modifier.height(12.dp))
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.Center,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                Icons.Default.Info,
//                                contentDescription = null,
//                                modifier = Modifier.size(16.dp),
//                                tint = MaterialTheme.colorScheme.error
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "Capture an image first",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.error
//                            )
//                        }
//                    }
//                }
//
//                AnimatedVisibility(
//                    visible = viewModel.capturedImage.value != null,
//                    enter = fadeIn() + expandVertically(),
//                    exit = fadeOut() + shrinkVertically()
//                ) {
//                    Column {
//                        Spacer(modifier = Modifier.height(12.dp))
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.Center,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                Icons.Default.CheckCircle,
//                                contentDescription = null,
//                                modifier = Modifier.size(16.dp),
//                                tint = Color(0xFF4CAF50)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "Image captured successfully",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = Color(0xFF4CAF50)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//
//
//
//@Composable
//fun RecognizeScreen(viewModel: FaceDetectionViewModel) {
//    val recognitionState by viewModel.recognitionState.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Camera Preview Card
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            shape = RoundedCornerShape(16.dp),
//            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black)
//            ) {
//                if (viewModel.isLiveRecognitionActive.value) {
//                    CameraPreview(modifier = Modifier.fillMaxSize())
//
//                    // Scanning animation overlay
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        LiveRecognitionOverlay(recognitionState)
//                    }
//                } else {
//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Icon(
//                            Icons.Default.FaceRetouchingOff,
//                            contentDescription = null,
//                            modifier = Modifier.size(64.dp),
//                            tint = Color.White.copy(alpha = 0.5f)
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text(
//                            text = "Recognition is off",
//                            color = Color.White.copy(alpha = 0.7f),
//                            style = MaterialTheme.typography.bodyLarge
//                        )
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Control Buttons
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Button(
//                onClick = {
//                    viewModel.startLiveRecognition()
//                    viewModel.startCamera()
//                },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(56.dp),
//                enabled = !viewModel.isLiveRecognitionActive.value,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                ),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Icon(Icons.Default.PlayArrow, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Start", style = MaterialTheme.typography.titleMedium)
//            }
//
//            Button(
//                onClick = {
//                    viewModel.stopLiveRecognition()
//                    viewModel.stopCamera()
//                },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(56.dp),
//                enabled = viewModel.isLiveRecognitionActive.value,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.error
//                ),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Icon(Icons.Default.Stop, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Stop", style = MaterialTheme.typography.titleMedium)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Recognition Status Card
//        when (recognitionState) {
//            is RecognitionState.Recognizing -> {
//                RecognitionStatusCard(
//                    icon = {
//                        CircularProgressIndicator(
//                            modifier = Modifier.size(32.dp),
//                            strokeWidth = 3.dp
//                        )
//                    },
//                    title = "Scanning...",
//                    message = "Looking for registered faces",
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                )
//
//                LaunchedEffect(Unit) {
//                    delay(2000)
//                    viewModel.simulateRecognition()
//                }
//            }
//            is RecognitionState.Success -> {
//                val user = (recognitionState as RecognitionState.Success).user
//                RecognitionStatusCard(
//                    icon = {
//                        Icon(
//                            Icons.Default.CheckCircle,
//                            contentDescription = null,
//                            modifier = Modifier.size(40.dp),
//                            tint = Color(0xFF4CAF50)
//                        )
//                    },
//                    title = "User Recognized!",
//                    message = user.name,
//                    containerColor = Color(0xFFE8F5E9),
//                    contentColor = Color(0xFF2E7D32)
//                )
//            }
//            is RecognitionState.Error -> {
//                val error = (recognitionState as RecognitionState.Error).message
//                Log.d("RecognitionState", error)
//                RecognitionStatusCard(
//                    icon = {
//                        Icon(
//                            Icons.Default.ErrorOutline,
//                            contentDescription = null,
//                            modifier = Modifier.size(40.dp),
//                            tint = MaterialTheme.colorScheme.error
//                        )
//                    },
//                    title = "Recognition Error",
//                    message = error,
//                    containerColor = MaterialTheme.colorScheme.errorContainer,
//                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
//                    dismissAction = { viewModel.clearRecognitionError() }
//                )
//            }
//            else -> {}
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Info Card
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.surfaceVariant
//            )
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    Icons.Default.People,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.primary
//                )
//                Spacer(modifier = Modifier.width(12.dp))
//                Text(
//                    text = "${viewModel.registeredUsers.size} registered users",
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun FaceDetectionOverlay() {
//    val infiniteTransition = rememberInfiniteTransition(label = "scan")
//    val scanPosition by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(2000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "scan"
//    )
//
//    Box(
//        modifier = Modifier
//            .size(250.dp)
//            .background(Color.Transparent)
//    ) {
//        // Scanning line
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(3.dp)
//                .align(Alignment.TopStart)
//                .offset(y = (250.dp * scanPosition))
//                .background(
//                    Brush.horizontalGradient(
//                        colors = listOf(
//                            Color.Transparent,
//                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
//                            Color.Transparent
//                        )
//                    )
//                )
//        )
//    }
//}
//
//@Composable
//fun LiveRecognitionOverlay(state: RecognitionState) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(
//                        Color.Black.copy(alpha = 0.6f),
//                        Color.Transparent,
//                        Color.Black.copy(alpha = 0.6f)
//                    )
//                )
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        when (state) {
//            is RecognitionState.Recognizing -> {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(48.dp),
//                    color = Color.White,
//                    strokeWidth = 4.dp
//                )
//            }
//            else -> {}
//        }
//    }
//}
//
//@Composable
//fun RecognitionStatusCard(
//    icon: @Composable () -> Unit,
//    title: String,
//    message: String,
//    containerColor: Color,
//    contentColor: Color,
//    dismissAction: (() -> Unit)? = null
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = containerColor
//        ),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(20.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(contentAlignment = Alignment.Center) {
//                icon()
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = contentColor
//                )
//                Text(
//                    text = message,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = contentColor.copy(alpha = 0.8f)
//                )
//            }
//
//            dismissAction?.let {
//                IconButton(onClick = it) {
//                    Icon(
//                        Icons.Default.Close,
//                        contentDescription = "Dismiss",
//                        tint = contentColor
//                    )
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RegistrationSuccessDialog(
//    name: String,
//    onDismiss: () -> Unit
//) {
//    BasicAlertDialog(
//        onDismissRequest = onDismiss
//    ) {
//        Card(
//            shape = RoundedCornerShape(28.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.surface
//            ),
//            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(32.dp)
//                    .fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(80.dp)
//                        .background(
//                            MaterialTheme.colorScheme.primaryContainer,
//                            CircleShape
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        Icons.Default.CheckCircle,
//                        contentDescription = null,
//                        modifier = Modifier.size(48.dp),
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Text(
//                    text = "Success!",
//                    style = MaterialTheme.typography.headlineSmall,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = name,
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.SemiBold,
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = "has been registered successfully",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Button(
//                    onClick = onDismiss,
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text("Continue", style = MaterialTheme.typography.titleMedium)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PermissionDeniedScreen(onRequestPermission: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(32.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(
//                Icons.Default.CameraAlt,
//                contentDescription = null,
//                modifier = Modifier.size(120.dp),
//                tint = MaterialTheme.colorScheme.primary
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Text(
//                text = "Camera Access Required",
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "This app needs camera permission to perform face recognition",
//                style = MaterialTheme.typography.bodyLarge,
//                textAlign = TextAlign.Center,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Button(
//                onClick = onRequestPermission,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Icon(Icons.Default.Check, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Grant Permission", style = MaterialTheme.typography.titleMedium)
//            }
//        }
//    }
//}









package com.example.face_detect_app

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.face_detect_app.ui.camera.CameraPreview
import com.example.face_detect_app.ui.theme.Face_Detect_AppTheme
import com.example.face_detect_app.viewmodel.FaceDetectionViewModel
import com.example.face_detect_app.viewmodel.RecognitionState
import com.example.face_detect_app.viewmodel.RegistrationState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: FaceDetectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Face_Detect_AppTheme {
                FaceDetectionApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceDetectionApp(viewModel: FaceDetectionViewModel) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (cameraPermissionState.status.isGranted) {
            FaceRecognitionSystem(viewModel)
        } else {
            PermissionDeniedScreen {
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceRecognitionSystem(viewModel: FaceDetectionViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    var showUsersDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Face Recognition",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { showUsersDialog = true }) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text("${viewModel.registeredUsers.size}")
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { showUsersDialog = true }) {
                        Icon(Icons.Default.People, contentDescription = "Users")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Register",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = {
                        Icon(Icons.Default.PersonAdd, contentDescription = null)
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Recognize",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = {
                        Icon(Icons.Default.Face, contentDescription = null)
                    }
                )
            }

            // Tab Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (selectedTab) {
                    0 -> RegisterScreen(viewModel)
                    1 -> RecognizeScreen(viewModel)
                }
            }
        }
    }

    // Users List Dialog
    if (showUsersDialog) {
        UsersListDialog(
            users = viewModel.registeredUsers,
            onDismiss = { showUsersDialog = false },
            onRefresh = { viewModel.loadAllUsers() }
        )
    }

    // Registration Success Dialog
    if (viewModel.showRegistrationSuccess.value) {
        RegistrationSuccessDialog(
            name = viewModel.registeredUserName.value,
            onDismiss = { viewModel.dismissRegistrationSuccess() }
        )
    }
}

@Composable
fun RegisterScreen(viewModel: FaceDetectionViewModel) {
    var nameInput by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val registrationState by viewModel.registrationState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Camera Preview Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                if (viewModel.isCameraActive.value) {
                    CameraPreview(modifier = Modifier.fillMaxSize())

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        FaceDetectionOverlay()
                    }

                    if (viewModel.showCaptureSuccess.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = Color(0xFF4CAF50)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Image Captured!",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Ready to register",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Videocam,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Camera is off",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (viewModel.isCameraActive.value) {
                        viewModel.stopCamera()
                    } else {
                        viewModel.startCamera()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (viewModel.isCameraActive.value)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    if (viewModel.isCameraActive.value) Icons.Default.Stop else Icons.Default.Videocam,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (viewModel.isCameraActive.value) "Stop" else "Start",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = {
                    viewModel.captureImage()
                    coroutineScope.launch {
                        delay(2000)
                        viewModel.dismissCaptureSuccess()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = viewModel.isCameraActive.value
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Capture", style = MaterialTheme.typography.titleMedium)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Register New User",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Full Name") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    enabled = registrationState !is RegistrationState.Loading
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nameInput.isNotBlank()) {
                            viewModel.registerUser(nameInput)
                            nameInput = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = nameInput.isNotBlank() &&
                            viewModel.capturedImage.value != null &&
                            registrationState !is RegistrationState.Loading,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (registrationState is RegistrationState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Registering...", style = MaterialTheme.typography.titleMedium)
                    } else {
                        Icon(Icons.Default.PersonAdd, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Register Face", style = MaterialTheme.typography.titleMedium)
                    }
                }

                AnimatedVisibility(
                    visible = viewModel.capturedImage.value == null && registrationState !is RegistrationState.Error,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Capture an image first",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = viewModel.capturedImage.value != null &&
                            registrationState !is RegistrationState.Loading &&
                            registrationState !is RegistrationState.Error,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF4CAF50)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Image captured successfully",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = registrationState is RegistrationState.Error,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = (registrationState as? RegistrationState.Error)?.message ?: "Error",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecognizeScreen(viewModel: FaceDetectionViewModel) {
    val recognitionState by viewModel.recognitionState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                if (viewModel.isLiveRecognitionActive.value) {
                    CameraPreview(modifier = Modifier.fillMaxSize())

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LiveRecognitionOverlay(recognitionState)
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.FaceRetouchingOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Recognition is off",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    viewModel.startLiveRecognition()
                    viewModel.startCamera()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                enabled = !viewModel.isLiveRecognitionActive.value,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start", style = MaterialTheme.typography.titleMedium)
            }

            Button(
                onClick = {
                    viewModel.stopLiveRecognition()
                    viewModel.stopCamera()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                enabled = viewModel.isLiveRecognitionActive.value,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Stop, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Stop", style = MaterialTheme.typography.titleMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (recognitionState) {
            is RecognitionState.Recognizing -> {
                RecognitionStatusCard(
                    icon = {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 3.dp
                        )
                    },
                    title = "Scanning...",
                    message = "Looking for registered faces",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )

                LaunchedEffect(Unit) {
                    delay(2000)
                    viewModel.simulateRecognition()
                }
            }
            is RecognitionState.Success -> {
                val success = recognitionState as RecognitionState.Success
                val confidencePercent = String.format("%.1f", success.confidence * 100)
                RecognitionStatusCard(
                    icon = {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = Color(0xFF4CAF50)
                        )
                    },
                    title = "User Recognized!",
                    message = "${success.user.name} ($confidencePercent% confidence)",
                    containerColor = Color(0xFFE8F5E9),
                    contentColor = Color(0xFF2E7D32)
                )
            }
            is RecognitionState.Error -> {
                val error = (recognitionState as RecognitionState.Error).message
                Log.d("RecognitionState", error)
                RecognitionStatusCard(
                    icon = {
                        Icon(
                            Icons.Default.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    title = "Recognition Error",
                    message = error,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    dismissAction = { viewModel.clearRecognitionError() }
                )
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.People,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${viewModel.registeredUsers.size} registered users",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListDialog(
    users: List<com.example.face_detect_app.models.User>,
    onDismiss: () -> Unit,
    onRefresh: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Registered Users",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (users.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.PersonOff,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No users registered yet",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(users.size) { index ->
                            val user = users[index]
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            user.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            "ID: ${user.id}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun FaceDetectionOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "scan")
    val scanPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan"
    )

    Box(
        modifier = Modifier
            .size(250.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .align(Alignment.TopStart)
                .offset(y = (250.dp * scanPosition))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun LiveRecognitionOverlay(state: RecognitionState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.6f),
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.6f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is RecognitionState.Recognizing -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Color.White,
                    strokeWidth = 4.dp
                )
            }
            else -> {}
        }
    }
}

@Composable
fun RecognitionStatusCard(
    icon: @Composable () -> Unit,
    title: String,
    message: String,
    containerColor: Color,
    contentColor: Color,
    dismissAction: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.Center) {
                icon()
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }

            dismissAction?.let {
                IconButton(onClick = it) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = contentColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationSuccessDialog(
    name: String,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Success!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "has been registered successfully",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Continue", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun PermissionDeniedScreen(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CameraAlt,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Camera Access Required",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "This app needs camera permission to perform face recognition",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRequestPermission,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Grant Permission", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
