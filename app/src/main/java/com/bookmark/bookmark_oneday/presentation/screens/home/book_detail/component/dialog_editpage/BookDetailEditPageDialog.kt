package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_editpage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bookmark.bookmark_oneday.databinding.DialogBookdetailBookmarkBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookDetailEditPageDialog(
    private val bookId : String,
    private val onSuccess : (Int, Int) -> Unit,
    private val currentPage : Int ?= null,
    private val totalPage : Int ?= null
) : DialogFragment() {
    private lateinit var binding : DialogBookdetailBookmarkBinding
    @Inject
    lateinit var viewModelFactory : BookDetailEditPageDialogViewModel.AssistedViewModelFactory

    private val viewModel by viewModels<BookDetailEditPageDialogViewModel>{
        BookDetailEditPageDialogViewModel.provideFactory(viewModelFactory, bookId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogBookdetailBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initPageInfo(currentPage ?: 0, totalPage ?: 1)

        setButton()
        setObserver()
    }

    private fun setButton() {
        binding.btnBookdetailBookmarkdialogInput.setOnClickListener {
            viewModel.tryEditPageInfo(
                currentPageString = binding.inputBookdetailCurrentpage.text.toString(),
                totalPageString = binding.inputBookdetailTotalPage.text.toString()
            )
        }
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.apply{
            launch{
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.state.collectLatest { state ->
                        binding.inputBookdetailCurrentpage.isEnabled = state.editTextActive
                        binding.inputBookdetailTotalPage.isEnabled = state.editTextActive

                        binding.btnBookdetailBookmarkdialogInput.isEnabled = state.inputButtonActive

                        isCancelable = state.availableClose
                    }
                }
            }

            launch{
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.sideEffectsCloseDialog.collectLatest { isCloseDialog ->
                        if (isCloseDialog) {
                            onSuccess(viewModel.currentPage.value, viewModel.totalPage.value)
                            dismiss()
                        }

                    }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentPage.collectLatest {
                        binding.inputBookdetailCurrentpage.setText(it.toString())
                    }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.totalPage.collectLatest {
                        binding.inputBookdetailTotalPage.setText(it.toString())
                    }
                }
            }

        }
    }
}