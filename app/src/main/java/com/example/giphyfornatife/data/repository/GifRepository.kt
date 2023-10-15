package com.example.giphyfornatife.data.repository

import com.example.giphyfornatife.data.api.GiphyApiImpl
import com.example.giphyfornatife.data.database.GifDb
import com.example.giphyfornatife.data.model.ApiResponse
import com.example.giphyfornatife.data.model.GifObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GifRepository @Inject constructor(private val api: GiphyApiImpl, db: GifDb) {
    private val dao = db.gifDao()

    suspend fun getGifs(searchPhrase: String, offset: Int): ApiResponse {
        return api.getGifs(searchPhrase, offset)
    }

    fun getGifIds(): Flow<List<String>> {
        return dao.getAllGifs()
    }

    suspend fun addGifId(gif: GifObject) {
        dao.insertGif(gif)
    }
}