package example.com.database.books

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object Books : Table("books") {
    val id = Books.uuid("id")
    val nameOfBook = Books.text("name")
    val author = Books.text("author")
    val publicationYear = Books.text("publication_year")

    fun insert(booksDTO: BooksDTO) {
        transaction {
            Books.insert {
                it[id] = UUID.fromString(booksDTO.id)
                it[nameOfBook] = booksDTO.nameOfBook
                it[author] = booksDTO.author
                it[publicationYear] = booksDTO.publicationYear
            }
        }
    }


    fun fetchBooks(): List<BooksDTO> {
        return try {
            transaction {
                Books.selectAll().toList()
                    .map {
                        BooksDTO(
                            id = it[Books.id].toString(),
                            nameOfBook = it[nameOfBook],
                            author = it[author],
                            publicationYear = it[publicationYear]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
