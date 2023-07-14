package com.almasgali.todoapp.util.notifications

import android.Manifest
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.almasgali.todoapp.R
import com.almasgali.todoapp.data.model.Importance
import com.almasgali.todoapp.data.model.TodoItem
import com.almasgali.todoapp.ui.activity.MainActivity
import java.util.UUID

class AlarmReceiver : BroadcastReceiver() {

    private var notificationManager: NotificationManagerCompat? = null

    override fun onReceive(p0: Context?, p1: Intent?) {
        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                p1?.getParcelableExtra("item", TodoItem::class.java)
            } else {
                p1?.getParcelableExtra("item")
            }
        val tapResultIntent = Intent(p0, MainActivity::class.java)
        tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent =
            getActivity(p0, 0, tapResultIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = p0?.let {
            NotificationCompat.Builder(it, "to_do_list")
                .setContentTitle("Task Reminder")
                .setContentText(item?.text)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(
                    when (item?.importance) {
                        Importance.LOW -> NotificationCompat.PRIORITY_LOW
                        Importance.HIGH -> NotificationCompat.PRIORITY_HIGH
                        else -> {NotificationCompat.PRIORITY_DEFAULT}
                    }
                )
                .setContentIntent(pendingIntent)
                .build()
        }
        notificationManager = p0?.let { NotificationManagerCompat.from(it) }
        notification?.let { item?.let { it1 -> if (ActivityCompat.checkSelfPermission(
                p0,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("NOTIF", "No permission")

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
            notificationManager?.notify(UUID.fromString(it1.id).hashCode(), it) } }
            Log.d("NOTIF", "success")
    }


}