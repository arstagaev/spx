package com.revolve44.solarpanelx.ui.fragments.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.base.materialdialog.BaseMaterialDialogFragment
import com.revolve44.solarpanelx.domain.core.setLocale
import com.revolve44.solarpanelx.domain.enums.LanguagesOfApp
import kotlin.collections.ArrayList


class DialogFragmentForChangeLanguage : BaseMaterialDialogFragment() {
    private lateinit var mListView: ListView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var array: ArrayList<String> = ArrayList()
        //arrayOf("PV Stations Characteristics", "Check Updates","Pro Version","About Us")
        //array.add("Future Updates")
        array.add("English              ")
        array.add("Hindi (हिन्दी)          ")
        //array.add(getString(R.string.settingsscreen_menu_proversion))
        //array.add("German (Deutsch)     ")
        //array.add("Russian (Русский)    ")
        // access the listView from xml file
        mListView = view.findViewById<ListView>(R.id.dialog_change_language_list_view)
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_single_choice,
            array
        )

        mListView.adapter = arrayAdapter
        when(PreferenceMaestro.languageOfApp){
            LanguagesOfApp.ENGLISH.id -> mListView.setItemChecked(0,true)
            LanguagesOfApp.HINDI.id ->   mListView.setItemChecked(1,true)
            LanguagesOfApp.GERMAN.id ->  mListView.setItemChecked(2,true)
            LanguagesOfApp.RUSSIAN.id -> mListView.setItemChecked(3,true)
        }

        mListView.setOnItemClickListener { parent, view, position, id ->
            //Toast.makeText(activity,"pos $position",Toast.LENGTH_SHORT).show()
            when(position){
                0 -> {
                    Log.d("ttt","hey0")
                    setLocale(requireActivity(),LanguagesOfApp.ENGLISH.id)
                    PreferenceMaestro.languageOfApp = LanguagesOfApp.ENGLISH.id

                }
                1 -> {
                    setLocale(requireActivity(),LanguagesOfApp.HINDI.id)
                    PreferenceMaestro.languageOfApp = LanguagesOfApp.HINDI.id
                }
                2 -> {
                    setLocale(requireActivity(),LanguagesOfApp.GERMAN.id)
                    PreferenceMaestro.languageOfApp = LanguagesOfApp.GERMAN.id
                }
                3 -> {
                    setLocale(requireActivity(),LanguagesOfApp.RUSSIAN.id)
                    PreferenceMaestro.languageOfApp = LanguagesOfApp.RUSSIAN.id
                }

            }
            requireActivity().finish();
            requireActivity().startActivity(requireActivity().getIntent());

        }



    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_change_language, container, false)
    }

}