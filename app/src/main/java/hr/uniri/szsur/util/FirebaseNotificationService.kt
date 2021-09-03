package hr.uniri.szsur.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class FirebaseNotificationService : FirebaseMessagingService() {


    companion object {
        private const val TAG = "FirebaseNotifService"
    }

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     * Associate the user's FCM registration token with his server-side account.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
        SharedPreferenceUtils.putString("fcmToken", token ?: "")
        if (Firebase.auth.currentUser != null) {
            val body = hashMapOf<String, String?>()
            body["fcmToken"] = token
            coroutineScope.launch(job) {
                UserRepository.updateUser(body)
            }
        }
    }

/**
* Create and show a simple notification containing the received FCM message.
*
* @param messageBody FCM message body received.
*/
private fun sendNotification(messageBody: String) {
val intent = Intent(this, MainActivity::class.java)
intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
    PendingIntent.FLAG_ONE_SHOT)

val channelId = getString(R.string.default_notification_channel_id)
val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
val notificationBuilder = NotificationCompat.Builder(this, channelId)
    .setSmallIcon(R.drawable.ic_launcher_foreground)
    .setContentTitle(getString(R.string.fcm_message))
    .setContentText(messageBody)
    .setAutoCancel(true)
    .setSound(defaultSoundUri)
    .setContentIntent(pendingIntent)

val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

// Since android Oreo notification channel is needed.
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(channelId,
        "Channel human readable title",
        NotificationManager.IMPORTANCE_DEFAULT)
    notificationManager.createNotificationChannel(channel)
}

notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
}

override fun onDestroy() {
super.onDestroy()
job.cancel()
}
}

class MyWorker(appContext: Context, workerParams: WorkerParameters)
: Worker(appContext, workerParams) {

companion object {
private val TAG = "MyWorker"
}

override fun doWork(): Result {
Log.d(TAG, "Performing long running task in scheduled job")
// TODO(developer): add long running task here.
return Result.success()
}
}