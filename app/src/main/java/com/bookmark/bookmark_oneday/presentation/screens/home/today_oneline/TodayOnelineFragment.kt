package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentTodayOnelineBinding
import com.bookmark.bookmark_oneday.presentation.adapter.today_oneline.TodayOnelineAdapter
import com.bookmark.bookmark_oneday.presentation.base.DataBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.home.HomeActivity
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model.TodayOnelineState
import com.bookmark.bookmark_oneday.presentation.util.applyStatusBarPadding
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle

class TodayOnelineFragment : DataBindingFragment<FragmentTodayOnelineBinding>(R.layout.fragment_today_oneline) {

    private val viewModel : TodayOnelineViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireContext().applyStatusBarPadding(binding.partialTodayOnlineToolbar)

        setButton()
        setPager()
        observeViewTree()
        setObserver()
    }

    private fun setButton() {
        binding.partialTodayOnlineToolbar.setMoreButtonEvent {
            // 신고, 삭제 팝업
        }

        binding.partialTodayOnlineToolbar.setWriteButtonEvent {
            // 화면 이동
        }
    }
    private fun setPager() {
        binding.pagerTodayOneline.adapter = TodayOnelineAdapter()
        binding.pagerTodayOneline.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var previousState = ViewPager2.SCROLL_STATE_IDLE

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setCurrentPage(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                if (state == ViewPager2.SCROLL_STATE_IDLE &&
                    previousState == ViewPager2.SCROLL_STATE_DRAGGING &&
                    viewModel.state.value.currentPosition != 0
                ) {
                    viewModel.tryGetNextPagingData()
                }

                previousState = state
            }
        })
    }

    private fun observeViewTree() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val contentAreaRight = resources.displayMetrics.widthPixels
                val contentAreaTop = binding.partialTodayOnlineToolbar.bottom
                val contentAreaBottom = (requireActivity() as HomeActivity).getBottomNavigationTop()

                (binding.pagerTodayOneline.adapter as TodayOnelineAdapter).setContentArea(
                    top = contentAreaTop, bottom = contentAreaBottom, left = 0, right = contentAreaRight
                )

                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })
    }

    private fun setObserver() {
        viewModel.state.collectLatestInLifecycle(viewLifecycleOwner) { state ->
            applyState(state)
        }
    }

    private fun applyState(state : TodayOnelineState) {
        (binding.pagerTodayOneline.adapter as TodayOnelineAdapter).submitList(state.onelineList)
        binding.progressTodayOnelineLoading.visibility = if (state.showLoading) View.VISIBLE else View.INVISIBLE
        state.userProfile?.let { binding.partialTodayOnlineToolbar.setWriterProfile(it) }
        if (binding.pagerTodayOneline.currentItem - 1 != state.currentPosition && state.currentPosition != null) {
            binding.pagerTodayOneline.post {
                binding.pagerTodayOneline.setCurrentItem(state.currentPosition, true)
            }
        }
        binding.pagerTodayOneline.isUserInputEnabled = !state.showLoading

    }
}