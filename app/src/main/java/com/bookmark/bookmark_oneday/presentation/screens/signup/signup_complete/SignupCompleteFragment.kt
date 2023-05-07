package com.bookmark.bookmark_oneday.presentation.screens.signup.signup_complete

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentSignupCompleteBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.signup.SignupViewModel

class SignupCompleteFragment : ViewBindingFragment<FragmentSignupCompleteBinding>(FragmentSignupCompleteBinding::bind, R.layout.fragment_signup_complete) {
    private val viewModel : SignupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarSignupComplete.startProgressAnimation(viewModel.getPrevProgress())
    }
}