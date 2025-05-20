
package utils

import android.app.NotificationChannel
import android.app.NotificationManager
import utils.NotificationWorker
import android.content.Context
import android.os.Build
import androidx.work.*
import java.util.concurrent.TimeUnit

object NotificationHelper {

    fun scheduleReminderNotification(
        context: Context,
        title: String,
        content: String,
        delayDays: Int
    ) {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to title,
                    "content" to content
                )
            )
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "reminders_channel"
            val name = "Reminders"
            val descriptionText = "Channel for task reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
