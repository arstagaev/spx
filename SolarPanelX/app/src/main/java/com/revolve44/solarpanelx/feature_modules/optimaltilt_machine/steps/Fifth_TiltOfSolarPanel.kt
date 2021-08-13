package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.steps

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.OptimalOrientationHelperActivity
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.tools.TiltSuggester
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels.OrientationSolarPanelViewModel
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.core.roundTo1decimials
import com.revolve44.solarpanelx.global_utils.Constants.Companion.is_TYPE_ROTATION_VECTOR_SELECTED
import kotlin.math.abs


class Fifth_TiltOfSolarPanel : Fragment(R.layout.sm_fragment_tilt_of_solar_panel) {
    private var txtOptimalTiltView :TextView? = null
    private var txtActualTiltView : TextView? = null
    private var pitcherSolarPanel : ImageView? = null

    private lateinit var btn_close_calc_tilt : TextView

    private lateinit var suggest_tilt_viewer : TextView

    private lateinit var orientationSolarPanelViewModel : OrientationSolarPanelViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtOptimalTiltView = view.findViewById(R.id.optimal_tilt_view)
        txtActualTiltView = view.findViewById(R.id.actual_tilt_view)
        pitcherSolarPanel = view.findViewById(R.id.pitch_tilt_solar_panel)

        btn_close_calc_tilt = view.findViewById(R.id.btn_close_calc_tilt)

        suggest_tilt_viewer = view.findViewById(R.id.suggest_tilt_viewer)


    }

    override fun onResume() {
        super.onResume()
        when(is_TYPE_ROTATION_VECTOR_SELECTED){
            true  -> {
                initVectorRotate()
            }
            false -> {
                initAccelerometer()
            }
        }

    }

    private fun initAccelerometer() {
        orientationSolarPanelViewModel = (activity as OptimalOrientationHelperActivity).viewModel

        orientationSolarPanelViewModel.pitchAccelerometer.observe(viewLifecycleOwner, Observer {
            rotateSolarPanelAzimuth(it)
            txtActualTiltView!!.setText("${roundTo1decimials(it)}°")

            var SUGGESTED_TILT = TiltSuggester().defineOptimalTilt(PreferenceMaestro.lat.toDouble(), TiltSuggester.Season.SUMMER)
            var PREDICTION_GOOD_OR_NOT =""
            var COLOR_PREDICTION_TILT = 0

            txtOptimalTiltView!!.text = "${SUGGESTED_TILT}"

            when(abs(SUGGESTED_TILT-(it))){
                in 0.0..5.0 ->    {
                    PREDICTION_GOOD_OR_NOT = "\uD83E\uDD29\uD83D\uDC4D"
                    COLOR_PREDICTION_TILT =Color.GREEN
                }
                in 5.0..25.0 ->   {
                    PREDICTION_GOOD_OR_NOT = "\uD83D\uDE10"
                    COLOR_PREDICTION_TILT =Color.YELLOW
                }
                in 25.0..360.0 -> {
                    PREDICTION_GOOD_OR_NOT = "\uD83D\uDC4E☹️"
                    COLOR_PREDICTION_TILT =Color.RED
                }
            }

            suggest_tilt_viewer.text = PREDICTION_GOOD_OR_NOT
            suggest_tilt_viewer.setBackgroundColor(COLOR_PREDICTION_TILT)
            if (COLOR_PREDICTION_TILT == Color.YELLOW){
                suggest_tilt_viewer.setTextColor(Color.BLACK)
            }else{
                suggest_tilt_viewer.setTextColor(Color.WHITE)
            }
        })

        btn_close_calc_tilt.setOnClickListener {
            (activity as OptimalOrientationHelperActivity).closeOptimalTiltActivity()
        }
    }

    private fun initVectorRotate() {
        orientationSolarPanelViewModel = (activity as OptimalOrientationHelperActivity).viewModel

        orientationSolarPanelViewModel.pitchRotationVector.observe(viewLifecycleOwner, Observer {
            rotateSolarPanelVector(it)
            txtActualTiltView!!.setText("${roundTo1decimials(90F+it)}°")

            var SUGGESTED_TILT = TiltSuggester().defineOptimalTilt(PreferenceMaestro.lat.toDouble(), TiltSuggester.Season.SUMMER)
            var PREDICTION_GOOD_OR_NOT =""
            var COLOR_PREDICTION_TILT = 0

            txtOptimalTiltView!!.text = "${SUGGESTED_TILT}"

            when(abs(SUGGESTED_TILT-(90F+it))){
                in 0.0..5.0 ->    {
                    PREDICTION_GOOD_OR_NOT = "\uD83E\uDD29\uD83D\uDC4D"
                    COLOR_PREDICTION_TILT =Color.GREEN
                }
                in 5.0..25.0 ->   {
                    PREDICTION_GOOD_OR_NOT = "\uD83D\uDE10"
                    COLOR_PREDICTION_TILT =Color.YELLOW
                }
                in 25.0..360.0 -> {
                    PREDICTION_GOOD_OR_NOT = "\uD83D\uDC4E☹️"
                    COLOR_PREDICTION_TILT =Color.RED
                }
            }

            suggest_tilt_viewer.text = PREDICTION_GOOD_OR_NOT
            suggest_tilt_viewer.setBackgroundColor(COLOR_PREDICTION_TILT)
            if (COLOR_PREDICTION_TILT == Color.YELLOW){
                suggest_tilt_viewer.setTextColor(Color.BLACK)
            }else{
                suggest_tilt_viewer.setTextColor(Color.WHITE)
            }
        })

        btn_close_calc_tilt.setOnClickListener {
            (activity as OptimalOrientationHelperActivity).closeOptimalTiltActivity()
        }
    }



    private fun rotateSolarPanelVector(tiltPitch : Float) {
        val an: Animation = RotateAnimation(
            tiltPitch, -tiltPitch,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        //currentAzimuth = azimuth
        an.duration = 2500
        an.repeatCount = 0
        an.fillAfter = true
        pitcherSolarPanel!!.startAnimation(an)
       // solarpanelDirection!!.startAnimation(an)
    }

    private fun rotateSolarPanelAzimuth(tiltPitch : Float) {
        val an: Animation = RotateAnimation(
            tiltPitch-90f, tiltPitch, // need that from and to must be with variables
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        //currentAzimuth = azimuth
        an.duration = 2500
        an.repeatCount = 0
        an.fillAfter = true
        pitcherSolarPanel!!.startAnimation(an)
        // solarpanelDirection!!.startAnimation(an)
    }
}