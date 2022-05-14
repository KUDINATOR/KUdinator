package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.item.view.*

class SubActivity : AppCompatActivity() {
    private var firestore : FirebaseFirestore? = null
    private var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        //파이어스토어 객체
        recyclerview.adapter = RecyclerViewAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this)

        val backbutton2 = findViewById<Button>(R.id.backbutton2)

        backbutton2.setOnClickListener({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

        btn_to_last_page.setOnClickListener {
            val intent = Intent(this, LastActivity::class.java)
            startActivity(intent)
        }
    }

        @SuppressLint("NotifyDataSetChanged")
        inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            // Calender 클래스 ArrayList 생성
            var calender: ArrayList<Calender> = arrayListOf()

            init {  // Situation 불러온 뒤 Calender 형식으로 변환해 ArrayList에 담음
                firestore?.collection("schedule")?.document("Situation_2") //situation 변경 가능
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
}
