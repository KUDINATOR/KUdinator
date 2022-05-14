package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityWeatherBinding
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.item.view.*
import retrofit2.Call
import retrofit2.Response
import java.util.*

class SubActivity : AppCompatActivity() {

    lateinit var binding : ActivityWeatherBinding
    private var firestore : FirebaseFirestore? = null
    private var uid : String? = null
    // 날씨정보 기본 값
    private var base_date = "20220513"
    // 발표 시간은 30분 단위로 끊음
    private var base_time = "2000"
    private var nx = "61"
    private var ny = "127"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 현재 날짜 받아와서 출력
        binding.weatherDate.text = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Calendar.getInstance().time)
        setWeather(nx,ny)
        setLocation()

        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        //파이어스토어 객체
        recyclerview.adapter = RecyclerViewAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this)

        backbutton2.setOnClickListener{
            val intent = Intent(this, SetupActivity::class.java)
            startActivity(intent)
        }

        btn_to_last_page.setOnClickListener{
            val intent = Intent(this, SocketActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Calender 클래스 ArrayList 생성
        var calender : ArrayList<Calender> = arrayListOf()

        init {  // Situation 불러온 뒤 Calender 형식으로 변환해 ArrayList에 담음
            firestore?.collection("schedule")?.document("Situation_1") //situation 변경 가능
                ?.addSnapshotListener { querySnapshot, _ ->
                    // ArrayList 비워줌
                    calender.clear()
                    if (querySnapshot == null) return@addSnapshotListener

                    if (querySnapshot.exists()) {
                        var item = querySnapshot.toObject(Calender::class.java)
                        calender.add(item!!)
                    }
                    notifyDataSetChanged()
                }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.what.text= calender!![position].what
            viewHolder.when_time.text = calender!![position].when_time
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return calender.size
        }
    }

//    private fun setItem(item:WeatherData){
//        val high_temp = findViewById<TextView>(R.id.high_temperature)
//        val low_temp = findViewById<TextView>(R.id.low_temperature)
//        val speed_wind = findViewById<TextView>(R.id.wind)
//        val humidity = findViewById<TextView>(R.id.humidity)
//        val pos_rain = findViewById<TextView>(R.id.rain)
//    }

    private fun setWeather(nx : String, ny : String) {
        // 현재 날짜, 시간 가져오기
        val cal = Calendar.getInstance()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시
        val timeM = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 분
        base_time = getBaseTime(timeH, timeM)

        // base_time이 2330이면 어제 정보 받아오기
        // 데이터는 매시간 30분에 제공되기 때문
        if (timeH == "00" && base_time == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }

        // 날씨 정보 가져오기
        val call = WeatherObject.getRetrofitService().GetWeather(100, 1, "JSON", base_date, "1430", nx, ny)


        // 통신
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    Log.d("dd",response.body().toString())
                    val it = response.body()!!.response.body.items.item

                    for (i in 0..9) {
//                        index %= 5
                        when (it[i].category) {
                            "TMP" -> binding.temperatrue.text = it[i].fcstValue + " ℃" // 기온
                            "PTY" -> binding.snowRain.text = set_snowrain(it[i].fcstValue)  // 눈, 비 여부
                            "WSD" -> binding.wind.text = it[i].fcstValue + " m/s"   // 풍속
                            "REH" -> binding.humidity.text = it[i].fcstValue  // 습도
                            "POP" -> binding.rain.text = it[i].fcstValue + " %" // 강수 확률
                            "PTY" -> binding.ultraviolet.text = it[i].fcstValue // 자외선
                            "SKY" -> set_sky(it[i].fcstValue)

                            else -> continue
                        }
//                        index++
                    }

                    // 토스트메시지로 확인

                    Toast.makeText(
                        applicationContext,
                        it[0].fcstDate + ", " + it[0].fcstTime + "의 날씨 ",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            // 통신실패 시 로그 메시지
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }

    private fun set_snowrain(v: String): String {
        var result = ""
        when(v){
            "0" -> result = "없음"
            "1" -> result = "비"
            "2" -> result = "비 / 눈"
            "3" -> result = "눈"
            "4" -> result = "소나기"
        }
        return result

    }

    private fun set_sky(v: String){

        when(v){
            // 맑음
            "1" -> binding.weatherImage.setImageResource(R.drawable.sunny)
            // 구름많음
            "3" -> binding.weatherImage.setImageResource(R.drawable.cloud)
            // 흐림
            "4" -> binding.weatherImage.setImageResource(R.drawable.cloudy
            )
        }
    }

    private fun setLocation(){
        // 사용자 위치 불러오기
        // setXY() 활용하고 권한 설정 할 것
    }


    // baseTime 설정 함수 - 데이터가 매시간 30분에 제공되므로 주의할것
    private fun getBaseTime(h : String, m : String) : String {
        var result = ""

        // 45분 전이면
        if (m.toInt() < 45) {
            // 자정넘기면 23:30
            if (h == "00") result = "2330"
            // 아니면 1시간 전 날씨 정보 부르기
            else {
                var resultH = h.toInt() - 1
                // 6:30 은 0630으로 표시되어야하므로 시가 한자리수라면 0 추가해주기
                if (resultH < 10) result = "0" + resultH + "30"
                // 2자리면 그대로
                else result = resultH.toString() + "30"
            }
        }
        else result = h + "30"
        return result
    }

    // 사용자 위치 찾기에 이용
    // 데이터는 위도와 경도로 주어져서 이를 기상청 격자 좌표로 변환하는 함수
    private fun setXY(v1: Double, v2: Double) : Point {
        val RE = 6371.00877     // 지구 반경(km)
        val GRID = 5.0          // 격자 간격(km)
        val SLAT1 = 30.0        // 표준 위도1(degree)
        val SLAT2 = 60.0        // 표준 위도2(degree)
        val OLON = 126.0        // 기준점 경도(degree)
        val OLAT = 38.0         // 기준점 위도(degree)
        val XO = 43             // 기준점 X좌표(GRID)
        val YO = 136            // 기준점 Y좌표(GRID)
        val DEGRAD = Math.PI / 180.0
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)

        var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5)
        ra = re * sf / Math.pow(ra, sn)
        var theta = v2 * DEGRAD - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = (ra * Math.sin(theta) + XO + 0.5).toInt()
        val y = (ro - ra * Math.cos(theta) + YO + 0.5).toInt()

        return Point(x, y)
    }

}

