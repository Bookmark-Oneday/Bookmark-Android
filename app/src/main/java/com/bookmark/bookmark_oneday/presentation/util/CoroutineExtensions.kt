package com.bookmark.bookmark_oneday.presentation.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

inline fun <T> Flow<T>.collectLatestInLifecycle(
    owner : LifecycleOwner,
    state : Lifecycle.State = Lifecycle.State.STARTED,
    context : CoroutineContext = Dispatchers.Default,
    crossinline block : suspend CoroutineScope.(T) -> Unit
) {
    owner.lifecycleScope.launch(context) {
        owner.repeatOnLifecycle(state) {
            withContext(Dispatchers.Main) {
                collectLatest { block(it) }
            }
        }
    }
}