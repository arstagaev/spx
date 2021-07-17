package com.revolve44.solarpanelx.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.ui.customviews.daylightmap.WeatherAnim


class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WeatherAnim.quickSetup()
        WeatherAnim.geometryL()


    }



    companion object {

    }
}