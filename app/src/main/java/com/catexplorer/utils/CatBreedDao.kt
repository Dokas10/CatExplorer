package com.catexplorer.utils

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.catexplorer.data.CatInfoTable

@Dao
interface CatBreedDao {
    @Upsert
    suspend fun insertBreed(breed: CatInfoTable)
    @Query("SELECT * FROM CatInfoTable WHERE isFavorite=1")
    suspend fun getFavoriteBreeds(): MutableList<CatInfoTable>
    @Query("SELECT * FROM CatInfoTable")
    suspend fun getAllBreedsInDatabase(): List<CatInfoTable>

    @Query("SELECT * FROM CatInfoTable WHERE name LIKE :name")
    suspend fun getBreedsInDatabaseByName(name : String): List<CatInfoTable>

    @Query("SELECT * FROM CatInfoTable WHERE id LIKE :id")
    suspend fun getBreedsInDatabaseById(id : String): CatInfoTable
}