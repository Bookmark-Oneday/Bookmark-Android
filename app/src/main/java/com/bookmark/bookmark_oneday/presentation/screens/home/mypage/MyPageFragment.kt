package com.bookmark.bookmark_oneday.presentation.screens.home.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentMypageBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.modify_profile.ModifyProfileActivity

class MyPageFragment : ViewBindingFragment<FragmentMypageBinding>(
    FragmentMypageBinding::bind,
    R.layout.fragment_mypage
) {
    private val viewModel : MyPageViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
    }

    private fun setButton() {
        binding.partialMypageProfile.btnMypageProfileSetting.setOnClickListener {
            val intent = Intent(requireActivity(), ModifyProfileActivity::class.java)
            startActivity(intent)
        }

        binding.llbtnMypageAlarmSetting.setOnClickListener {

        }

        binding.llbtnMypageDataClear.setOnClickListener {

        }
    }


}