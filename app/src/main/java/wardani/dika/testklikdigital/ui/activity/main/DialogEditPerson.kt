package wardani.dika.testklikdigital.ui.activity.main

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import wardani.dika.testklikdigital.R
import wardani.dika.testklikdigital.model.Person

class DialogEditPerson(context: Context) : BottomSheetDialog(context) {
    private lateinit var nameEt: EditText
    private lateinit var container: LinearLayout
    private var person: Person? = null

    fun init(action: (person: Person, name: String) -> Unit) {
        setContentView(R.layout.dialog_edit_person)

        container = findViewById(R.id.containerAddPerson)!!
        nameEt = findViewById(R.id.nameEt)!!
        findViewById<MaterialButton>(R.id.saveBtn)?.setOnClickListener {
            val currentPerson = person
            if (currentPerson != null) {
                val name = nameEt.text.toString().trim()
                action(currentPerson, name)
                dismiss()
            }
        }

        findViewById<MaterialButton>(R.id.cancelBtn)?.setOnClickListener {
            dismiss()
        }
    }

    fun setPerson(person: Person) {
        nameEt.setText(person.fullname)
        this.person = person
    }

    companion object {
        private const val TAG = "DialogFormPerson"
    }
}