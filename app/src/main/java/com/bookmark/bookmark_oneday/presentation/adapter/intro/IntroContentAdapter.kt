package com.bookmark.bookmark_oneday.presentation.adapter.intro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.databinding.ItemIntroContentBinding
import com.bookmark.bookmark_oneday.presentation.screens.intro.model.IntroData

class IntroContentAdapter(
    private val introDataList : List<IntroData> = listOf()
) : RecyclerView.Adapter<IntroContentAdapter.IntroContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroContentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemIntroContentBinding.inflate(layoutInflater, parent, false)
        return IntroContentViewHolder(binding)
    }

    override fun getItemCount(): Int = introDataList.size

    override fun onBindViewHolder(holder: IntroContentViewHolder, position: Int) {
        holder.bind(introDataList[position])
    }

    class IntroContentViewHolder(
        private val binding: ItemIntroContentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(introData: IntroData) {
            binding.labelItemIntroContentTitle.text = binding.root.context.getString(introData.titleResourceId)
            binding.labelItemIntroContentCaption.text = binding.root.context.getString(introData.captionResourceId)
            binding.imgItemIntro.setImageDrawable(ContextCompat.getDrawable(binding.root.context, introData.imageResourceId))
        }
    }
}