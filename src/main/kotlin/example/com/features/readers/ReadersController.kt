package example.com.features.readers

import example.com.database.readers.Readers
import example.com.database.readers.Readers.fetchReaders
import example.com.database.readers.Readers.readerId
import example.com.database.readers.ReadersDTO
import example.com.utils.TokenCheck
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ReadersController(private val call: ApplicationCall) {
    suspend fun addNewReader() {
        val request = call.receive<CreateReadersRequest>()
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            if (request.name.isBlank()) {
                call.respond(fetchReaders())
            } else {
                val name = (fetchReaders().firstOrNull { it.phone == request.phone })
                if (name != null) {
                    call.respond(HttpStatusCode.Conflict, "Reader already exists")
                } else {
                    val readerId = UUID.randomUUID()
                    Readers.insert(
                        ReadersDTO(
                            readerId = readerId.toString(),
                            name = request.name,
                            password = request.password,
                            phone = request.phone,
                            email = request.email
                        )
                    )
                    call.respond(ReadersResponseRemote(readerId = readerId.toString()))
                }
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun findReader() {
        val request = call.receive<FetchReadersRequest>()
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            if (request.searchQuery.isBlank()) {
                call.respond(fetchReaders())
            } else {
                call.respond(fetchReaders().filter { it.name.contains(request.searchQuery, ignoreCase = true) })
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun deleteReader() {
        val request = call.receive<FetchReadersRequest>()
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            transaction {
                Readers.deleteWhere { readerId eq UUID.fromString(request.searchQuery) }
            }
            call.respond(ReadersResponseRemote(readerId = readerId.toString()))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }
}

