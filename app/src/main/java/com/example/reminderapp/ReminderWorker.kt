package com.example.reminderapp

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val message = inputData.getString("message") ?: "Напоминание"
        NotificationHelper.showNotification(applicationContext, message)
        return Result.success()
    }
}
