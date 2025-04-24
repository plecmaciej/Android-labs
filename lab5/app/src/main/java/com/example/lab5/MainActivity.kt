package com.example.lab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.lab5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentArray: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI()

        binding.button.setOnClickListener {
            generateAndDisplayRandomNumbers()
        }

        binding.button2.setOnClickListener {
            sortAndDisplayArray()
        }
    }

    private fun generateAndDisplayRandomNumbers() {
        currentArray = IntArray(100) { (1..1000).random() }  // Generujemy nową tablicę
        binding.sampleText.text = "Wygenerowano: ${currentArray.contentToString()}"
    }

    private fun sortAndDisplayArray() {
        if (!this::currentArray.isInitialized) {  // Sprawdzamy, czy tablica istnieje
            binding.sampleText.text = "Najpierw wygeneruj liczby!"
            return
        }

        sortArray(currentArray)
        binding.sampleText.text = "Posortowano: ${currentArray.contentToString()}"
    }

    external fun stringFromJNI(): String
    external fun sortArray(array: IntArray)

    fun greetUser(name: String): String {
        return "Hello, $name!"
    }

    companion object {
        // Used to load the 'lab5' library on application startup.
        init {
            System.loadLibrary("lab5")
        }
    }
}