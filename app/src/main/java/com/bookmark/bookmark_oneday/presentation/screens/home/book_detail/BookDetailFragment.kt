package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentBookdetailBinding
import com.bookmark.bookmark_oneday.domain.model.BookDetail
import com.bookmark.bookmark_oneday.domain.model.ReadingHistory
import com.bookmark.bookmark_oneday.presentation.adapter.reading_history.BookDetailReadingHistoryAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.reading_history.BookDetailReadingHistoryDecoration
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_editpage.BookDetailEditPageDialog
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.bottomsheet_more.BookDetailMoreBottomSheetDialog
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove.BookDetailRemoveDialog
import com.bookmark.bookmark_oneday.presentation.screens.timer.TimerActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookDetailFragment : ViewBindingFragment<FragmentBookdetailBinding>(FragmentBookdetailBinding::bind, R.layout.fragment_bookdetail) {

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private val args : BookDetailFragmentArgs by navArgs()

    // getParcelableArray 사용시 type cast 에러 발생
    // 참고 : https://stackoverflow.com/questions/51714927/pass-array-in-intent-using-parcelable
    private val timerScreenLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        @Suppress("UNCHECKED_CAST")
        val response = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            result.data?.getParcelableArrayListExtra("reading_history", ReadingHistory::class.java)
        } else {
            result.data?.getParcelableArrayListExtra("reading_history")
        }

        if (result.resultCode == Activity.RESULT_OK && response != null) {
            viewModel.applyChangedReadingHistory(response.toList())
        }
    }

    @Inject
    lateinit var bookDetailViewModelFactory : BookDetailViewModel.ViewModelAssistedFactory

    private val viewModel: BookDetailViewModel by viewModels {
        BookDetailViewModel.provideViewModelFactory(
            assistedFactory = bookDetailViewModelFactory,
            bookId = args.bookId
        )
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToBack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun navigateToBack() {
        val navController = binding.root.findNavController()
        val bookState = viewModel.getBookState(removeState = false)

        if (bookState != null) {
            navController.previousBackStackEntry?.savedStateHandle?.set("book_state", bookState)
        }
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

        viewModel.tryGetBookDetail()
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
            val intent = Intent(requireActivity(), TimerActivity::class.java)
            timerScreenLauncher.launch(intent)
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
        val navController = binding.root.findNavController()
        val bookState = viewModel.getBookState(removeState = true)

        if (bookState != null) {
            navController.previousBackStackEntry?.savedStateHandle?.set("book_state", bookState)
        }
        navController.popBackStack()
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