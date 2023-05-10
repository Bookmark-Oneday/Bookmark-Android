package com.bookmark.bookmark_oneday.presentation.screens.signup.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentLoginBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.home.HomeActivity

class LoginFragment : ViewBindingFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("loginResult") { _, bundle ->
            if (bundle.getBoolean("loginSuccess")) goToMainScreen()
        }

        setButton()
    }

    private fun goToMainScreen() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    private fun setButton() {
        binding.btnLoginGoogle.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_loginFragment_to_signupNicknameFragment)
        }
    }
}