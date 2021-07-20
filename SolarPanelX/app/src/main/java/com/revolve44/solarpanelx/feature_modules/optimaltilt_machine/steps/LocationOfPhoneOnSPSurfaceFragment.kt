package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.steps

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.OptimalTiltHelperActivity
import com.revolve44.solarpanelx.R


class LocationOfPhoneOnSPSurfaceFragment : Fragment(R.layout.sm_fragment_location_of_phone_on_s_p_surface) {


    lateinit var butNext : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        butNext = view.findViewById(R.id.GoToOrientFrag)
        butNext.visibility = View.INVISIBLE

        var timer = object : CountDownTimer(4000,1000){
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                butNext.visibility = View.VISIBLE

            }

        }.start()

        butNext.setOnClickListener {
            (activity as OptimalTiltHelperActivity).viewModel.currentItemInViewPager2.value = 3
        }
    }

    companion object {

    }
}