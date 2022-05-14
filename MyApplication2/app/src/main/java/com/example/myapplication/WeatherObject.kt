package com.example.myapplication

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherObject {

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")

            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getRetrofitService(): WeatherInterface{
        return getRetrofit().create(WeatherInterface::class.java)
    }
}

val gson : Gson = GsonBuilder()
    .setLenient()
    .create()



