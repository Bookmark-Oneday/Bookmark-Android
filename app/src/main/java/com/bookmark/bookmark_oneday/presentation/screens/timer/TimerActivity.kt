package com.bookmark.bookmark_oneday.presentation.screens.timer

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ActivityTimerBinding
import com.bookmark.bookmark_oneday.presentation.adapter.timer_record.TimerRecordHistoryAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.timer_record.TimerRecordHistoryDecoration
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.model.ReadingHistoryParcelable
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.bottomsheet_more.TimerMoreBottomSheetDialog
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove.TimerRemoveHistoryDialog
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.StopWatchState
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.TimerViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerActivity : ViewBindingActivity<ActivityTimerBinding>(ActivityTimerBinding::inflate) {

    @Inject
    lateinit var timerViewModelFactory: TimerViewModel.AssistedViewModelFactory
    private val viewModel: TimerViewModel by viewModels {
        TimerViewModel.provideViewModelFactory(
            assistedFactory = timerViewModelFactory,
            bookId = intent.getStringExtra("book_id") ?: "1"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.tryGetReadingHistory()

        setBackButtonCallback()
        setButton()
        setRecyclerView()
        setObserver()
    }

    private fun setBackButtonCallback() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.getReadingHistoryIfChanged()?.let { readingHistoryList ->
                    intent.putExtra(
                        "reading_history",
                        ArrayList(readingHistoryList.map { readingHistory ->
                            ReadingHistoryParcelable.fromReadingHistory(readingHistory)
                        })
                    )
                    setResult(RESULT_OK, intent)
                }

                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun setButton() {
        binding.btnTimerBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
        TimerMoreBottomSheetDialog(::callRemoveDialog).show(
            supportFragmentManager,
            "TimerMoreBottomSheet"
        )
    }

    private fun callRemoveDialog(targetId: String? = null) {
        TimerRemoveHistoryDialog(
            onRemoveItemSuccess = viewModel::setReadingInfo,
            targetId = targetId,
            bookId = viewModel.bookId
        ).show(supportFragmentManager, "TimerRemoveHistoryDialog")
    }

    private fun setRecyclerView() {
        binding.listTimerHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.listTimerHistory.adapter =
            TimerRecordHistoryAdapter(onClickRemove = ::callRemoveDialog)
        binding.listTimerHistory.addItemDecoration(TimerRecordHistoryDecoration(this))
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        applyState(state)
                        applyStopWatchState(state.stopWatchState)
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

        binding.labelTimerTotal.visibility =
            if (state.totalButtonToggled) View.VISIBLE else View.INVISIBLE
        val totalTextColor = if (state.totalButtonToggled) R.color.orange else R.color.default_text
        binding.labelTimerTime.setTextColor(ContextCompat.getColor(this, totalTextColor))
    }

    private fun applyStopWatchState(state: StopWatchState) {
        binding.labelTimerTime.text = state.getTimerText()
        binding.partialTimerStopwatch.setProgress(state.getProgress())
    }

}
