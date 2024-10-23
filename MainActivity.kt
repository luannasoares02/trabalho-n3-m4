package com.example.appaudio

import android.os.Bundle
import android.media.AudioDeviceInfo
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.android.FlutterActivity

class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val audioHelper = AudioHelper(this)

        audioHelper.registerAudioDeviceCallback()

        audioHelper.checkBluetoothConnection(this)

        val isSpeakerAvailable = audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)
        val isBluetoothHeadsetConnected = audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)

        println("Speaker available: $isSpeakerAvailable")
        println("Bluetooth headset connected: $isBluetoothHeadsetConnected")

        if (isBluetoothHeadsetConnected) {
            audioHelper.playAudio(R.raw.audio.teste) }

        if (isSpeakerAvailable) {
            audioHelper.playNotificationSound()
            audioHelper.playAlarmSound()
        }
    }
}
