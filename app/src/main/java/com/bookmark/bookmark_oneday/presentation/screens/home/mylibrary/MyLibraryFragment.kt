package com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentMylibraryBinding
import com.bookmark.bookmark_oneday.presentation.adapter.mylibrary.MyLibraryBookAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.mylibrary.MyLibraryBookDecoration
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.book_recognition.BookRecognitionActivity
import com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.component.bottomsheet_sort.MyLibrarySortBottomSheet
import com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.component.dialog_permission.MyLibraryPermissionDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

class MyLibraryFragment : ViewBindingFragment<FragmentMylibraryBinding>(
    FragmentMylibraryBinding::bind,
    R.layout.fragment_mylibrary
) {

    private lateinit var viewModel: MyLibraryViewModel
    private lateinit var sortBottomSheet: MyLibrarySortBottomSheet

    private val cameraScreenLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.tryGetInitPagingData()
        }
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (allPermissionGranted()) {
            val intent = Intent(requireActivity(), BookRecognitionActivity::class.java)
            cameraScreenLauncher.launch(intent)
        } else {
            Toast.makeText(requireContext(), "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MyLibraryViewModel::class.java]

        setButton()
        setRecyclerView()
        setAppbarEvent()
        setObserver()
        setBottomSheet()
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setButton() {
        binding.llbtnMylibrarySort.setOnClickListener {
            if (sortBottomSheet.isAdded) return@setOnClickListener
            sortBottomSheet.show(childFragmentManager, "MyLibrarySortBottomSheet")
        }
    }

    private fun setRecyclerView() {
        binding.listBooklist.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.listBooklist.adapter = MyLibraryBookAdapter(
            onClickAdder = ::clickBookAdder,
            onClickBook = ::clickBookDetail
        )
        binding.listBooklist.addItemDecoration(MyLibraryBookDecoration(requireContext()))

        binding.listBooklist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager ?: return
                layoutManager as GridLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                if (firstVisibleItemPosition + visibleItemCount + 3 >= totalItemCount) {
                    viewModel.tryGetNextPagingData()
                }
            }
        })
    }
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    private fun clickBookAdder() {
        if (allPermissionGranted()) {
            val intent = Intent(requireActivity(), BookRecognitionActivity::class.java)
            cameraScreenLauncher.launch(intent)
        } else {
            MyLibraryPermissionDialog(onClick = {
                requestPermissions.launch(REQUIRED_PERMISSIONS)
            }).show(childFragmentManager, "MyLibrary Permission")
        }
    }

    private fun clickBookDetail(bookId : Int) {
        val action = MyLibraryFragmentDirections.actionMyLibraryFragmentToBookDetailFragment(bookId)
        binding.root.findNavController().navigate(action)
    }

    private fun setAppbarEvent() {
        binding.ablMylibrary.addOnOffsetChangedListener { _, verticalOffset ->
            if (abs(verticalOffset) + binding.toolbarMylibrary.height > binding.partialMylibraryProfile.viewMylibraryBackground.top) {
                binding.viewMylibraryDivider.visibility = View.VISIBLE
                binding.labelMylibraryTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.default_text))
                binding.toolbarMylibrary.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.default_background
                    )
                )
            } else {
                binding.viewMylibraryDivider.visibility = View.INVISIBLE
                binding.labelMylibraryTitle.setTextColor(Color.WHITE)
                binding.toolbarMylibrary.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.transparent
                    )
                )
            }
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        applyState(state)
                    }
                }
            }
        }

        // todo 직렬화된 책 객체를 받아 반영하도록 변경 필요
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("book")?.observe(viewLifecycleOwner){
            viewModel.applyItemChange(it)
        }
    }

    private fun applyState(state: MyLibraryState) {
        (binding.listBooklist.adapter as MyLibraryBookAdapter).updateList(state.bookList + state.footerList)

        binding.labelMylibrarySort.text = state.currentSortData.presentText
        binding.labelMylibraryBookcount.text = state.totalItemCountString
        binding.llbtnMylibrarySort.isEnabled = state.sortButtonActive

        sortBottomSheet.setCurrentSort(state.currentSortData)
    }

    private fun setBottomSheet() {
        sortBottomSheet = MyLibrarySortBottomSheet(
            MyLibraryViewModel.sortList,
            viewModel::tryGetInitPagingData
        )
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).toTypedArray()
    }

}
