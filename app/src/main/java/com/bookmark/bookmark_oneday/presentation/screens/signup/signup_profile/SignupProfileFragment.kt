package com.bookmark.bookmark_oneday.presentation.screens.signup.signup_profile

import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentSignupProfileBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.signup.SignupViewModel
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SignupProfileFragment : ViewBindingFragment<FragmentSignupProfileBinding>(FragmentSignupProfileBinding::bind, R.layout.fragment_signup_profile) {
    private val viewModel : SignupViewModel by activityViewModels()
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.setProfileImageUrl(uri.toString()) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setEditText()
        setObserver()

        binding.toolbarSignupProfile.startProgressAnimation(viewModel.getPrevProgress())
        viewModel.setPrevProgress(50)
    }

    private fun setButton() {
        binding.toolbarSignupProfile.setBackButtonEvent {
            binding.root.findNavController().popBackStack()
        }

        binding.btnSignupProfileNext.setOnClickListener {
            moveToNext()
        }

        binding.btnSignupProfileLater.setOnClickListener {
            viewModel.clearCommentAndProfile()
            moveToNext()
        }

        binding.cvSignupProfile.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setEditText() {
        binding.edittextSignupProfile.setTextChangedListener { comment ->
            viewModel.setComment(comment)
        }
    }

    private fun moveToNext() {
        binding.root.findNavController().navigate(R.id.action_signupProfileFragment_to_signupReadingTimeFragment)
    }

    private fun setObserver() {
        viewModel.profileImageUrl.collectLatestInLifecycle(viewLifecycleOwner) { url ->
            if (url == null) {
                binding.imgSignupProfile.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_all_default_profile)
                )
            } else {
               Glide.with(requireContext())
                   .load(url)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .skipMemoryCache(true)
                   .into(binding.imgSignupProfile)
            }
        }

        viewModel.comment.collectLatestInLifecycle(viewLifecycleOwner) { comment ->
            binding.edittextSignupProfile.setText(comment)
        }

        viewModel.nickname.collectLatestInLifecycle(viewLifecycleOwner) { nickname ->
            binding.labelSignupProfileNickname.text = nickname
        }
    }
}