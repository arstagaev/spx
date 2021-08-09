package com.revolve44.solarpanelx.ui.fragments.features

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.work.*
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.feature_modules.workmanager.WorkerForShowForecastDynamic
import java.util.concurrent.TimeUnit


class NotificationsAboutVariationOfForecastFragment : Fragment(R.layout.fragment_notifications_about_variation_of_forecast) {

    private lateinit var buttonStart : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonStart = view.findViewById(R.id.start_worker)

        buttonStart.setOnClickListener {
            startWorker()
        }

    }

    private fun startWorker() {
//        val data = Data.Builder()
//            .putString("images", jsonString)
//            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)

        val oneTimeRequest = PeriodicWorkRequest.Builder(WorkerForShowForecastDynamic::class.java,2,
            TimeUnit.HOURS) // every 2 hours
            //.setInputData(data)
            .setConstraints(constraints.build())
            .addTag("demo")
            .build()

        Toast.makeText(requireActivity(), "Starting worker", Toast.LENGTH_SHORT).show()

        WorkManager.getInstance(requireActivity()).enqueueUniquePeriodicWork("xxx",
            ExistingPeriodicWorkPolicy.REPLACE,oneTimeRequest)
        //.enqueueUniqueWork("AndroidVille", ExistingWorkPolicy.KEEP, oneTimeRequest)
    }
}