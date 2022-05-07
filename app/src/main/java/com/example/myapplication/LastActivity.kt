package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cloth.*

class LastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloth)

        val backbutton = findViewById<Button>(R.id.backbutton)
        backbutton.setOnClickListener({
            val intent = Intent(this, SubActivity::class.java)
            startActivity(intent)
        })

        val btn_exit = findViewById<Button>(R.id.btn_exit)
        btn_exit.setOnClickListener({
            finishAffinity()
            System.runFinalization()
            System.exit(0)
        })

        cloth_location1.setOnClickListener {
            Toast.makeText(this@LastActivity, "상의 : 3단 서랍 두번째 칸\n 하의 : 3단 서랍 첫번째 칸", Toast.LENGTH_LONG).show()
        }

        cloth_location2.setOnClickListener {
            Toast.makeText(this@LastActivity, "상의 : 3단 서랍 두번째 칸\n 하의 : 3단 서랍 첫번째 칸", Toast.LENGTH_LONG).show()
        }
    }
}