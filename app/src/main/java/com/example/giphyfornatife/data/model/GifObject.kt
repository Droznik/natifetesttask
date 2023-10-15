package com.example.giphyfornatife.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "gifs")
class GifObject(@Ignore @SerializedName("images") val images: GifImageURL, @PrimaryKey var id: String) {
    constructor() : this(GifImageURL(ImageURL(""), ImageURL("")), "")
}

data class GifImageURL(@SerializedName("fixed_width_downsampled") val downsampledImage: ImageURL,
                       @SerializedName("original") val originalImage: ImageURL)


data class ImageURL(@PrimaryKey @SerializedName("url") val url: String)