package example.com.utils

import example.com.database.tokens.Tokens

object TokenCheck {
    fun isTokenValid(token: String): Boolean = Tokens.fetchTokens().any { it.token == token }
}