package com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.core.presentation.util.toVisibility
import com.bookmark.bookmark_oneday.databinding.FragmentReadingCalendarBinding
import com.bookmark.bookmark_oneday.presentation.adapter.reading_calendar.ReadingCalendarBookAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.reading_calendar.ReadingCalendarCellAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.reading_calendar.ReadingCalendarItemDecoration
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.component.CalendarMonthPickerDialog
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.ReadingHistoryCalendar
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.ReadingHistoryOfTheDay
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.util.monthString
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle

class ReadingCalendarFragment : ViewBindingFragment<FragmentReadingCalendarBinding>(FragmentReadingCalendarBinding::bind, R.layout.fragment_reading_calendar) {

    private val viewModel : ReadingCalendarViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButton()
        initRecycler()
        initObserver()
    }

    private fun initButton() {
        binding.btnCalendarChangeDate.setOnClickListener {
            showMonthPickerDialog()
        }
    }

    private fun showMonthPickerDialog() {
        val monthPicker = CalendarMonthPickerDialog(viewModel::loadCalendar, viewModel.getYear(), viewModel.getMonth())
        monthPicker.show(childFragmentManager, "CalendarMonthPickerDialog")
    }

    private fun initRecycler() {
        binding.listCalendarCalendar.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.listCalendarCalendar.adapter = ReadingCalendarCellAdapter(viewModel::loadReadingHistoryOfTheDay)
        binding.listCalendarCalendar.addItemDecoration(ReadingCalendarItemDecoration(requireContext()))

        binding.listCalendarBook.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.listCalendarBook.adapter = ReadingCalendarBookAdapter()
    }

    private fun initObserver() {
        viewModel.state.collectLatestInLifecycle(this) {state ->
            binding.btnCalendarChangeDate.isEnabled = !state.showCalendarCellLoading

            binding.viewCalendarLoadingCalendar.setLoadingVisibility(state.showCalendarCellLoading)
            binding.viewCalendarLoadingCalendarBackground.visibility = state.showCalendarCellLoading.toVisibility()

            binding.viewCalendarLoadingBook.setLoadingVisibility(state.showReadingHistoryLoading)
            binding.viewCalendarLoadingBookBackground.visibility = state.showReadingHistoryLoading.toVisibility()

            applyReadingHistoryUi(state.readingHistoryOfTheDay, state.showReadingHistoryLoadingIntro)
            applyCalendarUi(state.readingHistoryCalendar)
        }
    }

    private fun applyReadingHistoryUi(readingHistoryOfTheDay : ReadingHistoryOfTheDay, showIntro : Boolean = false) {
        binding.labelCalendarReadingHistoryOfTheDay.text = if (showIntro) {
            getString(R.string.label_reading_history_of_the_day)
        } else {
            getString(R.string.label_reading_history_of_the_day_with_date, readingHistoryOfTheDay.month, readingHistoryOfTheDay.day)
        }
        (binding.listCalendarBook.adapter as ReadingCalendarBookAdapter).changeReadingHistoryData(readingHistoryOfTheDay.readingHistory)
        binding.viewCalendarLoadingBookIntro.root.visibility = showIntro.toVisibility()
        binding.viewCalendarLoadingBookEmpty.root.visibility = (readingHistoryOfTheDay.readingHistory.isEmpty() && !showIntro).toVisibility()
    }

    private fun applyCalendarUi(readingHistoryCalendar: ReadingHistoryCalendar) {
        binding.labelCalendarYear.text = readingHistoryCalendar.year.toString()
        binding.labelCalendarMonth.text = readingHistoryCalendar.month.monthString(requireContext())
        (binding.listCalendarCalendar.adapter as ReadingCalendarCellAdapter).changeCellData(
            cellList = readingHistoryCalendar.cell,
            year = readingHistoryCalendar.year,
            month = readingHistoryCalendar.month
        )
    }

}