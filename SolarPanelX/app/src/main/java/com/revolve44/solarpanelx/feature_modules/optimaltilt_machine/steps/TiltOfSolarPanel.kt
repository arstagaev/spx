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
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.OptimalTiltHelperActivity
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.tools.TiltSuggestor
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels.OrientationSolarPanelViewModel
import com.revolve44.solarpanelx.R
import kotlin.math.abs


class TiltOfSolarPanel : Fragment(R.layout.sm_fragment_tilt_of_solar_panel) {
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
        initOrientationModule()
    }

    private fun initOrientationModule() {
        orientationSolarPanelViewModel = (activity as OptimalTiltHelperActivity).viewModel

        orientationSolarPanelViewModel.pitch.observe(viewLifecycleOwner, Observer {
            rotateSolarPanel(it)
            txtActualTiltView!!.setText("${90F+it}°")

            var SUGGESTED_TILT = TiltSuggestor().defineOptimalTilt(53.3, TiltSuggestor.Season.SUMMER)
            var PREDICTION_GOOD_OR_NOT =""
            var COLOR_PREDICTION_TILT = 0

            txtOptimalTiltView!!.text = "${SUGGESTED_TILT}"

            when(abs(SUGGESTED_TILT-(90F+it))){
                in 0.0..5.0 ->    {
                    PREDICTION_GOOD_OR_NOT = "Excellent"
                    COLOR_PREDICTION_TILT =Color.GREEN
                }
                in 5.0..25.0 ->   {
                    PREDICTION_GOOD_OR_NOT = "Good"
                    COLOR_PREDICTION_TILT =Color.YELLOW
                }
                in 25.0..360.0 -> {
                    PREDICTION_GOOD_OR_NOT = "Bad"
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
            orientationSolarPanelViewModel.currentItemInViewPager2.value = 2
        }
    }

    private fun rotateSolarPanel(tiltPitch : Float) {
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
}