package com.bookmark.bookmark_oneday.presentation.adapter.reading_calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bookmark.bookmark_oneday.databinding.ItemReadingCalendarBookBinding
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.CalendarReadingHistory
import com.bumptech.glide.Glide

class ReadingCalendarBookAdapter : Adapter<ReadingCalendarBookAdapter.BookViewHolder>() {

    private var dataList : List<CalendarReadingHistory> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReadingCalendarBookBinding.inflate(inflater, parent, false)
        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeReadingHistoryData(newDataList : List<CalendarReadingHistory>) {
        dataList = newDataList
        notifyDataSetChanged()
    }

    inner class BookViewHolder(
        private val binding : ItemReadingCalendarBookBinding
    ) : ViewHolder(binding.root) {

        fun bind(newBook : CalendarReadingHistory) {
            binding.labelItemWriteTodayOnelineBookTitle.text = newBook.title
            binding.labelItemWriteTodayOnelineBookAuthor.text = newBook.author
            Glide.with(binding.root.context).load(newBook.cover).into(binding.imgItemWriteTodayOnelineBookCover)
        }
    }
}