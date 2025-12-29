package dev.bergkaese.volumereducer.feature.stayactive

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface StayActiveServiceController {
    fun startStayActiveService(activity: Activity)
    fun shutdownStayActiveService(activity: Activity)
}
const val NOTIFICATION_CHANNEL_ID = "Notification_Channel"
class StayActiveServiceControllerImpl : StayActiveServiceController {
    private val _isRunning = MutableStateFlow<Boolean>(false)

    override fun shutdownStayActiveService(activity: Activity) {
        if (_isRunning.value) {
            activity.stopService(Intent(activity, StayActiveService::class.java))
            _isRunning.update { false }
        }

    }

    override fun startStayActiveService(activity: Activity) {
        if(_isRunning.value.not()){
            registerNotification(activity)
            activity.startService(Intent(activity, StayActiveService::class.java))
            _isRunning.update { true }
        }
    }

    fun registerNotification(activity: Activity) {
        val channel = NotificationChannel(
            VOLUME_SERVICE_CHANNEL_ID, NOTIFICATION_CHANNEL_ID,
            NotificationManager.IMPORTANCE_LOW
        )
        activity.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }
}