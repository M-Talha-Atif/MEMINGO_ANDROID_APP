package com.umt.memingoapp.presentation.objectDetection.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.RectF
import android.media.Image
import android.util.Size
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import com.umt.memingoapp.domain.model.response.DetectionObject


typealias ObjectDetectorCallback = (image: List<DetectionObject>) -> Unit

class ObjectDetector(
    private val yuvToRgbConverter: YuvToRgbConverter,
    private val interpreter: Interpreter,
    private val labels: List<String>,
    private val resultViewSize: Size,
    private val listener: ObjectDetectorCallback
) : ImageAnalysis.Analyzer {

    companion object {
        // Model input and output size
        private const val IMG_SIZE_X = 300 //300
        private const val IMG_SIZE_Y = 300 //300
        private const val MAX_DETECTION_NUM = 10//10

        // Since the TFLite model used this time has been quantized, the related normalization is not 127.5f but the following
        private const val NORMALIZE_MEAN = 0f//0f
        private const val NORMALIZE_STD = 1f//1f

        // Score threshold for detection results
        private const val SCORE_THRESHOLD = 0.6f
    }

    private var imageRotationDegrees: Int = 0
    private val tfImageProcessor by lazy {
        ImageProcessor.Builder()
            .add(ResizeOp(IMG_SIZE_X, IMG_SIZE_Y, ResizeOp.ResizeMethod.BILINEAR)) // Resize the image to fit the model input
            .add(Rot90Op(-imageRotationDegrees / 90)) // The image proxy is rotated 90 degrees
            .add(NormalizeOp(NORMALIZE_MEAN, NORMALIZE_STD)) // Related to normalization
            .build()
    }

    private val tfImageBuffer = TensorImage(DataType.UINT8)
    //private val tfImageBuffer = TensorImage(DataType.FLOAT32)

    // Bounding box of detection result [1:10:4]
    // The bounding box has the form [top, left, bottom, right]
    private val outputBoundingBoxes: Array<Array<FloatArray>> = arrayOf(
        Array(MAX_DETECTION_NUM) {
            FloatArray(4) //4
        }
    )

    // Class label index of detection result [1:10]
    private val outputLabels: Array<FloatArray> = arrayOf(
        FloatArray(MAX_DETECTION_NUM)
    )

    // Each score in the detection result [1:10]
    private val outputScores: Array<FloatArray> = arrayOf(
        FloatArray(MAX_DETECTION_NUM)
    )

    // Number of detected objects (10 (constant) because this time it was set at the time of the TFLite conversion)
    private val outputDetectionNum: FloatArray = FloatArray(1)

    // Gather into a map to receive detection results
    private val outputMap = mapOf(
        0 to outputBoundingBoxes,
        1 to outputLabels,
        2 to outputScores,
        3 to outputDetectionNum

    //Change for new models version TensorFlow
    //0 to outputScores,
    //1 to outputBoundingBoxes,
    //2 to outputDetectionNum,
    //3 to outputLabels
    )

    // Perform inference on the preview image from CameraX by passing it to the object detection model.
    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        if (image.image == null) return
        imageRotationDegrees = image.imageInfo.rotationDegrees
        val detectedObjectList = detect(image.image!!)
        listener(detectedObjectList)
        image.close()
    }

    // Convert the image to YUV -> RGB bitmap -> TensorImage -> TensorBuffer, infer, and send the result as a list

    private fun detect(targetImage: Image): List<DetectionObject> {
        val targetBitmap =
            Bitmap.createBitmap(targetImage.width, targetImage.height, Bitmap.Config.ARGB_8888)
        yuvToRgbConverter.yuvToRgb(targetImage, targetBitmap) // conversion a rgb
        tfImageBuffer.load(targetBitmap)
        val tensorImage = tfImageProcessor.process(tfImageBuffer)

        // TensorFlow Lite: Performing inferences on the model
        interpreter.runForMultipleInputsOutputs(arrayOf(tensorImage.buffer), outputMap)

        // Format the inference result and return it as a list
        val detectedObjectList = arrayListOf<DetectionObject>()
        loop@ for (i in 0 until outputDetectionNum[0].toInt()) {
            val score = outputScores[0][i]
            val label = labels[outputLabels[0][i].toInt()]
            val boundingBox = RectF(
                outputBoundingBoxes[0][i][1] * resultViewSize.width,
                outputBoundingBoxes[0][i][0] * resultViewSize.height,
                outputBoundingBoxes[0][i][3] * resultViewSize.width,
                outputBoundingBoxes[0][i][2] * resultViewSize.height
            )

            // Add only those that are greater than the threshold
            if (score >= SCORE_THRESHOLD) {
                detectedObjectList.add(
                    DetectionObject(
                        score = score,
                        label = label,
                        boundingBox = boundingBox
                    )
                )
            } else {
                break@loop
            }
        }
        return detectedObjectList.take(4)
    }

    fun cleanUp() {
        interpreter.close()
    }
}