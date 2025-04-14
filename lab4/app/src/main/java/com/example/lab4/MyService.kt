package com.example.lab4

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyService : Service() {

    private val binder = LocalBinder()
    private var startTime: Long = 0
    private var callback: ServiceCallback? = null
    private var job: Job? = null

    inner class LocalBinder : Binder() {
        fun getService(): MyService = this@MyService
    }

    interface ServiceCallback {
        fun onTimeUpdate(seconds: Int)
    }

    fun setCallback(callback: ServiceCallback?) {
        this.callback = callback
    }

    override fun onCreate() {
        super.onCreate()

        val CHANNEL_ID = "my_channel_01"
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                "Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Usługa działa")
            .setContentText("Zliczamy czas działania...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()

        startForeground(101, notification)

        startTime = System.currentTimeMillis()


        job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val seconds = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                callback?.onTimeUpdate(seconds)
                delay(1000)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        setCallback(null)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
