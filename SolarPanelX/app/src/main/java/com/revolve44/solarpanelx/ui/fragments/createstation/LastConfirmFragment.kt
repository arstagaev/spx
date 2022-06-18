package com.revolve44.solarpanelx.ui.fragments.createstation

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.core.blinkATextView
import com.revolve44.solarpanelx.domain.core.getInvestmentsToPVStation
import com.revolve44.solarpanelx.domain.westcoast_customs.LockableScrollView
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations
import com.revolve44.solarpanelx.ui.AddSolarStationActivity
import com.revolve44.solarpanelx.ui.MainActivity
import io.feeeei.circleseekbar.CircleSeekBar
import net.cachapa.expandablelayout.ExpandableLayout
import timber.log.Timber


class LastConfirmFragment : Fragment(R.layout.fragment_confirm_station) {

    //lateinit var viewModelAddSolarStation: ViewModelAddSolarStation
    private lateinit var circleseekbar_efficiency: CircleSeekBar
    private lateinit var efficiency_indicator: TextView

    private lateinit var circleseekbar_installation_date: CircleSeekBar
    private lateinit var install_date_indicator: TextView


    private lateinit var nominalPowerOfStation: EditText
    private lateinit var currencySpinner: Spinner
    private lateinit var pricePerkWh: EditText

    private lateinit var investmentsToPVStation: EditText
    private lateinit var currencyInvestments: TextView

    private lateinit var confirmButton: Button
    private lateinit var scrollView: LockableScrollView

    //variables
    private var efficiencyOfSolarPanels = 0
    private var installationDate = 0
    private var nominalPowerOfPVStation = 0
    //private lateinit var firebaseAnalytics : FirebaseAnalytics

    private lateinit var expander_confirmst : ImageView

    private lateinit var relativeLayout : RelativeLayout
    private lateinit var relativeLayout2 : RelativeLayout

    private lateinit var advanced_expander : CardView
    private lateinit var expandable_layout : ExpandableLayout
    private lateinit var describe_about_nominal_power : TextView




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //firebaseAnalytics = Firebase.analytics


        nominalPowerOfStation = view.findViewById(R.id.nominalPowerOfStation)
        currencySpinner = view.findViewById(R.id.currencySpinner)
        pricePerkWh = view.findViewById(R.id.pricePerkWh)

        circleseekbar_efficiency = view.findViewById(R.id.circleseekbar_efficiency)
        efficiency_indicator = view.findViewById(R.id.efficiency_indicator)

        currencyInvestments = view.findViewById(R.id.currencyOfPVStation)


        circleseekbar_installation_date = view.findViewById(R.id.circleseekbar_installation_date)
        install_date_indicator = view.findViewById(R.id.install_date_indicator)

        confirmButton = view.findViewById(R.id.confirm_changes)
        scrollView = view.findViewById(R.id.scrollViewFromFragmentConfirmStation)

        investmentsToPVStation = view.findViewById(R.id.investments_to_pv_station)
        currencyInvestments = view.findViewById(R.id.currencyOfPVStation)
        advanced_expander = view.findViewById<CardView>(R.id.advanced_expander)
        expandable_layout = view.findViewById<ExpandableLayout>(R.id.expandable_layout)
        describe_about_nominal_power = view.findViewById(R.id.lux_describe_about_nominal_power)
//        expander_confirmst = view.findViewById(R.id.expander_confirmst)
//
//        relativeLayout = view.findViewById(R.id.relativeLayout)
//       // relativeLayout2 = view.findViewById(R.id.relativeLayout2)
//
//        expander_confirmst.setOnClickListener {
//
//        }

        circleseekbar_efficiency.maxProcess = 30
        circleseekbar_installation_date.maxProcess = 25



        activateCircleSeekBar()
        activateSpinnerofCurrency(view)


        confirmButton.setOnClickListener {

            try {
                if (nominalPowerOfStation.text.isNotEmpty()){
                    if((nominalPowerOfStation.text).toString().toInt()!=0 && (nominalPowerOfStation.text)!=null) {

                        saveInputCharacteristicsFromTwoFragmentsInViewPagerToPreferences()

                    } else{
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.nominal_power_cantbe_null),
                            Snackbar.LENGTH_LONG
                        ).show()
                        blinkATextView(describe_about_nominal_power, ContextCompat.getColor(requireActivity(),
                            R.color.hint_white2
                        ), Color.WHITE,ContextCompat.getColor(requireActivity(), R.color.hint_white2),1000)

                    }
                }else{
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.nominal_power_cantbe_null),
                        Snackbar.LENGTH_LONG
                    ).show()
                    blinkATextView(describe_about_nominal_power, ContextCompat.getColor(requireActivity(),
                        R.color.hint_white2
                    ), Color.WHITE,ContextCompat.getColor(requireActivity(), R.color.hint_white2),1000)

                }
            }catch (e: Exception){
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.nominal_power_cantbe_null)+" error:"+e.message,
                    Snackbar.LENGTH_LONG
                ).show()
                blinkATextView(describe_about_nominal_power, ContextCompat.getColor(requireActivity(),
                    R.color.hint_white2
                ), Color.WHITE,ContextCompat.getColor(requireActivity(), R.color.hint_white2),1000)

            }
        }
        var isExpandAdvance = true
        advanced_expander.setOnClickListener {



            when(isExpandAdvance){
                true -> expandable_layout.setExpanded(true,true)
                false -> expandable_layout.setExpanded(false,true)
            }
            isExpandAdvance = !isExpandAdvance
        }

    }

    override fun onResume() {
        super.onResume()

        loadCharacteristicsFromPreferences()

        blinkATextView(describe_about_nominal_power, ContextCompat.getColor(requireActivity(),
            R.color.hint_white2
        ), Color.WHITE,ContextCompat.getColor(requireActivity(), R.color.hint_white2),1000)

    }

    private fun activateSpinnerofCurrency(view: View) {
        var chosenPrice = 0.13f
        val values = arrayOf("$", "€", "₹", "₽", "SAR", "£", "¥")
        val spinner = view.findViewById(R.id.currencySpinner) as Spinner
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        spinner.setSelection(values.indexOf(PreferenceMaestro.chosenCurrency))
        //spinner.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.hint_white2))




        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View, pos: Int,
                id: Long
            ) {
                PreferenceMaestro.chosenCurrency = adapterView.getItemAtPosition(pos).toString()

                when(PreferenceMaestro.chosenCurrency){
                    "$"->{
                        (pricePerkWh.setText("0.13"))
                        currencyInvestments.text = "$"
                    }
                    "€"->{(pricePerkWh.setText("0.21"))
                        currencyInvestments.text = "€"
                    }
                    "₹"->{(pricePerkWh.setText("6"))
                        currencyInvestments.text = "₹"
                    }
                    "₽"->{(pricePerkWh.setText("4.4"))
                        currencyInvestments.text = "₽"
                    }
                    "SAR"->{(pricePerkWh.setText("0.18"))
                        currencyInvestments.text = "SAR"
                    }
                    "£"->{(pricePerkWh.setText("0.16"))
                        currencyInvestments.text = "£"
                    }
                    "¥"->{(pricePerkWh.setText("25"))
                        currencyInvestments.text = "¥"
                    }

                }
                investmentsToPVStation.setText(""+getInvestmentsToPVStation(nominalPowerOfStation.text,currencyInvestments.text.toString()))

//                Toast.makeText(
//                    adapter.context, PreferenceMaestro.pricePerkWh.toString() + " is chosen",
//                    Toast.LENGTH_LONG
//                ).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun activateCircleSeekBar() {
//        circleSeekBar1.curProcess = PreferenceMaestro.chosenSolarPanelEfficiency
//        circleSeekBar2.curProcess = PreferenceMaestro.chosenSolarPanelInstallationDate
        // when i touch a circleseekbar i deactivate scroll view

        circleseekbar_efficiency.setOnTouchListener { v, event ->
            if(event.action== MotionEvent.ACTION_DOWN ){
                //scrollView.isSmoothScrollingEnabled= false
                scrollView.setScrollingEnabled(false)
            }else if(event.action== MotionEvent.ACTION_UP){
                //scrollView.isSmoothScrollingEnabled = true
//                    if (circleseekbar_efficiency.curProcess<9){
//
//                    }

                scrollView.setScrollingEnabled(true)

            }
            false
        }
        circleseekbar_installation_date.setOnTouchListener { v, event ->
            if(event.action== MotionEvent.ACTION_DOWN ){
                //scrollView.isSmoothScrollingEnabled= false
                scrollView.setScrollingEnabled(false)
            }else if(event.action== MotionEvent.ACTION_UP){
                //scrollView.isSmoothScrollingEnabled = true
                scrollView.setScrollingEnabled(true)

            }
            false
        }


        circleseekbar_efficiency.setOnSeekBarChangeListener { seekbar, curValue ->
            Timber.i("New Calibration value = $curValue")

            if (curValue<9){
                circleseekbar_efficiency.curProcess = 9
                efficiency_indicator.text = "9%"
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Efficiency coeff. of solar panel must be at least 9%",
                    Snackbar.LENGTH_SHORT
                ).show()

            }else{
                efficiency_indicator.text = "${curValue}%"
                efficiencyOfSolarPanels = curValue
                PreferenceMaestro.chosenSolarPanelEfficiency = curValue
            }

            //PreferenceMaestro.calibration = curValue
        }

        circleseekbar_installation_date.setOnSeekBarChangeListener { seekbar, curValue ->
            Timber.i("New Calibration value = $curValue")

            install_date_indicator.text = "${curValue}y"
            installationDate = curValue

            PreferenceMaestro.chosenSolarPanelInstallationDate = curValue

            //PreferenceMaestro.calibration = curValue
        }
    }


    private fun loadCharacteristicsFromPreferences() {
        //nominal power
        nominalPowerOfStation.setText("" + PreferenceMaestro.chosenStationNOMINALPOWER)

        investmentsToPVStation.setText(""+PreferenceMaestro.investmentsToSolarStation)

        // Load circleseekbars efficiency and install date
        circleseekbar_efficiency.curProcess = PreferenceMaestro.chosenSolarPanelEfficiency
        efficiency_indicator.text = circleseekbar_efficiency.curProcess.toString()+"%"

        circleseekbar_installation_date.curProcess = PreferenceMaestro.chosenSolarPanelInstallationDate
        install_date_indicator.text = circleseekbar_installation_date.curProcess.toString()+"y"

        investmentsToPVStation.setText(""+getInvestmentsToPVStation(nominalPowerOfStation.text,currencyInvestments.text.toString()))

        nominalPowerOfStation.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //investmentsToPVStation.setText(""+getInvestmentsToPVStation(s,currencyInvestments.text.toString()))

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    investmentsToPVStation.setText(""+getInvestmentsToPVStation(s,currencyInvestments.text.toString()))
                }catch (e: Exception){
                    Timber.e("ERROR in nominalPowerOfStation.addTextChangedListener( object : TextWatcher{"+e.message)
                    investmentsToPVStation.setText(""+0)
                }

            }
        })
    }

    private fun saveInputCharacteristicsFromTwoFragmentsInViewPagerToPreferences(){
        try {
            //nominalPowerOfPVStation = (nominalPowerOfStation.text).toString()

            PreferenceMaestro.isFirstStart = false

            Timber.i("savedInputData: " + nominalPowerOfPVStation)
            PreferenceMaestro.chosenStationNAME = "ExampleName"
            PreferenceMaestro.chosenStationNOMINALPOWER = (nominalPowerOfStation.text).toString().toInt()
            PreferenceMaestro.chosenSolarPanelEfficiency = efficiencyOfSolarPanels
            PreferenceMaestro.chosenSolarPanelInstallationDate = installationDate
            PreferenceMaestro.pricePerkWh = (pricePerkWh.text).toString().toFloat()

            ConstantsCalculations.CURRENT_TIME_OF_DAY.isNeedAnimation = true
//            try {
//                firebaseAnalytics.logEvent("df"){
//                    param("lat_&_lon","lat: "+PreferenceMaestro.lat+" lon: "+PreferenceMaestro.lon)
//                    param("nominal_power",""+(nominalPowerOfStation.text).toString().toInt())
//                    param("chosen_currency",""+PreferenceMaestro.chosenCurrency)
//                    param("language",""+Locale.getDefault().language)
//                    param("country",""+Locale.getDefault().displayCountry)
//                }
//            }catch (e: Exception){
//                Timber.e("firebase error "+e.message)
//            }

            Timber.i("fun saveInputCharacteristicsFromTwoFragmentsInViewPagerToPreferences() is investments" +
                    "\n  "+(investmentsToPVStation.text).toString()+
            "\n ${(investmentsToPVStation.text).toString().toInt()}")

            if ((investmentsToPVStation.text).toString().toInt()<10000000){
                PreferenceMaestro.investmentsToSolarStation = (investmentsToPVStation.text).toString().toInt()
            }




            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("changingdata",true)
            startActivity(intent)
            (activity as AddSolarStationActivity).finish()


        }catch (e: Exception){

            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Please, fill correct all forms",
                Snackbar.LENGTH_LONG
            ).show()
            Timber.e("ERROR " + e.message)

        }
    }
}