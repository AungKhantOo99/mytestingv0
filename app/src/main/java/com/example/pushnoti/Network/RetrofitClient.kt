package com.example.pushnoti.Network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
    //    http://192.168.100.212:1337/api/auth/local/register
        .baseUrl(" https://randomuser.me/")
        .client(client)
        .build()
    val JsonApi = retrofit.create(ApiServise::class.java)


         //   https://randomuser.me/api/?page=10&results=5
    /***
    New Api for test
    val client = OkHttpClient()
    val request = Request.Builder()
    .url("https://realtor.p.rapidapi.com/locations/v2/auto-complete?input=new%20york&limit=10")
    .get()
    .addHeader("X-RapidAPI-Key", "66096fd285msh3cffd194e273e7bp13e3dcjsnffd802f200ad")
    .addHeader("X-RapidAPI-Host", "realtor.p.rapidapi.com")
    .build()

    val response = client.newCall(request).execute() **/

}