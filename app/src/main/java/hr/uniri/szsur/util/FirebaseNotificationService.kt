package hr.uniri.szsur.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import hr.uniri.szsur.R
import hr.uniri.szsur.ui.MainActivity
import kotlin.random.Random


class FirebaseNotificationService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FirebaseNotifService"
    }

    /**
     * Notification messages are only received here when the app
     * is in the foreground. When the app is in the background an automatically
     * generated notification is displayed.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Received notification from: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d(TAG, "Notification: $it")
            sendNotification(it)
        }
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        SharedPreferenceUtils.putString(SharedPreferenceUtils.Fields.FCM_TOKEN, token)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val surveysChannel = NotificationChannel(
            "surveys",
            "Rezultati anketa",
            NotificationManager.IMPORTANCE_MAX
        ).apply {
            description = "Obavijesti o objavljenim rezultatima onih anketa koje ti se sviđaju"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(surveysChannel)

        val eventsChannel = NotificationChannel(
            "events",
            "Nadolazeći događaji",
            NotificationManager.IMPORTANCE_MAX
        ).apply {
            description = "Obavijesti o nadolazećim događajima koji ti se sviđaju"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(eventsChannel)
    }

    private fun sendNotification(notification: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()
        Log.i(TAG, notification.channelId ?: "channelId")

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, notification.channelId ?: "")
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setColor(resources.getColor(R.color.blue_icon))
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationID, notificationBuilder.build())
    }
}
