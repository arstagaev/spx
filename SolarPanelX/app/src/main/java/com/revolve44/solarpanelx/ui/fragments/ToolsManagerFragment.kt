package com.revolve44.solarpanelx.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.base.recyclerview.ItemElementsDelegate
import com.revolve44.solarpanelx.feature_modules.lightsensor.LightSensorActivity
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.OptimalOrientationHelperActivity
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.CURRENT_TIME_OF_DAY
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.is_LIGHT_MODE
import com.revolve44.solarpanelx.global_utils.enums.TypeOfSky
import com.revolve44.solarpanelx.global_utils.toastShow
import com.revolve44.solarpanelx.ui.AddSolarStationActivity
import com.revolve44.solarpanelx.ui.MainActivity
import com.revolve44.solarpanelx.ui.adapters.ToolsMainscreenAdapter
import com.revolve44.solarpanelx.ui.models.StoriesLikeCardsInformation
import com.revolve44.solarpanelx.ui.models.ToolsRecyclerviewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timerTask


class ToolsManagerFragment : Fragment(R.layout.fragment_tools_manager) {

    private lateinit var recyclerViewTools: RecyclerView
    private lateinit var settingsMain : ImageView
    private lateinit var mode_indicator_in_tool_manager : TextView
    private lateinit var rateAppSign          : CardView
    private lateinit var rateAppSignCloseButt : ImageView
    private lateinit var tool_manager_layout : ConstraintLayout
    private lateinit var scroll_view_tools : ScrollView
    private lateinit var textSwitcher_main_screen : TextView

    private lateinit var rate_app_signLikeIt     : Button
    private lateinit var rate_app_signDontLikeIt : Button


    private val mAdapter =  ToolsMainscreenAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewTools = view.findViewById(R.id.tools_screen_recycler_view)
        settingsMain = view.findViewById(R.id.settings_main_tools_screen)
        mode_indicator_in_tool_manager = view.findViewById(R.id.mode_indicator_in_tool_manager)

        rateAppSign           = view.findViewById(R.id.rate_app_sign)
        rateAppSignCloseButt  = view.findViewById(R.id.rate_app_sign_button)

        rate_app_signLikeIt = view.findViewById(R.id.rate_app_sign_like_it)
        rate_app_signDontLikeIt = view.findViewById(R.id.rate_app_sign_dont_like)

        scroll_view_tools = view.findViewById(R.id.scroll_view_tools)
        tool_manager_layout = view.findViewById(R.id.tool_manager_layout)
        textSwitcher_main_screen = view.findViewById(R.id.textSwitcher_main_screen)

        settingsMain.setOnClickListener {
              findNavController().navigate(R.id.action_toolsManagerFragment_to_settings_mainscreen)
        }
        GlobalScope.launch(Dispatchers.Main) {
            initRecyclerViewWithMapAndNums()
        }

        when(is_LIGHT_MODE){
            false ->{  mode_indicator_in_tool_manager.text = "Standard Mode"            }
            true  ->{  mode_indicator_in_tool_manager.text = "Light UI Mode - Enabled"  }
        }

        if (PreferenceMaestro.isInvisibleFeedbackSign){
            rateAppSign.visibility = View.GONE
        }else{
            rateAppSign.visibility = View.VISIBLE
        }
        rateAppSignCloseButt.setOnClickListener {
            rateAppSign.visibility = View.GONE
            PreferenceMaestro.isInvisibleFeedbackSign = true
        }

        // like or not
        rate_app_signLikeIt.setOnClickListener { //https://play.google.com/store/apps/details?id=com.revolve44.solarpanelx

            //(activity as MainActivity).specialToast()

            try {
                toastShow("Please, write a  5⭐ review \uD83D\uDE0E", Color.GREEN,requireActivity())
//                Timer().schedule(timerTask {
//                    toastShow("Thank u\uD83D\uDE0A", Color.GREEN,requireActivity())
//                }, 2000)

            }catch (e: Exception){

                Toast.makeText(
                    requireActivity(),
                    "Please, write a  5⭐ review \uD83D\uDE0E",
                    Toast.LENGTH_SHORT
                ).show()
            }
            goToUrl("https://play.google.com/store/apps/details?id=com.revolve44.solarpanelx")
            Toast.makeText(
                requireActivity(),
                "Thank u \uD83D\uDE0A",
                Toast.LENGTH_SHORT
            ).show()
        }
        rate_app_signDontLikeIt.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("info@revolna.com"))
            i.putExtra(Intent.EXTRA_SUBJECT, "Suggestion for improving the application")
            i.putExtra(Intent.EXTRA_TEXT, " Write E-MAIl\uD83D\uDCE7 \uD83D\uDCE8")
            Toast.makeText(
                requireActivity(),
                "Please write some suggestions for improving the application",
                Toast.LENGTH_SHORT
            ).show()

            try {
                startActivity(Intent.createChooser(i, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    requireActivity(),
                    "There are no email clients installed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun initRecyclerViewWithMapAndNums() {
        var forRecyclerviewAdapter = arrayListOf<ToolsRecyclerviewModel>()

        when(is_LIGHT_MODE){
           true -> {
               forRecyclerviewAdapter = arrayListOf(
                   ToolsRecyclerviewModel(0,getString(R.string.My_PV_Station),"",0,null),
                   ToolsRecyclerviewModel(1,getString(R.string.Optimal_Tilt),"Status",1, null),
                   ToolsRecyclerviewModel(3,getString(R.string.Calibrating),"Status3",12,null),
                   ToolsRecyclerviewModel(2,getString(R.string.maintools_screen_title_ight_sensor),"Status",1,null),

                   // ToolsRecyclerviewModel(4,getString(R.string.Bad_Weather_Alerts),"Status3",12,ContextCompat.getDrawable(requireActivity(), R.drawable.alert_ic)),
                   ToolsRecyclerviewModel(4,getString(R.string.maintools_screen_title_Bad_Weather_Alerts),"Status3",12,null),
                   ToolsRecyclerviewModel(5,getString(R.string.maintools_screen_title_nn),"Status3",12,null)

               )
           }
           false -> {
               forRecyclerviewAdapter = arrayListOf(
                   ToolsRecyclerviewModel(0,getString(R.string.My_PV_Station),"",0,ContextCompat.getDrawable(requireActivity(), R.drawable.ic_solar_panel_logo)),
                   ToolsRecyclerviewModel(1,getString(R.string.Optimal_Tilt),"Status",1, ContextCompat.getDrawable(requireActivity(), R.drawable.ic_logo_optimal_tilt)),
                   ToolsRecyclerviewModel(3,getString(R.string.Calibrating),"Status3",12,ContextCompat.getDrawable(requireActivity(), R.drawable.ic_calibrating_logo)),
                   ToolsRecyclerviewModel(2,getString(R.string.maintools_screen_title_ight_sensor),"Status",1,ContextCompat.getDrawable(requireActivity(), R.drawable.ic_light_sensor_logo)),

                   // ToolsRecyclerviewModel(4,getString(R.string.Bad_Weather_Alerts),"Status3",12,ContextCompat.getDrawable(requireActivity(), R.drawable.alert_ic)),
                   ToolsRecyclerviewModel(4,getString(R.string.maintools_screen_title_Bad_Weather_Alerts),"Status3",12,ContextCompat.getDrawable(requireActivity(), R.drawable.ic_bad_weather_logo)),
                   ToolsRecyclerviewModel(5,getString(R.string.maintools_screen_title_nn),"Status3",12,ContextCompat.getDrawable(requireActivity(), R.drawable.ic_nn_logo))

               )
           }
        }


//        mAdapter.attachCallback(object : BaseAdapterCallback<ToolsRecyclerviewModel> {
//            override fun onItemClick(model: ToolsRecyclerviewModel, view: View) {
//                Timber.i("clickeddd is ${model.toString()}")
//               // if (model.idOfCard == 0){
//               //     DialogFragmentMap().show(childFragmentManager,"open_map_more_detail")
//               // }
////                when(model.name.toString()){
////                    "My PV Station" -> {}
////                    "Optimal Tilt" -> {
////                        val intent = Intent(requireActivity(),OptimalTiltHelperActivity::class.java)
////                        startActivity(intent)
////                    }
////                }
//
//            }
//
//            override fun onLongClick(model: ToolsRecyclerviewModel, view: View): Boolean {
//                return false
//            }
//
//        })
        mAdapter.attachDelegate(object : ItemElementsDelegate<ToolsRecyclerviewModel> {
            override fun onElementClick(model: ToolsRecyclerviewModel, view: View, clickedPosition: Int) {
                Timber.i("clickeddd is $clickedPosition")
                when(model.id){
                    0 -> {
                        val intent = Intent(requireActivity(),AddSolarStationActivity::class.java)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(requireActivity(),OptimalOrientationHelperActivity::class.java)
                        startActivity(intent)
                    }
                    2-> {
                        val intent = Intent(requireActivity(),LightSensorActivity::class.java)
                        startActivity(intent)
                    }
                    3 -> {
                        findNavController().navigate(R.id.action_toolsManagerFragment_to_forecast_calibrating_main)
                    }
                    4 -> {
                        findNavController().navigate(R.id.action_tlMng_to_notification_manage)
                        //Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.new_feature_coming_soon), Snackbar.LENGTH_SHORT).show()
                    }
                    5 -> {
                        //Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.new_feature_coming_soon2), Snackbar.LENGTH_SHORT).show()
                        Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.new_feature_coming_soon_plump), Snackbar.LENGTH_SHORT).show()
                    }

                }

//                when(clickedPosition){
//                    0 -> {
//                        val intent = Intent(requireActivity(),AddSolarStationActivity::class.java)
//                        startActivity(intent)
//                    }
//                    1 -> {
//                        val intent = Intent(requireActivity(),OptimalTiltHelperActivity::class.java)
//                        startActivity(intent)
//                    }
//                    2 -> {
//                        val intent = Intent(requireActivity(),LightSensorActivity::class.java)
//                        startActivity(intent)
//                    }
//                    3 -> {
//                        findNavController().navigate(R.id.action_toolsManagerFragment_to_forecast_calibrating_main)
//                    }
//
//                }

            }
            override fun onItemClick(item: StoriesLikeCardsInformation) {

            }

        })


        //moodDayForecastIndicator = generateMoodForecast()
        mAdapter.updateItems(forRecyclerviewAdapter)
        recyclerViewTools.adapter = mAdapter
        recyclerViewTools.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        recyclerViewTools.setHasFixedSize(false)



    }

    override fun onResume() {
        super.onResume()
        changeSkyEntourage()
    }

    private fun changeSkyEntourage() {
        when(CURRENT_TIME_OF_DAY.typeOfSky){
            TypeOfSky.NIGHT -> {
                scroll_view_tools.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black_night))
                tool_manager_layout.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black_night))
                textSwitcher_main_screen.setTextColor(Color.WHITE)
            }
            else ->{
                scroll_view_tools.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
                tool_manager_layout.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
                textSwitcher_main_screen.setTextColor(Color.BLACK)
            }
        }

    }

    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }
}