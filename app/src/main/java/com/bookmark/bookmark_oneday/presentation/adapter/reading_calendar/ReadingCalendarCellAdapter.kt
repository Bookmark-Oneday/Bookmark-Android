package com.bookmark.bookmark_oneday.presentation.adapter.reading_calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ItemReadingCalendarCellBinding
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.CalendarCell

class ReadingCalendarCellAdapter(
    private val onCellClick : (Int, Int, Int) -> Unit
) : RecyclerView.Adapter<ReadingCalendarCellAdapter.CellViewHolder>(){

    private var cellList = listOf<CalendarCell>()
    private var currentYear : Int = 0
    private var currentMonth : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReadingCalendarCellBinding.inflate(inflater, parent, false)
        return CellViewHolder(binding)
    }

    override fun getItemCount(): Int = cellList.size

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        holder.bind(cellList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeCellData(cellList : List<CalendarCell>, year : Int, month : Int) {
        currentYear = year
        currentMonth = month
        this.cellList = cellList
        notifyDataSetChanged()
    }

    inner class CellViewHolder(
        private val binding : ItemReadingCalendarCellBinding
    ) : ViewHolder(binding.root) {
        private lateinit var cell : CalendarCell

        init {
            binding.root.setOnClickListener {
                onCellClick(cell.year, cell.month, cell.day)
            }
        }

        fun bind(newCell : CalendarCell) {
            cell = newCell
            binding.labelCalendarCellDay.text = cell.day.toString()
            if (currentMonth == cell.month && currentYear == cell.year) {
                applyReadingTime(cell.readingTimeOfTargetTime)
            } else {
                applyPrevOrNextMonthCell()
            }
        }

        private fun applyReadingTime(readingTimeOfGoalTime : Float) {
            binding.labelCalendarCellDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.default_text))
            val color = when {
                (readingTimeOfGoalTime <= 0f) -> {
                    ContextCompat.getColor(binding.root.context, R.color.default_background)
                }
                readingTimeOfGoalTime < 0.4f -> {
                    binding.labelCalendarCellDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    ContextCompat.getColor(binding.root.context, R.color.light_orange)
                }
                readingTimeOfGoalTime < 1f -> {
                    ContextCompat.getColor(binding.root.context, R.color.orange)
                }
                else -> {
                    ContextCompat.getColor(binding.root.context, R.color.deep_orange)
                }
            }
            binding.imgCalendarCell.setColorFilter(color)
        }

        private fun applyPrevOrNextMonthCell() {
            binding.imgCalendarCell.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.default_background))
            binding.labelCalendarCellDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
        }

    }

}