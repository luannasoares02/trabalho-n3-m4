package com.example.appaudio

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.content.pm.PackageManager
import android.media.AudioDeviceCallback
import android.widget.Toast
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri

class AudioHelper(context: Context) {

    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val appContext = context.applicationContext

    fun audioOutputAvailable(type: Int): Boolean {
        if (!appContext.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
            return false
        }
        return audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS).any { it.type == type }
    }

    fun registerAudioDeviceCallback() {
        audioManager.registerAudioDeviceCallback(object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
                super.onAudioDevicesAdded(addedDevices)
                if (audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    showToast("Fone de ouvido Bluetooth conectado!")
                }
            }

            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
                super.onAudioDevicesRemoved(removedDevices)
                if (!audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    showToast("Fone de ouvido Bluetooth desconectado!")
                }
            }
        }, null)
    }

    private fun showToast(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
    }

    fun checkBluetoothConnection(activity: AppCompatActivity) {
        if (!audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
            // Se o Bluetooth não estiver conectado, direcionar para as configurações
            val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            activity.startActivity(intent)
        } else {
            showToast("Fone de ouvido Bluetooth já conectado!")
        }
    }

    fun playAudio(resourceId: Int) {
        val mediaPlayer = MediaPlayer.create(appContext, resourceId)
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
        mediaPlayer.start()
    }

    fun playNotificationSound() {
        if (audioOutputAvailable(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)) {
            val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val mediaPlayer = MediaPlayer.create(appContext, notification)
            mediaPlayer.setOnCompletionListener {
                it.release()
            }
            mediaPlayer.start()
        } else {
            showToast("Alto-falante não disponível!")
        }
    }

    fun playAlarmSound() {
        if (audioOutputAvailable(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)) {
            val alarm: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val mediaPlayer = MediaPlayer.create(appContext, alarm)
            mediaPlayer.setOnCompletionListener {
                it.release()
            }
            mediaPlayer.start()
        } else {
            showToast("Alto-falante não disponível!")
        }
    }
}
