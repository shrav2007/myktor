package example.com.features.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReceiveRemote (
    val login: String,
    val email: String,
    val password: String
)
@Serializable
data class RegisterResponseRemote (
    val login: String,
    val email: String,
    val password: String
)
