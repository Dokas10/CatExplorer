package com.catexplorer.utils

import com.catexplorer.data.CatMainInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("v1/images/search?api_key=live_DZstJjbM6A98rYsdok4CukjUfwenzxZwnLzAVzrmoq4UgMkY7CxPc5asjrDdpHQ1")
    fun getCatBreedList(@Query("limit") limit: Int, @Query("has_breeds") has_breeds: Int): Call<ArrayList<CatMainInfo>>

    @GET("v1/images/{id}")
    fun getCatBreedInfo(@Path("id") id: String?): Call<CatMainInfo>
}