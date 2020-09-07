package wardani.dika.testklikdigital.repository.person

import wardani.dika.testklikdigital.model.Person
import wardani.dika.testklikdigital.repository.RepositoryResult

interface PersonRepository {
    suspend fun getPersons(pageNumber: Int): RepositoryResult<List<Person>>
    suspend fun delete(person: Person): RepositoryResult<Unit>
    suspend fun add(person: Person): RepositoryResult<Unit>
    suspend fun addAll(persons: List<Person>): RepositoryResult<Unit>
    suspend fun update(person: Person): RepositoryResult<Unit>
}