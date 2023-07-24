package com.bookmark.bookmark_oneday.presentation.screens.home.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentMypageBinding
import com.bookmark.bookmark_oneday.domain.user.model.UserInfo
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.home.mypage.component.ConfirmClearDataDialog
import com.bookmark.bookmark_oneday.presentation.screens.modify_profile.ModifyProfileActivity
import com.bookmark.bookmark_oneday.presentation.screens.modify_reading_time.ModifyReadingTimeActivity
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MyPageFragment : ViewBindingFragment<FragmentMypageBinding>(
    FragmentMypageBinding::bind,
    R.layout.fragment_mypage
) {
    private val viewModel : MyPageViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setObserver()
    }

    private fun setButton() {
        binding.partialMypageProfile.btnMypageProfileSetting.setOnClickListener {
            val intent = Intent(requireActivity(), ModifyProfileActivity::class.java)
            startActivity(intent)
        }

        binding.llbtnMypageAlarmSetting.setOnClickListener {

        }

        binding.llbtnMypageModifyReadingTime.setOnClickListener {
            val intent = Intent(requireActivity(), ModifyReadingTimeActivity::class.java)
            startActivity(intent)
        }

        binding.llbtnMypageDataClear.setOnClickListener {
            showConfirmClearDialog()
        }
    }

    private fun showConfirmClearDialog() {
        ConfirmClearDataDialog(viewModel::clearData).show(childFragmentManager, "ConfirmClearDataDialog")
    }

    private fun setObserver() {
        viewModel.user.collectLatestInLifecycle(owner = viewLifecycleOwner) { user ->
            applyUserInfo(user)
        }
    }

    private fun applyUserInfo(userInfo: UserInfo) {
        binding.partialMypageProfile.labelMypageUsername.text = userInfo.nickname
        binding.partialMypageProfile.labelMypageUserIntro.text = userInfo.bio
        applyProfileImage(userInfo.profileImage)
    }

    private fun applyProfileImage(profileImage : String?) {
        if (profileImage == null) {
            binding.partialMypageProfile.imgMypageProfile.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_all_default_profile))
        } else {
            Glide.with(requireContext())
                .load(profileImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_all_default_profile)
                .into(binding.partialMypageProfile.imgMypageProfile)
        }
    }
}