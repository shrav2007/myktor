package example.com.features.readers

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureReadersRouting() {
    routing {
        post("/readers/create") {
            val readersController = ReadersController(call)
            readersController.addNewReader()
        }
    }
    routing {
        post("/readers/search") {
            val readersController = ReadersController(call)
            readersController.findReader()
        }
    }
    routing {
        delete("/readers") {
            val readersController = ReadersController(call)
            readersController.deleteReader()
        }
    }
}