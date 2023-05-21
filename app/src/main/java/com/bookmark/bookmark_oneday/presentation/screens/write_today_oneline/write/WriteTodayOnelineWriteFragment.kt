package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setContentView()
        setStateObserver()

        binding.partialWriteTodayOnelineContent.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setContentObserver()

                binding.partialWriteTodayOnelineContent.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })
    }

    private fun setButton() {
        binding.btnWriteTodayOnelineWriteBack.setOnClickListener {

        }

        binding.btnWriteTodayOnelineWriteBackground.setOnClickListener {

        }

    }

    private fun setContentView() {
        binding.partialWriteTodayOnelineContent.apply {
            setOnPositionChanged(viewModel::setContentPosition)
            setOnTextChanged(viewModel::setText)
            setBookText(viewModel.getBookText())
        }
    }

    private fun setStateObserver() {
        viewModel.currentState.collectLatestInLifecycle(viewLifecycleOwner) { state ->
            when (state) {
                TodayOnelineWriteScreenState.PreView -> {

                }
                TodayOnelineWriteScreenState.TextEdit -> {

                }
                TodayOnelineWriteScreenState.Uploading -> {

                }
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
        }

    }
}