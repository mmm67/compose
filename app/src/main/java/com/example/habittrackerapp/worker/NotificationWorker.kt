package com.example.habittrackerapp.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.habittrackerapp.MainActivity
import com.example.habittrackerapp.R
import com.example.habittrackerapp.compose.Routes
import com.example.habittrackerapp.utilities.Constants
import com.example.habittrackerapp.utilities.Constants.NotificationWorkerParams.HABIT_NAME
import com.example.habittrackerapp.utilities.Constants.NotificationWorkerParams.REMINDER_MESSAGE

class NotificationWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val habitName = inputData.getString(HABIT_NAME) ?: "Habit"
        val message =
            inputData.getString(REMINDER_MESSAGE) ?: "Don't forget to do your habit: $habitName"

        showNotification(message)

        return Result.success()
    }

    private fun showNotification(message: String) {
        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(appContext, MainActivity::class.java).apply {
            putExtra("TAB", Routes.DailyHabits.route)
        }
        val pendingIntent = PendingIntent.getActivity(
            appContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification =
            NotificationCompat.Builder(appContext, Constants.NotificationChannelProperties.CHANNEL_ID)
                .setSmallIcon(R.drawable.reminder)
                .setContentTitle("Time to do your habit!")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(1, notification)
    }
}
