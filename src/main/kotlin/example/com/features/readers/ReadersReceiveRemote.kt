package example.com.features.readers

import kotlinx.serialization.Serializable

@Serializable
data class ReadersReceiveRemote(
    val name: String,
    val password: String,
    val phone: String,
    val email: String
)

@Serializable
data class ReadersResponseRemote(
    val readerId: String
)
