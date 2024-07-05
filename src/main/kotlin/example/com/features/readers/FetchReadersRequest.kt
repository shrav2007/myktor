package example.com.features.readers

import kotlinx.serialization.Serializable

@Serializable
data class FetchReadersRequest(
    val searchQuery: String
)