package com.example.giphyfornatife.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.giphyfornatife.R
import com.example.giphyfornatife.data.model.GifObject
import com.example.giphyfornatife.databinding.ActivityMainBinding
import com.example.giphyfornatife.ui.HomeViewModel
import com.example.giphyfornatife.util.PaginationScrollListener
import dagger.hilt.android.AndroidEntryPoint

const val ITEMS_PER_PAGE_LIMIT = 24

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: GifsGridAdapter
    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false
    private var currentSearchPhrase = "dog"
    private var totalCountOfGifs = 0L

    private lateinit var searchView: SearchView
    private lateinit var searchQueryTextListener: SearchView.OnQueryTextListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadData(currentSearchPhrase, 0, false)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchQueryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean = true

            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query.isNullOrBlank())
                    loadData(query, 0, true)
                return true
            }
        }
        searchView.setOnQueryTextListener(searchQueryTextListener)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> return true
        }
        searchView.setOnQueryTextListener(searchQueryTextListener)
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(searchPhrase: String, offset: Int, isNewSearchData:Boolean){
        viewModel.getGifs(searchPhrase, offset).observe(this, Observer {
            it?.let { resource ->
                resource.let { response ->
                    totalCountOfGifs = response.pagination.count
                    addToList(response.gifs, isNewSearchData)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if(adapter.getItemViewType(position) == adapter.FOOTER_VIEW)
                    return 2
                return 1
            }
        }
        binding.rvGrid.layoutManager = layoutManager
        binding.rvGrid.addOnScrollListener(object: PaginationScrollListener(layoutManager){
            override fun loadMoreItems() {
                isLoading = true
                loadData(currentSearchPhrase, currentPage * ITEMS_PER_PAGE_LIMIT, false)
                currentPage+=1
            }

            override fun isLoading(): Boolean = isLoading

            override fun isLastPage(): Boolean = isLastPage
        })

        adapter = GifsGridAdapter(arrayListOf()) { gif -> viewModel.addGifToDeleted(gif) }
        binding.rvGrid.adapter = adapter
    }

    private fun addToList(gifs: List<GifObject>, isNewSearchData: Boolean) {
        adapter.apply{
            removeLoadingFooter()

            if(isNewSearchData) {
                notifyItemRangeRemoved(0, getCurrentNumOfGifs())
                addNewGifs(gifs)
            } else addNextGifs(gifs)

            if(getCurrentNumOfGifs() < totalCountOfGifs){
                isLastPage = false
                addLoadingFooter()
            } else isLastPage = true

            notifyItemRangeInserted(itemCount, gifs.size - 1)
            isLoading = false
        }
    }
}