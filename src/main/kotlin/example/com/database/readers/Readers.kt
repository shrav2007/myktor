package example.com.database.readers

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object Readers : Table("readers") {
    val readerId = Readers.uuid("id")
    val name = Readers.text("name")
    val password = Readers.text("password")
    val phone = Readers.text("phone")
    val email = Readers.text("email")

    fun insert(readersDTO: ReadersDTO) {
        transaction {
            Readers.insert {
                it[readerId] = UUID.fromString(readersDTO.readerId)
                it[name] = readersDTO.name
                it[password] = readersDTO.password
                it[phone] = readersDTO.phone
                it[email] = readersDTO.email
            }
        }
    }

    fun fetchReaders(): List<ReadersDTO> {
        return try {
            transaction {
                Readers.selectAll().toList()
                    .map {
                        ReadersDTO(
                            readerId = it[readerId].toString(),
                            password = it[password],
                            name = it[name],
                            phone = it[phone],
                            email = it[email]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
