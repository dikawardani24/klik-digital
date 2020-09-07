package wardani.dika.testklikdigital.repository

sealed class RepositoryResult<T> {
    data class Success<T>(val data: T): RepositoryResult<T>()
    data class Failed<T>(val error: Throwable): RepositoryResult<T>()
}