package wardani.dika.testklikdigital.model

import java.util.*

data class Person(
    var id: UUID,
    var fullname: String,
    var createdAt: Date,
    var updatedAt: Date?
)