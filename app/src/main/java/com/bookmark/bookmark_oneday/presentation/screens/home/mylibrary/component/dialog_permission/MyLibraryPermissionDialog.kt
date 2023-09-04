package com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.component.dialog_permission

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogMylibraryCameraPermissionBinding

class MyLibraryPermissionDialog(
    private val onClick : () -> Unit
) : DialogFragment() {
    private lateinit var binding : DialogMylibraryCameraPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogMylibraryCameraPermissionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
    }

    private fun setButton() {
        binding.btnMylibraryCameraPermissionConfirm.setOnClickListener {
            onClick()
            dismiss()
        }
    }

}