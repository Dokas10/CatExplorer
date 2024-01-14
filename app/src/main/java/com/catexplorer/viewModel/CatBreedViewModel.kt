package com.catexplorer.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catexplorer.data.CatMainInfo
import com.catexplorer.utils.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatBreedViewModel : ViewModel() {

    private var catListLiveData = MutableLiveData<ArrayList<CatMainInfo>>()
    private var catBreedInfoLiveData = MutableLiveData<CatMainInfo>()

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
                Log.d("TAG",t.message.toString())
            }
        })
    }
    fun getCatBreedInfo(id : String?) {
        RetrofitClientInstance.apiService.getCatBreedInfo(id).enqueue(object  : Callback<CatMainInfo> {
            override fun onResponse(call: Call<CatMainInfo>, response: Response<CatMainInfo>) {
                if (response.body()!=null){
                    catBreedInfoLiveData.value = response.body()!!
                }
                else{
                    return
                }
            }
            override fun onFailure(call: Call<CatMainInfo>, t: Throwable) {
                Log.d("TAG",t.message.toString())
            }
        })
    }
    fun observeCatListLiveData() : LiveData<ArrayList<CatMainInfo>> {
        return catListLiveData
    }
    fun observeCatBreedInfoLiveData() : LiveData<CatMainInfo> {
        return catBreedInfoLiveData
    }
}