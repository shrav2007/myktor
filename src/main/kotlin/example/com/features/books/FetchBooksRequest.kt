package example.com.features.books

import kotlinx.serialization.Serializable

@Serializable
data class FetchBooksRequest (
    val searchQuery: String
)