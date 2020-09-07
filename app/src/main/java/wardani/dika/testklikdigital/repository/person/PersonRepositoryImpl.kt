package wardani.dika.testklikdigital.repository.person

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.reflect.TypeToken
import wardani.dika.testklikdigital.exception.NotFoundException
import wardani.dika.testklikdigital.model.Person
import wardani.dika.testklikdigital.repository.RepositoryResult
import wardani.dika.testklikdigital.util.format
import wardani.dika.testklikdigital.util.fromJson
import wardani.dika.testklikdigital.util.toJson

class PersonRepositoryImpl(context: Context) : PersonRepository {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("wardani.dika.testklikdigital", Context.MODE_PRIVATE)
    private val personKey = "Person"
    private val allPersons: ArrayList<Person>
    private val perPage = 10

    init {
        val json = sharedPreferences.getString(personKey, null)

        allPersons = arrayListOf()
        if (json != null) {
            try {
                val type = object : TypeToken<List<Person>>() {}.type
                val persons = type.fromJson<List<Person>>(json)
                allPersons.addAll(persons)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun countMaxPage(): Int {
        val totalData = allPersons.size
        var totalPage: Int = totalData / perPage

        if (totalData % perPage > 0) {
            totalPage += 1
        }

        return totalPage
    }

    private fun findIndexOf(person: Person): Int {
        var foundIndex = -1

        for (i in 0 until allPersons.size) {
            val savedPerson = allPersons[i]
            if (savedPerson.id == person.id) {
                foundIndex = i
                break
            }
        }

        Log.d(TAG, "Data found on index : $foundIndex")
        return foundIndex
    }

    override suspend fun getPersons(pageNumber: Int): RepositoryResult<List<Person>> {
        val totalPage = countMaxPage()

        val foundPersons: List<Person>
        if (pageNumber < totalPage) {
            foundPersons = ArrayList()
            val startPoint = pageNumber * perPage

            for (i in startPoint until startPoint + perPage) {
                try {
                    foundPersons.add(allPersons[i])
                } catch (e: Exception) {
                    break
                }
            }
        } else {
            foundPersons = emptyList()
        }

        return RepositoryResult.Success(foundPersons)
    }

    override suspend fun delete(person: Person): RepositoryResult<Unit> {
        val foundIndex = findIndexOf(person)

        return if (foundIndex > -1) {
            allPersons.removeAt(foundIndex)
            sharedPreferences.edit()
                .putString(personKey, allPersons.toJson())
                .apply()
            RepositoryResult.Success(Unit)
        } else {
            RepositoryResult.Failed(NotFoundException("No data person with name : ${person.fullname} and created at : ${person.createdAt.format()}"))
        }
    }

    override suspend fun add(person: Person): RepositoryResult<Unit> {
        allPersons.add(person)
        val json = allPersons.toJson()
        sharedPreferences.edit()
                .putString(personKey, json)
                .apply()
        return RepositoryResult.Success(Unit)
    }

    override suspend fun addAll(persons: List<Person>): RepositoryResult<Unit> {
        allPersons.addAll(persons)
        val json = allPersons.toJson()

        Log.d(TAG, json)
        sharedPreferences.edit()
                .putString(personKey, json)
                .apply()

        return RepositoryResult.Success(Unit)
    }

    override suspend fun update(person: Person): RepositoryResult<Unit> {
        val foundIndex = findIndexOf(person)

        return if (foundIndex > -1) {
            allPersons[foundIndex] = person
            sharedPreferences.edit()
                .putString(personKey, allPersons.toJson())
                .apply()
            RepositoryResult.Success(Unit)
        } else {
            RepositoryResult.Failed(NotFoundException("No data person with name : ${person.fullname} and created at : ${person.createdAt.format()}"))
        }
    }

    companion object {
        private const val TAG = "PersonRepositoryImpl"
    }
}