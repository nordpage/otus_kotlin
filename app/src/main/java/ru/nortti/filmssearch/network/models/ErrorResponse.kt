package ru.nortti.filmssearch.network.models

data class ErrorResponse(
    val status_code: Int,
    val status_message: String
)