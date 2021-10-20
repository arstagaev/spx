package com.revolve44.solarpanelx.ui.fragments.features


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.revolve44.solarpanelx.BuildConfig
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.ui.fragments.dialog.DialogFragmentForChangeLanguage
import com.revolve44.solarpanelx.ui.MainActivity


/**
 * SettingsFragment
 */

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var setCharacteristics : Button
    private lateinit var mListView: ListView
    private lateinit var screenSettings : ConstraintLayout
   //private var isNigntMode : Boolean = PreferenceMaestro.isNightNode


    private fun switchNightMode(){
        if (PreferenceMaestro.isNightNode){
            screenSettings.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.black_spoke
                )
            )
        }else{
            screenSettings.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.hint_white
                )
            )
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as MainActivity).showProgressBar(getString(R.string.navigation_drawer_settings))
        screenSettings = view.findViewById(R.id.screen_settings)

        switchNightMode()
        showSettingsList_standart_version(view)


    }

    private fun showSettingsList_standart_version(view: View) {
        var array: ArrayList<String> = ArrayList()
        //arrayOf("PV Stations Characteristics", "Check Updates","Pro Version","About Us")
        //array.add("Future Updates")
        array.add(getString(R.string.settingsscreen_menu_checkupdates))
        array.add(getString(R.string.settingsscreen_menu_solarpanels_change_language))
        //array.add(getString(R.string.settingsscreen_menu_proversion))
        array.add(getString(R.string.settingsscreen_menu_aboutus))
        array.add("Terminal")
        // access the listView from xml file
        mListView = view.findViewById<ListView>(R.id.settings_list)

        val arrayAdapter = ArrayAdapter(
            activity as MainActivity,
            R.layout.settings_list_item,
            array
        )

        mListView.adapter = arrayAdapter

        mListView.setOnItemClickListener { parent, view, position, id ->
            //Toast.makeText(activity,"pos $position",Toast.LENGTH_SHORT).show()
            when(position){

                0 -> goToUrl("https://play.google.com/store/apps/details?id=com.revolve44.solarpanelx")

                1-> DialogFragmentForChangeLanguage().show(childFragmentManager,"dialog_change_lang")
                2 ->{
                     Snackbar.make(requireActivity().findViewById(android.R.id.content), "SPX v.${BuildConfig.VERSION_NAME} App for solar panels #1", Snackbar.LENGTH_SHORT).show()
                     findNavController().navigate(R.id.action_settings_mainscreen_to_about_fragment)
                }
                3 ->{
                    findNavController().navigate(R.id.action_settings_mainscreen_to_terminal)
                    //Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.new_feature_coming_soon2), Snackbar.LENGTH_SHORT).show()

                }
            }
        }
    }


    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    private fun showDialogAboutUs(){
        var version = ""
        try {
            val pInfo = requireActivity().packageManager?.getPackageInfo(requireActivity().packageName, 0)
             version = pInfo?.versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        //Timber.i("vvv9 "+version)

        val alertDialog: AlertDialog = AlertDialog.Builder(activity).create()
//        if (!Constants.IS_PRO_VERSION){
//
//        }else{
//            alertDialog.setTitle("Solar Panel X Pro \n(v." + version + "PRO)")
//        }
        alertDialog.setTitle(getString(R.string.app_name)+"\n (v." + version + ")")
        BuildConfig.APPLICATION_ID

        alertDialog.setMessage(getString(R.string.settingsscreen_menu_aboutus_message))
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Package info", DialogInterface.OnClickListener {
//                dialog,
//                which -> dialog.dismiss()
//        Toast.makeText(requireActivity(),""+BuildConfig.APPLICATION_ID,Toast.LENGTH_SHORT).show()})

//        alertDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE, "Don't show again",
//            DialogInterface.OnClickListener { dialog, which ->
//                editor.putBoolean("showagain", false)
//                editor.apply()
//                dialog.dismiss()
//            })
        alertDialog.show()

    }


}
