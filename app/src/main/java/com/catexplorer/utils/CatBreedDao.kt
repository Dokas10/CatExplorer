package com.catexplorer.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.catexplorer.data.CatInfoTable

@Dao
interface CatBreedDao {
    @Upsert
    suspend fun insertBreed(breed: CatInfoTable)
    //@Query("SELECT * FROM CatInfoTable")
    //suspend fun getCatBreeds(): LiveData<ArrayList<CatInfoTable>>
}