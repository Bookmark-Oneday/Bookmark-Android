package com.bookmark.bookmark_oneday.presentation.screens.timer.model

import com.bookmark.bookmark_oneday.domain.model.ReadingInfo

sealed class TimerViewEvent {
    object ApiResponseLoading : TimerViewEvent()
    class ReadingInfoLoadSuccess(val readingInfo: ReadingInfo) : TimerViewEvent()
    object ReadingInfoLoadFail : TimerViewEvent()
    object RecordFail : TimerViewEvent()
    class RecordSuccess(val readingInfo: ReadingInfo) : TimerViewEvent()
    class ToggleTotalButton(val total : Boolean) : TimerViewEvent()
    class TogglePlayButton(val playing : Boolean) : TimerViewEvent()
    class UpdateReadingInfo(val readingInfo : ReadingInfo) : TimerViewEvent()
    class UpdateTimer(val time : Int) : TimerViewEvent()
}
