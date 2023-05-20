package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline

import android.content.Intent
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
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.WriteTodayOnelineActivity
import com.bookmark.bookmark_oneday.presentation.util.applyStatusBarPadding
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle

class TodayOnelineFragment : DataBindingFragment<FragmentTodayOnelineBinding>(R.layout.fragment_today_oneline) {

    private val viewModel : TodayOnelineViewModel by activityViewModels()
    private var isInit = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireContext().applyStatusBarPadding(binding.partialTodayOnlineToolbar)

        setButton()
        setPager()
        observeViewTree()
    }

    private fun setButton() {
        binding.partialTodayOnlineToolbar.setMoreButtonEvent {
            // 신고, 삭제 팝업
        }

        binding.partialTodayOnlineToolbar.setWriteButtonEvent {
            val intent = Intent(requireContext(), WriteTodayOnelineActivity::class.java)
            requireActivity().startActivity(intent)
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
                    top = contentAreaTop,
                    bottom = contentAreaBottom,
                    left = 0,
                    right = contentAreaRight
                )

                setObserver()

                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })
    }

    // RecyclerView 내부의 moveViewInContentArea 가 onGlobalLayout 보다 먼저 호출될 경우,
    // 오늘 한줄 글자 위치가 부적절하게 적용되는 문제가 발생하여 순서를 맞추고자 observe 위치를 조정
    private fun setObserver() {
        viewModel.state.collectLatestInLifecycle(viewLifecycleOwner) { state ->
            (binding.pagerTodayOneline.adapter as TodayOnelineAdapter).submitList(state.onelineList)
            binding.progressTodayOnelineLoading.visibility = if (state.showLoading) View.VISIBLE else View.INVISIBLE
            state.userProfile?.let { binding.partialTodayOnlineToolbar.setWriterProfile(it) }
            changeViewPagerPosition(state.currentPosition)
            binding.pagerTodayOneline.isUserInputEnabled = !state.showLoading
        }
    }

    private fun changeViewPagerPosition(position : Int?) {
        if (position != null &&
            binding.pagerTodayOneline.currentItem - 1 != position) {
            binding.pagerTodayOneline.post {
                binding.pagerTodayOneline.setCurrentItem(position, !isInit)
                isInit = false
            }
        }
    }
}