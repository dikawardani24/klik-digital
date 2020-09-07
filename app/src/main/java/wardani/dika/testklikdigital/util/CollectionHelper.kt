package wardani.dika.testklikdigital.util


fun <T, O> List<T>.transformAsArrayList(mapper: (input: T) -> O): ArrayList<O> {
    val outputs = arrayListOf<O>()
    forEach {
        val output = mapper(it)
        outputs.add(output)
    }
    return outputs
}

fun <T, O> List<T>.transformAsList(mapper: (input: T) -> O): List<O> {
    val outputs = transformAsArrayList(mapper)
    return outputs.toList()
}