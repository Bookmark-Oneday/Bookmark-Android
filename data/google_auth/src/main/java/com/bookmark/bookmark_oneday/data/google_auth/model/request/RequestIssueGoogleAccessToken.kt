package com.bookmark.bookmark_oneday.data.google_auth.model.request

data class RequestIssueGoogleAccessToken(
    val code : String,
    val redirect_uri : String,
    val client_id : String,
    val client_secret : String,
    val grant_type : String = "authorization_code"
)