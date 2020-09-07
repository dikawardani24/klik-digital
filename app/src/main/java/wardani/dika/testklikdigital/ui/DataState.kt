package wardani.dika.testklikdigital.ui

abstract class DataState<T> {
    class Loading<T> : DataState<T>()
    class DataFetched<T>(val data: T): DataState<T>()
    class EmptyDataFetched<T>: DataState<T>()
    class ErrorOccurred<T>(val errorMessage: String): DataState<T>()
}