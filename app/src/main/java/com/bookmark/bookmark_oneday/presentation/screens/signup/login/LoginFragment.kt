package com.bookmark.bookmark_oneday.presentation.screens.signup.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import com.bookmark.bookmark_oneday.BuildConfig
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentLoginBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.home.HomeActivity
import com.bookmark.bookmark_oneday.presentation.screens.signup.SignupViewModel
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginFragment : ViewBindingFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel : SignupViewModel by activityViewModels()

    private val googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)
            val code = account.serverAuthCode
            code?.let { authCode ->
                // todo 소셜 로그인 관련 중복 검사 필요
                viewModel.tryGetGoogleAccessToken(authCode)
            }
        } catch (e : ApiException) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("loginResult") { _, bundle ->
            if (bundle.getBoolean("loginSuccess")) {
                goToMainScreen()
            }
        }

        setGoogleLogin()
        setButton()
        setObserver()
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            googleSignInClient.signOut()
        }
    }

    private fun setGoogleLogin() {
        val gos = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID, true)
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gos)
    }

    private fun goToMainScreen() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    private fun setButton() {
        binding.btnLoginGoogle.setOnClickListener {
            signInGoogle()
        }
    }

    private fun setObserver() {
        viewModel.getGoogleAccessTokenSuccess.collectLatestInLifecycle(viewLifecycleOwner) { success ->
            if (success) {
                binding.root.findNavController().navigate(R.id.action_loginFragment_to_signupNicknameFragment)
            }
        }
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleLoginResult.launch(signInIntent)
    }
}