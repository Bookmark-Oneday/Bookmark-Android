package com.bookmark.bookmark_oneday.presentation.adapter.today_oneline

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.databinding.ItemTodayOnelineBinding
import com.bookmark.bookmark_oneday.domain.oneline.model.OneLine
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.Font
import com.bumptech.glide.Glide

class TodayOnelineAdapter : ListAdapter<OneLine, TodayOnelineAdapter.TodayOnelineViewHolder>(OneLineDiffUtil()) {

    private var top = 0
    private var bottom = 360
    private var left = 0
    private var right = 360

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayOnelineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTodayOnelineBinding.inflate(inflater, parent, false)
        return TodayOnelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayOnelineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setContentArea(top : Int, bottom : Int, left : Int, right : Int) {
        this.top = top
        this.bottom = bottom
        this.left = left
        this.right = right
    }

    // 해당 ViewHolder 는 항상 TodayOnelineAdapter 와 같이 사용되므로, inner class 로 설정
    // top, bottom 변수를 사용
    inner class TodayOnelineViewHolder(private val binding : ItemTodayOnelineBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var oneLiner : OneLine

        fun bind(oneline : OneLine) {
            oneLiner = oneline

            oneline.backgroundImageUrl?.let {
                Glide.with(binding.root.context).load(it).into(binding.imgTodayOnelineBackground)
            }
            binding.labelTodayOnelineBookInfo.text = oneline.bookInfo
            binding.labelTodayOnelineContent.text = oneline.oneliner
            binding.labelTodayOnelineBookInfo.textSize = oneline.fontSize * 0.7f
            binding.labelTodayOnelineContent.textSize = oneline.fontSize.toFloat()
            binding.labelTodayOnelineBookInfo.setTextColor(Color.parseColor(oneline.textColor))
            binding.labelTodayOnelineContent.setTextColor(Color.parseColor(oneline.textColor))
            moveViewInContentArea(binding.llTodayOnelineContent, oneline.centerYPosition, oneline.centerXPosition)
            setFont(Font.getInstanceFromString(oneline.font))
        }

        private fun moveViewInContentArea(view : View, centerYPositionRatio : Float, centerXPositionRatio : Float) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val viewWidth = view.measuredWidth
            val viewHeight = view.measuredHeight

            val leftPositionPixel = centerXPositionRatio * (right + left) - viewWidth / 2
            val topPositionPixel = centerYPositionRatio * (bottom + top) - viewHeight / 2

            if (leftPositionPixel + viewWidth > right) {
                view.x = (right - viewWidth).toFloat()
            } else if (leftPositionPixel < left) {
                view.x = left.toFloat()
            } else {
                view.x = leftPositionPixel
            }

            if (topPositionPixel + viewHeight > bottom) {
                view.y = (bottom - viewHeight).toFloat()
            } else if (topPositionPixel < top) {
                view.y = top.toFloat()
            } else {
                view.y = topPositionPixel
            }
        }

        private fun setFont(font : Font) {
            val typeface = if (font.resourceId == Font.FONT_DEFAULT) {
                Typeface.DEFAULT
            } else {
                ResourcesCompat.getFont(binding.root.context, font.resourceId)
            }
            binding.labelTodayOnelineBookInfo.typeface = typeface
            binding.labelTodayOnelineContent.typeface = typeface
        }
    }

}


class OneLineDiffUtil : DiffUtil.ItemCallback<OneLine>() {
    override fun areItemsTheSame(oldItem: OneLine, newItem: OneLine): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(oldItem: OneLine, newItem: OneLine): Boolean {
        return (oldItem == newItem)
    }
}