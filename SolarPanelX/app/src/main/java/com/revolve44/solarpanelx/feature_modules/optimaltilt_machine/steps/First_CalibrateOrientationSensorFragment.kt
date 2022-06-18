package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.steps

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.OptimalOrientationHelperActivity
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels.OrientationSolarPanelViewModel
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro


class First_CalibrateOrientationSensorFragment : Fragment(R.layout.sm_fragment_calibrate_orientation_sensor) {

    private lateinit var viewMOdelOrientationSensorFragment : OrientationSolarPanelViewModel
    private lateinit var we_suggest_orientation_for_define_location : TextView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showGif(view)
        var cardViewBtnConfirmEndOfCalibration = (view.findViewById<CardView>(R.id.calibr_orient_sensor_confirm_btn) as CardView)
        we_suggest_orientation_for_define_location = view.findViewById<TextView>(R.id.we_suggest_orientation_for_define_location)

        viewMOdelOrientationSensorFragment = (activity as OptimalOrientationHelperActivity).viewModel


        var timer = object : CountDownTimer(3000,1000){
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                cardViewBtnConfirmEndOfCalibration.visibility = View.VISIBLE
            }

        }.start()
        cardViewBtnConfirmEndOfCalibration.setOnClickListener {
            viewMOdelOrientationSensorFragment.currentItemInViewPager2.value = 1
        }
    }

    override fun onResume() {
        super.onResume()
        we_suggest_orientation_for_define_location.text = "${PreferenceMaestro.chosenStationCITY}"
        
    }

    fun showGif(view: View) {
        val imageView: ImageView = view.findViewById(R.id.calibrate_orient)
        Glide.with(this).load(R.drawable.sm_calbrph3).into(imageView)


    }


}