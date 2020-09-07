package wardani.dika.testklikdigital.ui.activity.main

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import wardani.dika.testklikdigital.R
import wardani.dika.testklikdigital.databinding.ActivityMainBinding
import wardani.dika.testklikdigital.model.Person
import wardani.dika.testklikdigital.repository.RepositoryFactory
import wardani.dika.testklikdigital.ui.LoadingState
import wardani.dika.testklikdigital.ui.adapter.ItemPersonAdapter
import wardani.dika.testklikdigital.ui.adapter.listener.OnModificationSelectedListener
import wardani.dika.testklikdigital.ui.adapter.listener.OnRetryLoadMoreListener
import wardani.dika.testklikdigital.ui.listener.ScrollListener
import wardani.dika.testklikdigital.util.showError
import wardani.dika.testklikdigital.util.showSuccess

class MainActivity : AppCompatActivity(), OnRetryLoadMoreListener,
    OnModificationSelectedListener<Person> {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var dialogAddPerson: DialogAddPerson
    private lateinit var dialogEditPerson: DialogEditPerson
    private lateinit var itemPersonAdapter: ItemPersonAdapter
    private lateinit var scrollListener: ScrollListener

    private fun showView(view: View, show: Boolean) {
        val visibility = if (show) {
            View.VISIBLE
        } else {
            View.GONE
        }

        if (view.visibility != visibility) {
            view.visibility = visibility
        }
    }

    private fun showNoDataContainer(show: Boolean) {
        val noDataContainer = binding.noDataContainer.root
        showView(noDataContainer, show)
    }

    private fun showLoadingContainer(show: Boolean) {
        val loadingContainer = binding.loadingDataContainer.root
        showView(loadingContainer, show)
    }

    private fun showDataContainer(show: Boolean) {
        val dataContainer = binding.personsRv
        showView(dataContainer, show)
    }

    private fun handleDataState(it: List<Person>) {
        itemPersonAdapter.changeData(it)
    }

    private fun handleLoadingMore(it: LoadingState) {
        Log.d(TAG, "Load more data state : $it")
        when (it) {
            LoadingState.LOADING -> {
                scrollListener.isLoading = true
                scrollListener.isLastPage = false
                itemPersonAdapter.showLoading()
            }
            LoadingState.FINISH -> {
                scrollListener.isLoading = false
                scrollListener.isLastPage = false
                itemPersonAdapter.closeLoading()
            }
            LoadingState.ERROR -> {
                scrollListener.isLoading = false
                scrollListener.isLastPage = true
                itemPersonAdapter.closeLoading()
                itemPersonAdapter.showError()
            }
            LoadingState.NO_DATA -> {
                scrollListener.isLoading = false
                scrollListener.isLastPage = true
                itemPersonAdapter.closeError()
                itemPersonAdapter.closeLoading()
                itemPersonAdapter.showNoMoreData()
                Log.d(TAG, "SHOW NO MORE DATA")
            }
        }

        itemPersonAdapter.notifyDataSetChanged()
    }

    private fun handleLoadingState(it: LoadingState) {
        Log.d(TAG, "$it")
        when (it) {
            LoadingState.LOADING -> {
                showLoadingContainer(true)
                showDataContainer(false)
                showNoDataContainer(false)
            }
            LoadingState.FINISH -> {
                showLoadingContainer(false)
                showNoDataContainer(false)
                showDataContainer(true)
            }
            LoadingState.NO_DATA -> {
                showLoadingContainer(false)
                showNoDataContainer(true)
                showDataContainer(false)
            }
            LoadingState.ERROR -> {
                showLoadingContainer(false)
                showNoDataContainer(true)
                showDataContainer(false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = MainViewModel(
            application = application,
            personRepository = RepositoryFactory.createPersonRepository(this)
        )

        dialogAddPerson = DialogAddPerson(this)
        dialogAddPerson.init {name, howMany ->
            viewModel.save(name, howMany)
        }

        dialogEditPerson = DialogEditPerson(this)
        dialogEditPerson.init { person, name ->
            person.fullname = name
            viewModel.edit(person)
        }

        itemPersonAdapter = ItemPersonAdapter().apply {
            onRetryLoadMoreListener = this@MainActivity
            onModificationSelectedListener = this@MainActivity
        }
        binding.run {
            personsRv.run {
                val linearLayoutManager = LinearLayoutManager(this@MainActivity)
                adapter = itemPersonAdapter
                layoutManager = linearLayoutManager
                scrollListener =
                    ScrollListener(linearLayoutManager, object : ScrollListener.OnLoadMoreListener {
                        override fun onLoadMoreItems() {
                            viewModel.loadMoreData()
                        }
                    })

                addOnScrollListener(scrollListener)
            }

            addPersonFab.setOnClickListener {
                dialogAddPerson.show()
            }
        }

        viewModel.loadingLiveData.observe(this) {
            handleLoadingState(it)
        }

        viewModel.loadingMoreLiveData.observe(this) {
            handleLoadingMore(it)
        }

        viewModel.personsDataState.observe(this) {
            handleDataState(it)
        }

        viewModel.errorLiveData.observe(this) {
            if (it is UnknownError || it is Resources.NotFoundException) {
                showError(it.message)
            }
        }

        viewModel.successMessage.observe(this) {
            showSuccess(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }

    override fun onRetryLoadMore() {
        viewModel.reloadPage()
    }

    override fun onDeleteSelectedItem(item: Person) {
        viewModel.delete(item)
    }

    override fun onEditSelectedItem(item: Person) {
        dialogEditPerson.setPerson(item)
        dialogEditPerson.show()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}