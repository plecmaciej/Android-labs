package com.example.myapplication1

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Switch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.myapplication1.SecondActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var imageView : ImageView
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val thumbnail: Bitmap? = data?.extras?.get("data") as? Bitmap
            thumbnail?.let {
                imageView.setImageBitmap(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val startButton = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        imageView = findViewById<ImageView>(R.id.imageView)
        val switch = findViewById<Switch>(R.id.switch1)
        val yourTextView = findViewById<TextView>(R.id.textView)
        val second_intent = Intent(this, SecondActivity::class.java)

        startButton.setOnClickListener {
            yourTextView.text = "Click!";
            second_intent.putExtra("isAlternativeMode", switch.isChecked) // Przekazujemy wartość switcha
            startActivity(second_intent)

        }
        button2.setOnClickListener {
            val aparatPlay = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(aparatPlay)

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}