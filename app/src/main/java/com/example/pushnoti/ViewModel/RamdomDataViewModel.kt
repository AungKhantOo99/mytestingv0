package com.example.pushnoti.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pushnoti.Network.RetrofitClient
import com.example.pushnoti.model.response.responsedata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RamdomDataViewModel : ViewModel() {
    private val _apiResponse = MutableLiveData<responsedata>()
    val apiResponse: LiveData<responsedata> = _apiResponse

    private var timer: Timer? = null

    fun startDataUpdates() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                fetchDataFromApi()
            }
        }, 0, 1000) // Fetch data every second
    }

    fun stopDataUpdates() {
        timer?.cancel()
        timer = null
    }

    private fun fetchDataFromApi() {
        RetrofitClient.JsonApi.getpost().enqueue(object : Callback<responsedata>{
            override fun onResponse(call: Call<responsedata>, response: Response<responsedata>) {
                _apiResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<responsedata>, t: Throwable) {

            }

        })
    }

        override fun onCleared() {
            super.onCleared()
            stopDataUpdates()
    }
}
