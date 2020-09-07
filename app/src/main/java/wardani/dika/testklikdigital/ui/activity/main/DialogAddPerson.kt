package wardani.dika.testklikdigital.ui.activity.main

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import wardani.dika.testklikdigital.R

class DialogAddPerson(context: Context) : BottomSheetDialog(context) {
    private lateinit var nameEt: EditText
    private lateinit var howManyEt: EditText
    private lateinit var saveBtn: MaterialButton
    private lateinit var container: LinearLayout

    fun init(action: (name: String, howMany: Int) -> Unit) {
        setContentView(R.layout.dialog_add_person)

        container = findViewById(R.id.containerAddPerson)!!
        nameEt = findViewById(R.id.nameEt)!!
        howManyEt = findViewById(R.id.howManyEt)!!

        findViewById<MaterialButton>(R.id.saveBtn)?.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val howManyString = howManyEt.text.toString().trim()

            Log.d(TAG, "How many : $howManyString")

            val howMany = try {
                howManyString.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            action(name, howMany)
            dismiss()
        }

        findViewById<MaterialButton>(R.id.cancelBtn)?.setOnClickListener {
            dismiss()
        }

    }

    companion object {
        private const val TAG = "DialogFormPerson"
    }
}