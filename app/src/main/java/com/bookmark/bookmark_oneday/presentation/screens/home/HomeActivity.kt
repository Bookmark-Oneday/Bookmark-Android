package com.bookmark.bookmark_oneday.presentation.screens.home

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ActivityHomeBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity

class HomeActivity : ViewBindingActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navhost_home_fragment) as NavHostFragment
        val navHostController = navHostFragment.navController
    }
}