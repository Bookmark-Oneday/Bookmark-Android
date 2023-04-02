package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
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
import com.bookmark.bookmark_oneday.presentation.screens.timer.TimerActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookDetailFragment : ViewBindingFragment<FragmentBookdetailBinding>(FragmentBookdetailBinding::bind, R.layout.fragment_bookdetail) {
    private val viewModel: BookDetailViewModel by activityViewModels()
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private val args : BookDetailFragmentArgs by navArgs()
    override fun onAttach(context: Context) {
        super.onAttach(context)

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToBack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    // todo 책 객체를 전달하도록 직렬화 과정 필요, 단 현재 책이 로드되지 않았을 때는 그냥 popBackStack()
    private fun navigateToBack() {
        val navController = binding.root.findNavController()
        navController.previousBackStackEntry?.savedStateHandle?.set("book", args.bookId)
        navController.popBackStack()
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setButtons()
        setObserver()

        viewModel.tryGetBookDetail(args.bookId)
    }

    private fun setRecyclerView() {
        binding.listReadinghistory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.listReadinghistory.adapter = BookDetailReadingHistoryAdapter()
        binding.listReadinghistory.addItemDecoration(BookDetailReadingHistoryDecoration(requireContext()))
    }

    private fun setButtons() {
        binding.btnBookdetailBack.setOnClickListener {
            navigateToBack()
        }

        binding.btnBookdetailTimer.setOnClickListener {
            // timer 화면에서 결과 받아야함, bookId 전달 필요
            val intent = Intent(requireActivity(), TimerActivity::class.java)
            intent.putExtra("book_id", args.bookId)
            startActivity(intent)
        }

        binding.btnBookdetailInputPage.setOnClickListener {
            BookDetailEditPageDialog(
                bookId = args.bookId,
                onSuccess = viewModel::setPageInfo,
                currentPage = viewModel.state.value.bookDetail?.currentPage,
                totalPage = viewModel.state.value.bookDetail?.totalPage
            ).show(childFragmentManager, "BookDetailBookmarkDialog")
        }

        binding.btnBookdetailMore.setOnClickListener {
            BookDetailMoreBottomSheetDialog(::showRemoveConfirmDialog).show(childFragmentManager, "BookDetailMoreBottomSheet")
        }
    }

    private fun showRemoveConfirmDialog() {
        BookDetailRemoveDialog(onRemoveSuccess = ::removeSuccessCallback, bookId = args.bookId).show(childFragmentManager, "BookDetailRemoveDialog")
    }

    private fun removeSuccessCallback() {
        // todo 이전 화면으로 돌아가되, 이전 화면에서 이 책을 제거하도록 전달해야 함
        binding.root.findNavController().popBackStack()

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

                    showLoadingView(bookDetailState.isShowingLoadingView)
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
        binding.labelBookdetailReadProgress.text = "${100 - bookDetail.readingPageRatio}%"
        Glide.with(this@BookDetailFragment).load(bookDetail.imageUrl).into(binding.imgBookdetailBookcover)

        binding.labelBookdetailFirstReadDay.text = bookDetail.firstReadTime
        binding.labelBookdetailTotalReadingTime.text = bookDetail.totalTime

        binding.pbBookdetailReadpage.progress = bookDetail.readingPageRatio

        (binding.listReadinghistory.adapter as BookDetailReadingHistoryAdapter).setReadingHistoryListData(bookDetail.history)
    }

    private fun showLoadingView(show : Boolean) {
        if (show) {
            binding.partialBookdetailLoading.root.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(context, R.anim.place_holder)
            binding.partialBookdetailLoading.imgBookdetailLoadingBookcover.startAnimation(animation)
            binding.partialBookdetailLoading.labelBookdetailLoadingTitle.startAnimation(animation)
            binding.partialBookdetailLoading.labelBookdetailLoadingAuthor.startAnimation(animation)
            binding.partialBookdetailLoading.labelBookdetailLoadingTitleFirstReadDay.startAnimation(animation)
            binding.partialBookdetailLoading.labelBookdetailLoadingFirstReadDay.startAnimation(animation)
            binding.partialBookdetailLoading.labelBookdetailLoadingTitleTotalReadingTime.startAnimation(animation)
            binding.partialBookdetailLoading.labelBookdetailLoadingTotalReadingTime.startAnimation(animation)
        } else {
            binding.partialBookdetailLoading.root.visibility = View.GONE
        }
    }

}