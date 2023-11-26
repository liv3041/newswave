package com.example.newswave

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// Firebase messaging service to handle incoming messages
class FirebaseMessageReceiver:FirebaseMessagingService() {
    // Callback method triggered when a new FCM token is generated
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Refershed Token", token)
    }

    // Callback method triggered when a new FCM message is received
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FirebaseMessageReceiver", "Message received: ${remoteMessage.data}")

        // Check if the notification contains a title and body
        val title = remoteMessage.notification?.title
        val text = remoteMessage.notification?.body

        if (title != null && text != null) {
            Log.d("FirebaseMessageReceiver", "Notification Title: $title, Body: $text")

            // Define notification channel properties
            val channelName = "Heads Up Notification"
            val CHANNEL_ID = "HEADS_UP_NOTIFICATION"

            val importance = NotificationManager.IMPORTANCE_HIGH

            // Create notification channel
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = "Description of Heads Up Notification"
            }

            // Get NotificationManager and create the notification channel
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            // Create an explicit intent for an Activity in your app
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear the activity stack
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_ONE_SHOT
            )

            // Build and show the notification
            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.wave_sound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            NotificationManagerCompat.from(this).notify(1, notificationBuilder.build())


            Log.d("FirebaseMessageReceiver", "Notification displayed successfully.")
        } else {
            Log.e("FirebaseMessageReceiver", "Notification does not contain title and body.")
        }

    }
}