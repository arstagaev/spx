package com.revolve44.solarpanelx.feature_modules.lightsensor


import android.graphics.Color
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.revolve44.solarpanelx.BuildConfig
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.core.roundTo1decimials
import io.feeeei.circleseekbar.CircleSeekBar
import kotlin.math.round



class FragmentLightSensor : Fragment(R.layout.lightsensor_fragment_mainscreen) {

//    private lateinit var sensorManager: SensorManager
//    private lateinit var lightSensor: Sensor
//    private lateinit var lightEventListener: SensorEventListener
    private lateinit var calibrateCoeff : TextView

    private lateinit var rawValueIndicator : TextView
    private lateinit var minValueIndicator : TextView
    //lateinit var averageValueIndicator : TextView
    private lateinit var maxValueIndicator : TextView

    private lateinit var minIndicatorWatts : TextView
    private lateinit var maxIndicatorWatts : TextView

    private lateinit var realWattsIndicator : TextView
    private lateinit var inputNominalPower : EditText
    private lateinit var undoButton : ImageView
    private lateinit var exchangeRate : TextView

    private lateinit var toggle: ToggleButton

    private lateinit var viewModel: LightSensorViewModel
    private lateinit var circleSeekBar: CircleSeekBar

    private var dataSensorArray : ArrayList<Any> = ArrayList()
    private var lastValue : Int = 0
    private var coefficient : Float = 1.0F
    private var nominalPowerForLightSensor : Int = PreferenceMaestro.nominalPowerForLightSensor
    private lateinit var mp : MediaPlayer
    private lateinit var mp2 : MediaPlayer
    private lateinit var onCalibrSound : MediaPlayer
    private lateinit var offCalibrSound : MediaPlayer

    private lateinit var helperButton : ImageView
    private lateinit var helperLayout : ConstraintLayout

    private lateinit var debugConsoleLightSensor : ConstraintLayout
    private lateinit var debugSeekBar : SeekBar

    private var isCalibrate: Boolean = false
    private var helperIsEnabled : Boolean = false

    private val TAG = "LightSensor_"

    override fun onResume() {
        super.onResume()

        Log.d(TAG,"coeff = "+PreferenceMaestro.coeffForLightSensor)
        if ((PreferenceMaestro.coeffForLightSensor)>130000){
            PreferenceMaestro.coeffForLightSensor = 0.1F
        }
        inputNominalPower.setText(""+ PreferenceMaestro.nominalPowerForLightSensor)
    }

    private fun showDebugConsole(){
        debugConsoleLightSensor.visibility = View.VISIBLE

        debugSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
                viewModel.lighter.value = (progress*1000).toFloat()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped

            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputNominalPower = view.findViewById(R.id.input_nominal_for_sensor)
        calibrateCoeff = view.findViewById(R.id.calibrate_coeff)
        rawValueIndicator = view.findViewById(R.id.sensor_raw_indicator)
        minValueIndicator = view.findViewById(R.id.min_indicator)
        realWattsIndicator = view.findViewById(R.id.calibrate_coeff2)
        //averageValueIndicator = view.findViewById(R.id.average_indicator)
        maxValueIndicator = view.findViewById(R.id.max_indicator)

        minIndicatorWatts = view.findViewById(R.id.min_indicator_watts)
        maxIndicatorWatts = view.findViewById(R.id.max_indicator_watts)

        undoButton = view.findViewById(R.id.undo_button_light_sensor)
        toggle = view.findViewById(R.id.toggleButton)
        circleSeekBar = view.findViewById(R.id.circleseekbar)
        exchangeRate = view.findViewById(R.id.exchange_rate)

        helperButton = view.findViewById(R.id.helper_light_sensor)
        helperLayout = view.findViewById(R.id.helper_layout)
        debugConsoleLightSensor = view.findViewById(R.id.light_sensor_debug_console)
        debugSeekBar = view.findViewById(R.id.seekbar_for_debug)

        mp = MediaPlayer.create(requireActivity(),R.raw.beep_half_sec)
        mp2 = MediaPlayer.create(requireActivity(),R.raw.airplanebeep_sec_max)

        onCalibrSound = MediaPlayer.create(requireActivity(),R.raw.autopilot_on)
        offCalibrSound = MediaPlayer.create(requireActivity(),R.raw.autopilot_off)

        viewModel = (activity as LightSensorActivity).viewModel
        viewModel.max.value = 0
        viewModel.min.value = 1000

        circleSeekBar.maxProcess = 100
        inputNominalPower.setText((PreferenceMaestro.nominalPowerForLightSensor).toString())
        switchModes()
//        seekBarChanger()
//        changerMainRawData(false)
        listenerOfEditTextNominalPower()
        initHelperListener()
        router()

        undoButton.setOnClickListener {
            viewModel.max.value = 0
            viewModel.min.value = 1000

            maxValueIndicator.text = "0 \n max"
            minValueIndicator.text = "0 \n min"

            maxIndicatorWatts.text = "0 \n max"
            minIndicatorWatts.text = "0 \n min"
        }


        if (BuildConfig.DEBUG){
            showDebugConsole()
        }else{
            debugConsoleLightSensor.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun router() {
        viewModel.lighter.observe(viewLifecycleOwner, Observer { newValue ->

            moverOfseekBar(newValue.toInt())
            indicatorsChanger(newValue.toInt())
            changeValues(newValue.toInt())

        })

        circleSeekBar.setOnSeekBarChangeListener { seekbar, curValue ->
            recorderOfSeekBar(curValue)
            playBeep(curValue)
        }
    }

    private fun recorderOfSeekBar(curValue: Int) {
        if (isCalibrate){
            if (viewModel.lighter.value != null){
                coefficient = ((viewModel.lighter.value!!) / (curValue).toInt())

                PreferenceMaestro.coeffForLightSensor = coefficient
            }


//            if (coefficient!= Float.POSITIVE_INFINITY || coefficient != Float.NEGATIVE_INFINITY){
//
//            }
            PreferenceMaestro.lastStateOfLightSensorCalibrationTool = coefficient.toInt()

        }else{

        }

    }

    fun moverOfseekBar(newValue: Int){
        // newValue = 500 for e.x.


        if (isCalibrate){


        }else{
            //circleSeekBar.setOnSeekBarChangeListener(null)

            circleSeekBar.curProcess = ((newValue)/(PreferenceMaestro.coeffForLightSensor)).toInt()

        }
    }

    private fun indicatorsChanger(newValue: Int){
        rawValueIndicator.text = "${newValue} \n"+getString(R.string.light_sensor_sign_raw_value)
        try {
            exchangeRate.text = getString(R.string.light_sensor_sign_exchange_rate)+"${roundTo1decimials(PreferenceMaestro.coeffForLightSensor)} Lux"
        }catch (e: Exception) {
            Log.e("workaround_attention","error: ${e.message}")
        }

        realWattsIndicator.text = getString(R.string.light_sensor_sign_converted)+"\n${((circleSeekBar.curProcess/100F)* PreferenceMaestro.nominalPowerForLightSensor).toInt()} Watts"

        if (isCalibrate){

            calibrateCoeff.text = getString(R.string.light_sensor_sign_one_percent_of_powerout)+"\n${PreferenceMaestro.coeffForLightSensor} Lux"

        }else{

            calibrateCoeff.text = getString(R.string.light_sensor_sign_now_solpan)+"\n"+getString(R.string.light_sensor_sign_work_at_percent)+"\n${circleSeekBar.curProcess}%"
            // realWattsIndicator.text = "converted \n${((setterForSeekBar(newValue.toFloat())/100F)*PreferenceMaestro.nominalPowerForLightSensor).toFloat()}  Watts"
        }
    }



    private fun listenerOfEditTextNominalPower() {
        if (inputNominalPower.text!=null && inputNominalPower.text.toString() != ""){
            inputNominalPower.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(inputNominalPower.text.toString() != ""){
                        try{
                            if (inputNominalPower.text.length <= 7){
                                PreferenceMaestro.nominalPowerForLightSensor = s.toString().toInt()
                            }else{
                                Toast.makeText(requireActivity(),getString(R.string.lightsensor_warning1),Toast.LENGTH_LONG).show()
                            }

                        }catch (e: Exception){

                        }

                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    private fun switchModes() {
        // For starting manipulations
        ///////////////////////////////////////////////////
        toggle.setOnCheckedChangeListener { _, isChecked ->

            isCalibrate = isChecked

            //changerMainRawData(isChecked)

            if (isChecked){
                circleSeekBar.reachedColor = Color.RED
                toggle.highlightColor = Color.RED

                circleSeekBar.curProcess = PreferenceMaestro.lastStateOfLightSensorCalibrationTool
                onCalibrSound.start()

            }else{
                toggle.highlightColor = Color.WHITE
                circleSeekBar.reachedColor = Color.GREEN
                offCalibrSound.start()

            }
            //Log.d("toggle",""+isChecked+" rounder "+(roundTo2decimials(PreferenceMaestro.coeffForLightSensor)))
        }
        ///////////////////////////////////////////////////
    }
    //private var curValuex = 0F


    @RequiresApi(Build.VERSION_CODES.M)
    private fun playBeep(curValue : Int){
        //final MediaPlayer mp = MediaPlayer.create(this, R.raw.soho);
        var a = 2
        if (!isCalibrate){
            if (curValue<50){
                val newPlaybackParams = PlaybackParams()
                newPlaybackParams.setSpeed(2.0F)
                mp.playbackParams = newPlaybackParams
            }else if (curValue>=50){
                val newPlaybackParams = PlaybackParams()
                newPlaybackParams.setSpeed(1.0F)
                mp.playbackParams = newPlaybackParams
            }
        }

    }


    private fun changeValues(newValue: Int) {
        rawValueIndicator.text =""+newValue+" \n"+getString(R.string.light_sensor_sign_raw_value_footer)


        if (viewModel.max.value!=null && viewModel.min.value!=null){
            Log.d(TAG," max ${viewModel.max.value} , min ${viewModel.min.value} ")
            if(newValue > viewModel.max.value!!){
                // max
                maxValueIndicator.text = "$newValue \n max"
                viewModel.max.value = newValue

                if ((((newValue/ PreferenceMaestro.coeffForLightSensor)/100)* PreferenceMaestro.nominalPowerForLightSensor) > PreferenceMaestro.nominalPowerForLightSensor){
                    maxIndicatorWatts.text = "${PreferenceMaestro.nominalPowerForLightSensor} \n max"

                }else{
                    maxIndicatorWatts.text = "${round(((newValue/ PreferenceMaestro.coeffForLightSensor)/100)* PreferenceMaestro.nominalPowerForLightSensor)} \n max"
                }



            }else if (newValue < viewModel.min.value!!){
                // min
                minValueIndicator.text = "$newValue \n min"
                viewModel.min.value = newValue
                minIndicatorWatts.text = "${round(((newValue/ PreferenceMaestro.coeffForLightSensor)/100)* PreferenceMaestro.nominalPowerForLightSensor)} \n min"
            }
        }
    }



    private fun initHelperListener() {
        if (helperIsEnabled){
            helperLayout.visibility = View.VISIBLE
        }else{
            helperLayout.visibility = View.GONE
        }

        helperButton.setOnClickListener {
            helperIsEnabled = !helperIsEnabled
            initHelperListener()
        }

        helperLayout.setOnClickListener {
            helperIsEnabled = false
            initHelperListener()
        }
    }


    companion object {
        const val FRAGMENT_TAG = "light_sensor"
        //const val FIRSTRUN_PREF = "firstrun_shown"


        fun create(): FragmentLightSensor {
            //val uuid = currentSession?.id

//            val arguments = Bundle()
//            arguments.putString(ARGUMENT_SESSION_UUID, uuid)

            val fragment = FragmentLightSensor()
            //fragment.arguments = arguments

            return fragment
        }
    }
}