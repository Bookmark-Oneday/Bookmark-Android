package com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove

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
import com.bookmark.bookmark_oneday.databinding.DialogTimerRemoveHistoryBinding
import com.bookmark.bookmark_oneday.domain.model.ReadingInfo
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove.model.TimerRemoveHistoryDialogState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerRemoveHistoryDialog(
    private val onRemoveItemSuccess : (ReadingInfo) -> Unit = {},
    private val targetId : String ?= null,
    private val bookId : String
) : DialogFragment() {

    private lateinit var binding : DialogTimerRemoveHistoryBinding
    @Inject
    lateinit var assistedViewModelFactory : TimerRemoveHistoryDialogViewModel.AssistedViewModelFactory
    private val viewModel : TimerRemoveHistoryDialogViewModel by viewModels {
        TimerRemoveHistoryDialogViewModel.provideViewModelFactory(
            assistedFactory = assistedViewModelFactory,
            bookId = bookId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogTimerRemoveHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setObserver()
    }

    private fun setButton() {
        binding.btnTimerRemoveHistoryDialogRemove.setOnClickListener {
            viewModel.tryRemoveHistory(targetId)
        }

        binding.btnTimerRemoveHistoryDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        applyState(state)
                    }
                }

                // 삭제 후 변경된 readingInfo 데이터를 관찰합니다.
                launch {
                    viewModel.sideEffectsNewReadingInfo.collectLatest { readingInfo ->
                        onRemoveItemSuccess(readingInfo)
                        dismiss()
                    }
                }

            }
        }
    }

    private fun applyState(state : TimerRemoveHistoryDialogState) {
        binding.btnTimerRemoveHistoryDialogRemove.isEnabled = state.buttonActive
        binding.btnTimerRemoveHistoryDialogCancel.isEnabled = state.buttonActive

        isCancelable = state.availableClose

        if (state.showLoadingProgressBar) {
            binding.pbTimerRemoveHistoryDialogLoading.visibility = View.VISIBLE
            binding.btnTimerRemoveHistoryDialogRemove.text = " "
        } else {
            binding.pbTimerRemoveHistoryDialogLoading.visibility = View.GONE
            binding.btnTimerRemoveHistoryDialogRemove.text = "삭제"
        }
    }

}