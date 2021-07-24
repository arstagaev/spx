package com.revolve44.solarpanelx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.domain.base.recyclerview.BaseAdapterCallback
import com.revolve44.solarpanelx.domain.base.recyclerview.ItemElementsDelegate
import com.revolve44.solarpanelx.feature_modules.lightsensor.LightSensorActivity
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.OptimalTiltHelperActivity
import com.revolve44.solarpanelx.ui.AddSolarStationActivity
import com.revolve44.solarpanelx.ui.adapters.ToolsMainscreenAdapter
import com.revolve44.solarpanelx.ui.models.StoriesLikeCardsInformation
import com.revolve44.solarpanelx.ui.models.ToolsRecyclerviewModel
import timber.log.Timber

class ToolsManagerFragment : Fragment(R.layout.fragment_tools_manager) {

    private lateinit var recyclerViewTools: RecyclerView
    private lateinit var settingsMain : ImageView

    private val mAdapter =  ToolsMainscreenAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewTools = view.findViewById(R.id.tools_screen_recycler_view)
        settingsMain = view.findViewById(R.id.settings_main_tools_screen)
        settingsMain.setOnClickListener {
              findNavController().navigate(R.id.action_toolsManagerFragment_to_settings_mainscreen)
        }
        initRecyclerViewWithMapAndNums()

    }

    private fun initRecyclerViewWithMapAndNums() {
        var forRecyclerviewAdapter = arrayListOf(
            ToolsRecyclerviewModel("My PV Station","",0,ContextCompat.getDrawable(requireActivity(), R.drawable.sp)),

            ToolsRecyclerviewModel("Optimal Tilt","Status",1,
                ContextCompat.getDrawable(requireActivity(), R.drawable.optimal_tilt)),
            ToolsRecyclerviewModel("Light Sensor","Status",1,ContextCompat.getDrawable(requireActivity(), R.drawable.solar_sensor)),
            ToolsRecyclerviewModel("Calibrating","Status3",12,ContextCompat.getDrawable(requireActivity(), R.drawable.calibr_aim)),
            //ToolsRecyclerviewModel("Reminders about bad weather","Status3",12,1234),

        )

        mAdapter.attachCallback(object : BaseAdapterCallback<ToolsRecyclerviewModel> {
            override fun onItemClick(model: ToolsRecyclerviewModel, view: View) {
                Timber.i("clickeddd is ${model.toString()}")
               // if (model.idOfCard == 0){
               //     DialogFragmentMap().show(childFragmentManager,"open_map_more_detail")
               // }
                when(model.name.toString()){
                    "My PV Station" -> {}
                    "Optimal Tilt" -> {
                        val intent = Intent(requireActivity(),OptimalTiltHelperActivity::class.java)
                        startActivity(intent)
                    }
                }

            }

            override fun onLongClick(model: ToolsRecyclerviewModel, view: View): Boolean {
                return false
            }

        })
        mAdapter.attachDelegate(object : ItemElementsDelegate<ToolsRecyclerviewModel> {
            override fun onElementClick(model: ToolsRecyclerviewModel, view: View, clickedPosition: Int) {
                Timber.i("clickeddd is $clickedPosition")
                when(model.name){
                    "My PV Station" -> {
                        val intent = Intent(requireActivity(),AddSolarStationActivity::class.java)
                        startActivity(intent)
                    }
                    "Optimal Tilt" -> {
                        val intent = Intent(requireActivity(),OptimalTiltHelperActivity::class.java)
                        startActivity(intent)
                    }
                    "Light Sensor" -> {
                        val intent = Intent(requireActivity(),LightSensorActivity::class.java)
                        startActivity(intent)
                    }
                    "Calibrating" -> {
                        findNavController().navigate(R.id.action_toolsManagerFragment_to_forecast_calibrating_main)
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
}