package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cloth.*

class LastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloth)

        val clothtext1 = findViewById<TextView>(R.id.textView15)
        val clothtext2 = findViewById<TextView>(R.id.textView8)


        if (intent.hasExtra("cloth"))
        {
            val clothinfo = intent.getStringExtra("cloth")
            val clothnew = clothinfo?.replace('[', ' ')
            val cloth = clothnew?.split(']')
            clothtext1.text = ""
            clothtext1.append(cloth?.get(0).toString())
            clothtext2.text = ""
            clothtext2.append(cloth?.get(1).toString())
        }

        val backbutton = findViewById<Button>(R.id.backbutton)
        backbutton.setOnClickListener({
            val intent = Intent(this, SocketActivity::class.java)
            startActivity(intent)
        })

//        val btn_exit = findViewById<Button>(R.id.btn_exit)
//        btn_exit.setOnClickListener({
//            finishAffinity()
//            System.runFinalization()
//            System.exit(0)
//        })

        val btn_next = findViewById<Button>(R.id.btn_to_history_page)
        btn_next.setOnClickListener({
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        })

        cloth_location1.setOnClickListener {
            Toast.makeText(this@LastActivity, "상의 : 3단 서랍 두번째 칸\n 하의 : 3단 서랍 첫번째 칸", Toast.LENGTH_LONG).show()
        }

        cloth_location2.setOnClickListener {
            Toast.makeText(this@LastActivity, "상의 : 3단 서랍 두번째 칸\n 하의 : 3단 서랍 첫번째 칸", Toast.LENGTH_LONG).show()
        }
    }
}