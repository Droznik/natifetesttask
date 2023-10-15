package com.example.giphyfornatife.data.api

import com.example.giphyfornatife.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET(value = "gifs/search")
    suspend fun getGifs(
        @Query("q") searchPhrase: String,
        @Query("offset") offset: Int
    ): ApiResponse
}