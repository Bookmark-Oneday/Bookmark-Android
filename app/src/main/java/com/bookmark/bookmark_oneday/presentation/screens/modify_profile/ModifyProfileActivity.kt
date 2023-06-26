package com.bookmark.bookmark_oneday.presentation.screens.modify_profile

import android.os.Bundle
import androidx.activity.viewModels
import com.bookmark.bookmark_oneday.databinding.ActivityModifyProfileBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity

class ModifyProfileActivity : ViewBindingActivity<ActivityModifyProfileBinding>(ActivityModifyProfileBinding::inflate) {
    private val viewModel : ModifyProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
    }

    private fun setButton() {
        binding.btnModifyProfileComplete.setOnClickListener {

        }

        binding.btnModifyProfileAddImage.setOnClickListener {

        }

        binding.btnModifyProfileRemoveImage.setOnClickListener {

        }

        binding.btnModifyProfileBack.setOnClickListener {
            finish()
        }
    }
}