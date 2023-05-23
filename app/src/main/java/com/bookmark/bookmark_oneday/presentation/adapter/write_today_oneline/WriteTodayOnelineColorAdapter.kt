package com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.databinding.ItemWriteTodayOnelineColorBinding
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.defaultColorList

class WriteTodayOnelineColorAdapter : RecyclerView.Adapter<WriteTodayOnelineColorAdapter.ColorViewHolder>() {
    private val colorList = defaultColorList
    private var colorChangeCallback : (String) -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWriteTodayOnelineColorBinding.inflate(inflater, parent, false)
        return ColorViewHolder(binding)
    }

    override fun getItemCount(): Int = colorList.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colorList[position])
    }

    fun setColorChangeCallback(callback : (String) -> Unit) {
        colorChangeCallback = callback
    }

    inner class ColorViewHolder(private val binding : ItemWriteTodayOnelineColorBinding) : RecyclerView.ViewHolder(binding.root) {
        private var colorString : String = "#FFFFFF"
        init {
            binding.root.setOnClickListener {
                colorChangeCallback(colorString)
            }
        }

        fun bind(colorString : String) {
            this.colorString = colorString
            binding.imgColor.imageTintList = ColorStateList.valueOf(Color.parseColor(colorString))
        }
    }
}