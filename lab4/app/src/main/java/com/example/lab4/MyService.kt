package com.twoja.nazwa.aplikacji

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    private val binder = LocalBinder()
    private var startTime: Long = 0

    inner class LocalBinder : Binder() {
        fun getService(): MyService = this@MyService
    }

    override fun onCreate() {
        super.onCreate()
        startTime = System.currentTimeMillis()
        Log.d("MyService", "Service created")
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyService", "Service destroyed")
    }

    fun getElapsedTime(): Int {
        return ((System.currentTimeMillis() - startTime) / 1000).toInt()
    }
}