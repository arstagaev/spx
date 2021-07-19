package com.revolve44.solarpanelx.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.domain.base.recyclerview.BaseAdapterCallback
import com.revolve44.solarpanelx.ui.adapters.ToolsMainscreenAdapter
import com.revolve44.solarpanelx.ui.models.ToolsRecyclerviewModel
import timber.log.Timber

class ToolsManagerFragment : Fragment(R.layout.fragment_tools_manager) {

    private lateinit var recyclerViewTools: RecyclerView

    private val mAdapter =  ToolsMainscreenAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewTools = view.findViewById(R.id.tools_screen_recycler_view)
        initRecyclerViewWithMapAndNums()

    }

    private fun initRecyclerViewWithMapAndNums() {
        var forRecyclerviewAdapter = arrayListOf(
            ToolsRecyclerviewModel("My PV Station","",0,R.drawable.ic_solar_panel),
            //ToolsRecyclerviewModel("PV Statistics","Status3",12,1234),
            //ToolsRecyclerviewModel("HI","Status",1,123),
            //ToolsRecyclerviewModel("HI3","Status3",12,1234),
            //ToolsRecyclerviewModel("HI","Status",1,123),
            //ToolsRecyclerviewModel("HI3","Status3",12,1234),
            //ToolsRecyclerviewModel("HI","Status",1,123),
            //ToolsRecyclerviewModel("HI3","Status3",12,1234)
        )

        mAdapter.attachCallback(object : BaseAdapterCallback<ToolsRecyclerviewModel> {
            override fun onItemClick(model: ToolsRecyclerviewModel, view: View) {
                Timber.i("clickeddd is ${model.toString()}")
               // if (model.idOfCard == 0){
               //     DialogFragmentMap().show(childFragmentManager,"open_map_more_detail")
               // }

            }

            override fun onLongClick(model: ToolsRecyclerviewModel, view: View): Boolean {
                return false
            }

        })
//        mAdapter.attachDelegate(object : ItemElementsDelegate<StoriesLikeCardsInformation> {
//            override fun onElementClick(model: StoriesLikeCardsInformation, view: View, clickedPosition: Int) {
//                Timber.i("clickeddd is $clickedPosition")
//
//                if (clickedPosition == 0){
//                    DialogFragmentMap().show(childFragmentManager,"open_map_more_detail")
//
//                }
//
//            }
//
//            override fun onItemClick(item: StoriesLikeCardsInformation) {
//
//            }
//
//        })
//
//        viewmodel.percentGeneration.observe(viewLifecycleOwner, Observer {
//            updateRecyclerviewMapAndNums(it)
//        })

        //moodDayForecastIndicator = generateMoodForecast()
        mAdapter.updateItems(forRecyclerviewAdapter)
        recyclerViewTools.adapter = mAdapter
        recyclerViewTools.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        recyclerViewTools.setHasFixedSize(false)



    }
}