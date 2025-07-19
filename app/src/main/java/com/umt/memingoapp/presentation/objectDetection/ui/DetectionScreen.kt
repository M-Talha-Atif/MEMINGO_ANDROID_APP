package com.umt.memingoapp.presentation.objectDetection.ui

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import com.umt.memingoapp.presentation.objectDetection.viewmodel.DetectionViewModel
import com.umt.memingoapp.presentation.objectDetection.components.ObjectDetector
import com.umt.memingoapp.R
import com.umt.memingoapp.presentation.navigation.routes.Screen
import com.umt.memingoapp.presentation.objectDetection.components.DetectionBoxesOverlay
import com.umt.memingoapp.presentation.theme.MemingoAppTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DetectionScreen(
    navController: NavController,
    courseId: String,
    courseName: String,
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    // Cleanup when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.cleanup()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            TopBar()
            if (cameraPermissionState.status.isGranted) {
                OpenCamera(
                    navController = navController,
                    courseId = courseId,
                    courseName = courseName
                )
            } else {
                Permission(cameraPermissionState)
            }
        }
    }
}

//----------------------------- TOP BAR --------------------------------------
@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, top = 50.dp, end = 24.dp, bottom = 16.dp)
            .fillMaxWidth(),
        Arrangement.SpaceBetween
    ) {

        Text(
            text = "OBJECT DETECTION",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
    }
}


//----------------------------- CAMERA --------------------------------------

@Composable
fun OpenCamera(
    navController: NavController,
    courseId: String,
    courseName: String,
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Column {
        CameraPreview(
            context = context,
            lifecycleOwner = lifecycleOwner,
            navController = navController,
            courseId = courseId,
            courseName = courseName
        )
    }
}

@Composable
fun CameraPreview(
    context: Context,
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
    courseId: String,
    courseName: String,
    viewModel: DetectionViewModel = hiltViewModel()
)
    {
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
        var preview by remember { mutableStateOf<androidx.camera.core.Preview?>(null) }
        val executor = ContextCompat.getMainExecutor(context)
        val cameraProvider = cameraProviderFuture.get()

        val drawCanvas by remember { viewModel.isLoading }
        val detectionListObject by remember { viewModel.detectionList }

    val paint = Paint()
    val pathColorList = listOf(Color.Red, Color.Green, Color.Cyan, Color.Blue)
    val pathColorListInt = listOf(
        android.graphics.Color.RED,
        android.graphics.Color.GREEN,
        android.graphics.Color.CYAN,
        android.graphics.Color.BLUE
    )


    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ){
        val boxConstraint = this
        val sizeWith = with(LocalDensity.current) { boxConstraint.maxWidth.toPx() }
        val sizeHeight = with(LocalDensity.current) { boxConstraint.maxHeight.toPx() }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setTargetRotation(android.view.Surface.ROTATION_0)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(
                                viewModel.cameraExecutor,
                                ObjectDetector(
                                    yuvToRgbConverter = viewModel.yuvToRgbConverter,
                                    interpreter = viewModel.interpreter,
                                    labels = viewModel.labels,
                                    resultViewSize = Size(sizeWith.toInt(), sizeHeight.toInt())
                                ) { detectedObjectList ->
                                    viewModel.setList(detectedObjectList)
                                }
                            )
                        }

                    imageCapture = ImageCapture.Builder()
                        .setTargetRotation(previewView.display.rotation)
                        .build()

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        imageCapture,
                        preview,
                        imageAnalyzer
                    )
                }, executor)
                preview = androidx.camera.core.Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                previewView
            }
        )
        DetectionBoxesOverlay(
            detectionList = detectionListObject,
            onBoxClick = { detectedObject ->
                navController.navigate(
                    "${Screen.VocabularyScreen.route}/$courseId/$courseName/${detectedObject.label}"
                )
            },
            previewWidth = sizeWith,    // Use boxConstraint.maxWidth.toPx()
            previewHeight = sizeHeight  // Use boxConstraint.maxHeight.toPx()
        )

//        if (drawCanvas){
//            Canvas(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .pointerInput(detectionListObject) {
//                        detectTapGestures { offset ->
//                            detectionListObject.forEach { detectedObject ->
//                                val box = detectedObject.boundingBox
//                                if (offset.x >= box.left && offset.x <= box.right &&
//                                    offset.y >= box.top && offset.y <= box.bottom) {
//                                    Log.d("AR Screen", "Clicked on: ${detectedObject.label}" )
//                                    Toast.makeText(
//                                        context,
//                                        "Clicked on: ${detectedObject.label}",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//
//                                    // Navigate to vocabulary screen with the detected word
//                                    navController.navigate(
//                                        "${Screen.VocabularyScreen.route}/$courseId/$courseName/${detectedObject.label}"
//                                    )
//                                }
//
//                            }
//                        }
//                    },
//
//                onDraw = {
//
//                    detectionListObject.mapIndexed { i, detectionObject ->
//                        Log.d("Object", detectionObject.label + " --- "  +detectionObject.score)
//                        paint.apply {
//                            color = pathColorListInt[i]
//                            style = Paint.Style.FILL
//                            isAntiAlias = true
//                            textSize = 50f
//                        }
//
//                        drawRect(
//                            color = pathColorList[i],
//                            topLeft =  Offset(
//                                x = detectionObject.boundingBox.left,
//                                y = detectionObject.boundingBox.top
//                            ),
//                            size = androidx.compose.ui.geometry.Size(
//                                width = detectionObject.boundingBox.width(),
//                                height = detectionObject.boundingBox.height()
//                            ),
//                            style = Stroke(width = 3.dp.toPx())
//                        )
//
//                        drawIntoCanvas {
//                            it.nativeCanvas.drawText(
//                                detectionObject.label + " " + "%,.2f".format(detectionObject.score * 100) + "%",
//                                detectionObject.boundingBox.left,            // x-coordinates of the origin (top left)
//                                detectionObject.boundingBox.top - 5f, // y-coordinates of the origin (top left)
//                                paint
//                            )
//                        }
//                    }
//                }
//            )
//        }
    }
}


//----------------------------- PERMISSION --------------------------------------
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Permission(
    cameraPermissionState: PermissionState
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (!cameraPermissionState.status.isGranted) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally) {

                val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                    "The camera is important for this app.\n Please grant the permission."
                } else {
                    "Camera not available"
                }
                Text(textToShow, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    shape = CircleShape,
                    onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_camera_24),
                        contentDescription = "Icon camera", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MemingoAppTheme {
        TopBar()
    }
}
