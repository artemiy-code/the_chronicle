package ru.artem_torpedo.thechronicle.data.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.artem_torpedo.thechronicle.R
import ru.artem_torpedo.thechronicle.presentation.MainActivity
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext val context: Context,
    private val notificationManager: NotificationManager?,
) {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification),
            NotificationManager.IMPORTANCE_DEFAULT
        ).also {
            notificationManager?.createNotificationChannel(it)
        }
    }

    fun setUpNotification(topics: List<String>) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            PENDING_INTENT_RC,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_breaking_news)
            .setContentTitle(context.getString(R.string.new_articles))
            .setContentText(
                context.getString(
                    R.string.updated_topics,
                    topics.size,
                    topics.joinToString(", ")
                )
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle())
            .build()

        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val CHANNEL_ID = "new_articles"
        private const val NOTIFICATION_ID = 1
        private const val PENDING_INTENT_RC = 0
    }
}