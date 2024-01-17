package com.catexplorer.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CatMainInfo(
    @SerializedName("id")
    var id: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("width")
    var width: Int,
    @SerializedName("height")
    var height: Int,
    @SerializedName("breeds")
    var catBreedInfo: List<CatBreedInfo>,
    var favorite: Boolean = false
): Serializable
