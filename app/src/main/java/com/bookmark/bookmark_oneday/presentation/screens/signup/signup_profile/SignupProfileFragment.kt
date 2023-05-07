package com.bookmark.bookmark_oneday.presentation.screens.signup.signup_profile

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentSignupProfileBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.signup.SignupViewModel
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.bumptech.glide.Glide

class SignupProfileFragment : ViewBindingFragment<FragmentSignupProfileBinding>(FragmentSignupProfileBinding::bind, R.layout.fragment_signup_profile) {
    private val viewModel : SignupViewModel by activityViewModels()

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
            // viewModel 에 데이터 저장 후 이동
            moveToNext()
        }

        binding.btnSignupProfileLater.setOnClickListener {
            moveToNext()
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
               Glide.with(requireContext()).load(url).into(binding.imgSignupProfile)
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