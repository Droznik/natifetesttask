package com.example.giphyfornatife.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.giphyfornatife.data.model.ApiResponse
import com.example.giphyfornatife.data.model.GifObject
import com.example.giphyfornatife.data.model.PaginationResponse
import com.example.giphyfornatife.data.repository.GifRepository
import com.example.giphyfornatife.util.CombinedLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: GifRepository) : ViewModel() {

    fun getGifs(searchPhrase: String, offset: Int) = CombinedLiveData(
        getGifsFromApi(searchPhrase, offset),
        deletedLiveData)
    { data1, data2 -> filterData(data1, data2) }

    private fun filterData(resource: ApiResponse?, itemsToDelete: List<String>?): ApiResponse? {
        return if (resource != null  && itemsToDelete != null) {
            var itemsDeleted = 0
            val filteredGifs = ArrayList<GifObject>()
            for (item in resource.gifs) {
                if(!itemsToDelete.contains(item.id)) {
                    filteredGifs.add(item)
                } else itemsDeleted++

            }

            ApiResponse(filteredGifs, PaginationResponse(resource.pagination.count - itemsDeleted))
        } else resource
    }

    private val deletedLiveData: LiveData<List<String>> = repository.getGifIds().asLiveData()

    private fun getGifsFromApi(searchPhrase: String, offset: Int): LiveData<ApiResponse> = liveData(
        Dispatchers.IO){
        emit(repository.getGifs(searchPhrase, offset))
    }

    fun addGifToDeleted(gif: GifObject) {
        viewModelScope.launch {
            repository.addGifId(gif)
        }

    }
}