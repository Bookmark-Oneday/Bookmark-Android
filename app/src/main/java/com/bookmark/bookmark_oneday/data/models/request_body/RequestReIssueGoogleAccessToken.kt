package com.bookmark.bookmark_oneday.data.models.request_body

data class RequestReIssueGoogleAccessToken(
    val client_id : String,
    val client_secret : String,
    val refresh_token : String,
    val grant_type : String = "refresh_token"
)
