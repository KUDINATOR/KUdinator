package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cloth.*

class SetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        val backbtn = findViewById<Button>(R.id.backbtn)
        backbtn.setOnClickListener({
            val intent = Intent(this, LastActivity::class.java)
            startActivity(intent)
        })

        val btn_next = findViewById<Button>(R.id.btn_to_history_page)
        btn_next.setOnClickListener({
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        })

    }
}