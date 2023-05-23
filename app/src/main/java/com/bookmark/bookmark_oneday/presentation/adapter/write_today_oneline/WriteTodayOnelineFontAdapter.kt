package com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ItemWriteTodayOnelineFontBinding
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.Font

class WriteTodayOnelineFontAdapter : RecyclerView.Adapter<WriteTodayOnelineFontAdapter.FontViewHolder>() {

    private val fontList = Font.defaultList
    private var selectedFont = fontList[0]
    private var lastSelectedIndex = 0
    private var fontChangeCallback : (Font) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWriteTodayOnelineFontBinding.inflate(inflater, parent, false)
        return FontViewHolder(binding)
    }

    override fun getItemCount(): Int = fontList.size

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        holder.bind(fontList[position])
    }

    fun setFontChangeCallback(callback : (Font) -> Unit) {
        fontChangeCallback = callback
    }

    fun changeSelectedFont(newFont : Font) {
        val newSelectedFontIndex = fontList.indexOfFirst { font ->
            font == newFont
        }

        if (newSelectedFontIndex >= 0) {
            selectedFont = newFont
            notifyItemChanged(lastSelectedIndex)
            notifyItemChanged(newSelectedFontIndex)
            lastSelectedIndex = newSelectedFontIndex
        }
    }

    inner class FontViewHolder(private val binding : ItemWriteTodayOnelineFontBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var font : Font
        init {
            binding.root.setOnClickListener {
                fontChangeCallback(font)
            }
        }

        fun bind(font : Font) {
            this.font = font

            binding.labelFontTitle.text = font.title

            if (selectedFont == font) {
                binding.clRoot.setBackgroundResource(R.drawable.all_strokebutton_positive)
            } else {
                binding.clRoot.setBackgroundResource(R.drawable.all_strokebutton_negative)
            }
        }
    }
}