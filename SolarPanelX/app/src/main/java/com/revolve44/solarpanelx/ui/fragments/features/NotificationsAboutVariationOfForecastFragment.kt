package com.revolve44.solarpanelx.ui.fragments.features

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.RemoteViews
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.core.checkPercent
import com.revolve44.solarpanelx.feature_modules.workmanager.isWorkScheduled
import com.revolve44.solarpanelx.feature_modules.workmanager.WorkerForShowForecastDynamic
import com.revolve44.solarpanelx.feature_modules.workmanager.model.NotificationWarningModel
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations
import com.revolve44.solarpanelx.ui.MainActivity
import timber.log.Timber
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


class NotificationsAboutVariationOfForecastFragment : Fragment(R.layout.fragment_notifications_about_variation_of_forecast) {

    private lateinit var buttonStart : ToggleButton

    private lateinit var radioGroup : RadioGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonStart = view.findViewById(R.id.start_worker)
        radioGroup = view.findViewById(R.id.radiogroup_notification_manager)



        Timber.i("wwww work manager: ${isWorkScheduled("spx_tag_max_min")}")
//        Timber.i("wwww work manager2: ${WorkManager.getInstance(requireActivity()).getWorkInfosForUniqueWork("spx_max_min").isCancelled}")
//        Timber.i("wwww work manager3: ${WorkManager.getInstance(requireActivity()).getWorkInfosForUniqueWork("spx_max_min").isDone}")
//        Timber.i("wwww work manager3: ${WorkManager.getInstance(requireActivity()).getWorkInfosForUniqueWork("spx_max_min").toString()}")
        //var a = WorkManager.getInstance(requireActivity()).getWorkInfosForUniqueWork("spx_max_min")



        buttonStart.setOnClickListener {
            if (isWorkScheduled("spx_tag_max_min")){
                stopWorker()
            }else {
                startWorker()
            }

            Timber.i("wwww work manager: ${isWorkScheduled("spx_tag_max_min")}")
        }

        buttonStart.setOnCheckedChangeListener { _, isChecked ->
            //changerMainRawData(isChecked)

            if (isChecked){

                buttonStart.highlightColor = Color.GREEN
                buttonStart.setBackgroundColor(Color.GREEN)

            }else{
                buttonStart.highlightColor = Color.RED
                buttonStart.setBackgroundColor(Color.RED)

            }
            //Log.d("toggle",""+isChecked+" rounder "+(roundTo2decimials(PreferenceMaestro.coeffForLightSensor)))
        }


        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.set_daily_notification ->     {
                    PreferenceMaestro.choosenDurationOfShowNotification = 1
                }
                R.id.set_threedays_notification -> {
                    PreferenceMaestro.choosenDurationOfShowNotification = 2
                }
                R.id.set_weekly_notification ->    {
                    PreferenceMaestro.choosenDurationOfShowNotification = 3
                }

            }
        }

    }
    //private lateinit var builder: Notification.Builder
    //private lateinit var notificationManager : NotificationManager

    val gson = Gson()
    var forecastTodayMaxMinShow = arrayListOf<NotificationWarningModel>()

    private fun startCalculate() {

        val type: Type = object : TypeToken<ArrayList<NotificationWarningModel?>?>() {}.type
        forecastTodayMaxMinShow = (gson.fromJson<ArrayList<NotificationWarningModel>>(
            PreferenceMaestro.notificationJSON,
            type
        ))

        Log.i(
            "fff",
            "fff ${forecastTodayMaxMinShow.joinToString()}  ${forecastTodayMaxMinShow.toString()} ${forecastTodayMaxMinShow.size}"
        )

        if (forecastTodayMaxMinShow == null) {
            forecastTodayMaxMinShow = ArrayList()
        }
        displayNotification(forecastTodayMaxMinShow)

    }

    private fun displayNotification(str: ArrayList<NotificationWarningModel>) {
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_ID,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            channel.enableVibration(false)
//            notificationManager.createNotificationChannel(channel)
//        }

        val notificationBuilder =
            NotificationCompat.Builder(requireActivity(), ConstantsCalculations.CHANNEL_ID)
        val pendingIntentTODO = PendingIntent.getActivity(requireActivity(), 0,
            Intent(requireActivity(), MainActivity::class.java), 0)

        val remoteView = RemoteViews(requireActivity().packageName, R.layout.notification_custom_max_min)
        //remoteView.setImageViewResource(R.id.iv_notif, R.drawable.eminem)
        //remoteView.setTextViewText(R.id.tv_notif_progress, "complete))) ${PreferenceMaestro.counnnter}")
        //remoteView.setTextViewText(R.id.notification_alert_weather,      "${str[0].description}")
        var asd = ""
        try {
            asd = (activity as MainActivity).viewModelMain?.forecastNow?.value?.let { checkPercent(it) }!!
            if (asd == null){
                asd = checkPercent(str[1].sumOfDay)
            }
            remoteView.setTextViewText(R.id.notification_alert_weather,      "${asd}")
        }catch (e: Exception){}

        remoteView.setTextViewText(R.id.notif_today_forecast_value,      "${str[1].sumOfDay}W")
        remoteView.setTextViewText(R.id.notif_today_forecast_description,"${str[1].description}")

        remoteView.setTextViewText(R.id.notif_max_forecast_value,        "${str[2].sumOfDay}W")
        remoteView.setTextViewText(R.id.notif_max_forecast_description,  "${str[2].description}📈")

        remoteView.setTextViewText(R.id.notif_min_forecast_value,        "${str[3].sumOfDay}W")
        remoteView.setTextViewText(R.id.notif_min_forecast_description,  "${str[3].description}\uD83D\uDCC9")
        //remoteView.setOnClickPendingIntent(R.id.notification_alert_weather, )
        //remoteView.setProgressBar(R.id.pb_notif, prog.total, prog.progress, false)

        notificationBuilder
            .setContent(remoteView)
            .setSmallIcon(R.drawable.ic_for_notification_sol)
            .setContentIntent(pendingIntentTODO)

        notificationManager.notify(ConstantsCalculations.CHANNEL_IDNum, notificationBuilder.build())

        //PreferenceMaestro.dayOfLastShowedNotification = getCurrentDayOfYear() disable special
    }

    override fun onResume() {
        super.onResume()

        //buttonStart.isChecked =
        if (isWorkScheduled("spx_tag_max_min")){
            buttonStart.highlightColor = Color.GREEN
            buttonStart.isChecked = true

        }else{
            buttonStart.highlightColor = Color.RED
            buttonStart.isChecked = false
        }

        when(PreferenceMaestro.choosenDurationOfShowNotification){
            1->{
                radioGroup.check(R.id.set_daily_notification)
            }
            2->{
                radioGroup.check(R.id.set_threedays_notification)
            }
            3->{
                radioGroup.check(R.id.set_weekly_notification)
            }

        }
    }

    private fun stopWorker(){
        WorkManager.getInstance(requireActivity()).cancelAllWorkByTag("spx_tag_max_min")
    }

    private fun startWorker() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)

        val foreverTimeRequest = PeriodicWorkRequest.Builder(WorkerForShowForecastDynamic::class.java,1,
            TimeUnit.HOURS) // every 1 hours
            //.setInputData(data)
            .setConstraints(constraints.build())
            .addTag("spx_tag_max_min")
            .build()

        Toast.makeText(requireActivity(), "Starting worker", Toast.LENGTH_SHORT).show()
        //var a = WorkManager.getInstance(requireActivity()).enqueueUniquePeriodicWork("spx_max_min", ExistingPeriodicWorkPolicy.KEEP , foreverTimeRequest);

        WorkManager.getInstance(requireActivity()).enqueueUniquePeriodicWork("spx_max_min",
            ExistingPeriodicWorkPolicy.REPLACE,foreverTimeRequest)
        try{
            startCalculate()
        }
        catch (e : Exception){
            Toast.makeText(requireActivity(),"Please update data from server in main screen",Toast.LENGTH_LONG).show()
        }

        //.enqueueUniqueWork("AndroidVille", ExistingWorkPolicy.KEEP, oneTimeRequest)
    }

//    private fun isWorkScheduled(tag: String): Boolean {
//        val instance = WorkManager.getInstance()
//        val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosByTag(tag)
//        return try {
//
//            var running = false
//            val workInfoList: List<WorkInfo> = statuses.get()
//            for (workInfo in workInfoList) {
//                val state = workInfo.state
//                running = state  == WorkInfo.State.ENQUEUED // pr running
//            }
//            running
//
//        } catch (e: ExecutionException) {
//            e.printStackTrace()
//            false
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//            false
//        }
//    }
}