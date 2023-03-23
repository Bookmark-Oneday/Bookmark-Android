package com.bookmark.bookmark_oneday.presentation.adapter.mylibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.databinding.ItemMylibrarySortBinding
import com.bookmark.bookmark_oneday.domain.model.SortData
import kotlin.math.max

class MyLibrarySortAdapter (
    private val sortList : List<SortData>,
    private val onClickSort : (SortData) -> Unit,
    initSelectedSort : SortData
) : RecyclerView.Adapter<MyLibrarySortViewHolder>() {
    // 혹시 알맞은 정렬기준이 없는 경우 (-1), 첫 번째 값으로 설정
    private var selectedIndex = max(sortList.indexOf(initSelectedSort), 0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyLibrarySortViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMylibrarySortBinding.inflate(inflater, parent, false)
        return MyLibrarySortViewHolder(binding, onClickSort)
    }

    override fun getItemCount(): Int = sortList.size

    override fun onBindViewHolder(holder: MyLibrarySortViewHolder, position: Int) {
        holder.bind(sortList[position], position == selectedIndex)
    }
}

class MyLibrarySortViewHolder(
    private val binding : ItemMylibrarySortBinding,
    private val onClick : (SortData) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var currentSortData: SortData

    init {
        binding.root.setOnClickListener {
            onClick(currentSortData)
        }
    }

    fun bind(sortData: SortData, isSelected : Boolean) {
        currentSortData = sortData
        binding.imgMylibrarySortCheck.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
        binding.labelMylibrarySortItem.text = sortData.presentText
    }
}