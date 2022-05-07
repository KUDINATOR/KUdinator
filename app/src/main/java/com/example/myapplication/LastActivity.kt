package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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
    }
}