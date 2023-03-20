package com.bookmark.bookmark_oneday.presentation.screens.book_recognition

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bookmark.bookmark_oneday.databinding.ActivityBookRecognitionBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.screens.book_confirmation.BookConfirmationActivity
import com.bookmark.bookmark_oneday.presentation.screens.book_recognition.component.BookRecognitionFailDialog
import com.bookmark.bookmark_oneday.presentation.screens.book_recognition.model.ScreenSizeAdapter
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
class BookRecognitionActivity : ViewBindingActivity<ActivityBookRecognitionBinding>(ActivityBookRecognitionBinding::inflate) {

    private lateinit var cameraExecutor : ExecutorService
    private lateinit var screenSizeAdapter: ScreenSizeAdapter
    private lateinit var viewModel: BookRecognitionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[BookRecognitionViewModel::class.java]

        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        setObserver()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // todo 이전 화면에서 호출하는 부분이므로 전 화면 구현시 제거
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()){
                startCamera()
            } else {
                Toast.makeText(this, "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setObserver() {
        lifecycleScope.apply{
            launch{
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.state.collectLatest { state ->
                        binding.btnBookrecognitionBack.isEnabled = state.buttonActive
                        binding.pbBookrecognitionLoading.visibility = if (state.showLoadingDialog) View.VISIBLE else View.GONE
                        if (state.showErrorDialog) showFailDialog()

                    }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.sideEffectsSearchBookSuccess.collectLatest {
                        val intent = Intent(this@BookRecognitionActivity, BookConfirmationActivity::class.java)
                        intent.putExtra("book", it)
                        this@BookRecognitionActivity.startActivity(intent)
                        this@BookRecognitionActivity.finish()
                    }
                }
            }

        }
    }

    private fun showFailDialog() {
        BookRecognitionFailDialog(viewModel::closePopup).show(supportFragmentManager, "BookRecognitionFail")
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider : ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewBookrecognition.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                .build()
            val barcodeScanner = BarcodeScanning.getClient(options)
            val analysisPreview = ImageAnalysis.Builder()
                .build()

            analysisPreview.setAnalyzer(
                Executors.newSingleThreadExecutor()
            ) { imageProxy ->
                processImageProxy(barcodeScanner, imageProxy)
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    analysisPreview
                )

                val resolution = analysisPreview.resolutionInfo
                // 카메라 해상도의 가로 세로가 반대로 적용되어있는 이유는, 핸드폰을 세로로 사용할 때,
                // 카메라 해상도 값의 가로 세로가 90도 기울어져 있기 때문입니다.
                screenSizeAdapter = ScreenSizeAdapter(
                    originalHeightPx = binding.clBookrecognitionRoot.height,
                    originalWidthPx = binding.clBookrecognitionRoot.width,
                    boundingBoxHeightPx = binding.viewBookrecognitionArea.height,
                    boundingBoxWidthPx = binding.viewBookrecognitionArea.width,
                    cameraResolutionHeight = resolution?.resolution?.width ?: 640,
                    cameraResolutionWidth = resolution?.resolution?.height ?: 480
                )
            } catch (e : Exception) {
                Log.e("TAG", "Use case binding failed", e)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun  processImageProxy(
        barCodeScanner : BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            barCodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    val barcode = barcodeList.getOrNull(0)

                    tryBarcodeRecognize(barcode)
                }
                .addOnFailureListener {

                }
                .addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }

    }

    private fun tryBarcodeRecognize(barcode : Barcode?) {
        val canRecognizeBarcode = (::screenSizeAdapter.isInitialized && barcode != null && barcode.boundingBox != null)
        if (!canRecognizeBarcode) return

        val barcodeInBoundingBox = screenSizeAdapter.checkInBoundingBox(barcode!!.boundingBox!!)
        if (barcodeInBoundingBox) {
            barcode.rawValue?.let { value ->
                viewModel.trySearchAndGetBookInfo(value)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).toTypedArray()
    }

}
