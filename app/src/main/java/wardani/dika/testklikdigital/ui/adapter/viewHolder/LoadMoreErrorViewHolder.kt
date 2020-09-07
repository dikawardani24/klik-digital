package wardani.dika.testklikdigital.ui.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import wardani.dika.testklikdigital.databinding.ItemLoadMoreErrorBinding
import wardani.dika.testklikdigital.ui.adapter.listener.OnRetryLoadMoreListener

class LoadMoreErrorViewHolder(private val binding: ItemLoadMoreErrorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(onRetryLoadMoreListener: OnRetryLoadMoreListener) {
        binding.retryBtn.setOnClickListener {
            onRetryLoadMoreListener.onRetryLoadMore()
        }
    }
}