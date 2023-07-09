package com.example.feastfast


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.google.firebase.FirebaseMessagingService"

class MyFirebaseMessagingService : FirebaseMessagingService()  {


    override fun onMessageReceived(remoteMessage : RemoteMessage){
        if(remoteMessage.notification != null){
            generateNotification(remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!)
        }
    }


    private fun getRemoteView(title: String, message: String) : RemoteViews{
        val remoteView = RemoteViews("com.example.feastfast", R.layout.notification_layout)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, title)
        // remoteView.setImageViewResource(R.id.l, R.drawable.order)

        return remoteView
    }
    private fun generateNotification(title: String, message: String){
        val intent = Intent(this , MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT)
        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            channelId)
            //.setSmallIcon(R.drawable.order)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the device's API level is 26 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 , builder.build())
    }

}