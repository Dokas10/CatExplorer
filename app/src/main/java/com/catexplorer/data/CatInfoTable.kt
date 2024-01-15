package com.catexplorer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CatInfoTable(
    @ColumnInfo
    @PrimaryKey
    var id: String,
    @ColumnInfo
    var url: String,
    @ColumnInfo
    var width: Int,
    @ColumnInfo
    var height: Int,
    @ColumnInfo
    var lifespan: String,
    @ColumnInfo
    var name: String,
    @ColumnInfo
    var origin: String,
    @ColumnInfo
    var temperament: String,
    @ColumnInfo
    var description: String,
    @ColumnInfo
    var isFavorite: Boolean
)