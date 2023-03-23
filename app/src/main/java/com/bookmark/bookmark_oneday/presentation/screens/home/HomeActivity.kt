package com.bookmark.bookmark_oneday.presentation.screens.home

import android.os.Bundle
import com.bookmark.bookmark_oneday.databinding.ActivityHomeBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.MyLibraryFragment

class HomeActivity : ViewBindingActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().add(binding.flHomeFragment.id, MyLibraryFragment()).commit()
    }
}