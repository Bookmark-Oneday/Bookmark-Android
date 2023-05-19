package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bookmark.bookmark_oneday.databinding.PartialTodayOnelineToolbarBinding
import com.bookmark.bookmark_oneday.domain.model.UserProfile
import com.bumptech.glide.Glide

class TodayOnelineToolbar(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding : PartialTodayOnelineToolbarBinding

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = PartialTodayOnelineToolbarBinding.inflate(layoutInflater, this, true)
    }

    fun setWriteButtonEvent(event : () -> Unit) {
        binding.btnTodayOnelineWrite.setOnClickListener {
            event()
        }
    }

    fun setMoreButtonEvent(event : () -> Unit) {
        binding.btnTodayOnelineMore.setOnClickListener {
            event()
        }
    }

    fun setWriterProfile(userProfile: UserProfile) {
        binding.labelTodayOnelineUserName.text = userProfile.nickname
        userProfile.profileImageUrl?.let { imageUrl ->
            Glide.with(context).load(imageUrl).into(binding.imgTodayOnelineProfile)
        }
    }
}