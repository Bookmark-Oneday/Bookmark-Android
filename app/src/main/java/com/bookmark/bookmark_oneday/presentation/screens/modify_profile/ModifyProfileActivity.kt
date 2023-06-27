package com.bookmark.bookmark_oneday.presentation.screens.modify_profile

import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ActivityModifyProfileBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModifyProfileActivity : ViewBindingActivity<ActivityModifyProfileBinding>(ActivityModifyProfileBinding::inflate) {
    private val viewModel : ModifyProfileViewModel by viewModels()
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.setProfileImageUri(uri.toString()) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setObserver()
        setEditText()
    }

    private fun setButton() {
        binding.btnModifyProfileComplete.setOnClickListener {
            viewModel.tryModifyUserInfo()
        }

        binding.btnModifyProfileAddImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnModifyProfileRemoveImage.setOnClickListener {
            viewModel.setProfileImageUri(null)
        }

        binding.btnModifyProfileBack.setOnClickListener {
            finish()
        }
    }

    private fun setObserver() {
        viewModel.modifyProfileResult.collectLatestInLifecycle(owner = this) { success ->
            if (success) finish()
        }

        viewModel.bio.collectLatestInLifecycle(this) {
            binding.edittextModifyProfile.setText(it)
        }

        viewModel.nickname.collectLatestInLifecycle(this) {
            binding.labelModifyProfileNickname.text = it
        }

        viewModel.profileImageUri.collectLatestInLifecycle(this) { imageUri ->
            if (imageUri == null) {
                binding.btnModifyProfileAddImage.visibility = View.VISIBLE
                binding.btnModifyProfileRemoveImage.visibility = View.INVISIBLE
                binding.imgSignupProfile.setBackgroundResource(R.drawable.ic_all_default_profile)
            } else {
                binding.btnModifyProfileAddImage.visibility = View.INVISIBLE
                binding.btnModifyProfileRemoveImage.visibility = View.VISIBLE
                Glide.with(baseContext).load(imageUri).into(binding.imgSignupProfile)
            }
        }

    }

    private fun setEditText(){
        binding.edittextModifyProfile.setTextChangedListener {
            viewModel.setBio(it)
        }
    }

}