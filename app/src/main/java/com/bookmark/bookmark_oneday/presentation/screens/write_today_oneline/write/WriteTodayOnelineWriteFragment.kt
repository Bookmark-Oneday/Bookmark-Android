package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentWriteTodayOnelineWriteBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.TodayOnelineWriteScreenState
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
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
        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setContentView()
        setFontSizeSeekbar()
        setStateObserver()

        binding.partialWriteTodayOnelineContent.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setContentObserver()
                binding.partialWriteTodayOnelineContent.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
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

    private fun setFontSizeSeekbar() {
        binding.partialWriteTodayOnelineSeekbar.apply {
            setProgressChangeCallback(viewModel::setTextSize)
        }
    }

    private fun setStateObserver() {
        viewModel.currentState.collectLatestInLifecycle(viewLifecycleOwner) { state ->
            when (state) {
                TodayOnelineWriteScreenState.TextEdit -> {
                    binding.btnWriteTodayOnelineWriteNext.isEnabled = true
                    binding.btnWriteTodayOnelineWriteBack.isEnabled = true
                    binding.btnWriteTodayOnelineWriteNext.setText(R.string.label_write_today_oneline_write_complete)
                    binding.partialWriteTodayOnelineContent.setToEditMode()
                    binding.partialWriteTodayOnelineSeekbar.visibility = View.VISIBLE
                }
                TodayOnelineWriteScreenState.TextMove -> {
                    binding.btnWriteTodayOnelineWriteNext.isEnabled = true
                    binding.btnWriteTodayOnelineWriteBack.isEnabled = true
                    binding.btnWriteTodayOnelineWriteNext.setText(R.string.label_write_today_oneline_write_upload)
                    binding.partialWriteTodayOnelineContent.setToMoveMode()
                    binding.partialWriteTodayOnelineSeekbar.visibility = View.INVISIBLE
                }
                else -> {
                    binding.btnWriteTodayOnelineWriteNext.isEnabled = false
                    binding.btnWriteTodayOnelineWriteBack.isEnabled = false
                    binding.btnWriteTodayOnelineWriteNext.setText(R.string.label_write_today_oneline_write_upload)
                    binding.partialWriteTodayOnelineContent.setToMoveMode()
                    binding.partialWriteTodayOnelineSeekbar.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.finishCall.collectLatestInLifecycle(viewLifecycleOwner) { finish ->
            if (finish) {
                requireActivity().finish()
            }
        }
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
        }

        viewModel.textSize.collectLatestInLifecycle(viewLifecycleOwner) { sp ->
            binding.partialWriteTodayOnelineContent.setTextSize(sp)
            binding.partialWriteTodayOnelineSeekbar.setProgressValue(sp)
        }

    }
}