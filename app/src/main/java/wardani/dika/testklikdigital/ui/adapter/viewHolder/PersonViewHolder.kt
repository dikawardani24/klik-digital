package wardani.dika.testklikdigital.ui.adapter.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import wardani.dika.testklikdigital.R
import wardani.dika.testklikdigital.ui.adapter.ItemPersonAdapter
import wardani.dika.testklikdigital.databinding.ItemPersonBinding
import wardani.dika.testklikdigital.model.Person
import wardani.dika.testklikdigital.ui.adapter.listener.OnModificationSelectedListener
import wardani.dika.testklikdigital.util.PopupMenuHelper
import wardani.dika.testklikdigital.util.format

class PersonViewHolder(
    private val binding: ItemPersonBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(person: Person, onModificationSelectedListener: OnModificationSelectedListener<Person>?) {
        binding.run {
            nameTv.text = person.fullname
            uuidTv.text = person.id.toString()
            createdAtTv.text = person.createdAt.format()
            updatedAtTv.text = person.updatedAt?.format() ?: "-"

            onModificationSelectedListener?.run {
                root.setOnClickListener {
                    PopupMenuHelper.showPopupMenuWithIcon(context, optionTv, R.menu.menu_modif_person, object : PopupMenuHelper.PopMenuItemSelectedListener {
                        override fun onSelectedMenuId(selectedMenu: PopupMenuHelper.Menu) {
                            when(selectedMenu.id) {
                                R.id.editPerson -> onEditSelectedItem(person)
                                R.id.deletePerson -> onDeleteSelectedItem(person)
                            }
                        }
                    })
                }
            }
        }
    }

}