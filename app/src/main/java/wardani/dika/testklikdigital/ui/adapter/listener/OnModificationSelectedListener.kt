package wardani.dika.testklikdigital.ui.adapter.listener

interface OnModificationSelectedListener<T> {
    fun onDeleteSelectedItem(item: T)
    fun onEditSelectedItem(item: T)
}
