package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.steps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.OptimalTiltHelperActivity
import com.revolve44.solarpanelx.R


class Second_CalcHelperFragment : Fragment(R.layout.sm_fragment_calc_helper) {

    private lateinit var imagePhoneSideView : ImageView
    private lateinit var imagePhoneFrontView : ImageView
    private lateinit var btnNextAfterOrientation : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnNextAfterOrientation = view.findViewById(R.id.btnNextAfterOrientation)
        imagePhoneSideView = view.findViewById(R.id.helper_phone_tilt)
        imagePhoneFrontView = view.findViewById(R.id.helper_phone_azimuth)

        btnNextAfterOrientation.setOnClickListener {

            (activity as OptimalTiltHelperActivity).viewModel.currentItemInViewPager2.value = 2
        }

    }

    override fun onResume() {
        super.onResume()
        playAnimations()
    }

    fun playAnimations(){
        val an: Animation = RotateAnimation(
            -50F, 50F,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )

        an.duration = 500
        an.repeatCount = -1
        an.fillAfter = true
        an.repeatMode = 2


        val an2: Animation = RotateAnimation(
            0F, 360F,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )

        an2.duration = 5000
        an2.repeatCount = -1
        an2.fillAfter = true
        an2.repeatMode = 1

        imagePhoneSideView.startAnimation(an)
        imagePhoneFrontView.startAnimation(an2)
    }



    companion object {

    }
}