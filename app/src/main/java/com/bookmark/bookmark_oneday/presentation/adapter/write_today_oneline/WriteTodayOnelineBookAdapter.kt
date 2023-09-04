package com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ItemWriteTodayOnelineBookAddBinding
import com.bookmark.bookmark_oneday.databinding.ItemWriteTodayOnelineBookBinding
import com.bookmark.bookmark_oneday.databinding.ItemWriteTodayOnelineBookLoadingBinding
import com.bookmark.bookmark_oneday.domain.book.model.BookItem
import com.bookmark.bookmark_oneday.presentation.model.ListItem
import com.bumptech.glide.Glide


class WriteTodayOnelineBookAdapter(
    private val onClickBook : (BookItem) -> Unit,
    private val onClickAdder : () -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {

    private val asyncDiffer = AsyncListDiffer(this, BookItemDiffUtil())
    private var lastSelectedPosition : Int ?= null
    private var selectedBook : BookItem?= null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ListItem.LIST_ADDER -> {
                val binding = ItemWriteTodayOnelineBookAddBinding.inflate(inflater, parent, false)
                WriteTodayOnelineBookAddViewHolder(binding)
            }
            ListItem.LIST_CONTENT -> {
                val binding = ItemWriteTodayOnelineBookBinding.inflate(inflater, parent, false)
                WriteTodayOnelineBookViewHolder(binding)
            }
            ListItem.LIST_LOADING -> {
                val binding = ItemWriteTodayOnelineBookLoadingBinding.inflate(inflater, parent, false)
                WriteTodayOnelineBookLoadingViewHolder(binding)
            }
            else -> {
                val binding = ItemWriteTodayOnelineBookLoadingBinding.inflate(inflater, parent, false)
                WriteTodayOnelineBookLoadingViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = asyncDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is WriteTodayOnelineBookViewHolder) {
            val currentItem = asyncDiffer.currentList[position]

            require(currentItem is ListItem.Content<BookItem>)
            holder.bind(currentItem.data)
        }
        if (holder is WriteTodayOnelineBookLoadingViewHolder) {
            holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return asyncDiffer.currentList[position].viewType
    }

    fun updateList(list : List<ListItem<BookItem>>) {
        asyncDiffer.submitList(list)
    }

    fun changeSelectedItem(book : BookItem?) {
        selectedBook = book
        lastSelectedPosition?.let { notifyItemChanged(it) }

        if (book == null) return

        val dataList = asyncDiffer.currentList
        val newSelectedItemIndex = dataList.indexOfFirst { listItem ->
            listItem is ListItem.Content<BookItem> && listItem.data.id == book.id
        }
        lastSelectedPosition = if (newSelectedItemIndex >= 0) {
            notifyItemChanged(newSelectedItemIndex)
            newSelectedItemIndex
        } else {
            null
        }

    }

    inner class WriteTodayOnelineBookViewHolder(private val binding : ItemWriteTodayOnelineBookBinding) : ViewHolder(binding.root) {
        private lateinit var bookItem: BookItem

        init {
            binding.root.setOnClickListener{
                onClickBook(bookItem)
            }
        }

        fun bind(bookItem : BookItem) {
            this.bookItem = bookItem

            binding.viewItemWriteTodayOnelineSelectFilter.visibility = if(selectedBook?.id == bookItem.id) View.VISIBLE else View.INVISIBLE

            Glide.with(binding.root.context).load(bookItem.thumbnail).into(binding.imgItemWriteTodayOnelineBookCover)
            binding.labelItemWriteTodayOnelineBookTitle.text = bookItem.title
            binding.labelItemWriteTodayOnelineBookAuthor.text = bookItem.author
        }
    }

    inner class WriteTodayOnelineBookAddViewHolder(binding : ItemWriteTodayOnelineBookAddBinding) : ViewHolder(binding.root) {
        init {
            binding.btnItemWriteTodayOnelineBookAdd.setOnClickListener {
                onClickAdder()
            }
        }
    }

    class WriteTodayOnelineBookLoadingViewHolder(private val binding : ItemWriteTodayOnelineBookLoadingBinding) : ViewHolder(binding.root) {
        fun bind() {
            binding.imgItemWriteTodayOnelineBookCover.startAnimation(AnimationUtils.loadAnimation(binding.root.context, R.anim.place_holder))
            binding.labelItemWriteTodayOnelineBookTitle.startAnimation(AnimationUtils.loadAnimation(binding.root.context, R.anim.place_holder))
            binding.labelItemWriteTodayOnelineBookAuthor.startAnimation(AnimationUtils.loadAnimation(binding.root.context, R.anim.place_holder))
        }
    }
}

// todo 리펙토링 시 위치 변경
class BookItemDiffUtil : DiffUtil.ItemCallback<ListItem<BookItem>>() {
    override fun areItemsTheSame(
        oldItem: ListItem<BookItem>,
        newItem: ListItem<BookItem>
    ): Boolean {

        return if (oldItem is ListItem.ItemAdder && newItem is ListItem.ItemAdder) {
            true
        } else if (oldItem is ListItem.Content<BookItem> && newItem is ListItem.Content<BookItem>) {
            oldItem.data.id == newItem.data.id
        } else {
            false
        }
    }

    override fun areContentsTheSame(
        oldItem: ListItem<BookItem>,
        newItem: ListItem<BookItem>
    ): Boolean {
        return oldItem == newItem
    }
}
