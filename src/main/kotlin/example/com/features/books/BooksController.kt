package example.com.features.books

import example.com.database.books.Books
import example.com.database.books.Books.fetchBooks
import example.com.database.books.Books.id
import example.com.database.books.BooksDTO
import example.com.utils.TokenCheck
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class BooksController(private val call: ApplicationCall) {
    suspend fun addNewBook() {
        val request = call.receive<CreateBooksRequest>()
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            if (request.nameOfBook.isBlank()) {
                call.respond(fetchBooks())
            } else {
                val name = (fetchBooks().firstOrNull { it.nameOfBook == request.nameOfBook })
                if (name != null) {
                    call.respond(HttpStatusCode.Conflict, "Book already exists")
                } else {
                    val id = UUID.randomUUID()
                    Books.insert(
                        BooksDTO(
                            id = id.toString(),
                            nameOfBook = request.nameOfBook,
                            author = request.author,
                            publicationYear = request.publicationYear,
                        )
                    )
                    call.respond(BooksResponseRemote(id = id.toString()))
                }
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun findBook() {
        val request = call.receive<FetchBooksRequest>()
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            if (request.searchQuery.isBlank()) {
                call.respond(fetchBooks())
            } else {
                call.respond(fetchBooks().filter { it.nameOfBook.contains(request.searchQuery, ignoreCase = true) })
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun deleteBook() {
        val request = call.receive<FetchBooksRequest>()
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            transaction {
                Books.deleteWhere { nameOfBook eq request.searchQuery }
            }
            call.respond(BooksResponseRemote(id = id.toString()))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }
}

