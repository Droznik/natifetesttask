package com.example.giphyfornatife.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.giphyfornatife.data.model.GifObject

@Database(entities = [GifObject::class], version = 1)
abstract class GifDb : RoomDatabase(){
    abstract fun gifDao(): GifDao
}