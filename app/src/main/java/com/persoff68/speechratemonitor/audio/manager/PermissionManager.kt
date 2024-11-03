package com.persoff68.speechratemonitor.audio.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor(
    @ApplicationContext private var context: Context,
) {
    companion object {
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private var onSuccess: (() -> Unit)? = null
    private var onFailure: (() -> Unit)? = null

    fun initialize(
        activity: ComponentActivity
    ) {
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                onSuccess?.invoke()
            } else {
                onFailure?.invoke()
            }
        }
    }

    fun checkAndRequestPermissions(
        onSuccess: (() -> Unit)?,
        onFailure: (() -> Unit)? = null
    ) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        val missingPermissions = getMissingPermissions()
        if (missingPermissions.isNotEmpty()) {
            permissionLauncher.launch(missingPermissions.toTypedArray())
        } else {
            onSuccess?.invoke()
        }
    }

    private fun getMissingPermissions(): List<String> {
        return REQUIRED_PERMISSIONS.filter { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }
    }

}
