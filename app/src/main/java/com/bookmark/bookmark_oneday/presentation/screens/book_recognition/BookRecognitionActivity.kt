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
import com.bookmark.bookmark_oneday.databinding.ActivityBookRecognitionBinding
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.screens.book_confirmation.BookConfirmationActivity
import com.bookmark.bookmark_oneday.presentation.screens.book_recognition.component.BookRecognitionFailDialog
import com.bookmark.bookmark_oneday.presentation.screens.book_recognition.model.CameraFeature
import com.bookmark.bookmark_oneday.presentation.screens.book_recognition.model.ScreenSizeAdapter
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
@AndroidEntryPoint
class BookRecognitionActivity : ViewBindingActivity<ActivityBookRecognitionBinding>(ActivityBookRecognitionBinding::inflate) {

    private val cameraExecutor : ExecutorService by lazy { Executors.newSingleThreadExecutor() }
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
        viewModel.state.collectLatestInLifecycle(this) { state ->
            binding.btnBookrecognitionBack.isEnabled = state.buttonActive
            binding.pbBookrecognitionLoading.visibility = if (state.showLoadingDialog) View.VISIBLE else View.GONE
            if (state.showErrorDialog) showFailDialog()
        }

        viewModel.sideEffectsSearchBookSuccess.collectLatestInLifecycle(this) { recognizedBook ->
            callBookConfirmScreen(recognizedBook)
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
        val cameraFeature = CameraFeature(cameraProviderFuture, cameraExecutor)

        cameraFeature.addListener({
            val cameraProvider : ProcessCameraProvider = cameraFeature.get()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val preview = cameraFeature.getPreview(binding.previewBookrecognition.surfaceProvider)

            val analysisPreviewOptions = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                .build()
            val analysisPreview = cameraFeature.getBarcodeAnalysisPreview(
                options = analysisPreviewOptions,
                onRecognizeCallback = viewModel::trySearchAndGetBookInfo,
                checkCanRecognizeBarcode = ::checkCanRecognizeBarcode
            )

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    analysisPreview
                )

                val resolution = analysisPreview.resolutionInfo
                // 카메라 해상도의 가로 세로가 반대로 적용되어있는 이유는 핸드폰을 세로로 사용할 때
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

    // 바코드를 인식할 수 있는 상황인지 여부를 리턴합니다.
    private fun checkCanRecognizeBarcode(barcode: Barcode?): Boolean {
        return (barcode != null
                && barcode.boundingBox != null
                && screenSizeAdapter.checkAllInitialize()
                && screenSizeAdapter.checkInBoundingBox(barcode.boundingBox!!))
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).toTypedArray()
    }

}
