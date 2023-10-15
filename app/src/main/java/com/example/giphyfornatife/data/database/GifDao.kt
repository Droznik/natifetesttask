package com.example.giphyfornatife.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giphyfornatife.data.model.GifObject
import kotlinx.coroutines.flow.Flow
@Dao
interface GifDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGif(id: GifObject)
    @Query("SELECT * FROM gifs")
    fun getAllGifs(): Flow<List<String>>

}