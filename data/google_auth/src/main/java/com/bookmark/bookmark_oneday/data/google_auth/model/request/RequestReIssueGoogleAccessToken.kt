package com.bookmark.bookmark_oneday.data.google_auth.model.request

data class RequestReIssueGoogleAccessToken(
    val client_id : String,
    val client_secret : String,
    val refresh_token : String,
    val grant_type : String = "refresh_token"
)
