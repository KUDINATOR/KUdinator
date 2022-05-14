package com.example.myapplication

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// http로 보낼 요청 메세지 작성
interface WeatherInterface {
    @GET("getVilageFcst?serviceKey=6aHqvzUARDzuoossgLjAuEzwheha%2B03XGLVr4LOnnc9zqe9It%2Bqu04EteZ1wOi2QvaygdWjqQGu5MyzcvWh5qQ%3D%3D")

    fun GetWeather(@Query("numOfRows") num_of_rows : Int,   // 한 페이지 경과 수
                   @Query("pageNo") page_no : Int,          // 페이지 번호
                   @Query("dataType") data_type : String,   // 자료 형식
                   @Query("base_date") base_date : String,  // 발표 일자
                   @Query("base_time") base_time : String,  // 발표 시각
                   @Query("nx") nx : String,                // 예보지점 X좌표
                   @Query("ny") ny : String

    )                // 예보지점 Y좌

            : Call<WEATHER>
}


