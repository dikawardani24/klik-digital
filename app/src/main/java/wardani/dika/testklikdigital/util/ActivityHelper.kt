package wardani.dika.testklikdigital.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

fun AppCompatActivity.startActivity(kClass: KClass<*>, block: Intent.() -> Unit = {}) {
    val intent = Intent(this, kClass.java)
    block(intent)
    startActivity(intent)
}

fun AppCompatActivity.showWarning(message: String?) {
    showWarning(this, message)
}
fun AppCompatActivity.showError(message: String?) {
    showError(this, message)
}
fun AppCompatActivity.showSuccess(message: String?) {
    showSuccess(this, message)
}