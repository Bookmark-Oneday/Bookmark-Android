package com.bookmark.bookmark_oneday.presentation.screens.intro

import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bookmark.bookmark_oneday.databinding.ActivityIntroBinding
import com.bookmark.bookmark_oneday.presentation.adapter.intro.IntroContentAdapter
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : ViewBindingActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {
    private val viewModel : IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setViewPager()
        setObserver()
    }

    private fun setButton() {
        binding.btnIntroStart.setOnClickListener {
            // todo 로그인 화면 구현 후 화면이동 로직 추가
            viewModel.setExecuted()
            finish()
        }
    }

    private fun setViewPager() {
        binding.pagerIntroContents.adapter = IntroContentAdapter(viewModel.introDataList)
        binding.pagerIntroContents.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setActivePosition(position)
            }
        })
        binding.viewIntroPagerDot.setTotalSize(viewModel.introDataList.size)
    }

    private fun setObserver() {
        viewModel.dotPosition.collectLatestInLifecycle(this) { position ->
            binding.viewIntroPagerDot.setActivePosition(position)
        }
    }
}