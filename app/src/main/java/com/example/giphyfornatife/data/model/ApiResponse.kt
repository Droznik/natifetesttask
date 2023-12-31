package com.example.giphyfornatife.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("data") val gifs: ArrayList<GifObject>,
    @SerializedName("pagination") val pagination: PaginationResponse
)