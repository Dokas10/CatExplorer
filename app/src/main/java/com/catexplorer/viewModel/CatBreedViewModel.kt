package com.catexplorer.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.catexplorer.data.CatBreedInfo
import com.catexplorer.data.CatInfoTable
import com.catexplorer.data.CatMainInfo
import com.catexplorer.utils.CatBreedDatabase
import com.catexplorer.utils.RetrofitClientInstance
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatBreedViewModel : ViewModel() {

    private var catListLiveData = MutableLiveData<ArrayList<CatMainInfo>>()
    private var catBreedInfoLiveData = MutableLiveData<CatMainInfo>()
    private var catBreedsLiveData = MutableLiveData<ArrayList<CatBreedInfo>>()
    private var catImagesByBreed = MutableLiveData<ArrayList<CatMainInfo>>()

    private var catInformation = MutableLiveData<ArrayList<CatMainInfo>>()

    //Functions with callbacks to the multiple API endpoints
    fun getCatList(limit : Int, has_breeds: Int) {
        RetrofitClientInstance.apiService.getCatBreedList(limit, has_breeds).enqueue(object  : Callback<ArrayList<CatMainInfo>> {
            override fun onResponse(call: Call<ArrayList<CatMainInfo>>, response: Response<ArrayList<CatMainInfo>>) {
                if (response.body()!=null){
                    catListLiveData.value = response.body()!!
                }
                else{
                    return
                }
            }
            override fun onFailure(call: Call<ArrayList<CatMainInfo>>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }
        })
    }
    fun getCatBreedInfo(id : String?) {
        RetrofitClientInstance.apiService.getCatBreedInfo(id).enqueue(object  : Callback<CatMainInfo> {
            override fun onResponse(call: Call<CatMainInfo>, response: Response<CatMainInfo>) {
                if (response.body() != null){
                    catBreedInfoLiveData.value = response.body()!!
                }
                else{
                    return
                }
            }
            override fun onFailure(call: Call<CatMainInfo>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }
        })
    }

    fun getAllBreeds() {
        RetrofitClientInstance.apiService.getAllCatBreeds().enqueue(object  : Callback<ArrayList<CatBreedInfo>> {
            override fun onResponse(
                call: Call<ArrayList<CatBreedInfo>>,
                response: Response<ArrayList<CatBreedInfo>>
            ) {
                catBreedsLiveData.value = response.body()!!
            }

            override fun onFailure(call: Call<ArrayList<CatBreedInfo>>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }

        })
    }

    fun getImagesByBreed(limit: Int, breed_id: String){
        RetrofitClientInstance.apiService.getCatImagesByBreed(limit, breed_id).enqueue(object: Callback<ArrayList<CatMainInfo>>{
            override fun onResponse(
                call: Call<ArrayList<CatMainInfo>>,
                response: Response<ArrayList<CatMainInfo>>
            ) {
                catImagesByBreed.value = response.body()!!
            }

            override fun onFailure(call: Call<ArrayList<CatMainInfo>>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    //Observable functions
    fun observeCatListLiveData() : LiveData<ArrayList<CatMainInfo>> {
        return catListLiveData
    }
    fun observeCatBreedInfoLiveData() : LiveData<CatMainInfo> {
        return catBreedInfoLiveData
    }

    fun observeCatBreedListLiveData() : LiveData<ArrayList<CatBreedInfo>> {
        return catBreedsLiveData
    }
    fun observeCatImagesByBreedLiveData() : LiveData<ArrayList<CatMainInfo>> {
        return catImagesByBreed
    }

    //Getter and setter of the data that is common to the multiple fragments
    fun setCatInformation(info : ArrayList<CatMainInfo>){
        this.catInformation.value = info
    }

    fun getCatInformation() : LiveData<ArrayList<CatMainInfo>>{
        return catInformation
    }

    //Database functions
    fun insertDataIntoDatabase(dbInstance: CatBreedDatabase, itemToInsert: CatMainInfo){
        viewModelScope.launch {
            val breed = CatInfoTable(
                itemToInsert.id,
                itemToInsert.url,
                itemToInsert.width,
                itemToInsert.height,
                itemToInsert.catBreedInfo[0].id!!,
                itemToInsert.catBreedInfo[0].life_span!!,
                itemToInsert.catBreedInfo[0].name!!,
                itemToInsert.catBreedInfo[0].origin!!,
                itemToInsert.catBreedInfo[0].temperament!!,
                itemToInsert.catBreedInfo[0].description!!,
                itemToInsert.favorite
            )
            dbInstance.dao.insertBreed(breed)
        }
    }

    companion object{
        private const val TAG = "TAG"
    }
}