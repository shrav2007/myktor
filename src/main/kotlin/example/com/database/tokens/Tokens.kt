package example.com.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens : Table("tokens") {
    private val id = Tokens.text("id")
    private val login = Tokens.text("login")
    private val token = Tokens.text("token")

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.rowId
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token

            }
        }
    }

    fun fetchTokens(): List<TokenDTO> {
        return try {
            transaction {
                Tokens.selectAll().toList()
                    .map {
                        TokenDTO(
                            rowId = it[Tokens.id],
                            token = it[Tokens.token],
                            login = it[Tokens.login]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}