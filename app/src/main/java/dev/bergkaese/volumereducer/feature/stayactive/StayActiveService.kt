package dev.bergkaese.volumereducer.feature.stayactive

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dev.bergkaese.volumereducer.MainActivity
import dev.bergkaese.volumereducer.R

const val VOLUME_SERVICE_CHANNEL_ID = "volume_service"

class StayActiveService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val name = getString(R.string.volume_service)
        val running = getString(R.string.is_running)
        val notifIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notifIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, VOLUME_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_service_icon)
            .setContentTitle(name)
            .setContentText(running)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}