package tech.cognix.sauntatonttu.response

data class BaseListResponse<T>(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<T>
)