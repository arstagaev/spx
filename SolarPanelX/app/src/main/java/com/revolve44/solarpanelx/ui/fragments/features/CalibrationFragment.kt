package com.revolve44.solarpanelx.ui.fragments.features


import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.core.blinkATextView
import com.revolve44.solarpanelx.domain.core.roundTo2decimials
import com.revolve44.solarpanelx.domain.core.scaleOfkWh
import com.revolve44.solarpanelx.ui.MainActivity
import com.revolve44.solarpanelx.ui.viewmodels.MainViewModel
import io.feeeei.circleseekbar.CircleSeekBar
import timber.log.Timber
import kotlin.math.roundToInt


class CalibrationFragment : Fragment(R.layout.fragment_calibration) {
    private lateinit var viewmodel : MainViewModel

    private lateinit var circleSeekBar: CircleSeekBar
    private lateinit var calibrate_indicator: TextView
    private lateinit var calibratedOutputPower: TextView
    private lateinit var changesSaved: TextView
    var coeff = 1.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as MainActivity).showProgressBar(getString(R.string.navigation_drawer_calibration))

        (activity as MainActivity).let { it ->

            if (it.viewModelMain != null) {
                viewmodel = (activity as MainActivity).viewModelMain!!
            }
        }




        circleSeekBar = view.findViewById(R.id.circleseekbar)
        calibrate_indicator = view.findViewById(R.id.calibrate_coeff)
        calibratedOutputPower = view.findViewById(R.id.calibrated_outputPower)
        changesSaved = view.findViewById(R.id.changes_saved_indicator_fragment_calibr)






        // for moving circle seekbar
        circleSeekBar.setOnSeekBarChangeListener { seekbar, IntegerPercentForCalibration ->
            Timber.i("New Calibration value = $IntegerPercentForCalibration")

            calibrate_indicator.text = "$IntegerPercentForCalibration %"

            coeff = (IntegerPercentForCalibration/100f).toFloat()
            if(coeff != null){
                PreferenceMaestro.calibrationCoeff = coeff
            }else{
                PreferenceMaestro.calibrationCoeff = 1.0F
            }

            //Timber.i("calibr")
            //calibratedOutputPower.text = (scaleOfkWh((viewmodel.forecastPower.value)!! * coeff).roundTo(2))).toString()+"W"
            if (viewmodel.forecastNow.value!=null){

                calibratedOutputPower.text = scaleOfkWh(((viewmodel.forecastNow.value)!! * coeff).toInt(),true)
                Timber.i("fnow calibr vm: ${viewmodel.forecastNow.value} ${PreferenceMaestro.calibrationCoeff} ")

            }else{
                calibratedOutputPower.text = "please refresh data"
                Timber.e("ERROR in calibrating fragment")
            }

             //= IntegerPercentForCalibration

            notifyAboutSavedChanges()
        }

    }

    override fun onResume() {
        super.onResume()
        //remind saved position of circle seek bar
        circleSeekBar.maxProcess = 200
        circleSeekBar.curProcess = ((PreferenceMaestro.calibrationCoeff)*100).roundToInt()
        //PreferenceMaestro.calibrationCoeff = circleSeekBar.curProcess

        //remind data of indicators
        if (viewmodel.forecastNow.value != null){
            calibratedOutputPower.text = "${roundTo2decimials(viewmodel.forecastNow.value!!*PreferenceMaestro.calibrationCoeff)}Wh"

        }
        calibrate_indicator.text = "${PreferenceMaestro.calibrationCoeff * 100.0f}%"
    }

//    fun selfCalibrating() : Int {
//        if ()
//        return
//
//    }

    fun notifyAboutSavedChanges(){
        blinkATextView(
            changesSaved,
            Color.GREEN,
            Color.BLACK,
            Color.BLACK,
            4200
        )

    }
}

