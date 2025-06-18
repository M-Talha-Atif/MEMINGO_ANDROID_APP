package com.mlr_apps.objectdetection.ui.views

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.umt.memingoapp.featureObjectDetection.YuvToRgbConverter
import com.umt.memingoapp.models.ml.DetectionObject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import loadLabels
import loadModel
import org.tensorflow.lite.Interpreter
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DetectionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    var detectionList = mutableStateOf<List<DetectionObject>>(listOf())
    var isLoading = mutableStateOf(value = false)

    // TensorFlow Lite resources
    val interpreter by lazy { Interpreter(loadModel(context)) }
    val labels by lazy { loadLabels(context) }
    val yuvToRgbConverter by lazy { YuvToRgbConverter(context) }
    val cameraExecutor by lazy { Executors.newSingleThreadExecutor() }

    fun setList(detectedObjectList: List<DetectionObject>) {
        if (detectedObjectList.isNotEmpty()) {
            isLoading.value = true
            detectionList.value = detectedObjectList
        } else {
            isLoading.value = false
        }
    }

    // In DetectionViewModel
    fun cleanup() {
        try {
            Log.d("DetectionViewModel", "Starting cleanup")
            cameraExecutor.shutdown()
            // what will this line do? write below
            // This line attempts to shut down the cameraExecutor thread pool gracefully.
            // It will stop accepting new tasks and will wait for the currently running tasks to complete.
            if (!cameraExecutor.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                cameraExecutor.shutdownNow()
            }
            interpreter.close()
            Log.d("DetectionViewModel", "Cleanup completed successfully")
        } catch (e: Exception) {
            Log.e("DetectionViewModel", "Error during cleanup", e)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("DetectionViewModel", "OnCleared successful")
        cleanup()
    }
}