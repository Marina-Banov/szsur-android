package hr.uniri.szsur.ui

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import androidx.core.app.NotificationCompat
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.PlacesRepository


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    companion object {
        private const val CHANNEL_ID = "hr.szsur"
        private const val NOTIFICATION_ID = 1234
    }

    //private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        PlacesRepository.client = Places.createClient(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)


        //createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val intentNottification = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intentNottification, 0)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }
}