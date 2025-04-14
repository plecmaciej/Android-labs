package com.example.lab4

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity(), MyService.ServiceCallback {
    private lateinit var mService: MyService
    private var mBound = false
    private lateinit var timeText: TextView


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyService.LocalBinder
            mService = binder.getService()
            mBound = true
            mService.setCallback(this@MainActivity)
            timeText.text = "Połączono z usługą"
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
            timeText.text = "Usługa została rozłączona"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timeText = findViewById(R.id.text_time)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MyService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            mService.setCallback(null)
            unbindService(connection)
            mBound = false
        }
    }

    override fun onTimeUpdate(seconds: Int) {
        runOnUiThread {
            timeText.text = "Running time: $seconds s"
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] ==
                android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Zgoda na powiadomienia przyznana", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Brak zgody na powiadomienia", Toast.LENGTH_SHORT).show()
            }
        }
    }
}