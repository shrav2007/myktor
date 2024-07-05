package example.com.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    private val login = Users.text("login")
    private val password = Users.text("password")
    private val username = Users.text("username")
    private val email = Users.text("email")

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[username] = userDTO.username
                it[email] = userDTO.email
            }
        }
    }

    fun fetchUsers(login: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.select { Users.login.eq(login) }.single()
                UserDTO(
                    login = userModel[Users.login],
                    password = userModel[password],
                    username = userModel[username],
                    email = userModel[email]
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}