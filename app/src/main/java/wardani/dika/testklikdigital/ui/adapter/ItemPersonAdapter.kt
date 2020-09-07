package wardani.dika.testklikdigital.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import wardani.dika.testklikdigital.R
import wardani.dika.testklikdigital.databinding.ItemPersonBinding
import wardani.dika.testklikdigital.model.Person
import wardani.dika.testklikdigital.ui.adapter.listener.OnModificationSelectedListener
import wardani.dika.testklikdigital.ui.adapter.viewHolder.PersonViewHolder

class ItemPersonAdapter: PaginationAdapter<Person>() {
    var onModificationSelectedListener: OnModificationSelectedListener<Person>? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, value: Person) {
        if (holder is PersonViewHolder) {
            holder.bind(value, onModificationSelectedListener)
        }
    }

    override fun onCreateViewHolderData(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        val binding: ItemPersonBinding = DataBindingUtil.inflate(inflater, R.layout.item_person, parent, false)
        return PersonViewHolder(binding, inflater.context)
    }

}

