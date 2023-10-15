package com.example.giphyfornatife.data.api

import com.example.giphyfornatife.data.model.ApiResponse
import javax.inject.Inject

class GiphyApiImpl @Inject constructor(private val apiService: GiphyApi) {
    suspend fun getGifs(searchPhrase: String, offset: Int): ApiResponse = apiService.getGifs(searchPhrase, offset)
}