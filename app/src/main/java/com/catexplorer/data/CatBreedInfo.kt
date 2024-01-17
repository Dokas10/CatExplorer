package com.catexplorer.data

import com.google.gson.annotations.SerializedName

data class CatBreedInfo(
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("temperament")
    var temperament: String?,
    @SerializedName("origin")
    var origin: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("life_span")
    var life_span: String?,
)