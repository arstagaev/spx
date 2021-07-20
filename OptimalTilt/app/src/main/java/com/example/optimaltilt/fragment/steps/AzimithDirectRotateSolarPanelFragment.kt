package com.example.optimaltilt.fragment.steps

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.optimaltilt.MainActivity
import com.example.optimaltilt.R
import com.example.optimaltilt.model.HowWellDirectedSolarPanel
import com.example.optimaltilt.tools.AzimuthToNorthSouthFormatterAndSuggestor
import com.example.optimaltilt.tools.Compass
import com.example.optimaltilt.tools.SOTWFormatter
import com.example.optimaltilt.viewmodels.OrientationSolarPanelViewModel

class AzimithDirectRotateSolarPanelFragment : Fragment(R.layout.fragment_azimith_direct_rotate_solar_panel) {
    private val TAG = "CompassActivity"

    private var compass: Compass? = null
    private var sotwFormatter: SOTWFormatter? = null

    private var sotwLabel: TextView? = null           // SOTW is for "side of the world"

    private var azi: TextView? = null

    private var currentAzimuthArrow = 0f
    private var currentAzimuthSolarPanel = 0f

    private lateinit var orientationViewModel : OrientationSolarPanelViewModel

    private lateinit var btnDirectionMode    : CardView
    private lateinit var btnCompassMode      : CardView
    private lateinit var actualDirectionView : TextView
    private var isPanelOrientationView : Boolean = true

    private var arrowView           : ImageView? = null
    private var solarpanelDirection : ImageView? = null
    private var compassClockFace    : ImageView? = null

    private lateinit var optimal_azimuth_suggested : TextView
    private lateinit var suggest_azimuth : TextView
    private var LATITUDE = 0F

    lateinit var defineWhichCloseToOptimal : HowWellDirectedSolarPanel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        solarpanelDirection = view.findViewById<ImageView>(R.id.solar_panel_direction)
        arrowView = view.findViewById<ImageView>(R.id.main_image_hands)
        //sotwLabel = view.findViewById<TextView>(R.id.sotw_label)
        azi =       view.findViewById<TextView>(R.id.azimuths_view)
        //sotwLabel = view.findViewById(R.id.actual_direction_view)
        compassClockFace = view.findViewById(R.id.main_image_dial)

        //btnDirectionMode   = view.findViewById(R.id.solar_panel_direction_azimuth_btn)
        btnCompassMode     = view.findViewById(R.id.compass_btn)

        actualDirectionView   = view.findViewById(R.id.actual_direction_view)
        optimal_azimuth_suggested = view.findViewById(R.id.optimal_azimuth_suggested)

        suggest_azimuth = view.findViewById(R.id.suggest_azimuth)

        sotwFormatter = SOTWFormatter(requireActivity())

        btnCompassMode.setOnClickListener {
            orientationViewModel.currentItemInViewPager2.value = 4

        }


        LATITUDE = -39F
        orientationViewModel = (activity as MainActivity).viewModel

        orientationViewModel.directionOfSolarPanel.observe(viewLifecycleOwner, Observer {

            adjustArrow(it)
            rotateSolarPanel(it)

            actualDirectionView.setText(sotwFormatter?.format(-it))


            azi?.setText("azimuth= $it")

            defineWhichCloseToOptimal = AzimuthToNorthSouthFormatterAndSuggestor().defineWhichCloseToOptimal(LATITUDE,it)

            suggest_azimuth.text = defineWhichCloseToOptimal.word
            suggest_azimuth.setBackgroundColor(defineWhichCloseToOptimal.color)

            if (defineWhichCloseToOptimal.color == Color.YELLOW){
                suggest_azimuth.setTextColor(Color.BLACK)
            }else{
                suggest_azimuth.setTextColor(Color.WHITE)
            }


        })
        optimal_azimuth_suggested.text = AzimuthToNorthSouthFormatterAndSuggestor().findOptimalDirection(LATITUDE)

        //orientationViewModel.suggestedAzimuthForSolarPanel.value = AzimuthToNorthSouthFormatterAndSuggestor().findOptimalDirection(53F)
        //switcherOfDirectionMode()


    }

    override fun onResume() {
        super.onResume()
        if (AzimuthToNorthSouthFormatterAndSuggestor().findOptimalDirection(LATITUDE)== "N"){
            solarpanelDirection!!.rotation = 180F
        }else{
            solarpanelDirection!!.rotation = 0F
        }
    }

    private fun adjustArrow(azimuth: Float) {
        val an: Animation = RotateAnimation(
            -currentAzimuthArrow, azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuthArrow = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true

        //arrowView!!.startAnimation(an)
        arrowView!!.startAnimation(an)
    }

    fun rotateSolarPanel(azimuth: Float){
        val an2: Animation = RotateAnimation(
            -currentAzimuthSolarPanel, azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuthSolarPanel = azimuth
        an2.duration = 500
        an2.repeatCount = 0
        an2.fillAfter = true



        solarpanelDirection!!.startAnimation(an2)
    }
}