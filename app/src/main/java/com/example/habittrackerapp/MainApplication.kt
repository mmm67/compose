package com.example.habittrackerapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.habittrackerapp.utilities.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            Constants.NotificationChannelProperties.CHANNEL_ID,
            Constants.NotificationChannelProperties.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = Constants.NotificationChannelProperties.CHANNEL_DESCRIPTION
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}