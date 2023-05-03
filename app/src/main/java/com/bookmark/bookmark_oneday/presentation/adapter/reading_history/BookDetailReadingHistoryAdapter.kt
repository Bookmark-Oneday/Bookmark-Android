package com.bookmark.bookmark_oneday.presentation.adapter.reading_history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.databinding.ItemReadinghistoryBinding
import com.bookmark.bookmark_oneday.domain.model.ReadingHistory
import com.bookmark.bookmark_oneday.presentation.util.dpToPx
import kotlin.math.max

class BookDetailReadingHistoryAdapter : RecyclerView.Adapter<ReadingHistoryViewHolder>() {
    private val readingHistoryList = arrayListOf<ReadingHistory>()
    private var maxReadingValue = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemReadinghistoryBinding.inflate(layoutInflater, parent, false)
        return ReadingHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = readingHistoryList.size

    override fun onBindViewHolder(holder: ReadingHistoryViewHolder, position: Int) {
        holder.bind(readingHistoryList[position], maxReadingValue)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setReadingHistoryListData(newReadingHistoryList : List<ReadingHistory>) {
        readingHistoryList.clear()
        readingHistoryList.addAll(newReadingHistoryList)

        setMaxReadingValue(newReadingHistoryList)
        notifyDataSetChanged()
    }

    private fun setMaxReadingValue(readingHistoryList : List<ReadingHistory>, minOfMaxTime : Int = 300) {
        maxReadingValue = readingHistoryList.maxOfOrNull { it.time }?.let { maxValue ->
            max(maxValue, minOfMaxTime)
        } ?: minOfMaxTime
    }

}

class ReadingHistoryViewHolder(private val binding : ItemReadinghistoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(readingHistory: ReadingHistory, maxValue : Int) {
        binding.labelReadinghistoryDate.text = readingHistory.dateString.getOnlyMonthAndDay()
        binding.labelReadinghistoryTime.text = (readingHistory.time / 60).toString()

        val maxHeight = dpToPx(binding.root.context, 190)

        binding.viewReadinghistoryBar.layoutParams.height = max((maxHeight * (readingHistory.time / maxValue.toFloat())).toInt(), 1)
    }
}