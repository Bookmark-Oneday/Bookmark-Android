package com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.bottomsheet_sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmark.bookmark_oneday.databinding.BottomsheetMylibrarySortBinding
import com.bookmark.bookmark_oneday.domain.model.SortData
import com.bookmark.bookmark_oneday.presentation.adapter.mylibrary.MyLibrarySortAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.mylibrary.MyLibrarySortDecoration
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyLibrarySortBottomSheet(
    private val sortList : List<SortData>,
    private val onClick : (SortData) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding : BottomsheetMylibrarySortBinding
    private var currentSort : SortData = sortList[0]

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetMylibrarySortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    private fun onClickItem(sortData: SortData) {
        onClick(sortData)
        dismiss()
    }

    private fun setRecyclerView() {
        binding.listMylibrarySort.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.listMylibrarySort.adapter = MyLibrarySortAdapter(sortList, ::onClickItem, currentSort)
        binding.listMylibrarySort.addItemDecoration(MyLibrarySortDecoration(requireContext()))
    }

    fun setCurrentSort(sortData: SortData) {
        currentSort = sortData
    }
}