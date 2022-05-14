package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class WeatherData (
//    var high_temp = ""
//    var low_temp = ""
//    var speed_wind = ""
//    var humidity = ""
//    var pos_rain = ""
//    var fcstTime = ""

    @SerializedName("high_temp") var high_temp: String = "",
    @SerializedName("low_temp") var low_temp: String = "",   // 강수 형태
    @SerializedName("humidity") var humidity: String = "",      // 습도
    @SerializedName("speed_wind") var speed_wind: String = "",           // 하능 상태
    @SerializedName("pos_rain") var pos_rain: String = "",          // 기온
    @SerializedName("fcstTime") var fcstTime: String = "",

    )


// xml 파일 형식을 data class로 구현 - API 이용 가이드 참고
data class WEATHER (val response : RESPONSE)
data class RESPONSE(val header : HEADER, val body : BODY)
data class HEADER(val resultCode : Int, val resultMsg : String)
data class BODY(val dataType : String, val items : ITEMS)
data class ITEMS(val item : List<ITEM>)
// category : 자료 구분 코드, fcstValue : 예보 값
data class ITEM(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)
