package example.com.features.books

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureBooksRouting() {
    routing {
        post("/books/create") {
            val booksController = BooksController(call)
            booksController.addNewBook()
        }
    }
    routing {
        post("/books/search") {
            val booksController = BooksController(call)
            booksController.findBook()
        }
    }
    routing {
        delete("/books") {
            val booksController = BooksController(call)
            booksController.deleteBook()
        }
    }
}