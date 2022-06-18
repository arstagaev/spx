package com.revolve44.solarpanelx.feature_modules.workmanager

import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.core.getCurrentDayOfYear
import timber.log.Timber
import java.util.concurrent.ExecutionException
import kotlin.math.abs

fun isWorkScheduled(tag: String): Boolean {
    val instance = WorkManager.getInstance()
    val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosByTag(tag)
    return try {

        var running = false
        val workInfoList: List<WorkInfo> = statuses.get()
        for (workInfo in workInfoList) {
            val state = workInfo.state
            running = state  == WorkInfo.State.ENQUEUED // pr running
        }
        running

    } catch (e: ExecutionException) {
        e.printStackTrace()
        false
    } catch (e: InterruptedException) {
        e.printStackTrace()
        false
    }
}

fun ensureNeedShowNotificationOrNot() : Boolean{
    Timber.i("wwww duration of notif: ${PreferenceMaestro.choosenDurationOfShowNotification}// current day: ${getCurrentDayOfYear()} last show notif: ${PreferenceMaestro.dayOfLastShowedNotification }")
    when(PreferenceMaestro.choosenDurationOfShowNotification){
        0->{ return false }
        1->{
            if(getCurrentDayOfYear() != PreferenceMaestro.dayOfLastShowedNotification ){ return true }
        }
        2->{
            if(abs(getCurrentDayOfYear() - PreferenceMaestro.dayOfLastShowedNotification) > 2 ){ return true }
        }
        3->{
            if(abs(getCurrentDayOfYear() - PreferenceMaestro.dayOfLastShowedNotification) > 6 ){ return true }
        }
        else -> return false

    }

   return false
}