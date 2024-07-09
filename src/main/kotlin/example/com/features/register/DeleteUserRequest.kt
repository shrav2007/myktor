package example.com.features.register

import kotlinx.serialization.Serializable

@Serializable
data class DeleteUserRequest(
    val login: String
)
@Serializable
data class DeleteUserResponse (
    val login: String
)