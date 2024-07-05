package example.com.features.books

import kotlinx.serialization.Serializable

@Serializable
data class CreateBooksRequest(
    val nameOfBook: String,
    val author: String,
    val publicationYear: String
)
