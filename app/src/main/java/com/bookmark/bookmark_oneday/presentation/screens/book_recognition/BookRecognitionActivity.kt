package com.bookmark.bookmark_oneday.presentation.screens.book_recognition

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bookmark.bookmark_oneday.databinding.ActivityBookRecognitionBinding
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.screens.book_confirmation.BookConfirmationActivity
import com.bookmark.bookmark_oneday.presentation.screens.book_recognition.component.BookRecognitionFailDialog
import com.bookmark.bookmark_oneday.presentation.screens.book_recognition.model.ScreenSizeAdapter
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
@AndroidEntryPoint
class BookRecognitionActivity : ViewBindingActivity<ActivityBookRecognitionBinding>(ActivityBookRecognitionBinding::inflate) {

    private lateinit var cameraExecutor : ExecutorService
    private val viewModel: BookRecognitionViewModel by viewModels()
    private val screenSizeAdapter = ScreenSizeAdapter()

    private val bookRegisterLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        } else {
            viewModel.setScannable()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (allPermissionGranted()) {
            startCamera()
        } else {
            Toast.makeText(this, "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }

        setButton()
        setObserver()
        setOnGlobalLayoutListener()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setButton() {
        binding.btnBookrecognitionBack.setOnClickListener { finish() }
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
                        callBookConfirmScreen(it)
                    }
                }
            }

        }
    }

    private fun showFailDialog() {
        BookRecognitionFailDialog(viewModel::setScannable).show(supportFragmentManager, "BookRecognitionFail")
    }

    private fun callBookConfirmScreen(recognizedBook : RecognizedBook) {
        val intent = Intent(this, BookConfirmationActivity::class.java)
        intent.putExtra("book", recognizedBook)
        bookRegisterLauncher.launch(intent)
    }

    private fun setOnGlobalLayoutListener() {
        binding.clBookrecognitionRoot.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    screenSizeAdapter.setViewSizeInfo(
                        totalHeightPx = binding.root.height,
                        totalWidthPx = binding.root.width,
                        targetHeightPx = binding.viewBookrecognitionArea.height,
                        targetWidthPx = binding.viewBookrecognitionArea.width
                    )

                    binding.clBookrecognitionRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )
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
                screenSizeAdapter.setCameraResolutionInfo(
                    heightPx = resolution?.resolution?.width ?: 640,
                    widthPx = resolution?.resolution?.height ?: 480
                )
            } catch (e : Exception) {
                Log.e("TAG", "Use case binding failed", e)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun processImageProxy(
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
        val canRecognizeBarcode = (barcode != null && barcode.boundingBox != null && screenSizeAdapter.checkAllInitialize())
        if (!canRecognizeBarcode) return

        val barcodeInBoundingBox = screenSizeAdapter.checkInBoundingBox(barcode!!.boundingBox!!)
        if (barcodeInBoundingBox) {
            barcode.rawValue?.let { value ->
                viewModel.trySearchAndGetBookInfo(value)
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).toTypedArray()
    }

}
