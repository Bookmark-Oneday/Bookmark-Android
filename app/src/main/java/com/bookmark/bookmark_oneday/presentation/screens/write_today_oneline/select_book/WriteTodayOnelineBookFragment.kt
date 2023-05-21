package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.select_book

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentWriteTodayOnelineBookBinding
import com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline.WriteTodayOnelineBookAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline.WriteTodayOnelineBookDecoration
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.book_recognition.BookRecognitionActivity
import com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.component.dialog_permission.MyLibraryPermissionDialog
import com.bookmark.bookmark_oneday.presentation.util.CAMERA_REQUIRED_PERMISSIONS
import com.bookmark.bookmark_oneday.presentation.util.checkCameraPermissionGranted
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle

class WriteTodayOnelineBookFragment : ViewBindingFragment<FragmentWriteTodayOnelineBookBinding>(
    FragmentWriteTodayOnelineBookBinding::bind,
    R.layout.fragment_write_today_oneline_book
) {
    private val viewModel : WriteTodayOnelineBookViewModel by activityViewModels()
    private val cameraScreenLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.tryRefreshPage()
        }
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (requireContext().checkCameraPermissionGranted(CAMERA_REQUIRED_PERMISSIONS)) {
            val intent = Intent(requireActivity(), BookRecognitionActivity::class.java)
            cameraScreenLauncher.launch(intent)
        } else {
            Toast.makeText(requireContext(), "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setRecyclerView()
        setObserver()
    }

    private fun setButton() {
        binding.btnWriteTodayOnelineBookBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnWriteTodayOnelineBookSelect.setOnClickListener {
            val selectedBook = viewModel.state.value.selectedBook
            selectedBook?.let { book ->
                val direction = WriteTodayOnelineBookFragmentDirections.actionWriteTodayOnelineBookFragmentToWriteTodayOnelineWriteFragment(book)
                findNavController().navigate(direction)
            }
        }
    }

    private fun setRecyclerView() {
        binding.listWriteTodayOnelineBook.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.listWriteTodayOnelineBook.adapter = WriteTodayOnelineBookAdapter(
            onClickAdder = {
                clickBookAdder()
            },
            onClickBook = { bookItem ->
                viewModel.selectBook(bookItem)
            }
        )
        binding.listWriteTodayOnelineBook.addItemDecoration(WriteTodayOnelineBookDecoration(requireContext()))

        binding.listWriteTodayOnelineBook.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager ?: return
                layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                if (firstVisibleItemPosition + visibleItemCount + 3 >= totalItemCount) {
                    viewModel.tryNextPage()
                }

            }
        })
        binding.listWriteTodayOnelineBook.itemAnimator = null
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    private fun clickBookAdder() {
        if (requireContext().checkCameraPermissionGranted(CAMERA_REQUIRED_PERMISSIONS)) {
            val intent = Intent(requireActivity(), BookRecognitionActivity::class.java)
            cameraScreenLauncher.launch(intent)
        } else {
            MyLibraryPermissionDialog(onClick = {
                requestPermissions.launch(CAMERA_REQUIRED_PERMISSIONS)
            }).show(childFragmentManager, "MyLibrary Permission")
        }
    }

    private fun setObserver() {
        viewModel.state.collectLatestInLifecycle(viewLifecycleOwner) { state ->
            val listItem = state.dataList + state.footerList
            (binding.listWriteTodayOnelineBook.adapter as WriteTodayOnelineBookAdapter).run {
                updateList(listItem)
                changeSelectedItem(state.selectedBook)
            }
            binding.btnWriteTodayOnelineBookSelect.isEnabled = state.selectButtonActive
        }
    }
}