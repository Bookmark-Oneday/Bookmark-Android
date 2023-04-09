package com.bookmark.bookmark_oneday.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.databinding.ItemReadinghistoryBinding
import com.bookmark.bookmark_oneday.domain.model.ReadingHistory
import com.bookmark.bookmark_oneday.presentation.util.dpToPx

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
        readingHistoryList.addAll(newReadingHistoryList)
        maxReadingValue = newReadingHistoryList.maxOfOrNull { it.time } ?: 100
        notifyDataSetChanged()
    }

}

class ReadingHistoryViewHolder(private val binding : ItemReadinghistoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(readingHistory: ReadingHistory, maxValue : Int) {
        binding.labelReadinghistoryDate.text = readingHistory.dateString.split(".").slice(1..2).joinToString("/")
        binding.labelReadinghistoryTime.text = (readingHistory.time / 60).toString()

        val maxHeight = dpToPx(binding.root.context, 190)

        binding.viewReadinghistoryBar.layoutParams.height = (maxHeight * (readingHistory.time / maxValue.toFloat())).toInt()
    }
}