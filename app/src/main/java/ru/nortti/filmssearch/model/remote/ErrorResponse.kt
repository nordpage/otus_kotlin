package ru.nortti.filmssearch.model.remote

data class ErrorResponse(
    val status_code: Int,
    val status_message: String
)