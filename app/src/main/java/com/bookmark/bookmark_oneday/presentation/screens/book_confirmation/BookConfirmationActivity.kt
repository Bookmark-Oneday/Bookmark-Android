package com.bookmark.bookmark_oneday.presentation.screens.book_confirmation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.bookmark.bookmark_oneday.databinding.ActivityBookConfirmationBinding
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.screens.book_confirmation.component.BookConfirmDuplicateDialog
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookConfirmationActivity : ViewBindingActivity<ActivityBookConfirmationBinding>(ActivityBookConfirmationBinding::inflate) {

    private val viewModel: BookConfirmationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bookInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("book", RecognizedBook::class.java)
        } else {
            intent.getSerializableExtra("book") as RecognizedBook
        }

        viewModel.applyBookInfo(bookInfo!!)

        setButton()
        setObserver()
    }

    private fun setButton() {
        binding.btnBookconfirmConfirm.setOnClickListener {
            viewModel.tryCheckAndRegisterBook()
        }

        binding.btnBookconfirmBack.setOnClickListener {
            finish()
        }
    }

    private fun setObserver() {
        viewModel.state.collectLatestInLifecycle(this) { state ->
            state.book?.let { applyBookData(it) }
            binding.btnBookconfirmConfirm.isEnabled = state.buttonActive
            binding.btnBookconfirmBack.isEnabled = state.buttonActive
        }

        viewModel.sideEffectShowDuplicateDialog.collectLatestInLifecycle(this) { show ->
            if (show) {
                BookConfirmDuplicateDialog(
                    onClose = viewModel::cancelRegister,
                    onClickRegister = viewModel::tryRegisterBook
                ).show(supportFragmentManager, "BookConfirmDuplicate")
            }
        }

        viewModel.sideEffectSuccessRegister.collectLatestInLifecycle(this) { success ->
            if (success) {
                setResult(RESULT_OK)
                finish()
            }
        }
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