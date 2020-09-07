package wardani.dika.testklikdigital.ui.adapter.model

@Suppress("CanSealedSubClassBeObject")
sealed class PageData<T> {
    data class Data<T>(val value: T): PageData<T>()
    class Loading<T>: PageData<T>()
    class NoMoreData<T>: PageData<T>()
    class Error<T>: PageData<T>()
}