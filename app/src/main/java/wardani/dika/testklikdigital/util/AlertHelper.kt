package wardani.dika.testklikdigital.util

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import wardani.dika.testklikdigital.R

enum class NotificationType {
    ERROR,
    SUCCESS,
    WARNING
}

private fun showNotificationDialog(context: Context, title: String, message: String?, notificationType: NotificationType) {
    val bottomSheetDialog = BottomSheetDialog(context).apply {
        setContentView(R.layout.dialog_error)

        findViewById<TextView>(R.id.textView)?.text = title
        findViewById<TextView>(R.id.messageError)?.text = message
        findViewById<MaterialButton>(R.id.closeBtn)?.setOnClickListener {
            dismiss()
        }

        val drawableId = when(notificationType) {
            NotificationType.ERROR -> R.drawable.ic_baseline_not_interested_24
            NotificationType.SUCCESS -> R.drawable.ic_baseline_check_circle_24
            NotificationType.WARNING -> R.drawable.ic_baseline_warning_24
        }

        val drawable = ContextCompat.getDrawable(context, drawableId)
        findViewById<ImageView>(R.id.typeImg)?.setImageDrawable(drawable)
    }

    bottomSheetDialog.show()
}


fun showWarning(context: Context, message: String?) {
    showNotificationDialog(
            context = context,
            title = "Attention",
            message= message,
            notificationType = NotificationType.WARNING
    )
}

fun showError(context: Context, message: String?) {
    showNotificationDialog(
            context = context,
            title = "Uppss...",
            message = message,
            notificationType = NotificationType.ERROR
    )
}

fun showSuccess(context: Context, message: String?) {
    showNotificationDialog(
            context = context,
            title = "Success",
            message = message,
            notificationType = NotificationType.SUCCESS
    )
}