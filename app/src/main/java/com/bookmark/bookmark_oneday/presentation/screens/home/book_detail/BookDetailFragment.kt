package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentBookdetailBinding
import com.bookmark.bookmark_oneday.domain.model.BookDetail
import com.bookmark.bookmark_oneday.presentation.adapter.BookDetailReadingHistoryAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.BookDetailReadingHistoryDecoration
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_editpage.BookDetailEditPageDialog
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.bottomsheet_more.BookDetailMoreBottomSheetDialog
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove.BookDetailRemoveDialog
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookDetailFragment : ViewBindingFragment<FragmentBookdetailBinding>(FragmentBookdetailBinding::bind, R.layout.fragment_bookdetail) {
    private lateinit var viewModel: BookDetailViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[BookDetailViewModel::class.java]

        setRecyclerView()
        setButtons()
        setObserver()

        viewModel.tryGetBookDetail()
    }

    private fun setRecyclerView() {
        binding.listReadinghistory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.listReadinghistory.adapter = BookDetailReadingHistoryAdapter()
        binding.listReadinghistory.addItemDecoration(BookDetailReadingHistoryDecoration(requireContext()))
    }

    private fun setButtons() {
        binding.btnBookdetailBack.setOnClickListener {

        }

        binding.btnBookdetailInputPage.setOnClickListener {
            BookDetailEditPageDialog(
                onClick = viewModel::tryEditPageInfo,
                currentPage = viewModel.state.value.bookDetail?.currentPage,
                totalPage = viewModel.state.value.bookDetail?.totalPage
            ).show(childFragmentManager, "BookDetailBookmarkDialog")
        }

        binding.btnBookdetailMore.setOnClickListener {
            BookDetailMoreBottomSheetDialog(::showRemoveConfirmDialog).show(childFragmentManager, "BookDetailMoreBottomSheet")
        }
    }

    private fun showRemoveConfirmDialog() {
        BookDetailRemoveDialog(viewModel::tryDeleteBook).show(childFragmentManager, "BookDetailRemoveDialog")
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { bookDetailState ->
                    bookDetailState.bookDetail?.let { bookDetail -> renderBookDetail(bookDetail) }

                    binding.btnBookdetailInputPage.isEnabled = bookDetailState.inputPageButtonActive

                    binding.btnBookdetailMore.isEnabled = bookDetailState.toolbarButtonActive
                    binding.btnBookdetailLike.isEnabled = bookDetailState.toolbarButtonActive
                    binding.btnBookdetailTimer.isEnabled = bookDetailState.toolbarButtonActive
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderBookDetail(bookDetail : BookDetail) {
        binding.labelBookdetailTitle.text = bookDetail.title
        binding.labelBookdetailAuthor.text = bookDetail.author
        binding.labelBookdetailCurrentPage.text = bookDetail.currentPage.toString()
        binding.labelBookdetailTotalPage.text = "/" + bookDetail.totalPage.toString()
        Glide.with(this@BookDetailFragment).load(bookDetail.imageUrl).into(binding.imgBookdetailBookcover)

        binding.labelBookdetailFirstReadDay.text = bookDetail.firstReadTime
        binding.labelBookdetailTotalReadingTime.text = bookDetail.totalTime

        binding.pbBookdetailReadpage.progress = bookDetail.readingPageRatio

        (binding.listReadinghistory.adapter as BookDetailReadingHistoryAdapter).setReadingHistoryListData(bookDetail.history)
    }
}