package com.bookmark.bookmark_oneday.presentation.screens.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ActivityHomeBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ViewBindingActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navhost_home_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeNav_todayOnelineFragment -> {
                    applyTransparentStatusBar()
                }
                else -> {
                    if (destination.id == R.id.bookDetailFragment){
                        binding.navHomeBottom.menu.findItem(R.id.homeNav_mylibraryFragment).isChecked = true
                    }
                    restoreFromTransparentStatusBar()
                }
            }
        }

        binding.navHomeBottom.setupWithNavController(navController)
    }

    private fun applyTransparentStatusBar() {
        binding.navHomeBottom.background = ContextCompat.getDrawable(this, R.color.transparent)
        binding.navHomeBottom.itemBackground = ContextCompat.getDrawable(this, R.color.transparent)
        binding.navHomeBottom.itemTextColor = ContextCompat.getColorStateList(this, R.color.selector_menu_on_transparent)
        binding.navHomeBottom.itemIconTintList = ContextCompat.getColorStateList(this, R.color.selector_menu_on_transparent)

        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        binding.lineHomeBottom.visibility = View.INVISIBLE

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun restoreFromTransparentStatusBar() {
        binding.navHomeBottom.background = ContextCompat.getDrawable(this, R.color.default_background)
        binding.navHomeBottom.itemBackground = ContextCompat.getDrawable(this, R.color.default_background)
        binding.navHomeBottom.itemTextColor = ContextCompat.getColorStateList(this, R.color.selector_menu_tint)
        binding.navHomeBottom.itemIconTintList = ContextCompat.getColorStateList(this, R.color.selector_menu_tint)

        window.statusBarColor = ContextCompat.getColor(this, R.color.default_background)
        binding.lineHomeBottom.visibility = View.VISIBLE

        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding.navHomeBottom.setPadding(0, 0, 0, 0)
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