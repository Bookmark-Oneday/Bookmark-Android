package com.bookmark.bookmark_oneday.presentation.screens.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.bookmark.bookmark_oneday.databinding.ActivitySplashBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.screens.home.HomeActivity
import com.bookmark.bookmark_oneday.presentation.screens.intro.IntroActivity
import com.bookmark.bookmark_oneday.presentation.screens.signup.SignupBaseActivity
import com.bookmark.bookmark_oneday.presentation.screens.splash.model.NextAction
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : ViewBindingActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    private val viewModel : SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setObserver()

        viewModel.checkFirstOrLogin()
    }

    private fun setObserver() {
        viewModel.nextDest.collectLatestInLifecycle(this) { nextAction ->
            println(nextAction)
            when (nextAction) {
                NextAction.HOME -> {
                    goToHomeScreen()
                }
                NextAction.LOGIN -> {
                    goToLoginScreen()
                }
                NextAction.INTRO -> {
                    goToIntroScreen()
                }
            }
        }
    }

    private fun goToHomeScreen() {
        val intent = Intent(baseContext, HomeActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun goToLoginScreen() {
        val intent = Intent(baseContext, SignupBaseActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun goToIntroScreen() {
        val intent = Intent(baseContext, IntroActivity::class.java)
        finish()
        startActivity(intent)
    }
}