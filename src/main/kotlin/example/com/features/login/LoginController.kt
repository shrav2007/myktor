package example.com.features.login

import example.com.database.tokens.Tokens
import example.com.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUsers(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User  not found")
        } else {
            if (userDTO.password == receive.password) {
                val newToken = UUID.randomUUID().toString()
                transaction {
                    Tokens.update({ Tokens.login eq receive.login }) {
                        it[token] = newToken
                    }
                }
                call.respond(LoginResponseRemote(token = newToken))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}