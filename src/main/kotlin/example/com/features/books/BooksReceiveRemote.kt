package example.com.features.books

import kotlinx.serialization.Serializable

@Serializable
data class BooksReceiveRemote(
    val nameOfBook: String,
    val author: String,
    val publicationYear: String,
)

@Serializable
data class BooksResponseRemote(
    val id: String
)
