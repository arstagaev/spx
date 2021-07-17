//package com.revolve44.solarpanelx.ui.fragments.features
//
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.View
//import android.widget.LinearLayout
//import android.widget.TextView
//import com.google.android.material.snackbar.Snackbar
//import com.revolve44.solarpanelx.activity.MainActivity
//import com.revolve44.solarpanelx.R
//import com.revolve44.solarpanelx.core.*
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
//
//
//class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
//
//    private lateinit var yearlyGeneration : TextView
//    private lateinit var solarRadiation : TextView
//    private lateinit var solarPanelArea : TextView
//    private lateinit var paybackPeriod : TextView
//    private lateinit var willSavePerYear : TextView
//    private lateinit var place_current : TextView
//
//    private lateinit var paybackPeriodSign : LinearLayout
//    private lateinit var solarPanelAreaSign : LinearLayout
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).showProgressBar(getString(R.string.statistics_screen_title))
//
//        yearlyGeneration = view.findViewById(R.id.year_generation)
//        solarRadiation = view.findViewById(R.id.solar_radiation)
//        solarPanelArea = view.findViewById(R.id.solar_panel_area)
//        paybackPeriod = view.findViewById(R.id.payback_period)
//        willSavePerYear = view.findViewById(R.id.save_per_year)
//        place_current = view.findViewById(R.id.place_for)
//        paybackPeriodSign = view.findViewById(R.id.payback_period_sign)
//        solarPanelAreaSign = view.findViewById(R.id.solar_panel_area_layout)
//        showSigns()
//        activateClickListeners()
//
//    }
//
//    private fun activateClickListeners() {
//        paybackPeriodSign.setOnClickListener {
//            if (getPaybackPeriod()<0){
//                Snackbar.make(
//                    requireActivity().findViewById(android.R.id.content),
//                    getString(R.string.ifpayback_period_60yrs),
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }else{
//                Snackbar.make(
//                    requireActivity().findViewById(android.R.id.content),
//                    getString(R.string.paybackperiod_sign_helper)+" ${PreferenceMaestro.investmentsToSolarStation}${PreferenceMaestro.chosenCurrency}",
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//
//        }
//
//        solarPanelAreaSign.setOnClickListener {
////            Snackbar.make(
////                requireActivity().findViewById(android.R.id.content),
////                getString(R.string.paybackperiod_sign_helper)+" ${PreferenceMaestro.investmentsToSolarStation}${PreferenceMaestro.chosenCurrency}",
////                Snackbar.LENGTH_LONG
////            ).show()
//        }
//
//    }
//
//    private fun showSigns() {
//        place_current.text = getString(R.string.statistics_screen_label_itsforecast)+" ${PreferenceMaestro.chosenStationCITY}"
//
//        yearlyGeneration.text = "${scaleOfkWh(getYearlyGeneration(),false)}"
//        solarRadiation.text = "${getSolarRadiationInDefineLocation()}w/m2"
//        solarPanelArea.text = "${getSolarPanelArea()}m2"
//        if (getPaybackPeriod()<0){
//            paybackPeriod.text = "60+ yrs"
//        }else{
//            paybackPeriod.text = "${getPaybackPeriod()}yrs"
//        }
//
//        willSavePerYear.text = "${roundTo1decimials((getYearlyGeneration()/1000) *PreferenceMaestro.pricePerkWh)}${PreferenceMaestro.chosenCurrency}"
//    }
//
//}
