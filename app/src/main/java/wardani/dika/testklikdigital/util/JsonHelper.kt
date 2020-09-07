package wardani.dika.testklikdigital.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

val GSON: Gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .disableHtmlEscaping()
    .create()

fun <T> Class<T>.fromJson(json: String): T {
    return GSON.fromJson(json, this)
}

fun <T: Any> java.lang.reflect.Type.fromJson(json: String): T {
    return GSON.fromJson(json, this)
}

fun <T: Any> T.toJson(): String {
    return GSON.toJson(this, this::class.java)
}
