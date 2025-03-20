package com.example.myapplication1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.CustomView
import com.example.myapplication1.R

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val customView = findViewById<CustomView>(R.id.customView)

        val isAlternativeMode = intent.getBooleanExtra("isAlternativeMode", false)


        customView.isAlternativeMode = isAlternativeMode
        customView.invalidate()
    }
}