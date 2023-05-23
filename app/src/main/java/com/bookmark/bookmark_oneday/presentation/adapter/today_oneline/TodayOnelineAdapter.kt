package com.bookmark.bookmark_oneday.presentation.adapter.today_oneline

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.databinding.ItemTodayOnelineBinding
import com.bookmark.bookmark_oneday.domain.model.OneLine
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
            binding.labelTodayOnelineBookInfo.textSize = oneline.fontSize.toFloat()
            binding.labelTodayOnelineBookInfo.setTextColor(Color.parseColor(oneline.textColor))
            binding.labelTodayOnelineContent.setTextColor(Color.parseColor(oneline.textColor))
            moveViewInContentArea(binding.llTodayOnelineContent, oneline.topPosition, oneline.leftPosition)


        }

        private fun moveViewInContentArea(view : View, topPositionRatio : Float, leftPositionRatio : Float) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val viewWidth = view.measuredWidth
            val viewHeight = view.measuredHeight
            val leftPositionPixel = ((right - left) * leftPositionRatio).toInt() + left
            val topPositionPixel = ((bottom - top) * topPositionRatio).toInt() + top

            if (leftPositionPixel + viewWidth > right) {
                view.x = (right - viewWidth).toFloat()
            } else {
                view.x = leftPositionPixel.toFloat()
            }

            if (topPositionPixel + viewHeight > bottom) {
                view.y = (bottom - viewHeight).toFloat()
            } else {
                view.y = topPositionPixel.toFloat()
            }
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