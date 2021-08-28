package com.revolve44.solarpanelx.feature_modules.workmanager

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.revolve44.solarpanelx.BuildConfig
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.datasource.local.SolarDao
import com.revolve44.solarpanelx.datasource.local.SolarDatabase
import com.revolve44.solarpanelx.domain.core.checkPercent
import com.revolve44.solarpanelx.domain.core.defineTimeOfDay
import com.revolve44.solarpanelx.domain.core.ensureNeedUpdateOrNot_PeriodTwoDays
import com.revolve44.solarpanelx.domain.core.getCurrentDayOfYear
import com.revolve44.solarpanelx.domain.enums.TypeOfSky
import com.revolve44.solarpanelx.feature_modules.workmanager.model.NotificationWarningModel
import com.revolve44.solarpanelx.global_utils.Constants.Companion.CHANNEL_ID
import com.revolve44.solarpanelx.global_utils.Constants.Companion.CHANNEL_ID2
import com.revolve44.solarpanelx.global_utils.Constants.Companion.CHANNEL_ID2Num
import com.revolve44.solarpanelx.global_utils.Constants.Companion.CHANNEL_IDNum
import com.revolve44.solarpanelx.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.reflect.Type
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

/**
 * ITS BETA
 */

class WorkerForShowForecastDynamic(private val mContext: Context, workerParameters: WorkerParameters) :
    Worker(mContext, workerParameters), LifecycleOwner {
    // ???????
    companion object {
        fun run() : LiveData<WorkInfo> {
            val work = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PeriodicWorkRequestBuilder<WorkerForShowForecastDynamic>(Duration.ofHours(2L)).build()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            WorkManager.getInstance().enqueue(work)

            return WorkManager.getInstance().getWorkInfoByIdLiveData(work.id)
        }
    }

    private lateinit var builder: Notification.Builder
    //private lateinit var notificationManager : NotificationManager
    private val notificationChannelId = "ENDLESS SERVICE CHANNEL"

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var workoutDAO: SolarDao? = null
    private var database: SolarDatabase? = null
    private lateinit var lifecycleRegistry: LifecycleRegistry

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    
    @SuppressLint("RestrictedApi", "CheckResult")
    override fun doWork(): Result {
        Timber.i("AndroidVille fff "+ Thread.currentThread().toString())
        GlobalScope.launch(Dispatchers.Main) {
            val worker = this@WorkerForShowForecastDynamic

            lifecycleRegistry = LifecycleRegistry(worker)
            lifecycleRegistry.currentState = Lifecycle.State.CREATED

            //val lifecyclerOwner = work
            // test if Lifecycle.Event.ON_DESTROY is called

            lifecycleRegistry.currentState = Lifecycle.State.STARTED
            //workoutDAO!!.getAllForecastCells()

            try{
                startCalculate()
            }
            catch (e : Exception){
                Toast.makeText(mContext,"Please update data from server in main screen",
                    Toast.LENGTH_LONG).show()
            }


            //RetrofitInstance.api5daysRequest.get5daysRequest()

            //notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //PreferenceMaestro.counnnter = PreferenceMaestro.counnnter + 1
//        val imagesJson = inputData.getString("images")
//        val gson = Gson()
//        val listOfImages = gson.fromJson<List<Image>>(imagesJson, object : TypeToken<List<Image>>() {}.type);

            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }


        //notificationManager.cancel(notificationId)
        return Result.Success()
    }
    val gson = Gson()
    var forecastTodayMaxMinShow = arrayListOf<NotificationWarningModel>()
    private fun startCalculate() {

        val type: Type = object : TypeToken<ArrayList<NotificationWarningModel?>?>() {}.type
        forecastTodayMaxMinShow = (gson.fromJson<ArrayList<NotificationWarningModel>>(PreferenceMaestro.notificationJSON, type))

        Log.i("fff", "fff ${forecastTodayMaxMinShow.joinToString()}  ${forecastTodayMaxMinShow.toString()} ${forecastTodayMaxMinShow.size}")

        if (forecastTodayMaxMinShow == null) {
            forecastTodayMaxMinShow = ArrayList()
        }



        Timber.i("wwww ${defineTimeOfDay()} need upd or not:${ensureNeedShowNotificationOrNot()} need upd or not API:${ensureNeedUpdateOrNot_PeriodTwoDays()}")
        when (defineTimeOfDay()){
            TypeOfSky.MORNING, TypeOfSky.DAY, TypeOfSky.EVENING ->{
                if (ensureNeedShowNotificationOrNot()){

                    displayNotification(forecastTodayMaxMinShow)

                }

            }
            else -> {
                Timber.w("fff current time of day ${defineTimeOfDay()}")
                if (BuildConfig.DEBUG){
                    displayNotificationWarning()
                }
            }
        }


        database = SolarDatabase.getInstance(mContext);
        workoutDAO = database!!.solarDao
        Timber.i("fff ${workoutDAO}")


//        if (ensureNeedUpdateOrNot_PeriodTwoDays()){
//
//
//
//        }else{
//            Timber.i("fff Need to update")
//            //Toast.makeText(this,"Need to update",Toast.LENGTH_SHORT).
//            //please refresh
//        }
    }

//    @SuppressLint("CheckResult")
//    private fun downloadImage(image: Image, index: Int) {
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url(image.url)
//            .build()
//
//        try {
//            val response = client.newCall(request).execute()
//            val bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
//
//            ImageUtil.saveBitmap(mContext, bitmap, image.title).subscribe({ img ->
//                displayNotification(ProgressUpdateEvent(image.title, 3, index + 1))
//                EventBus.getDefault().post(ImageDownloadedEvent(img, image.title, image.id))
//            }, { error ->
//                error.printStackTrace()
//            })
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    //private val notificationId: Int = 500
    private val notificationChannel: String = "demo"

    private fun refreshNotification() {

        builder.setContentText(" spd: 454564");

        notificationManager.notify(notificationChannelId,1, builder.build());


    }

    private fun displayNotification(str: ArrayList<NotificationWarningModel>) {

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_ID,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            channel.enableVibration(false)
//            notificationManager.createNotificationChannel(channel)
//      //  }
        //val pendingIntent: PendingIntent = Intent(mContext, MainActivity::class.java).let { notificationIntent ->
        //    PendingIntent.getActivity(mContext, 0, notificationIntent, 0)
        //}
        val pendingIntentTODO = PendingIntent.getActivity(mContext, 0,
            Intent(mContext, MainActivity::class.java), 0)
        //val resultIntent : Intent = Intent(mContext,MainActivity::class.java)
        //val resultPendingIntent : PendingIntent = PendingIntent.getActivity(mContext,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)

        val remoteView = RemoteViews(applicationContext.packageName, R.layout.notification_custom_max_min)
        //remoteView.setImageViewResource(R.id.iv_notif, R.drawable.eminem)
        //remoteView.setTextViewText(R.id.tv_notif_progress, "complete))) ${PreferenceMaestro.counnnter}")
        //remoteView.setTextViewText(R.id.notification_alert_weather,      "${str[0].description}")
        //remoteView.setTextViewText(R.id.notification_alert_weather,      "${defineTimeOfDay()}")
        remoteView.setTextViewText(R.id.notification_alert_weather,      "${checkPercent((str[1].sumOfDay)/PreferenceMaestro.solarDayDuration)}")
        remoteView.setTextViewText(R.id.notif_today_forecast_value,      "${str[1].sumOfDay}W")
        remoteView.setTextViewText(R.id.notif_today_forecast_description,"${str[1].description}")

        remoteView.setTextViewText(R.id.notif_max_forecast_value,        "${str[2].sumOfDay}W")
        remoteView.setTextViewText(R.id.notif_max_forecast_description,  "${str[2].description}📈")

        remoteView.setTextViewText(R.id.notif_min_forecast_value,        "${str[3].sumOfDay}W")
        remoteView.setTextViewText(R.id.notif_min_forecast_description,  "${str[3].description}\uD83D\uDCC9")
        remoteView.setOnClickPendingIntent(R.id.notification_alert_weather, pendingIntentTODO)
        //remoteView.setProgressBar(R.id.pb_notif, prog.total, prog.progress, false)

        notificationBuilder
            .setContent(remoteView)
            .setSmallIcon(R.drawable.ic_for_notification_sol)
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .setContentIntent(pendingIntentTODO)

        notificationManager.notify(CHANNEL_IDNum, notificationBuilder.build())

        PreferenceMaestro.dayOfLastShowedNotification = getCurrentDayOfYear()
    }

    private fun displayNotificationWarning() {

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_ID,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            channel.enableVibration(false)
//            notificationManager.createNotificationChannel(channel)
//      //  }
        //val pendingIntent: PendingIntent = Intent(mContext, MainActivity::class.java).let { notificationIntent ->
        //    PendingIntent.getActivity(mContext, 0, notificationIntent, 0)
        //}
        val pendingIntentTODO = PendingIntent.getActivity(mContext, 0,
            Intent(mContext, MainActivity::class.java), 0)
        //val resultIntent : Intent = Intent(mContext,MainActivity::class.java)
        //val resultPendingIntent : PendingIntent = PendingIntent.getActivity(mContext,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID2)

//        val remoteView = RemoteViews(applicationContext.packageName, R.layout.notification_custom_max_min)
//        //remoteView.setImageViewResource(R.id.iv_notif, R.drawable.eminem)
//        //remoteView.setTextViewText(R.id.tv_notif_progress, "complete))) ${PreferenceMaestro.counnnter}")
//        //remoteView.setTextViewText(R.id.notification_alert_weather,      "${str[0].description}")
//        remoteView.setTextViewText(R.id.notification_alert_weather,      "${defineTimeOfDay()}")
//        remoteView.setTextViewText(R.id.notif_today_forecast_value,      "${str[1].sumOfDay}W")
//        remoteView.setTextViewText(R.id.notif_today_forecast_description,"${str[1].description}")
//
//        remoteView.setTextViewText(R.id.notif_max_forecast_value,        "${str[2].sumOfDay}W")
//        remoteView.setTextViewText(R.id.notif_max_forecast_description,  "${str[2].description}📈")
//
//        remoteView.setTextViewText(R.id.notif_min_forecast_value,        "${str[3].sumOfDay}W")
//        remoteView.setTextViewText(R.id.notif_min_forecast_description,  "${str[3].description}\uD83D\uDCC9")
//        remoteView.setOnClickPendingIntent(R.id.notification_alert_weather, pendingIntentTODO)



        notificationBuilder
            //.setContent(remoteView)
            .setContentText("Its not perfect time to notification!!! isDebug =${BuildConfig.DEBUG}")
            .setSmallIcon(R.drawable.ic_for_notification_sol)
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .setContentIntent(pendingIntentTODO)

        notificationManager.notify(CHANNEL_ID2Num, notificationBuilder.build())

        PreferenceMaestro.dayOfLastShowedNotification = getCurrentDayOfYear()
    }


    override fun onStopped() {
        super.onStopped()
        //notificationManager.cancel(CHANNEL_IDNum)
    }






    data class ProgressUpdateEvent(var message: String, var total: Int, var progress: Int)
}