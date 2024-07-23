package example.com.features.register

import example.com.database.tokens.TokenDTO
import example.com.database.tokens.Tokens
import example.com.database.users.UserDTO
import example.com.database.users.Users
import example.com.utils.TokenCheck
import example.com.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        if (!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        val userDTO = Users.fetchUsers(registerReceiveRemote.login)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()
            Users.insert(
                UserDTO(
                    login = registerReceiveRemote.login,
                    password = registerReceiveRemote.password,
                    email = registerReceiveRemote.email,
                    username = ""
                )
            )
            Tokens.insert(
                TokenDTO(
                    rowId = UUID.randomUUID().toString(), login = registerReceiveRemote.login,
                    token = token
                )
            )
            call.respond(
                RegisterResponseRemote(
                    login = registerReceiveRemote.login,
                    email = registerReceiveRemote.email,
                    password = registerReceiveRemote.password
                )
            )
        }
    }

    suspend fun deleteUser() {
        val request = call.receive<DeleteUserRequest>()
        val token = call.request.headers["Bearer-Authorization"]
        var deleted = false

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            transaction {
                deleted = Users.deleteWhere { login eq request.login } != 0
            }
            if (deleted) {
                transaction {
                    Tokens.deleteWhere { login eq request.login }
                }
                call.respond(DeleteUserResponse(login = request.login))
            } else {
                call.respond(HttpStatusCode.NotFound, "Юзер с таким ID не найден")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }
}