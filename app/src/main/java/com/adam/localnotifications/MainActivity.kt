package com.adam.localnotifications

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adam.localnotifications.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.Date
import android.media.AudioAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import com.adam.localnotifications.adapter.NotificationAdapter
import com.adam.localnotifications.model.PendingNotification

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotificationAdapter
    private val pendingNotifications = mutableListOf<PendingNotification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotificationAdapter(pendingNotifications)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Add notifications to the list for demonstration
        pendingNotifications.add(PendingNotification(1, "Test Title", "Test Message"))
        adapter.notifyDataSetChanged()

        // Setup the submit button to schedule a notification
        binding.submitButton.setOnClickListener {
            scheduleNotification()
        }

        createNotificationChannel()  // Make sure to call this to setup notification channels
    }




    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = binding.titleET.text.toString()
        val message = binding.messageET.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlart(time, title, message)

    }

    private fun showAlart(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage("" +
                    "Title: " + title +
                    "\nMessage: " + message +
                    "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okey"){_,_ ->}
            .show()
    }

    private fun getTime(): Long
    {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datepicker.dayOfMonth
        val month = binding.datepicker.month
        val year = binding.datepicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val descriptionText = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH  // Set importance to high
        val channel = NotificationChannel(channelID, name, importance).apply {
            description = descriptionText

            // Set the custom sound URI
            val soundUri = Uri.parse("android.resource://${packageName}/raw/alerts")  // Replace your_sound_file with your actual file name
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            setSound(soundUri, audioAttributes)
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}