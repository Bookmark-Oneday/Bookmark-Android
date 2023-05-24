package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write

import android.app.Activity.RESULT_OK
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentWriteTodayOnelineWriteBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.EditTextDetailState
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.TodayOnelineWriteScreenState
import com.bookmark.bookmark_oneday.presentation.util.applyBottomNavigationPadding
import com.bookmark.bookmark_oneday.presentation.util.applyStatusBarPadding
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.bookmark.bookmark_oneday.presentation.util.getBottomNavigationHeight
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WriteTodayOnelineWriteFragment : ViewBindingFragment<FragmentWriteTodayOnelineWriteBinding>(
    FragmentWriteTodayOnelineWriteBinding::bind, R.layout.fragment_write_today_oneline_write
) {
    @Inject
    lateinit var writeTodayOnelineWriteViewModelFactory : WriteTodayOnelineWriteViewModel.ViewModelAssistedFactory
    private val args : WriteTodayOnelineWriteFragmentArgs by navArgs()
    private val viewModel : WriteTodayOnelineWriteViewModel by viewModels {
        WriteTodayOnelineWriteViewModel.providerViewModelFactory(
            assistedFactory = writeTodayOnelineWriteViewModelFactory,
            book = args.book
        )
    }
    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
        viewModel.setBackgroundImageUri(uri?.toString())
    }
    private lateinit var backPressedCallback : OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBackPressEvent()
    }

    private fun setBackPressEvent() {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.handleBackPress()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setContentView()
        setFontSettingView()
        setFontSizeSeekbar()
        setStateObserver()

        binding.partialWriteTodayOnelineContent.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setContentObserver()
                binding.partialWriteTodayOnelineContent.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        setImeListener()

        requireContext().applyStatusBarPadding(binding.clWriteTodayOnelineWriteToolbar)
        requireContext().applyBottomNavigationPadding(binding.clWriteTodayOnelineWriteBottom)

        binding.imgWriteTodayOnelineThumbnail.setOnClickListener {
            closeSoftKeyboard()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressedCallback.remove()
    }

    private fun setButton() {
        binding.btnWriteTodayOnelineWriteNext.setOnClickListener {
            viewModel.handleNextPress()
        }

        binding.btnWriteTodayOnelineWriteBackground.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.btnWriteTodayOnelineWriteBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun setContentView() {
        binding.partialWriteTodayOnelineContent.apply {
            initOnPositionChanged(viewModel::setContentPosition)
            setOnTextChanged(viewModel::setText)
            setBookText(viewModel.getBookText())
            initModeSwitchEvent(viewModel::changeToTextEditMode)
        }
    }

    private fun setFontSettingView() {
        binding.partialWriteTodayOnelineTextAttrSetting.apply {
            setColorChangeCallback(viewModel::setTextColor)

            setFontChangeCallback(viewModel::setFont)

            setFontButtonClick {
                viewModel.setEditTextDetailState(EditTextDetailState.Font)
            }
        }
    }

    private fun setFontSizeSeekbar() {
        binding.partialWriteTodayOnelineSeekbar.apply {
            setProgressChangeCallback(viewModel::setTextSize)
        }
    }

    private fun setStateObserver() {
        viewModel.currentState.collectLatestInLifecycle(viewLifecycleOwner) { state ->
            when (state) {
                TodayOnelineWriteScreenState.TextEdit -> {
                    setEnableButtons(enable = true)
                    setEnableEditView(enable = true)
                    setToEditMode()
                    showSoftKeyboard()
                }
                TodayOnelineWriteScreenState.TextMove -> {
                    setEnableButtons(enable = true)
                    setEnableEditView(enable = true)
                    setToMoveMode()
                }
                TodayOnelineWriteScreenState.Uploading -> {
                    setEnableButtons(enable = false)
                    setEnableEditView(enable = false)
                }
            }
        }

        viewModel.finishWithSuccess.collectLatestInLifecycle(viewLifecycleOwner) { registerSuccess ->
            if (registerSuccess) {
                requireActivity().setResult(RESULT_OK)
            }
            requireActivity().finish()
        }

        viewModel.editTextDetailState.collectLatestInLifecycle(viewLifecycleOwner) { editTextDetailState ->
            binding.partialWriteTodayOnelineTextAttrSetting.hideColorSelectView()
            when (editTextDetailState) {
                EditTextDetailState.Normal -> {
                    closeSoftKeyboard()
                    moveSettingViewY(-requireContext().getBottomNavigationHeight().toFloat())
                    binding.partialWriteTodayOnelineTextAttrSetting.toHideMode()
                }
                EditTextDetailState.Font -> {
                    closeSoftKeyboard()
                    val translationY =
                        -(requireContext().getBottomNavigationHeight()+binding.partialWriteTodayOnelineTextAttrSetting.getFontViewHeight()).toFloat()
                    moveSettingViewY(translationY)
                    binding.partialWriteTodayOnelineTextAttrSetting.toFontSettingMode()
                }
                EditTextDetailState.IME -> {
                    showSoftKeyboard()
                    moveSettingViewY(-viewModel.imeHeight.toFloat())
                    binding.partialWriteTodayOnelineTextAttrSetting.toSoftKeyboardMode()
                }

            }
        }
    }

    private fun setImeListener() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.clWriteTodayOnelineWriteBottom) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            if (imeVisible) {
                viewModel.imeHeight = imeHeight
                viewModel.setEditTextDetailState(EditTextDetailState.IME)
            } else {
                viewModel.hideSoftKeyboard()
            }

            insets
        }
    }

    private fun setEnableButtons(enable : Boolean) {
        binding.btnWriteTodayOnelineWriteNext.isEnabled = enable
        binding.btnWriteTodayOnelineWriteBack.isEnabled = enable
    }

    private fun setEnableEditView(enable: Boolean) {
        binding.partialWriteTodayOnelineContent.setEnable(enable)
    }

    private fun setToEditMode() {
        binding.btnWriteTodayOnelineWriteNext.setText(R.string.label_write_today_oneline_write_complete)
        binding.partialWriteTodayOnelineContent.setToEditMode()
        binding.partialWriteTodayOnelineSeekbar.visibility = View.VISIBLE
        binding.clWriteTodayOnelineWriteBottom.visibility = View.INVISIBLE
        binding.partialWriteTodayOnelineTextAttrSetting.visibility = View.VISIBLE
    }

    private fun setToMoveMode() {
        binding.btnWriteTodayOnelineWriteNext.setText(R.string.label_write_today_oneline_write_upload)
        binding.partialWriteTodayOnelineContent.setToMoveMode()
        binding.partialWriteTodayOnelineSeekbar.visibility = View.INVISIBLE
        binding.clWriteTodayOnelineWriteBottom.visibility = View.VISIBLE
        binding.partialWriteTodayOnelineTextAttrSetting.visibility = View.INVISIBLE
    }

    private fun setContentObserver() {
        viewModel.position.collectLatestInLifecycle(viewLifecycleOwner) { position ->
            binding.partialWriteTodayOnelineContent.setPosition(position.x, position.y)
        }

        viewModel.content.collectLatestInLifecycle(viewLifecycleOwner) { content ->
            binding.partialWriteTodayOnelineContent.setText(content)
        }

        viewModel.backgroundUri.collectLatestInLifecycle(viewLifecycleOwner) { uri ->
            uri?.let { Glide.with(this@WriteTodayOnelineWriteFragment).load(it).into(binding.imgWriteTodayOnelineThumbnail) }
        }

        viewModel.textColor.collectLatestInLifecycle(viewLifecycleOwner) { colorString ->
            binding.partialWriteTodayOnelineContent.setTextColor(colorString)
            binding.partialWriteTodayOnelineTextAttrSetting.setSelectedColor(colorString)
        }

        viewModel.textSize.collectLatestInLifecycle(viewLifecycleOwner) { sp ->
            binding.partialWriteTodayOnelineContent.setTextSize(sp)
            binding.partialWriteTodayOnelineSeekbar.setProgressValue(sp)
        }

        viewModel.font.collectLatestInLifecycle(viewLifecycleOwner) { font ->
            binding.partialWriteTodayOnelineTextAttrSetting.setFont(font)
        }
    }
    private fun showSoftKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(requireActivity().currentFocus, 0)
    }

    private fun closeSoftKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.partialWriteTodayOnelineContent.getFocusViewWindowToken(), 0)
    }

    private fun moveSettingViewY(translationY : Float) {
        binding.partialWriteTodayOnelineTextAttrSetting.translationY = translationY
    }

}