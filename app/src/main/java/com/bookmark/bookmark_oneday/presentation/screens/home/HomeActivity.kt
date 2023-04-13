package com.bookmark.bookmark_oneday.presentation.screens.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ActivityHomeBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ViewBindingActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navhost_home_fragment) as NavHostFragment
        val navHostController = navHostFragment.navController
    }

    /**
     * 홈 화면을 구성하는 각 fragment 에서 첫 데이터 로드 실패시 호출합니다.
     *
     * retry : 첫 데이터를 다시 요청하는 함수
     *
     * title : 에러 view 의 타이틀 텍스트로, 해당 fragment 의 타이틀을 입력하면 됩니다.
     */
    fun callErrorView(retry: () -> Unit, title : String) {
        binding.partialHomeNetworkError.labelErrorToolbarTitle.text = title
        binding.partialHomeNetworkError.root.visibility = View.VISIBLE
        binding.partialHomeNetworkError.btnErrorRetry.setOnClickListener {
            retry()
            hideErrorView()
        }
    }

    private fun hideErrorView() {
        binding.partialHomeNetworkError.root.visibility = View.GONE
        binding.partialHomeNetworkError.btnErrorRetry.setOnClickListener(null)
    }

}