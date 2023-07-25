package com.bookmark.bookmark_oneday.presentation.screens.signup.signup_nickname

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentSignupNicknameBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.signup.SignupViewModel
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle

class SignupNicknameFragment : ViewBindingFragment<FragmentSignupNicknameBinding>(FragmentSignupNicknameBinding::bind, R.layout.fragment_signup_nickname) {
    private val viewModel : SignupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setEditText()
        setObserver()

        binding.toolbarSignupNickname.startProgressAnimation(viewModel.getPrevProgress())
        viewModel.setPrevProgress(25)
    }

    private fun setButton() {
        binding.btnSignupNicknameNext.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_signupNicknameFragment_to_signupProfileFragment)
        }
    }

    private fun setEditText() {
        binding.edittextSignupNickname.setTextChangedListener { nickname ->
            viewModel.setNickname(nickname)
        }
    }

    private fun setObserver() {
        viewModel.nickname.collectLatestInLifecycle(viewLifecycleOwner) {nickname ->
            binding.edittextSignupNickname.setText(nickname)
            binding.btnSignupNicknameNext.setButtonEnabled(nickname.trim().isNotEmpty())
        }
    }
}