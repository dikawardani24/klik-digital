package wardani.dika.testklikdigital.util

inline fun <reified T> tryCast(instance: Any?, block: T.() -> Unit) {
    if (instance is T) {
        block(instance)
    }
}