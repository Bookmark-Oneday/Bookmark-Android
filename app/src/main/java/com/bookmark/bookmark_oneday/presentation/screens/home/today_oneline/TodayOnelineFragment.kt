package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.core.presentation.util.toVisibility
import com.bookmark.bookmark_oneday.databinding.FragmentTodayOnelineBinding
import com.bookmark.bookmark_oneday.presentation.adapter.today_oneline.TodayOnelineAdapter
import com.bookmark.bookmark_oneday.presentation.base.DataBindingFragment
import com.bookmark.bookmark_oneday.presentation.base.dialog.TwoButtonDialog
import com.bookmark.bookmark_oneday.presentation.screens.book_confirmation_oneline.BookConfirmationFromOnelineActivity
import com.bookmark.bookmark_oneday.presentation.screens.home.HomeActivity
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.component.TodayOnelineBottomSheet
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model.TodayOnelineSideEffect
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model.ViewPagerPosition
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.WriteTodayOnelineActivity
import com.bookmark.bookmark_oneday.presentation.util.applyStatusBarPadding
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle

class TodayOnelineFragment : DataBindingFragment<FragmentTodayOnelineBinding>(R.layout.fragment_today_oneline) {

    private val viewModel : TodayOnelineViewModel by activityViewModels()
    private val writeTodayOneLineScreenLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.tryGetFirstPagingData()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireContext().applyStatusBarPadding(binding.partialTodayOnlineToolbar)

        setButton()
        setPager()
        observeViewTree {
            setObserver()
        }

    }

    private fun setButton() {
        binding.partialTodayOnlineToolbar.setMoreButtonEvent {
            val moreBottomSheet = TodayOnelineBottomSheet(
                onRemoveClick = ::callDeleteConfirmDialog,
                onBookInfoClick = ::goToBookConfirmation
            )
            moreBottomSheet.show(childFragmentManager, "OnelineMoreBottomSheet")
        }

        binding.partialTodayOnlineToolbar.setWriteButtonEvent {
            val intent = Intent(requireContext(), WriteTodayOnelineActivity::class.java)
            writeTodayOneLineScreenLauncher.launch(intent)
        }
    }

    private fun callDeleteConfirmDialog() {
        val deleteConfirmDialog = TwoButtonDialog(
            title = getString(R.string.label_oneline_delete_dialog),
            caption = getString(R.string.caption_oneline_delete_dialog),
            leftText = getString(R.string.label_oneline_delete_dialog_cancel),
            rightText = getString(R.string.label_oneline_delete_dialog_delete),
            onLeftButtonClick = {},
            onRightButtonClick = viewModel::deleteOneline
        )
        deleteConfirmDialog.show(childFragmentManager, "onelineDeleteConfirmationDialog")
    }

    private fun goToBookConfirmation() {
        val isbn = viewModel.getCurrentOnelineBookIsbn() ?: return
        val intent = Intent(requireContext(), BookConfirmationFromOnelineActivity::class.java)
        intent.putExtra("isbn", isbn)
        startActivity(intent)
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
                    binding.pagerTodayOneline.currentItem != 0
                ) {
                    viewModel.tryGetNextPagingData()
                }

                previousState = state
            }
        })
    }

    private fun observeViewTree(callback : () -> Unit) {
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

                callback()

                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })
    }

    // RecyclerView 내부의 moveViewInContentArea 가 onGlobalLayout 보다 먼저 호출될 경우,
    // 오늘 한줄 글자 위치가 부적절하게 적용되는 문제가 발생하여 순서를 맞추고자 observe 위치를 조정
    private fun setObserver() {
        viewModel.state.collectLatestInLifecycle(viewLifecycleOwner) { state ->
            binding.partialTodayOnelineEmpty.root.visibility = state.showEmptyView.toVisibility()
            binding.partialTodayOnlineToolbar.setOnelineInfoVisible(!state.showEmptyView)
            setLoadingDialogVisibility(state.showLoading)
            state.userProfile?.let { binding.partialTodayOnlineToolbar.setWriterProfile(it) }
            binding.pagerTodayOneline.isUserInputEnabled = !state.showLoading
            (binding.pagerTodayOneline.adapter as TodayOnelineAdapter).submitList(state.onelineList) {
                viewModel.callMoveSideEffect(state.onelineList.size)
            }
        }

        viewModel.sideEffect.collectLatestInLifecycle(viewLifecycleOwner) { sideEffect ->
            when (sideEffect) {
                is TodayOnelineSideEffect.MovePage -> {
                    changeViewPagerPosition(sideEffect.viewPagerPosition)
                }
                is TodayOnelineSideEffect.ShowToast -> {
                    Toast.makeText(requireContext(), sideEffect.toastMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setLoadingDialogVisibility(show : Boolean) {
        if (show) {
            binding.progressTodayOnelineLoading.visibility = View.VISIBLE
            binding.progressTodayOnelineLoading.playAnimation()
        } else {
            binding.progressTodayOnelineLoading.visibility = View.INVISIBLE
            binding.progressTodayOnelineLoading.pauseAnimation()
        }
    }

    private fun changeViewPagerPosition(viewPagerPosition: ViewPagerPosition) {
        binding.pagerTodayOneline.post {
            binding.pagerTodayOneline.setCurrentItem(viewPagerPosition.position, viewPagerPosition.useAnimation)
        }
    }

}