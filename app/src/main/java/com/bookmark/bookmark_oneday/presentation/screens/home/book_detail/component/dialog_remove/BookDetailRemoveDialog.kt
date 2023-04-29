package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.DialogBookdetailRemoveBinding
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookDetailRemoveDialog(
    private val onRemoveSuccess : () -> Unit = {},
    private val bookId : String
) : DialogFragment() {
    private lateinit var binding : DialogBookdetailRemoveBinding

    @Inject
    lateinit var viewModelFactory : BookDetailRemoveDialogViewModel.ViewModelAssistedFactory

    private val viewModel : BookDetailRemoveDialogViewModel by viewModels {
        BookDetailRemoveDialogViewModel.provideViewModelFactory(
            viewModelFactory,
            bookId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogBookdetailRemoveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setObserver()
    }

    private fun setButton() {
        binding.btnBookdetailRemovedialogRemove.setOnClickListener{
            viewModel.tryDeleteBook()
        }

        binding.btnBookdetailRemovedialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setObserver() {
        viewModel.state.collectLatestInLifecycle(viewLifecycleOwner) { state ->
            binding.btnBookdetailRemovedialogRemove.isEnabled = state.buttonActive
            binding.btnBookdetailRemovedialogCancel.isEnabled = state.buttonActive

            isCancelable = state.availableClose

            if (state.showLoadingProgressBar) {
                binding.pbBookdetailRemovedialogLoading.visibility = View.VISIBLE
                binding.btnBookdetailRemovedialogRemove.text = " "
            } else {
                binding.pbBookdetailRemovedialogLoading.visibility = View.GONE
                binding.btnBookdetailRemovedialogRemove.text = getString(R.string.label_bookdetail_remove)
            }
        }

        viewModel.sideEffectsCloseDialog.collectLatestInLifecycle(viewLifecycleOwner) { isCloseDialog ->
            if (isCloseDialog) {
                onRemoveSuccess()
                dismiss()
            }
        }

    }
}