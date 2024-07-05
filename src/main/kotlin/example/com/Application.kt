package example.com

import example.com.features.books.configureBooksRouting
import example.com.features.login.configureLoginRouting
import example.com.features.readers.configureReadersRouting
import example.com.features.register.configureRegisterRouting
import example.com.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database


fun main() {
    Database.connect(
        "jdbc:postgresql://localhost:5433/ktor",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )

    embeddedServer(CIO, port = 8082, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureLoginRouting()
    configureRegisterRouting()
    configureBooksRouting()
    configureReadersRouting()
}
