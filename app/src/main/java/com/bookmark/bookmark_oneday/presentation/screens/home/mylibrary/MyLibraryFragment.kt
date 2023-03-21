package com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentMylibraryBinding
import com.bookmark.bookmark_oneday.presentation.adapter.mylibrary.MyLibraryBookDecoration
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import kotlin.math.abs

class MyLibraryFragment : ViewBindingFragment<FragmentMylibraryBinding>(FragmentMylibraryBinding::bind, R.layout.fragment_mylibrary) {

    private lateinit var viewModel : MyLibraryViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MyLibraryViewModel::class.java]

        setRecyclerView()
        setAppbarEvent()
    }

    private fun setRecyclerView() {
        binding.listBooklist.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.listBooklist.addItemDecoration(MyLibraryBookDecoration(requireContext()))
    }

    private fun setAppbarEvent() {
        binding.ablMylibrary.addOnOffsetChangedListener { _, verticalOffset ->
            if (abs(verticalOffset) + binding.toolbarMylibrary.height > binding.partialMylibraryProfile.viewMylibraryBackground.top) {
                binding.viewMylibraryDivider.visibility = View.VISIBLE
                binding.labelMylibraryTitle.setTextColor(Color.BLACK)
                binding.toolbarMylibrary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                binding.viewMylibraryDivider.visibility = View.INVISIBLE
                binding.labelMylibraryTitle.setTextColor(Color.WHITE)
                binding.toolbarMylibrary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
            }
        }
    }

}