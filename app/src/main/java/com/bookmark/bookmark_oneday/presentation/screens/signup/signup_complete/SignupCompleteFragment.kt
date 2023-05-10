package com.bookmark.bookmark_oneday.presentation.screens.signup.signup_complete

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentSignupCompleteBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.signup.SignupViewModel
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle

class SignupCompleteFragment : ViewBindingFragment<FragmentSignupCompleteBinding>(FragmentSignupCompleteBinding::bind, R.layout.fragment_signup_complete) {
    private val viewModel : SignupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarSignupComplete.startProgressAnimation(viewModel.getPrevProgress())

        setObserver()
        viewModel.trySignup()
    }

    private fun setObserver() {
        viewModel.showLoadingDialog.collectLatestInLifecycle(viewLifecycleOwner) { show ->

        }

        viewModel.loginSuccess.collectLatestInLifecycle(viewLifecycleOwner) { success ->
            if (success) {
                val action = SignupCompleteFragmentDirections.actionSignupCompleteFragmentToLoginFragment()
                setFragmentResult("loginResult", bundleOf("loginSuccess" to true))
                findNavController().navigate(action)
            } else {
                findNavController().popBackStack()
            }
        }
    }
}