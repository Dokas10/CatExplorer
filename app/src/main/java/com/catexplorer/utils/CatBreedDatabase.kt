package com.catexplorer.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.catexplorer.data.CatInfoTable

@Database(entities = [CatInfoTable::class], version = 1)
abstract class CatBreedDatabase : RoomDatabase(){
    abstract val dao: CatBreedDao
}