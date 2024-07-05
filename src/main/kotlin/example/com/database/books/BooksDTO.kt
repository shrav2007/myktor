package example.com.database.books

import kotlinx.serialization.Serializable

@Serializable
class BooksDTO(
    val id: String,
    val nameOfBook: String,
    val author: String,
    val publicationYear: String
)