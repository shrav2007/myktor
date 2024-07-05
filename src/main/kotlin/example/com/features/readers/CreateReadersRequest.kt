package example.com.features.readers

import kotlinx.serialization.Serializable

@Serializable
data class CreateReadersRequest(
    val name: String,
    val password: String,
    val phone: String,
    val email: String
)
