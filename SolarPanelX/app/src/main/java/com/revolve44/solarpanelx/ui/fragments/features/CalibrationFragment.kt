//package com.revolve44.solarpanelx.ui.fragments.features
//
//
//import android.graphics.Color
//import android.os.Bundle
//import android.os.CountDownTimer
//import androidx.fragment.app.Fragment
//import android.view.View
//import android.widget.TextView
//import com.google.android.material.snackbar.Snackbar
//import com.revolve44.solarpanelx.activity.MainActivity
//import com.revolve44.solarpanelx.R
//import com.revolve44.solarpanelx.core.blinkATextView
//import com.revolve44.solarpanelx.core.listOfColor
//import com.revolve44.solarpanelx.core.roundTo2decimials
//import com.revolve44.solarpanelx.core.scaleOfkWh
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
//import com.revolve44.solarpanelx.ui.viewmodels.MainViewModel
//import io.feeeei.circleseekbar.CircleSeekBar
//import timber.log.Timber
//
//
//class CalibrationFragment : Fragment(R.layout.fragment_calibration) {
//    private lateinit var viewmodel : MainViewModel
//
//    private lateinit var circleSeekBar: CircleSeekBar
//    private lateinit var calibrate_indicator: TextView
//    private lateinit var calibratedOutputPower: TextView
//    private lateinit var changesSaved: TextView
//    var coeff = 1.0f
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).showProgressBar(getString(R.string.navigation_drawer_calibration))
//
//        viewmodel =(activity as MainActivity).viewModel
//
//        circleSeekBar = view.findViewById(R.id.circleseekbar)
//        calibrate_indicator = view.findViewById(R.id.calibrate_coeff)
//        calibratedOutputPower = view.findViewById(R.id.calibrated_outputPower)
//        changesSaved = view.findViewById(R.id.changes_saved_indicator_fragment_calibr)
//
//
//        //remind saved position of circle seek bar
//        circleSeekBar.maxProcess = 200
//        circleSeekBar.curProcess = (PreferenceMaestro.calibrationCoeff)
//        PreferenceMaestro.calibrationCoeff = circleSeekBar.curProcess
//
//        //remind data of indicators
//        if (viewmodel.forecastNow.value != null){
//            calibratedOutputPower.text = "${roundTo2decimials(viewmodel.forecastNow.value!!*PreferenceMaestro.calibrationCoeff/100f)}Wh"
//
//        }
//        calibrate_indicator.text = "${PreferenceMaestro.calibrationCoeff}%"
//
//
//
//        // for moving circle seekbar
//        circleSeekBar.setOnSeekBarChangeListener { seekbar, curValue ->
//            Timber.i("New Calibration value = $curValue")
//
//            calibrate_indicator.text = "$curValue %"
//            coeff = (curValue/100f).toFloat()
//
//            //Timber.i("calibr")
//            //calibratedOutputPower.text = (scaleOfkWh((viewmodel.forecastPower.value)!! * coeff).roundTo(2))).toString()+"W"
//            if (viewmodel.forecastNow.value!=null){
//                calibratedOutputPower.text = scaleOfkWh(((viewmodel.forecastNow.value!!) * coeff).toInt(),true)
//
//            }else{
//                calibratedOutputPower.text = "please refresh data"
//                Timber.e("ERROR in calibrating fragment")
//            }
//
//            PreferenceMaestro.calibrationCoeff = curValue
//
//            notifyAboutSavedChanges()
//        }
//
//    }
//
////    fun selfCalibrating() : Int {
////        if ()
////        return
////
////    }
//
//    fun notifyAboutSavedChanges(){
//        blinkATextView(
//            changesSaved,
//            Color.GREEN,
//            Color.BLACK,
//            Color.BLACK,
//            4200
//        )
//
//    }
//}
//
