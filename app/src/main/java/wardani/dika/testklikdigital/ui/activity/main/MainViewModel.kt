package wardani.dika.testklikdigital.ui.activity.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wardani.dika.testklikdigital.exception.NoDataException
import wardani.dika.testklikdigital.exception.NoMoreDataException
import wardani.dika.testklikdigital.model.Person
import wardani.dika.testklikdigital.repository.RepositoryResult
import wardani.dika.testklikdigital.repository.person.PersonRepository
import wardani.dika.testklikdigital.ui.LoadingState
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(
    application: Application,
    private val personRepository: PersonRepository
) : AndroidViewModel(application) {
    val personsDataState: LiveData<List<Person>> = MutableLiveData()
    val loadingLiveData: LiveData<LoadingState> = MutableLiveData()
    val loadingMoreLiveData: LiveData<LoadingState> = MutableLiveData()
    val errorLiveData: LiveData<Throwable> = MutableLiveData()
    val successMessage: LiveData<String> = MutableLiveData()

    private val currentData = ArrayList<Person>()
    private var currentPage = 0

    private suspend fun loadDataPersons(loadingLiveData: MutableLiveData<LoadingState>) {
        loadingLiveData.postValue(LoadingState.LOADING)
        Log.d(TAG, "currentPage: $currentPage")

        val errorLiveData = errorLiveData as MutableLiveData

        when(val result = personRepository.getPersons(currentPage)) {
            is RepositoryResult.Success  -> {
                val persons = result.data

                if (persons.isNotEmpty()) {
                    val liveData = personsDataState as MutableLiveData
                    persons.forEach {
                        if (!currentData.contains(it)) {
                            currentData.add(it)
                        }
                    }

                    liveData.postValue(currentData)

                    loadingLiveData.postValue(LoadingState.FINISH)
                } else {
                    val errorMessage = if (currentPage > 0) {
                        currentPage -= 1
                        NoMoreDataException("No more data available for page : $currentPage")
                    } else {
                        NoDataException("No data available ON PAGE $currentPage")
                    }

                    errorLiveData.postValue(errorMessage)
                    loadingLiveData.postValue(LoadingState.NO_DATA)
                }
            }
            is RepositoryResult.Failed -> {
                val error = result.error
                errorLiveData.postValue(error)
                loadingLiveData.postValue(LoadingState.ERROR)
            }
        }
    }

    fun loadData() {
        if (currentPage > 0) {
            reloadPage()
            return
        }

        currentPage = 0
        viewModelScope.launch {
            loadDataPersons(loadingLiveData as MutableLiveData)
        }
    }

    fun reloadPage() {
        viewModelScope.launch {
            loadDataPersons(loadingMoreLiveData as MutableLiveData)
        }
    }

    fun loadMoreData() {
        currentPage += 1
        viewModelScope.launch {
            loadDataPersons(loadingMoreLiveData as MutableLiveData)
        }
    }

    fun delete(person: Person) {
        viewModelScope.launch {

            when (val result = personRepository.delete(person)) {
                is RepositoryResult.Success -> {
                    currentData.remove(person)
                    (personsDataState as MutableLiveData).postValue(currentData)
                    (successMessage as MutableLiveData).postValue("Data deleted successfully")

                    if (currentData.isEmpty()) {
                        (loadingLiveData as MutableLiveData).postValue(LoadingState.NO_DATA)
                    }
                } 
                is RepositoryResult.Failed -> {
                    val errorLiveData = errorLiveData as MutableLiveData
                    errorLiveData.postValue(result.error)
                }
            }
        }
    }

    fun edit(person: Person) {
        viewModelScope.launch {
            when(val result = personRepository.update(person)) {
                is RepositoryResult.Success -> {

                    val index = currentData.indexOf(person)
                    currentData[index] = person
                    (personsDataState as MutableLiveData).postValue(currentData)
                    (successMessage as MutableLiveData).postValue("Data updated successfully")
                }
                is RepositoryResult.Failed -> {
                    val errorLiveData = errorLiveData as MutableLiveData
                    errorLiveData.postValue(result.error)
                }
            }
        }
    }

    fun save(name: String, howMany: Int) {
        viewModelScope.launch {
            val persons = arrayListOf<Person>().apply {
                for (i in 0 until howMany) {
                    val person = Person(
                        id = UUID.randomUUID(),
                        fullname = name,
                        createdAt = Calendar.getInstance().time,
                        updatedAt = null
                    )

                    add(person)
                }
            }

            Log.d(TAG, "totalPerson to add with name $name is ${persons.size}")
            
            when(val result = personRepository.addAll(persons)) {
                is RepositoryResult.Success -> {
                    currentData.addAll(persons)
                    (loadingLiveData as MutableLiveData).postValue(LoadingState.FINISH)
                    (loadingMoreLiveData as MutableLiveData).postValue(LoadingState.NO_DATA)
                    (personsDataState as MutableLiveData).postValue(currentData)
                    (successMessage as MutableLiveData).postValue("Data added successfully")
                }
                is RepositoryResult.Failed -> {
                    val errorLiveData = errorLiveData as MutableLiveData
                    errorLiveData.postValue(result.error)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}