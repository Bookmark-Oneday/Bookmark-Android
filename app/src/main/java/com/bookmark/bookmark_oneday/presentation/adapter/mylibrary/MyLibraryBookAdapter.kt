package com.bookmark.bookmark_oneday.presentation.adapter.mylibrary

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ItemMylibraryBookAddBinding
import com.bookmark.bookmark_oneday.databinding.ItemMylibraryBookLoadingBinding
import com.bookmark.bookmark_oneday.databinding.ItemMylibraryBookNormalBinding
import com.bookmark.bookmark_oneday.domain.book.model.MyLibraryItem
import com.bumptech.glide.Glide

class MyLibraryBookAdapter(
    private val onClickBook : (String) -> Unit,
    private val onClickAdder : () -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {

    private val asyncDiffer = AsyncListDiffer(this, MyLibraryItemDiffUtil())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MyLibraryItem.BOOK_ADDER -> {
                val binding = ItemMylibraryBookAddBinding.inflate(inflater, parent, false)
                MyLibraryBookAdderViewHolder(binding, onClickAdder)
            }
            MyLibraryItem.BOOK_NORMAL -> {
                val binding = ItemMylibraryBookNormalBinding.inflate(inflater, parent, false)
                MyLibraryBookNormalViewHolder(binding, onClickBook)
            }
            MyLibraryItem.BOOK_LOADING -> {
                val binding = ItemMylibraryBookLoadingBinding.inflate(inflater, parent, false)
                MyLibraryBookLoadingViewHolder(binding)
            }
            else -> {
                val binding = ItemMylibraryBookLoadingBinding.inflate(inflater, parent, false)
                MyLibraryBookLoadingViewHolder(binding)
            }
        }

    }

    override fun getItemCount(): Int = asyncDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is MyLibraryBookAdderViewHolder -> {
                holder.bind()
            }
            is MyLibraryBookNormalViewHolder -> {
                holder.bind(asyncDiffer.currentList[position] as MyLibraryItem.Book)
            }
            is MyLibraryBookLoadingViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return asyncDiffer.currentList[position].viewType
    }

    fun updateList(list : List<MyLibraryItem>) {
         asyncDiffer.submitList(list)
    }

}

class MyLibraryBookNormalViewHolder(
    private val binding : ItemMylibraryBookNormalBinding,
    private val onClick : (String) -> Unit
) : ViewHolder(binding.root) {
    private lateinit var currentBook : MyLibraryItem.Book

    init {
        binding.root.setOnClickListener {
            onClick(currentBook.id)
        }
    }

    fun bind(book : MyLibraryItem.Book) {
        currentBook = book
        binding.labelItemMylibraryBookTitle.text = book.title
        binding.labelItemMylibraryBookAuthor.text = book.author
        Glide.with(binding.root.context).load(book.thumbnail).into(binding.imgItemMylibraryBookThumbnail)

        setLikeButtonDrawable(book.favorite, book.reading)
        binding.imgItemMylibraryBookmark.visibility = if (book.reading) View.VISIBLE else View.INVISIBLE
    }

    private fun setLikeButtonDrawable(favorite : Boolean, reading : Boolean) {
        if (!favorite) {
            binding.imgItemMylibraryLike.visibility = View.INVISIBLE
        } else {
            binding.imgItemMylibraryLike.visibility = View.VISIBLE
            val tintColor = if (reading) {
                R.color.white
            } else {
                R.color.orange
            }
            binding.imgItemMylibraryLike.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, tintColor))
        }
    }
}

class MyLibraryBookAdderViewHolder(
    binding : ItemMylibraryBookAddBinding,
    private val onClick : () -> Unit
) : ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClick()
        }
    }

    fun bind() {

    }
}

class MyLibraryBookLoadingViewHolder(
    private val binding : ItemMylibraryBookLoadingBinding
) : ViewHolder(binding.root) {
    fun bind() {
        binding.cvItemMylibraryBook.startAnimation(AnimationUtils.loadAnimation(binding.root.context, R.anim.place_holder))
        binding.labelItemMylibraryBookAuthor.startAnimation(AnimationUtils.loadAnimation(binding.root.context, R.anim.place_holder))
        binding.labelItemMylibraryBookTitle.startAnimation(AnimationUtils.loadAnimation(binding.root.context, R.anim.place_holder))
    }
}

class MyLibraryItemDiffUtil : DiffUtil.ItemCallback<MyLibraryItem>() {
    override fun areItemsTheSame(oldItem: MyLibraryItem, newItem: MyLibraryItem): Boolean {
        return if (oldItem is MyLibraryItem.BookAdder && newItem is MyLibraryItem.BookAdder) {
            true
        } else if (oldItem is MyLibraryItem.Book && newItem is MyLibraryItem.Book) {
            oldItem.id == newItem.id
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItem: MyLibraryItem, newItem: MyLibraryItem): Boolean {
        return oldItem == newItem
    }
}
