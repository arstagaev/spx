package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.steps

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.OptimalTiltHelperActivity
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels.OrientationSolarPanelViewModel
import com.revolve44.solarpanelx.R


class CalibrateOrientationSensorFragment : Fragment() {

    private lateinit var viewMOdelOrientationSensorFragment : OrientationSolarPanelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sm_fragment_calibrate_orientation_sensor, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showGif(view)
        var cardViewBtnConfirmEndOfCalibration = (view.findViewById<CardView>(R.id.calibr_orient_sensor_confirm_btn) as CardView)

        viewMOdelOrientationSensorFragment = (activity as OptimalTiltHelperActivity).viewModel

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

    fun showGif(view: View) {
        val imageView: ImageView = view.findViewById(R.id.calibrate_orient)
        Glide.with(this).load(R.drawable.sm_calbrph3).into(imageView)


    }


}