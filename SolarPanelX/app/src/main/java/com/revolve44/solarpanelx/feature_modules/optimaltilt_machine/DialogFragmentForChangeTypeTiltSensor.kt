package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.base.materialdialog.BaseMaterialDialogFragment
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.is_TYPE_ROTATION_VECTOR_SELECTED
import kotlin.collections.ArrayList


class DialogFragmentForChangeTypeTiltSensor : BaseMaterialDialogFragment() {

    private lateinit var mListView: ListView

    private lateinit var okay_confirm_language : Button
    private lateinit var title_of_dialog_fragment : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var array: ArrayList<String> = ArrayList()
        //arrayOf("PV Stations Characteristics", "Check Updates","Pro Version","About Us")
        //array.add("Future Updates")
        array.add(getString(R.string.tilt_sensor_rot_vector))
        array.add(getString(R.string.tilt_sensor_accelerometer))

        // access the listView from xml file
        mListView = view.findViewById<ListView>(R.id.dialog_change_language_list_view)
        okay_confirm_language = view.findViewById(R.id.okay_confirm_language)
        title_of_dialog_fragment = view.findViewById(R.id.title_of_dialog_fragment)

        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_single_choice,
            array
        )

        mListView.adapter = arrayAdapter
        if (PreferenceMaestro.isTypeRotationSensor){
            mListView.setItemChecked(0,true)
        }else{
            mListView.setItemChecked(1,true)
        }


        mListView.setOnItemClickListener { parent, view, position, id ->
            //Toast.makeText(activity,"pos $position",Toast.LENGTH_SHORT).show()
            when(position){
                0 -> {
                    PreferenceMaestro.isTypeRotationSensor = true
                    is_TYPE_ROTATION_VECTOR_SELECTED = true
                }
                1 -> {
                    PreferenceMaestro.isTypeRotationSensor = false
                    is_TYPE_ROTATION_VECTOR_SELECTED = false
                }
            }

        }
        okay_confirm_language.setOnClickListener {
            dismiss()
        }

        title_of_dialog_fragment.text = getString(R.string.choose_type_of_sensor_solar_pan)


    }





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.dialog_fragment_change_language, container, false)

    }

}