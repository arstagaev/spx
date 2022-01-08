package com.revolve44.solarpanelx.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.datasource.models.db.FirstChartDataTransitor
import com.revolve44.solarpanelx.datasource.models.db.ForecastCell
import com.revolve44.solarpanelx.domain.Resource
import com.revolve44.solarpanelx.domain.core.*
import com.revolve44.solarpanelx.domain.westcoast_customs.VerticalTextView
import com.revolve44.solarpanelx.feature_modules.workmanager.model.NotificationWarningModel
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.CURRENT_TIME_OF_DAY
import com.revolve44.solarpanelx.global_utils.enums.TypeOfSky
import com.revolve44.solarpanelx.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber


class MainScreenFragment : Fragment(R.layout.fragment_main_screen) , SwipeRefreshLayout.OnRefreshListener{
    //private val values = arrayListOf<String>("0hr", "3hr", "6hr", "9hr", "12hr", "15hr", "18hr", "21hr") private val xAxisTimes_X = arrayListOf<String>("00:00","00:30","01:00","01:30","02:00","02:30", "03:00","3:30","4:00","4:30","5:00","5:30","6:00","6:30", "7:00","7:30","8:00","8:30","9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:00","21:30","22:00","22:30","23:00", "23:30")
    private val xAxisTimes_X = arrayListOf<String>("00:00","00:30","01:00","01:30","02:00","02:30", "03:00","03:30","04:00","04:30","05:00","05:30","06:00","06:30", "07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:00","21:30","22:00","22:30","23:00", "23:30")
    private val xAxisTimes_TOP = arrayListOf<String>("0:00", "3:00", "6:00", "9:00", "12:00", "15:00", "18:00", "21:00")
    private val xAxisTimes_1 = arrayListOf<String>("0:00", "3:00", "6:00", "9:00", "12:00", "15:00", "18:00", "21:00")
    private val xAxisTimes_2 = arrayListOf<String>("0:00", "3:00", "6:00", "9:00", "12:00", "15:00", "18:00", "21:00")
    private val xAxisTimes_3 = arrayListOf<String>("0:00", "3:00", "6:00", "9:00", "12:00", "15:00", "18:00", "21:00")
    private val xAxisTimes_4 = arrayListOf<String>("0:00", "3:00", "6:00", "9:00", "12:00", "15:00", "18:00", "21:00")

    private val newValues = arrayListOf<String>("0:00", "3:00", "6:00", "9:00", "12:00", "15:00", "18:00", "21:00")
    val firstChartSpecialValues : ArrayList<String>
            = arrayListOf("0:00","1:00","2:00", "3:00","4:00","5:00", "6:00","7:00","8:00", "9:00","10:00","11:00", "12:00",
        "13:00","14:00", "15:00","16:00","17:00", "18:00","19:00","20:00", "21:00","22:00","23:00")
    //private val mainViewmodelF : MainViewModel by activityViewModels()
    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
   // private val mainViewmodel : MainViewModel by viewModels()
    private var mainscreen_current_time : TextView? = null
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var lineChart1 : LineChart
    private lateinit var lineChart2 : LineChart
    private lateinit var lineChart3 : LineChart

    private lateinit var lineChart4 : LineChart
    private lateinit var lineChart5 : LineChart
    private lateinit var cardview_chart4 : CardView
    private lateinit var cardview_chart5 : CardView

    private lateinit var textSwitcher_main_screen : TextSwitcher
    private lateinit var lastUpdate : TextView
    private var mainLabelOfMainScreenInsideTxtSwitcher: TextView? = null
    private var dataForMainLabelOnMainscreen = arrayOf("⚡Forecast now:", "city: London", "temp: +23 C")
    private var stringIndex = 0
    private lateinit var forecastNowAbsol   : TextView
    private lateinit var forecastNowRelativ : TextView

    private lateinit var sunriseTime :    TextView
    private lateinit var timeOfSunShine : TextView
    private lateinit var sunsetTime :     TextView

    private lateinit var frcnowCardview : LinearLayout

    private lateinit var first_chart_description  : VerticalTextView
    private lateinit var second_chart_description : VerticalTextView
    private lateinit var third_chart_description  : VerticalTextView

    private lateinit var to_chart_forecastdescription4  : VerticalTextView
    private lateinit var to_chart_forecastdescription5  : VerticalTextView

    private lateinit var sunshine_duration_sigh : CardView

    private lateinit var cardview_forecastnow_mainscreen : CardView
    private lateinit var main_screen_background : ConstraintLayout

    private var card_item_with_map : CardView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainscreen_current_time = view.findViewById(R.id.mainscreen_current_time)

        textSwitcher_main_screen = view.findViewById(R.id.textSwitcher_main_screen)
        lineChart1 = view.findViewById<LineChart>(R.id.lineChart1)
        lineChart2 = view.findViewById<LineChart>(R.id.lineChart2)
        lineChart3 = view.findViewById<LineChart>(R.id.lineChart3)

        /** for Premium                           */
        lineChart4 = view.findViewById<LineChart>(R.id.lineChart4)
        lineChart5 = view.findViewById<LineChart>(R.id.lineChart5)
        /** for Premium                           */
        cardview_chart4 = view.findViewById(R.id.cardview_chart4)
        cardview_chart5 = view.findViewById(R.id.cardview_chart5)
        ///////////////
        forecastNowAbsol   = view.findViewById(R.id.forecast_now_absolute)
        forecastNowRelativ = view.findViewById(R.id.forecast_now_relatively)
        sunriseTime =        view.findViewById(R.id.sunshineIndicatorBlockSUNRISE)
        timeOfSunShine =     view.findViewById(R.id.sunshineIndicatorBlockDURATION)
        sunsetTime =         view.findViewById(R.id.sunshineIndicatorBlockSUNSET)

        frcnowCardview =     view.findViewById(R.id.linearlayout_frcst_now)

        sunshine_duration_sigh = view.findViewById(R.id.sunshine_duration_sigh)

        lastUpdate = view.findViewById(R.id.last_upd_date)

        card_item_with_map = view.findViewById(R.id.card_item_with_map)

        first_chart_description       = view.findViewById(R.id.first_chart_description)
        second_chart_description      = view.findViewById(R.id.to_tomorrow_chart_forecast)
        third_chart_description       = view.findViewById(R.id.to_after_tomorrow_chart_forecast)
        to_chart_forecastdescription4 = view.findViewById(R.id.to_chart_forecast4)
        to_chart_forecastdescription5 = view.findViewById(R.id.to_chart_forecast5)

        //textSwitcher_main_screen = view.findViewById(R.id.textSwitcher_main_screen)
        cardview_forecastnow_mainscreen = view.findViewById(R.id.cardview_forecastnow_mainscreen)
        main_screen_background = view.findViewById(R.id.main_screen_background)



        swipeRefreshTools(view)

        val slideInLeftAnimation: Animation = AnimationUtils.loadAnimation(
            requireActivity(),
            android.R.anim.slide_in_left
        )
        val slideOutRightAnimation: Animation = AnimationUtils.loadAnimation(
            requireActivity(),
            android.R.anim.slide_out_right
        )

        //mTextSwitcher = findViewById(R.id.textSwitcher) as TextSwitcher
        textSwitcher_main_screen.inAnimation = slideInLeftAnimation
        textSwitcher_main_screen.outAnimation = slideOutRightAnimation

        initFactoryOfMainLabel()

        changeSkyInMainScreen() // I HARE ADDED FIXME

        observersCharts()

        textSwitcher_main_screen.setOnClickListener {
            setManuallyMainLabelOnMainScreen()
        }

        cardview_forecastnow_mainscreen.setOnClickListener {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),getString(R.string.main_screen_tip_label_forecast),Snackbar.LENGTH_LONG).show()
        }

        card_item_with_map?.setOnClickListener {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),"\uD83D\uDD34"+getString(
                R.string.mainscreen_map_info
            ),Snackbar.LENGTH_LONG).show()

        }

        sunshine_duration_sigh.setOnClickListener {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),"\uD83C\uDF05"+getString(
                            R.string.sunrise)+" ~ \uD83C\uDF1E "+getString(R.string.sunshine_time) +"~ \uD83C\uDF07"+getString(
                                            R.string.sunset),Snackbar.LENGTH_LONG).show()
        }

        // checker when premium -> set premium
        switchPremium(PreferenceMaestro.isPremiumStatus)

    }

    private fun switchPremium(isPremium : Boolean){
        if (isPremium){

            cardview_chart4.visibility = View.VISIBLE
            cardview_chart5.visibility = View.VISIBLE
            to_chart_forecastdescription4.visibility = View.VISIBLE
            to_chart_forecastdescription5.visibility = View.VISIBLE

        }else{

            cardview_chart4.visibility = View.GONE
            cardview_chart5.visibility = View.GONE
            to_chart_forecastdescription4.visibility = View.GONE
            to_chart_forecastdescription5.visibility = View.GONE
        }
    }

    private fun observersCharts() {
        GlobalScope.launch(Dispatchers.Main) {
            refreshSunshineIndicatorBlock()

            //wait when viewmodel not null from activity
            (activity as MainActivity).let { it ->
                //val spxRepository = SpxRepository()
                //val viewModelFactory = MassiveViewModelProviderFactory(application,spxRepository)

                //it.viewModelMain = ViewModelProvider(it).get(MainViewModel::class.java)
                if (it.viewModelMain != null){

                    it.viewModelMain!!.getAllForecastForChart().observe(viewLifecycleOwner) {
                        if(it.isNotEmpty() && it.size == 40){
                            Timber.i("ccc input size:${it.size}")

                            sortingDataForDifferentCharts(it)

                            lastUpdate.text = getString(R.string.main_screen_last_upd)+ " "+PreferenceMaestro.timeOfLastDataUpdate
                        }

                        //initFactoryOfMainLabel()
                        changeSkyInMainScreen()


                    }

                    it.viewModelMain!!.forecastNow.observe(viewLifecycleOwner) { fNow ->
                        forecastNowRelativ.text = "${roundTo1decimials((fNow.toFloat() / PreferenceMaestro.chosenStationNOMINALPOWER.toFloat() )*100f)}%"
                        Timber.i("fnow ${fNow} ${PreferenceMaestro.calibrationCoeff}  ${PreferenceMaestro.chosenStationNOMINALPOWER}")
                        forecastNowAbsol.text = displayWattsKiloWattsInSexually( toRealFit(fNow.toFloat() * PreferenceMaestro.calibrationCoeff ) )
                        //                        CURRENT_TIME_OF_DAY.isNeedDisplay = true

                        //initFactoryOfMainLabel()
                    }

                    it.viewModelMain!!.fiveDaysRequestRes.observe(viewLifecycleOwner) {
                        setMainLabelMainScreen(it)
                        when(it){
                            is Resource.Success -> {
                                refreshSunshineIndicatorBlock()

                            }
                            is Resource.Loading -> {
                                gradientAnimation(frcnowCardview,Color.GREEN,Color.MAGENTA,Color.RED,Color.BLUE,Color.WHITE,5000)

                            }
                            is Resource.Error -> {

                            }
                        }
                    }
                    it.viewModelMain!!.timeNow.observe(viewLifecycleOwner) {
                        mainscreen_current_time?.text = it
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        CURRENT_TIME_OF_DAY.isNeedAnimation = false
    }

    override fun onResume() {
        super.onResume()

        //WeatherAnim.quickSetup()
        //WeatherAnim.geometryL()
        //val viewModelFactory = MassiveViewModelProviderFactory(application,spxRepository)
        //mainViewmodel = ViewModelProvider(this).get(MainViewModel::class.java)


    }

    private fun initFactoryOfMainLabel() {

        textSwitcher_main_screen.setFactory(ViewSwitcher.ViewFactory {
            mainLabelOfMainScreenInsideTxtSwitcher = TextView(requireActivity())
            when(CURRENT_TIME_OF_DAY.typeOfSky) {
                TypeOfSky.NIGHT -> {
                    mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(Color.WHITE)
                }
                else -> mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(Color.BLACK)
            }
            //mainLabelOfMainScreenInsideTxtSwitcher!!.setTextColor(Color.BLACK)
            try{
                val typeface = ResourcesCompat.getFont(requireActivity(), R.font.montserrat)
                mainLabelOfMainScreenInsideTxtSwitcher?.setTypeface(typeface)
            }catch (e: Exception){
                Timber.e("ERROR: ${e.message}")
            }


            mainLabelOfMainScreenInsideTxtSwitcher?.setTextSize(35f)
            //textView!!.setGravity(Gravity.START)
            mainLabelOfMainScreenInsideTxtSwitcher?.setGravity(Gravity.CENTER_VERTICAL)
            return@ViewFactory mainLabelOfMainScreenInsideTxtSwitcher
        })
    }

    private fun changeSkyInMainScreen() {
        Timber.i("aaalready chang ${CURRENT_TIME_OF_DAY.isNeedAnimation} type ${CURRENT_TIME_OF_DAY.typeOfSky}")


        when(CURRENT_TIME_OF_DAY.typeOfSky) {
            TypeOfSky.NIGHT ->{
                mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                to_chart_forecastdescription4.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                to_chart_forecastdescription5.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))


                if (!CURRENT_TIME_OF_DAY.isNeedAnimation){
                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black_night))
                }else {
                    gradientAnimationLayout(main_screen_background,
                        ContextCompat.getColor(requireActivity(), R.color.white),
                        ContextCompat.getColor(requireActivity(), R.color.black_night),2000)
                }


                //LIGHT_MODE

            }
            TypeOfSky.EARLY_MORNING ->{
                mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                to_chart_forecastdescription4.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                to_chart_forecastdescription5.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))


                if (!CURRENT_TIME_OF_DAY.isNeedAnimation){
                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.early_morning))
                }else {
                    gradientAnimationLayout(main_screen_background,
                        ContextCompat.getColor(requireActivity(), R.color.black_night),
                        ContextCompat.getColor(requireActivity(), R.color.early_morning),2000)
                }


                //LIGHT_MODE

            }
            TypeOfSky.MORNING ->{
                mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                to_chart_forecastdescription4.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                to_chart_forecastdescription5.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))


                if (!CURRENT_TIME_OF_DAY.isNeedAnimation){
                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.morning))
                }else {
                    gradientAnimationLayout(main_screen_background,
                        ContextCompat.getColor(requireActivity(), R.color.black_night),
                        ContextCompat.getColor(requireActivity(), R.color.morning),2000)
                }


                //LIGHT_MODE

            }
            TypeOfSky.DAY ->{
                mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                to_chart_forecastdescription4.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                to_chart_forecastdescription5.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))



                if (!CURRENT_TIME_OF_DAY.isNeedAnimation){
                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.day))
                }else {
                    gradientAnimationLayout(main_screen_background,
                        ContextCompat.getColor(requireActivity(), R.color.black_night),
                        ContextCompat.getColor(requireActivity(), R.color.day),2000)
                }
            }
            TypeOfSky.EVENING ->{
                mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                to_chart_forecastdescription4.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                to_chart_forecastdescription5.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))


                if (!CURRENT_TIME_OF_DAY.isNeedAnimation){
                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.evening))
                }else {
                    gradientAnimationLayout(main_screen_background,
                        ContextCompat.getColor(requireActivity(), R.color.black_night),
                        ContextCompat.getColor(requireActivity(), R.color.evening),2000)
                }


                //LIGHT_MODE

            }
            TypeOfSky.LATE_EVENING ->{
                mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(),  R.color.white))
                to_chart_forecastdescription4.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                to_chart_forecastdescription5.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))


                if (!CURRENT_TIME_OF_DAY.isNeedAnimation){
                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.late_evening))
                }else {
                    gradientAnimationLayout(main_screen_background,
                        ContextCompat.getColor(requireActivity(), R.color.black_night),
                        ContextCompat.getColor(requireActivity(), R.color.late_evening),2000)
                }


                //LIGHT_MODE

            }
            else -> {
                //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                to_chart_forecastdescription4.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                to_chart_forecastdescription5.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                if (!CURRENT_TIME_OF_DAY.isNeedAnimation) {
                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
                }else {
                    gradientAnimationLayout(main_screen_background,
                        ContextCompat.getColor(requireActivity(), R.color.black_night),
                        ContextCompat.getColor(requireActivity(), R.color.white),2000)
                }
            }
        }
        if (true){


        } else {

//            when(CURRENT_TIME_OF_DAY.typeOfSky){
//
//                TypeOfSky.NIGHT ->{
//                    mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//                    //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//                    //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//
//                    first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//                    second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//                    third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//                    to_chart_forecastdescription4.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//                    to_chart_forecastdescription5.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//
//                    lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//
//                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black_night))
//                }
//                else -> {
//                    //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//                    //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//                    mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//
//                    first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//                    second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//                    third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//                    to_chart_forecastdescription4.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//                    to_chart_forecastdescription5.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//
//                    lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
//
//                }
//            }
        }
    }

    // its average num from two first elements of main array
    private fun saveForecastNow(forecastFor20hr: ArrayList<Int>) {
        (activity as MainActivity).let { it ->
            it.viewModelMain!!.forecastNow.value = setForecastForNow(forecastFor20hr)
        }
    }

    fun sortingDataForDifferentCharts(data : List<ForecastCell>){
        var forecastForNow:ArrayList<Int> = ArrayList()

        var forecastArray :ArrayList<Float> = ArrayList()
        var forecastDateArray :ArrayList<Long> = ArrayList()

        var allDataForChartsX :ArrayList<FirstChartDataTransitor> = ArrayList()

        // for one number - current forecast - we solving problem when forecast show zero but sun is no goes down
        // (its happen from openweathermap API, coz they send data with periodic interval of 3 hours )
        for (i in 0 until data.size) {

            if (forecastForNow.size <= 8) {
                forecastForNow.add(data.get(i).forecast.toInt())
            }
            // prepare forecasts for charts + calibrate included numbers
            forecastArray.add(toRealFit(data.get(i).forecast.toFloat() * PreferenceMaestro.calibrationCoeff))
            forecastDateArray.add(data.get(i).unixTime)

        }
        /**
         * First 20 hr of forecast:
         */
        var firstChart = chartDatasortforFirstChart(forecastDateArray, forecastArray)
        allDataForChartsX.add(0, firstChart)

        /**
         * Another 4 charts from 0:00 to 21:00 with forecast
         */
        allDataForChartsX.add(1, chartDatasort(forecastDateArray, forecastArray, 0)) // tomorrow
        allDataForChartsX.add(2, chartDatasort(forecastDateArray, forecastArray, 1)) // after tomorrow
        allDataForChartsX.add(3, chartDatasort(forecastDateArray, forecastArray, 2)) // PRO v. //after after tomorrow
        allDataForChartsX.add(4, chartDatasort(forecastDateArray, forecastArray, 3)) // PRO v. //after after after tomorrow

        fillAllCharts(allDataForChartsX)

        saveForecastNow(forecastForNow)

    }

    fun setManuallyMainLabelOnMainScreen(){
        if (stringIndex == dataForMainLabelOnMainscreen.size - 1) {
            stringIndex = 0;
            textSwitcher_main_screen.setText(dataForMainLabelOnMainscreen[stringIndex]);
        } else {
            textSwitcher_main_screen.setText(dataForMainLabelOnMainscreen[++stringIndex]);
        }
    }

    fun setMainLabelMainScreen(res : Resource<*>){
        when(res){

            is Resource.Success<*>, is Resource.Init<*> ->{
                dataForMainLabelOnMainscreen = arrayOf(getString(R.string.mainscreen_fr_forecastnow),"\uD83C\uDFD9"+ getString(
                                    R.string.mainscreen_fr_city)+" ${PreferenceMaestro.chosenStationCITY}","\uD83C\uDF21"+ getString(
                                                        R.string.mainscreen_fr_temp)+"${PreferenceMaestro.temp}°C")
                mSwipeRefreshLayout.isRefreshing = false

                var timer = object : CountDownTimer(6000,2000){
                    override fun onTick(millisUntilFinished: Long) {
                        if (stringIndex == dataForMainLabelOnMainscreen.size - 1) {
                            stringIndex = 0;
                            textSwitcher_main_screen.setText(dataForMainLabelOnMainscreen[stringIndex]);
                        } else {
                            textSwitcher_main_screen.setText(dataForMainLabelOnMainscreen[++stringIndex]);
                        }
                    }
                    override fun onFinish() {}

                }.start()


            }
            is Resource.Loading<*> -> {
                textSwitcher_main_screen.setText(getString(R.string.mainscreen_fr_loading));
                stringIndex = 0
                mSwipeRefreshLayout.isRefreshing = true
            }
            is Resource.Error<*> -> {
                textSwitcher_main_screen.setText(getString(R.string.mainscreen_fr_errornointernet));
                stringIndex = 0
                mSwipeRefreshLayout.isRefreshing = false
            }

        }
    }

    fun fillAllCharts(arrayList: ArrayList<FirstChartDataTransitor>) {

        //val lineChart2 = view.findViewById<LineChart>(R.id.lineChart2)
        //val lineChart3 = view.findViewById<LineChart>(R.id.lineChart3)
        //var description: Description = lineChart1.description
        val legend = lineChart1.legend
        val legend2 = lineChart2.legend
        val legend3 = lineChart3.legend
        val legend4 = lineChart4.legend
        val legend5 = lineChart5.legend

        // main chart
        topLineChartSetup(lineChart1,legend,arrayList.get(0))

        secondChartInit(lineChart2,legend2,arrayList.get(1).forecasts)
        thirdChartInit(lineChart3,legend3,arrayList.get(2).forecasts)

        /**
         * PRO version below
         */
        /**init chart 4  */
        fourthLineChartSetup(lineChart4, legend4, arrayList.get(3).forecasts)
        /**init chart 5 */
        fiveLineChartSetup(lineChart5, legend5,   arrayList.get(4).forecasts)


        GlobalScope.launch {
            saveForNotification(arrayList)
        }


        mSwipeRefreshLayout.isRefreshing = false

        Timber.i("xxx ${arrayList.joinToString()}")
    }

    val gson = Gson()
    var forecastTodayMaxMinShow = arrayListOf<NotificationWarningModel>()

    fun saveForNotification(arrayList: ArrayList<FirstChartDataTransitor>) {
        var todaySum          = arrayList.get(0).forecasts.sum()
        var tomorrow          = arrayList.get(1).forecasts.sum()
        var twoDaysFromNow    = arrayList.get(2).forecasts.sum()
        var threeDaysFromNow  = arrayList.get(3).forecasts.sum()
        var fourthDaysFromNow = arrayList.get(4).forecasts.sum()

        var mainArray = arrayListOf<NotificationWarningModel>(
            NotificationWarningModel("${todaySum}",         "today",todaySum),
            NotificationWarningModel("${tomorrow}",         PreferenceMaestro.leftChartMonthandDay,tomorrow),
            NotificationWarningModel("${twoDaysFromNow}",   PreferenceMaestro.rightChartMonthandDay,twoDaysFromNow),
            NotificationWarningModel("${threeDaysFromNow}", PreferenceMaestro.fourChartMonthandDay,threeDaysFromNow),
            NotificationWarningModel("${fourthDaysFromNow}",PreferenceMaestro.fiveChartMonthandDay,fourthDaysFromNow)
        )

        var maxIndex = 0
        var minIndex = 0

        var maxValue = 0
        var minValue = 0

        for (i in 1 until mainArray.size){ // for no today coz 20 hr~
            if (mainArray[i].sumOfDay > maxValue){
                maxValue = mainArray[i].sumOfDay
                maxIndex = i

            }else if(mainArray[i].sumOfDay < minValue){
                minValue = mainArray[i].sumOfDay
                minIndex = i //mainArray[i].sumOfDay

            }
        }


        PreferenceMaestro.notificationJSON = gson.toJson(arrayListOf(
            NotificationWarningModel("~","*",0),
            NotificationWarningModel(mainArray[0].power,mainArray[0].description,mainArray[0].sumOfDay),
            NotificationWarningModel(mainArray[maxIndex].power,mainArray[maxIndex].description,mainArray[maxIndex].sumOfDay),
            NotificationWarningModel(mainArray[minIndex].power,mainArray[minIndex].description,mainArray[minIndex].sumOfDay)
        ))
        Timber.i("fff ${PreferenceMaestro.notificationJSON}")



    }

    //nominal power of your PV system

    private fun topLineChartSetup(
        lineChart: LineChart,
        legend: Legend,
        frcstData: FirstChartDataTransitor
    ){

        // Transit
        val arrayData : ArrayList<Int> = frcstData.forecasts
        val firstChartSpecialValues : ArrayList<String> = frcstData.dates

        //var yValues = smoothLineOfChart(arrayData)
        //for (i in ) // commented in 29/12/2021
        var yValues = ArrayList<Entry>()
        var REMBO = smoothLineOfChartGOGOGO(arrayData,firstChartSpecialValues)
        yValues = REMBO.arrEnt
        Timber.i("toppp ppp ${REMBO.arrEnt.toString()}")
        Timber.i("toppp ppp ${REMBO.xAxisTimes.toString()}")
//        try {
//            for (i in 0..arrayData.size-1){
//                yValues.add(Entry(i.toFloat(), (arrayData.get(i)).toFloat()))
//            }
//        }catch (e: Exception){
//            Timber.i("vvv4top ${e.message}")
//        }

        var set1 = LineDataSet(yValues, "")
        set1.apply {
            fillAlpha = 110
            setDrawHorizontalHighlightIndicator(false)
            disableDashedLine()
            isHighlightEnabled = false
        }

        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val dataX = LineData(dataSet)

        // add goal line to chart
        val goalLine = LimitLine(
                PreferenceMaestro.chosenStationNOMINALPOWER.toFloat(), String.format(
                    getString(R.string.mainscreen_chart_nominal_power_is_here),
                    "goal"
                )
        )

        lineChart.apply {

            goalLine.lineColor = Color.rgb(255, 87, 34)
            goalLine.textColor = ContextCompat.getColor(requireActivity(), R.color.greenTight)
            goalLine.lineWidth = 1f
            //goalLine.isDashedLineEnabled
            isDragEnabled = true
            setScaleEnabled(true)
            description.isEnabled = false
            legend.isEnabled = false // description of define line
            legend.position = Legend.LegendPosition.BELOW_CHART_CENTER



            //Disable right axis
            axisRight.isEnabled = false

            axisLeft.apply {
                axisMaximum = PreferenceMaestro.chosenStationNOMINALPOWER.toFloat() * 1.2F
                axisMinimum = 0f
                removeAllLimitLines()
                addLimitLine(goalLine)

                setDrawLimitLinesBehindData(true)
                disableGridDashedLine()
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            xAxis.apply {
                valueFormatter = MyXAxisValuesFormatter(REMBO.xAxisTimes)
                granularity    = 1F
                position       = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            data = dataX


            set1.apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillAlpha = 100
                cubicIntensity = 0.07f
                setDrawCircles(true)
                setDrawValues(true)
                setMaxVisibleValueCount(5)


                valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
                color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
                setCircleColorHole(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)
            }
            notifyDataSetChanged()
            invalidate()

        }
        Legend.LegendPosition.RIGHT_OF_CHART


        first_chart_description.text = getString(R.string.mainscreen_chart_title_forecast20hr)+""
//        first_chart_description.text = getString(R.string.mainscreen_chart_title_forecast20hr)+" (Σ= ${scaleOfkWh(
//            sumOfCharts(arrayData),
//            true
//        )})"

    }

    private fun secondChartInit(
        lineChart: LineChart,
        legend: Legend,
        arrayData: ArrayList<Int>
    ){
        // "0:00", "3:00", "6:00", "9:00", "12:00", "15:00", "18:00", "21:00"
        //  4.30
        /**
         * "0:00", "3:00", "6:00", "9:00", "12:00", "15:00", "18:00", "21:00"
         * 4.50
         * 9.0 (9:00) - 8.01 (8:04) = 0.99
         *
         *  8.01 / 3 = 2.6666..
         *
         *
         *
         *
         */
        var yValues = ArrayList<Entry>()
        Timber.i("vvv4 ->chart 2:  $arrayData")
        var REMBO = smoothLineOfChartGOGOGO(arrayData,xAxisTimes_1)
        yValues = REMBO.arrEnt


        Timber.i("vvvv >>>> ${yValues.joinToString()}")
        ///////////////////////////////////////////
        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        //val data = LineData(dataSet)

        val dataX = LineData(dataSet)

        // add goal line to chart
        val goalLine =  // line
            LimitLine(
                PreferenceMaestro.chosenStationNOMINALPOWER.toFloat(), String.format(
                    getString(R.string.mainscreen_chart_nominal_power_is_here),
                    "goal"
                )
            )
//        val yAxis: YAxis = lineChart.getAxisLeft()
//        yAxis.isGranularityEnabled = true
//        yAxis.granularity = 0.1f

        lineChart.apply {

            goalLine.lineColor = Color.rgb(255, 87, 34)
            goalLine.textColor = ContextCompat.getColor(requireActivity(), R.color.greenTight)
            goalLine.lineWidth = 1f
            //goalLine.isDashedLineEnabled
            isDragEnabled = true
            setScaleEnabled(true)
            description.isEnabled = false
            legend.isEnabled = false // description of define line
            legend.position = Legend.LegendPosition.BELOW_CHART_CENTER



            //Disable right axis
            axisRight.isEnabled = false

            axisLeft.apply {

                axisMaximum = PreferenceMaestro.chosenStationNOMINALPOWER.toFloat() * 1.2F
                axisMinimum = 0f
                removeAllLimitLines()
                addLimitLine(goalLine)

                setDrawLimitLinesBehindData(true)
                disableGridDashedLine()
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            xAxis.apply {
                valueFormatter = MyXAxisValuesFormatter_1(REMBO.xAxisTimes)
                granularity    = 1F
                position       = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            data = dataX


            set1.apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillAlpha = 100
                cubicIntensity = 0.07f
                setDrawCircles(true)
                setDrawValues(true)
                setMaxVisibleValueCount(5)


                valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
                color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
                setCircleColorHole(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)
            }
            notifyDataSetChanged()
            invalidate()

        }
        Legend.LegendPosition.RIGHT_OF_CHART


        second_chart_description.text = "${PreferenceMaestro.leftChartMonthandDay} (Σ= ${scaleOfkWh(arrayData.sum(), true)})"
//        second_chart_description.text = getString(R.string.mainscreen_chart_title_forecasttomorrow) +"\n(Σ= ${scaleOfkWh(
//            sumOfCharts(arrayData),
//            true
//        )}): ${PreferenceMaestro.leftChartMonthandDay}"


    }

    private fun thirdChartInit(
        lineChart: LineChart,
        legend: Legend,
        arrayData: ArrayList<Int>
    ){

        var yValues = ArrayList<Entry>()
        Timber.i("vvv4 ->chart 3:  $arrayData")
        var REMBO = smoothLineOfChartGOGOGO(arrayData,xAxisTimes_1)
        yValues = REMBO.arrEnt
        //yValues = makeChartLineSmoothAndCompare(arrayData)

//        for (i in 0..arrayData.size-1){
//            yValues.add(Entry(i.toFloat(), arrayData.get(i).toFloat()))
//        }
//        try {
//            for (i in 0..arrayData.size-1){
//                yValues.add(Entry(i.toFloat(), arrayData.get(i).toFloat()))
//            }
//        }catch (e: Exception){
//            Timber.i("vvv4 ${e.message}")
//        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val dataX = LineData(dataSet)

        // add goal line to chart
        val goalLine =  // line
            LimitLine(
                PreferenceMaestro.chosenStationNOMINALPOWER.toFloat(), String.format(
                    getString(R.string.mainscreen_chart_nominal_power_is_here),
                    "goal"
                )
            )

        lineChart.apply {

            goalLine.lineColor = Color.rgb(255, 87, 34)
            goalLine.textColor = ContextCompat.getColor(requireActivity(), R.color.greenTight)
            goalLine.lineWidth = 1f
            //goalLine.isDashedLineEnabled
            isDragEnabled = true
            setScaleEnabled(true)
            description.isEnabled = false
            legend.isEnabled = false // description of define line
            legend.position = Legend.LegendPosition.BELOW_CHART_CENTER



            //Disable right axis
            axisRight.isEnabled = false

            axisLeft.apply {

                axisMaximum = PreferenceMaestro.chosenStationNOMINALPOWER.toFloat() * 1.2F
                axisMinimum = 0f
                removeAllLimitLines()
                addLimitLine(goalLine)

                setDrawLimitLinesBehindData(true)
                disableGridDashedLine()
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            xAxis.apply {
                valueFormatter = MyXAxisValuesFormatter(REMBO.xAxisTimes)
                granularity    = 1F
                position       = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            data = dataX


            set1.apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillAlpha = 100
                cubicIntensity = 0.07f
                setDrawCircles(true)
                setDrawValues(true)
                setMaxVisibleValueCount(5)


                valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
                color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
                setCircleColorHole(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)
            }
            notifyDataSetChanged()
            invalidate()

        }
        Legend.LegendPosition.RIGHT_OF_CHART


        third_chart_description.text = "${PreferenceMaestro.rightChartMonthandDay} (Σ= ${scaleOfkWh(arrayData.sum(), true)})"

//        third_chart_description.text = getString(R.string.mainscreen_chart_title_forecasttomorrow) +
//        "\n(Σ= ${scaleOfkWh(sumOfCharts(arrayData), true)}): ${PreferenceMaestro.leftChartMonthandDay}"
    }

    private fun fourthLineChartSetup(
        lineChart: LineChart,
        legend: Legend,
        arrayData: ArrayList<Int>
    ){
        var yValues = ArrayList<Entry>()
        Timber.i("vvv4 ->chart 4:  $arrayData")
        var REMBO = smoothLineOfChartGOGOGO(arrayData,xAxisTimes_1)
        yValues = REMBO.arrEnt
//        try {
//
//            for (i in 0..arrayData.size-1){
//                yValues.add(Entry(i.toFloat(), arrayData.get(i).toFloat()))
//            }
//        }catch (e: Exception){
//            Timber.i("vvv4 ${e.message}")
//        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        //val data = LineData(dataSet)

        val dataX = LineData(dataSet)

        // add goal line to chart
        val goalLine =  // line
            LimitLine(
                PreferenceMaestro.chosenStationNOMINALPOWER.toFloat(), String.format(
                    getString(R.string.mainscreen_chart_nominal_power_is_here),
                    "goal"
                )
            )

        lineChart.apply {

            goalLine.lineColor = Color.rgb(255, 87, 34)
            goalLine.textColor = ContextCompat.getColor(requireActivity(), R.color.greenTight)
            goalLine.lineWidth = 1f
            //goalLine.isDashedLineEnabled
            isDragEnabled = true
            setScaleEnabled(true)
            description.isEnabled = false
            legend.isEnabled = false // description of define line
            legend.position = Legend.LegendPosition.BELOW_CHART_CENTER



            //Disable right axis
            axisRight.isEnabled = false

            axisLeft.apply {

                axisMaximum = PreferenceMaestro.chosenStationNOMINALPOWER.toFloat() * 1.2F
                axisMinimum = 0f
                removeAllLimitLines()
                addLimitLine(goalLine)

                setDrawLimitLinesBehindData(true)
                disableGridDashedLine()
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            xAxis.apply {
                valueFormatter = MyXAxisValuesFormatter(REMBO.xAxisTimes)
                granularity    = 1F
                position       = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            data = dataX


            set1.apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillAlpha = 100
                cubicIntensity = 0.07f
                setDrawCircles(true)
                setDrawValues(true)
                setMaxVisibleValueCount(5)


                valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
                color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
                setCircleColorHole(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)
            }
            notifyDataSetChanged()
            invalidate()

        }
        //Legend.LegendPosition.RIGHT_OF_CHART

        to_chart_forecastdescription4.text = "${PreferenceMaestro.fourChartMonthandDay} (Σ= ${scaleOfkWh(arrayData.sum(), true)})"

    }

    private fun fiveLineChartSetup(
        lineChart: LineChart,
        legend: Legend,
        arrayData: ArrayList<Int>
    ){
        var yValues = ArrayList<Entry>()
        Timber.i("vvv4 ->chart 5: $arrayData")
        var REMBO = smoothLineOfChartGOGOGO(arrayData,xAxisTimes_1)
        yValues = REMBO.arrEnt
//        try {
//
//            for (i in 0..arrayData.size-1){
//                yValues.add(Entry(i.toFloat(), arrayData.get(i).toFloat()))
//            }
//        }catch (e: Exception){
//            Timber.i("vvv4 ${e.message}")
//        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        //val data = LineData(dataSet)

        val dataX = LineData(dataSet)

        // add goal line to chart
        val goalLine =  // line
            LimitLine(
                PreferenceMaestro.chosenStationNOMINALPOWER.toFloat(), String.format(
                    getString(R.string.mainscreen_chart_nominal_power_is_here),
                    "goal"
                )
            )

        lineChart.apply {

            goalLine.lineColor = Color.rgb(255, 87, 34)
            goalLine.textColor = ContextCompat.getColor(requireActivity(), R.color.greenTight)
            goalLine.lineWidth = 1f
            //goalLine.isDashedLineEnabled
            isDragEnabled = true
            setScaleEnabled(true)
            description.isEnabled = false
            legend.isEnabled = false // description of define line
            legend.position = Legend.LegendPosition.BELOW_CHART_CENTER



            //Disable right axis
            axisRight.isEnabled = false

            axisLeft.apply {

                axisMaximum = PreferenceMaestro.chosenStationNOMINALPOWER.toFloat() * 1.2F
                axisMinimum = 0f
                removeAllLimitLines()
                addLimitLine(goalLine)

                setDrawLimitLinesBehindData(true)
                disableGridDashedLine()
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            xAxis.apply {
                valueFormatter = MyXAxisValuesFormatter(REMBO.xAxisTimes)
                granularity    = 1F
                position       = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
            }

            data = dataX


            set1.apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillAlpha = 100
                cubicIntensity = 0.07f
                setDrawCircles(true)
                setDrawValues(true)
                setMaxVisibleValueCount(5)


                valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
                color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
                setCircleColorHole(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
                fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)
            }
            notifyDataSetChanged()
            invalidate()

        }
        //Legend.LegendPosition.RIGHT_OF_CHART

        to_chart_forecastdescription5.text = "${PreferenceMaestro.fiveChartMonthandDay} (Σ= ${scaleOfkWh(arrayData.sum(), true)})"

    }

    override fun onRefresh() {

        (activity as MainActivity).let {
            it.viewModelMain!!.manualRequest()
        }

        CURRENT_TIME_OF_DAY.isNeedAnimation = true

    }

    private fun swipeRefreshTools(view: View) {

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_mainscreen)
        mSwipeRefreshLayout.setOnRefreshListener(this as SwipeRefreshLayout.OnRefreshListener)

        mSwipeRefreshLayout.nestedScrollAxes

        mSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_red_light
        )

    }
    private fun refreshSunshineIndicatorBlock(){
        sunriseTime.text = "\uD83C\uDF05\n${PreferenceMaestro.sunrise}"
        timeOfSunShine.text = "${PreferenceMaestro.solarDayDuration}hr"
        sunsetTime.text = "\uD83C\uDF07\n${PreferenceMaestro.sunset}"
    }


    companion object {

    }
}