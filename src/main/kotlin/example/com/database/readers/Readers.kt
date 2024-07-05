package example.com.database.readers

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Readers : Table("readers") {
    private val id = Readers.text("id")
    private val name = Readers.text("name")
    private val password = Readers.text("password")
    private val phone = Readers.text("phone")
    private val email = Readers.text("email")

    fun insert(readersDTO: ReadersDTO) {
        transaction {
            Readers.insert {
                it[id] = readersDTO.id
                it[name] = readersDTO.name
                it[password] = readersDTO.password
                it[phone] = readersDTO.phone
                it[email] = readersDTO.email
            }
        }
    }

    fun fetchReader(login: String): ReadersDTO? {
        return try {
            transaction {
                val readerModel = Readers.select { Readers.name.eq(login) }.single()
                ReadersDTO(
                    id = readerModel[Readers.id],
                    password = readerModel[password],
                    name = readerModel[name],
                    phone = readerModel[phone],
                    email = readerModel[email]
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}
