package wardani.dika.testklikdigital.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import wardani.dika.testklikdigital.R
import wardani.dika.testklikdigital.databinding.ItemLoadMoreDataBinding
import wardani.dika.testklikdigital.databinding.ItemLoadMoreErrorBinding
import wardani.dika.testklikdigital.databinding.ItemNoMoreDataBinding
import wardani.dika.testklikdigital.ui.adapter.constant.ViewType
import wardani.dika.testklikdigital.ui.adapter.listener.OnRetryLoadMoreListener
import wardani.dika.testklikdigital.ui.adapter.model.PageData
import wardani.dika.testklikdigital.ui.adapter.viewHolder.LoadMoreErrorViewHolder
import wardani.dika.testklikdigital.ui.adapter.viewHolder.LoadingViewHolder
import wardani.dika.testklikdigital.ui.adapter.viewHolder.NoMoreDataViewHolder
import wardani.dika.testklikdigital.util.transformAsArrayList

abstract class PaginationAdapter<T>: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataAdapters: ArrayList<PageData<T>> = arrayListOf()
    var onRetryLoadMoreListener: OnRetryLoadMoreListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val pageViewType = ViewType.toPageViewType(viewType)
        Log.d(TAG, "Type $pageViewType")

        return when(pageViewType) {
            ViewType.DATA -> onCreateViewHolderData(inflater, parent)
            ViewType.LOADING -> {
                val binding: ItemLoadMoreDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_load_more_data, parent, false)
                LoadingViewHolder(binding)
            }
            ViewType.NO_MORE_DATA -> {
                val binding: ItemNoMoreDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_no_more_data, parent, false)
                NoMoreDataViewHolder(binding)
            }
            ViewType.ERROR_OCCURED -> {
                val binding: ItemLoadMoreErrorBinding = DataBindingUtil.inflate(inflater, R.layout.item_load_more_error, parent, false)
                LoadMoreErrorViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataAdapter = dataAdapters[position]
        if (dataAdapter is  PageData.Data<T>) {
            onBindViewHolder(holder, dataAdapter.value)
        } else if (dataAdapter is PageData.Error<T> && holder is LoadMoreErrorViewHolder) {
            onRetryLoadMoreListener?.run {
                holder.bind(this)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataAdapters.size
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = when(dataAdapters[position]) {
            is PageData.Data -> ViewType.DATA
            is PageData.Loading -> ViewType.LOADING
            is PageData.NoMoreData -> ViewType.NO_MORE_DATA
            is PageData.Error -> ViewType.ERROR_OCCURED
        }

        return viewType.value
    }

    protected abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, value: T)

    protected abstract fun onCreateViewHolderData(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder

    fun showLoading() {
        dataAdapters.add(PageData.Loading())
        notifyItemInserted(dataAdapters.size -1)
    }

    fun closeLoading() {
        var positionToRemove: Int = -1

        for(i in 0 until dataAdapters.size) {
            if (dataAdapters[i] is PageData.Loading<T>) {
                positionToRemove = i
                break
            }
        }

        if (positionToRemove > -1) {
            dataAdapters.remove(dataAdapters[positionToRemove])
            notifyItemRemoved(positionToRemove)
        }
    }

    fun showNoMoreData() {
        Log.d(TAG, "before : ${dataAdapters.size}")
        dataAdapters.add(PageData.NoMoreData())
        notifyItemInserted(dataAdapters.size -1)
        Log.d(TAG, "after : ${dataAdapters.size}")
    }

    fun closeNoMoreData() {
        val lastPosition = dataAdapters.size -1
        val typeWrapper = dataAdapters[lastPosition]

        if (typeWrapper is PageData.NoMoreData<T>) {
            dataAdapters.remove(typeWrapper)
            notifyItemRemoved(dataAdapters.size)
        }
    }

    fun showError() {
        dataAdapters.add(PageData.Error())
        notifyItemInserted(dataAdapters.size -1)
    }

    fun closeError() {
        var positionToRemove: Int = -1

        for(i in 0 until dataAdapters.size) {
            if (dataAdapters[i] is PageData.Error<T>) {
                positionToRemove = i
                break
            }
        }

        if (positionToRemove > -1) {
            dataAdapters.remove(dataAdapters[positionToRemove])
            notifyItemRemoved(positionToRemove)
        }
    }

    fun clear() {
        dataAdapters.clear()
        notifyDataSetChanged()
    }

    fun changeData(newListData: List<T>) {
        dataAdapters = newListData.transformAsArrayList {
            PageData.Data(it)
        }
        notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "PaginationAdapter"
    }

}