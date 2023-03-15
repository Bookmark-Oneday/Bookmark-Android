package com.bookmark.bookmark_oneday.domain.model

data class BookDetail(
    val bookId: Int,
    val title: String,
    val author: String,
    val translators: String,
    val publisher: String,
    val imageUrl: String,
    val currentPage : Int,
    val totalPage : Int,
    val history: List<ReadingHistory>
) {
    val readingPageRatio : Int get() {
        return currentPage * 100 / totalPage
    }

    val firstReadTime : String get() {
        return if (history.isEmpty()) {
            "-"
        } else {
            history[0].dateString
        }
    }

    val totalTime : String get() {
        return if (history.isEmpty()) {
            ""
        } else {
            val totalTime = history.sumOf { it.time }
            String.format("%d:%02d:%02d", totalTime / 3600, (totalTime % 3600) / 60, totalTime % 60)
        }
    }
}