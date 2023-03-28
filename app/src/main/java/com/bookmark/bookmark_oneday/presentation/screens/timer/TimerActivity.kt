package com.bookmark.bookmark_oneday.presentation.screens.timer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ActivityTimerBinding
import com.bookmark.bookmark_oneday.domain.model.ReadingHistory
import com.bookmark.bookmark_oneday.presentation.adapter.timer_record.TimerRecordHistoryAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.timer_record.TimerRecordHistoryDecoration
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.bottomsheet_more.TimerMoreBottomSheetDialog
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove.TimerRemoveHistoryDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TimerActivity : ViewBindingActivity<ActivityTimerBinding>(ActivityTimerBinding::inflate) {

    private lateinit var viewModel: TimerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[TimerViewModel::class.java]

        // 실제로는 이전 화면에서 받아옵니다.
        viewModel.setReadingHistory(List(30){ ReadingHistory(it, "23.01.01", it * 30) })

        setButton()
        setRecyclerView()
        setObserver()
    }

    private fun setButton() {
        binding.btnTimerBack.setOnClickListener {
            finish()
        }

        binding.btnTimerMore.setOnClickListener {
            callBottomSheet()
        }

        binding.btnTimerTotal.apply {
            setToggleOffClick(viewModel::showTotalTime)
            setToggleOnClick(viewModel::hideTotalTime)
        }


        binding.btnTimerPlay.apply {
            setToggleOffClick(viewModel::playTimer)
            setToggleOnClick(viewModel::pauseTimer)
        }
    }

    private fun callBottomSheet() {
        TimerMoreBottomSheetDialog(::callRemoveDialog).show(supportFragmentManager, "TimerMoreBottomSheet")
    }

    private fun callRemoveDialog(targetId : Int ?= null) {
        TimerRemoveHistoryDialog(
            onRemoveItemSuccess = viewModel::applyRemovedItemToList,
            targetId = targetId
        ).show(supportFragmentManager, "TimerRemoveHistoryDialog")
    }

    private fun setRecyclerView() {
        binding.listTimerHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.listTimerHistory.adapter = TimerRecordHistoryAdapter(onClickRemove = ::callRemoveDialog)
        binding.listTimerHistory.addItemDecoration(TimerRecordHistoryDecoration(this))
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        applyState(state)
                    }
                }

                launch {
                    viewModel.sideEffects.collectLatest { sideEffect ->
                        handleSideEffects(sideEffect)
                    }
                }

                launch {
                    viewModel.stopWatchState.collectLatest { state ->
                        applyStopWatchState(state)
                    }
                }
            }
        }
    }

    private fun applyState(state: TimerViewState) {
        (binding.listTimerHistory.adapter as TimerRecordHistoryAdapter).setButtonActive(state.buttonActive)
        (binding.listTimerHistory.adapter as TimerRecordHistoryAdapter).updateList(state.readingHistoryList)

        binding.btnTimerTotal.setToggleState(state.totalButtonToggled)
        binding.btnTimerPlay.setToggleState(state.playButtonToggled)

        binding.labelTimerTotal.visibility = if (state.totalButtonToggled) View.VISIBLE else View.INVISIBLE
        val totalTextColor = if (state.totalButtonToggled) R.color.orange else R.color.default_text
        binding.labelTimerTime.setTextColor(ContextCompat.getColor(this, totalTextColor))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleSideEffects(sideEffect: TimerViewSideEffect) {
        when (sideEffect) {
            is TimerViewSideEffect.RemoveReadingHistory -> {
                (binding.listTimerHistory.adapter as TimerRecordHistoryAdapter).notifyItemRemoved(sideEffect.position)
            }
            TimerViewSideEffect.ClearReadingHistory -> {
                (binding.listTimerHistory.adapter as TimerRecordHistoryAdapter).notifyDataSetChanged()
            }
        }
    }

    private fun applyStopWatchState(state : StopWatchState) {
        binding.labelTimerTime.text = state.timeString
        binding.partialTimerStopwatch.setProgress(state.progress)
    }

}
