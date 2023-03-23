package com.bookmark.bookmark_oneday.presentation.adapter.timer_record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.databinding.ItemTimerHistoryBinding
import com.bookmark.bookmark_oneday.domain.model.ReadingHistory

class TimerRecordHistoryAdapter(
    private val onClickRemove: (Int) -> Unit
) : RecyclerView.Adapter<TimerRecordHistoryViewHolder>() {

    private val asyncDiffer = AsyncListDiffer(this, ReadingHistoryDiffUtil())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimerRecordHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTimerHistoryBinding.inflate(layoutInflater, parent, false)
        return TimerRecordHistoryViewHolder(binding, onClickRemove)
    }

    override fun getItemCount(): Int = asyncDiffer.currentList.size

    override fun onBindViewHolder(holder: TimerRecordHistoryViewHolder, position: Int) {
        holder.bind(asyncDiffer.currentList[position])
    }

    fun setButtonActive(active : Boolean) {
        TimerRecordHistoryViewHolder.buttonActive = active
    }

    fun updateList(items : List<ReadingHistory>) {
        asyncDiffer.submitList(items)
    }

}

class TimerRecordHistoryViewHolder(
    private val binding : ItemTimerHistoryBinding,
    private val onClickRemove : (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var history : ReadingHistory

    init {
        binding.btnItemTimerHistoryRemove.setOnClickListener {
            if (!::history.isInitialized) return@setOnClickListener
            if (!buttonActive) return@setOnClickListener

            onClickRemove(history.id)
        }
    }

    fun bind(readingHistory : ReadingHistory) {
        history = readingHistory
        binding.labelTimerHistoryDate.text = history.dateString
        binding.labelTimerHistoryTime.text = history.time.toString()
    }

    companion object {
        var buttonActive : Boolean = true
    }
}

class ReadingHistoryDiffUtil() : DiffUtil.ItemCallback<ReadingHistory>() {
    override fun areItemsTheSame(oldItem: ReadingHistory, newItem: ReadingHistory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ReadingHistory, newItem: ReadingHistory): Boolean {
        return oldItem == newItem
    }

}