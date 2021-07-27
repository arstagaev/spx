package com.revolve44.solarpanelx.domain.core

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import androidx.annotation.RequiresApi
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.domain.enums.WhatIMustSay

class SoundPlay {
    @RequiresApi(Build.VERSION_CODES.M)
    fun engine(context: Context, whatIMustSay: WhatIMustSay){


        when(whatIMustSay){
            WhatIMustSay.BEEP -> {
                var connectSuccess =   MediaPlayer.create(context, R.raw.autopilot_on)

                val newPlaybackParams = PlaybackParams()
                newPlaybackParams.speed = 1.0F
                connectSuccess.playbackParams = newPlaybackParams
            }
            WhatIMustSay.CONNECTED_SUCCESS -> {
                var disconectSuccess = MediaPlayer.create(context, R.raw.autopilot_off)
                val newPlaybackParams = PlaybackParams()
                newPlaybackParams.speed = 1.0F
                disconectSuccess.playbackParams = newPlaybackParams
            }


        }

    }
}