package example.com.database.readers

import kotlinx.serialization.Serializable

@Serializable
class ReadersDTO (
    val readerId: String,
    val name: String,
    val password: String,
    val phone: String,
    val email: String
)