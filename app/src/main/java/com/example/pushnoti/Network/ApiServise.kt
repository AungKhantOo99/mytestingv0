package com.example.pushnoti.Network

import androidx.paging.PagingData
import com.example.pushnoti.model.pageresponse.paginationdata
import com.example.pushnoti.model.response.responsedata
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServise {
    @GET("api/?results=10")
    fun getpost(): Call<responsedata>

    @GET("api/")
    fun getdata(
        @Query("page") page: Int,
        @Query("results") result: Int
    ):Call<paginationdata>

}
