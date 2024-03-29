package com.bookmark.bookmark_oneday.presentation.screens.timer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
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
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_cancel_timer.CancelTimerDialog
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_permission.NotificationPermissionDialog
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove.TimerRemoveHistoryDialog
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.StopWatchState
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.TimerViewState
import com.bookmark.bookmark_oneday.presentation.util.POST_NOTIFICATIONS_33
import com.bookmark.bookmark_oneday.presentation.util.checkPostNotificationAvailable
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

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.setTimerNotificationUseSwitchState(true)
        } else {
            Toast.makeText(this, getString(R.string.label_notification_permission_not_allowed), Toast.LENGTH_SHORT).show()
            setUseNotificationSwitchState(false)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.tryGetReadingHistory()

        setBackButtonCallback()
        setButton()
        setRecyclerView()
        setObserver()
        setSwitch()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTimerService()
    }

    private fun setBackButtonCallback() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.state.value.playButtonToggled) {
                    callCancelDialog()
                } else {
                    setBackStackExtra()
                    finish()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun setBackStackExtra() {
        viewModel.getReadingHistoryIfChanged()?.let { readingHistoryList ->
            intent.putExtra(
                "reading_history",
                ArrayList(readingHistoryList.map { readingHistory ->
                    ReadingHistoryParcelable.fromReadingHistory(readingHistory)
                })
            )
            setResult(RESULT_OK, intent)
        }
    }

    private fun callCancelDialog() {
        val dialog = CancelTimerDialog(
            onClickBack = {
                cancelTimerService()
                setBackStackExtra()
                finish()
            }
        )
        dialog.show(supportFragmentManager, "timerCancelDialog")
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

    private fun callTimerService() {
        Intent(this@TimerActivity, TimerService::class.java)
            .putExtra(TimerService.TIMER_ACTION, TimerService.START)
            .run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(this)
                } else {
                    startService(this)
                }
            }
    }

    private fun cancelTimerService() {
        Intent(this@TimerActivity, TimerService::class.java)
            .putExtra(TimerService.TIMER_ACTION, TimerService.PAUSE)
            .run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(this)
                } else {
                    startService(this)
                }
            }
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.timerServiceActionEvent.collectLatest { actionString ->
                        when (actionString) {
                            TimerService.START -> {
                                callTimerService()
                            }
                            TimerService.PAUSE -> {
                                cancelTimerService()
                            }
                        }

                    }
                }
            }
        }
    }

    private fun setSwitch() {
        binding.switchUseNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !checkPostNotificationAvailable(this)) {
                NotificationPermissionDialog(onClick = {requestPermission.launch(POST_NOTIFICATIONS_33)}).show(supportFragmentManager, "Notification Permission Dialog")
            } else {
                viewModel.setTimerNotificationUseSwitchState(isChecked)
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

        setUseNotificationSwitchState(state.timerNotificationSwitch)
    }

    private fun setUseNotificationSwitchState(on : Boolean) {
        if (binding.switchUseNotification.isChecked != on) {
            binding.switchUseNotification.isChecked = on
        }
    }

    private fun applyStopWatchState(state: StopWatchState) {
        binding.labelTimerTime.text = state.getTimerText()
        binding.partialTimerStopwatch.setProgress(state.getProgress())
    }

}
