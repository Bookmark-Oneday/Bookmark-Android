package com.bookmark.bookmark_oneday.presentation.screens.book_confirmation_oneline

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ActivityBookConfirmationBinding
import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.base.dialog.OneButtonDialog
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookConfirmationFromOnelineActivity : ViewBindingActivity<ActivityBookConfirmationBinding>(ActivityBookConfirmationBinding::inflate) {
    private val viewModel : BookConfirmationFromOnelineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initObserver()
        initButton()

        handleNavigationParameter()
    }

    private fun initView() {
        binding.btnBookconfirmConfirm.visibility = View.INVISIBLE
    }

    private fun initObserver() {
        viewModel.state.collectLatestInLifecycle(this) {state ->
            state.recognizedBook?.let { applyBookData(it) }
        }
    }

    private fun initButton() {
        binding.btnBookconfirmBack.setOnClickListener {
            finish()
        }
    }

    private fun handleNavigationParameter() {
        val isbn = intent.getStringExtra("isbn")
        if (isbn == null) {
            callEmptyParameterDialog()
        } else {
            viewModel.searchBook(isbn)
        }
    }

    private fun callEmptyParameterDialog() {
        val dialog = OneButtonDialog(
            title = getString(R.string.label_bookconfirm_cannot_founc_book_info),
            caption = getString(R.string.caption_bookconfirm_cannot_found_book_info),
            buttonText = getString(R.string.label_bookconfirm_confirm),
            buttonClick = {
                finish()
            }
        )
        dialog.show(supportFragmentManager, "bookconfirmParameterErrorDialog")
    }

    @SuppressLint("SetTextI18n")
    private fun applyBookData(book: RecognizedBook) {
        binding.labelBookconfirmTitle.text = book.title
        binding.labelBookconfirmAuthor.text = book.authors.joinToString(", ")

        binding.labelBookconfirmSummary.text = book.content + "..."
        Glide.with(this).load(book.thumbnail_url).into(binding.imgBookdetailBookcover)

        binding.labelBookconfirmPublisher.text = book.publisher
        binding.labelBookconfirmPublishDate.text = book.dateTime
    }
}