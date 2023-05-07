package com.bookmark.bookmark_oneday.presentation.screens.signup.login

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentLoginBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment

class LoginFragment : ViewBindingFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
    }

    private fun setButton() {
        binding.btnLoginGoogle.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_loginFragment_to_signupNicknameFragment)
        }
    }
}