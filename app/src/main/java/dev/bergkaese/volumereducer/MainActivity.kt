package dev.bergkaese.volumereducer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.bergkaese.volumereducer.feature.controlvolume.AudioController
import dev.bergkaese.volumereducer.feature.handlepermissions.PermissionService
import dev.bergkaese.volumereducer.feature.handlepermissions.PermissionServiceImpl
import dev.bergkaese.volumereducer.feature.persistvolume.VolumeStorageBySharedPrefs
import dev.bergkaese.volumereducer.feature.stayactive.StayActiveServiceController
import dev.bergkaese.volumereducer.feature.stayactive.StayActiveServiceControllerImpl
import dev.bergkaese.volumereducer.ui.AppModel
import dev.bergkaese.volumereducer.ui.MainScreen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(),
    StayActiveServiceController by StayActiveServiceControllerImpl(),
    PermissionService by PermissionServiceImpl(){
    private val viewModel by viewModels<AppModel>(){
        viewModelFactory {
            addInitializer(AppModel::class) {
                AppModel(AudioController(), VolumeStorageBySharedPrefs(applicationContext))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BackHandler(enabled = true) {
                moveTaskToBack(true)
            }
            val isGranted by viewModel.permissionsGranted.collectAsState()
            MainScreen(isGranted,
                getString(R.string.volume),
                viewModel.currentVolume,
                { factor -> viewModel.setVolume(factor) },
                getString(R.string.stop),
                { finishAndRemoveTask() })
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getPermissionState().collect { granted ->
                    viewModel.permissionsGranted.update { granted }
                    if (granted) {
                        startStayActiveService(this@MainActivity)
                    } else {
                        shutdownStayActiveService(this@MainActivity)
                    }
                }
            }
        }
        initPermissionService(this)
    }

    override fun onDestroy() {
        if(isChangingConfigurations.not()){
            shutdownStayActiveService(this)
        }
        super.onDestroy()
    }
}