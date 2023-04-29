package com.bookmark.bookmark_oneday.presentation.screens.book_recognition.model

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService

@ExperimentalGetImage
class CameraFeature(
    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    private val cameraExecutor : ExecutorService
) : ListenableFuture<ProcessCameraProvider> by cameraProviderFuture {

    fun getPreview(
        surfaceProvider: SurfaceProvider
    ): Preview {
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(surfaceProvider)
            }

        return preview
    }

    fun getBarcodeAnalysisPreview(
        options: BarcodeScannerOptions,
        onRecognizeCallback: (String) -> Unit,
        checkCanRecognizeBarcode: (Barcode?) -> Boolean
    ): ImageAnalysis {
        val barcodeScanner = BarcodeScanning.getClient(options)
        val analysisPreview = ImageAnalysis.Builder()
            .build()

        analysisPreview.setAnalyzer(
            cameraExecutor
        ) { imageProxy ->
            this.processImageProxy(
                barCodeScanner = barcodeScanner,
                imageProxy = imageProxy,
                onRecognizeCallback = onRecognizeCallback,
                checkCanRecognizeBarcode = checkCanRecognizeBarcode
            )
        }

        return analysisPreview
    }

    private fun processImageProxy(
        barCodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,
        onRecognizeCallback: (String) -> Unit,
        checkCanRecognizeBarcode: (Barcode?) -> Boolean
    ) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            barCodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    val barcode = barcodeList.getOrNull(0)
                    if (checkCanRecognizeBarcode(barcode))
                        tryBarcodeRecognize(barcode, onRecognizeCallback)
                }
                .addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }

    private fun tryBarcodeRecognize(
        barcode: Barcode?,
        onRecognizeCallback: (String) -> Unit
    ) {
        barcode?.rawValue?.let { value ->
            onRecognizeCallback(value)
        }
    }
}