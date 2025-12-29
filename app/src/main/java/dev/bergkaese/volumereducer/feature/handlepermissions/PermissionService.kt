package dev.bergkaese.volumereducer.feature.handlepermissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface PermissionService {
    fun initPermissionService(activity: ComponentActivity)
    fun getPermissionState(): StateFlow<Boolean>
    fun areAllPermissionsGranted(): Boolean
}

class PermissionServiceImpl() : PermissionService {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val permissions: List<String>
    private val _permissionState = MutableStateFlow(false)

    init {
        val permissionsList = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= 33) {
            permissionsList += Manifest.permission.POST_NOTIFICATIONS
        }

        if (Build.VERSION.SDK_INT >= 34) {
            permissionsList += Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
        }
        permissions = permissionsList.toList()
    }

    override fun getPermissionState(): StateFlow<Boolean> {
        return _permissionState.asStateFlow()
    }

    override fun areAllPermissionsGranted(): Boolean {
        return _permissionState.value
    }

    override fun initPermissionService(
        activity: ComponentActivity,
    ) {
        registerForResult(activity)
        updatePermissions(activity)
        if(_permissionState.value.not()){
            requestPermissions()
        }
    }

    private fun updatePermissions(activity: ComponentActivity) {
        if (_permissionState.value.not()) {
            _permissionState.update { hasAllPermissions(activity) }
        }
    }

    private fun registerForResult(activity: ComponentActivity) {
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result -> handleResult(result) }
    }

    private fun handleResult(
        result: Map<String, @JvmSuppressWildcards Boolean>,
    ) {
        val denied = result.filter { !it.value }.keys
        if (denied.isNotEmpty()) {
            Log.d("PermissionHandler", "Permission denied: $denied")
            _permissionState.update { false }
        } else {
            Log.d("PermissionHandler", "Permission granted: $result")
            _permissionState.update { true }
        }
    }

    private fun hasAllPermissions(activity: Activity): Boolean {
        if (_permissionState.value) {
            return true
        }
        return permissions.all {
            ContextCompat.checkSelfPermission(
                activity.applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun requestPermissions() {
        permissionLauncher.launch(permissions.toTypedArray())
    }
}
