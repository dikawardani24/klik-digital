package wardani.dika.testklikdigital.util

import java.text.SimpleDateFormat
import java.util.*

const val DATE_PATTERN = "dd-mm-yyyy hh:mm:ss"

fun Date.format(pattern: String): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}

fun Date.format(): String {
    return format(DATE_PATTERN)
}

fun String.toDate(pattern: String): Date? {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.parse(this)
}

fun String.toDate(): Date? {
    return toDate(DATE_PATTERN)
}
