package wardani.dika.testklikdigital.repository

import android.content.Context
import wardani.dika.testklikdigital.repository.person.PersonRepository
import wardani.dika.testklikdigital.repository.person.PersonRepositoryImpl

object RepositoryFactory {
    fun createPersonRepository(context: Context): PersonRepository {
        return PersonRepositoryImpl(context)
    }
}