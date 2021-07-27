package com.revolve44.solarpanelx.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.snackbar.Snackbar
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.datasource.model.db.FirstChartDataTransitor
import com.revolve44.solarpanelx.datasource.model.db.ForecastCell
import com.revolve44.solarpanelx.domain.Resource
import com.revolve44.solarpanelx.domain.core.*
import com.revolve44.solarpanelx.domain.enums.TypeOfSky
import com.revolve44.solarpanelx.domain.westcoast_customs.VerticalTextView
import com.revolve44.solarpanelx.global_utils.Constants.Companion.CURRENT_TIME_OF_DAY
import com.revolve44.solarpanelx.ui.MainActivity
import timber.log.Timber


class MainScreenFragment : Fragment(R.layout.fragment_main_screen) , SwipeRefreshLayout.OnRefreshListener{
    private val values = arrayListOf<String>("0hr", "3hr", "6hr", "9hr", "12hr", "15hr", "18hr", "21hr")
    //private val mainViewmodelF : MainViewModel by activityViewModels()
    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
   // private val mainViewmodel : MainViewModel by viewModels()
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var lineChart1 : LineChart
    private lateinit var lineChart2 : LineChart
    private lateinit var lineChart3 : LineChart
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

    private lateinit var sunshine_duration_sigh : CardView

    private lateinit var cardview_forecastnow_mainscreen : CardView
    private lateinit var main_screen_background : ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        textSwitcher_main_screen = view.findViewById(R.id.textSwitcher_main_screen)
        lineChart1 = view.findViewById<LineChart>(R.id.lineChart1)
        lineChart2 = view.findViewById<LineChart>(R.id.lineChart2)
        lineChart3 = view.findViewById<LineChart>(R.id.lineChart3)

        forecastNowAbsol   = view.findViewById(R.id.forecast_now_absolute)
        forecastNowRelativ = view.findViewById(R.id.forecast_now_relatively)
        sunriseTime = view.findViewById(R.id.sunshineIndicatorBlockSUNRISE)
        timeOfSunShine = view.findViewById(R.id.sunshineIndicatorBlockDURATION)
        sunsetTime = view.findViewById(R.id.sunshineIndicatorBlockSUNSET)

        frcnowCardview = view.findViewById(R.id.linearlayout_frcst_now)

        sunshine_duration_sigh = view.findViewById(R.id.sunshine_duration_sigh)

        lastUpdate = view.findViewById(R.id.last_upd_date)

        first_chart_description  = view.findViewById(R.id.first_chart_description)
        second_chart_description = view.findViewById(R.id.to_tomorrow_chart_forecast)
        third_chart_description  = view.findViewById(R.id.to_after_tomorrow_chart_forecast)
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

        textSwitcher_main_screen.setFactory(ViewSwitcher.ViewFactory {
            mainLabelOfMainScreenInsideTxtSwitcher = TextView(requireActivity())
            when(CURRENT_TIME_OF_DAY.typeOfSky){
                TypeOfSky.NIGHT -> {
                    mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(Color.WHITE)
                }
                else -> mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(Color.BLACK)
            }
            //mainLabelOfMainScreenInsideTxtSwitcher!!.setTextColor(Color.BLACK)
            val typeface = ResourcesCompat.getFont(requireActivity(), R.font.montserrat)
            mainLabelOfMainScreenInsideTxtSwitcher?.setTypeface(typeface)
            mainLabelOfMainScreenInsideTxtSwitcher?.setTextSize(35f)
           //textView!!.setGravity(Gravity.START)
            mainLabelOfMainScreenInsideTxtSwitcher?.setGravity(Gravity.CENTER_VERTICAL)
            return@ViewFactory mainLabelOfMainScreenInsideTxtSwitcher
        })


        textSwitcher_main_screen.setOnClickListener {
            setManuallyMainLabelOnMainScreen()
        }

        cardview_forecastnow_mainscreen.setOnClickListener {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),"1️⃣Forecast power now  2️⃣ % of max generation",Snackbar.LENGTH_LONG).show()
        }

        sunshine_duration_sigh.setOnClickListener {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),"\uD83C\uDF05Sunrise ~ \uD83C\uDF1E Sunshine time ~ \uD83C\uDF07Sunset",Snackbar.LENGTH_LONG).show()
        }

    }

    override fun onResume() {
        super.onResume()
        //WeatherAnim.quickSetup()
        //WeatherAnim.geometryL()
        //val viewModelFactory = MassiveViewModelProviderFactory(application,spxRepository)
        //mainViewmodel = ViewModelProvider(this).get(MainViewModel::class.java)
        refreshSunshineIndicatorBlock()

        //wait when viewmodel not null from activity
        (activity as MainActivity).let { it ->
            //val spxRepository = SpxRepository()
            //val viewModelFactory = MassiveViewModelProviderFactory(application,spxRepository)

            //it.viewModelMain = ViewModelProvider(it).get(MainViewModel::class.java)
            if (it.viewModelMain != null){

                it.viewModelMain!!.getAllForecastForChart().observe(viewLifecycleOwner) {
                    if(it.isNotEmpty()){

                        firstStepToCharts(it)

                        lastUpdate.text = "${PreferenceMaestro.timeOfLastDataUpdate}"
                    }

                }

                it.viewModelMain!!.forecastNow.observe(viewLifecycleOwner) { fNow ->
                    forecastNowRelativ.text = "${roundTo1decimials((fNow.toFloat() / PreferenceMaestro.chosenStationNOMINALPOWER.toFloat() )*100f)}%"
                    Timber.i("fnow ${fNow} ${PreferenceMaestro.calibrationCoeff}  ${PreferenceMaestro.chosenStationNOMINALPOWER}")
                    forecastNowAbsol.text = displayWattsKiloWattsInSexually( toRealFit(fNow.toFloat() * PreferenceMaestro.calibrationCoeff ) )

                    changeSkyInMainScreen()
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
            }
        }
    }

    private fun changeSkyInMainScreen() {

        if (CURRENT_TIME_OF_DAY.isChangeColorAlreadyHappen){

            when(CURRENT_TIME_OF_DAY.typeOfSky){
                TypeOfSky.NIGHT ->{
                    mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                    first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                    lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    gradientAnimationLayout(main_screen_background,ContextCompat.getColor(requireActivity(), R.color.white),ContextCompat.getColor(requireActivity(), R.color.black_night),2000)
                }
                else -> {
                    //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                    first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                    lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    gradientAnimationLayout(main_screen_background,ContextCompat.getColor(requireActivity(), R.color.black_night),ContextCompat.getColor(requireActivity(), R.color.white),2000)

                }
            }
        }else{

            when(CURRENT_TIME_OF_DAY.typeOfSky){

                TypeOfSky.NIGHT ->{
                    mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                    first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                    lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black_night))
                }
                else -> {
                    //forecastNowAbsol.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    //forecastNowRelativ.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    mainLabelOfMainScreenInsideTxtSwitcher?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                    first_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    second_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    third_chart_description.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                    lastUpdate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                    main_screen_background.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))

                }
            }
        }
    }

    // its average num from two first elements of main array
    private fun saveForecastNow(forecastFor20hr: ArrayList<Int>) {
        (activity as MainActivity).let { it ->
            it.viewModelMain!!.forecastNow.value = setForecastForNow(forecastFor20hr)
        }
    }

    fun firstStepToCharts(data : List<ForecastCell>){
        var forecastForNow:ArrayList<Int> = ArrayList()

        var forecastArray :ArrayList<Float> = ArrayList()
        var forecastDateArray :ArrayList<Long> = ArrayList()

        var allDataForChartsX :ArrayList<FirstChartDataTransitor> = ArrayList()

        for (i in 0 until data.size) {

            if (forecastForNow.size <= 8) {
                forecastForNow.add(data.get(i).forecast.toInt())
            }
            // prepare forecasts for charts + calibrate included numbers
            forecastArray.add(toRealFit(data.get(i).forecast.toFloat() * PreferenceMaestro.calibrationCoeff))
            forecastDateArray.add(data.get(i).unixTime)

        }

        var firstChart = chartDatasortforFirstChart(forecastDateArray, forecastArray)



        allDataForChartsX.add(0, firstChart)
        allDataForChartsX.add(1, chartDatasort(forecastDateArray, forecastArray, 0))
        allDataForChartsX.add(2, chartDatasort(forecastDateArray, forecastArray, 1))

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
                                                        R.string.mainscreen_fr_temp)+"${PreferenceMaestro.temp}")
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
        var description: Description = lineChart1.description
        val legend = lineChart1.legend
        val legend2 = lineChart2.legend
        val legend3 = lineChart3.legend

        topLineChartSetup(lineChart1,legend,arrayList.get(0))
        secondChartInit(lineChart2,legend2,arrayList.get(1).forecasts)
        thirdChartInit(lineChart3,legend3,arrayList.get(2).forecasts)

        mSwipeRefreshLayout.isRefreshing = false

        Timber.i("xxx ${arrayList.joinToString()}")
    }

    private fun topLineChartSetup(
        lineChart: LineChart,
        legend: Legend,
        frcstData: FirstChartDataTransitor
    ){
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false // description of define line


        //lineChart.legend.textColor = Color.GREEN

        //lineChart.axisLeft.axisLineColor = Color.CYAN

        legend.position = Legend.LegendPosition.BELOW_CHART_CENTER

        //Disable right axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        val leftAxis = lineChart.axisLeft
        leftAxis.removeAllLimitLines()

        //leftAxis.enableGridDashedLine(30F, 30F, 0F)
        leftAxis.setDrawLimitLinesBehindData(true)
        leftAxis.disableGridDashedLine()

        // Transit
        val arrayData : ArrayList<Int> = frcstData.forecasts
        val firstChartSpecialValues : ArrayList<String> = frcstData.dates

        val yValues = ArrayList<Entry>()

        //for (i in )
        try {
            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), (arrayData.get(i)).toFloat()))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")


        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false

        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)
        lineChart.data = data

        //   X
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = MyXAxisValuesFormatter(firstChartSpecialValues)
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM



        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.setDrawFilled(true)
        set1.fillAlpha = 100
        set1.cubicIntensity = 0.2f

        lineChart.axisLeft.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        xAxis.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
        set1.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
        set1.fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)


        /** Refresh all chart, i use this when i again setup new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
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
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false // description of define line


        legend.position= Legend.LegendPosition.BELOW_CHART_CENTER

        val leftAxis = lineChart.axisLeft
        leftAxis.removeAllLimitLines()

        //leftAxis.enableGridDashedLine(30F, 30F, 0F)

        leftAxis.setDrawLimitLinesBehindData(true)

        //Disable right axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false


        val yValues = ArrayList<Entry>()
        Timber.i("vvv4 $arrayData")
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i).toFloat()))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)

        lineChart.data = data
        //   X
        val xAxis = lineChart.xAxis
        try {
            xAxis.valueFormatter = MyXAxisValuesFormatter(values)
        }catch (e: Exception){}

        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.mode = LineDataSet.Mode.CUBIC_BEZIER

        set1.setDrawFilled(true)
//        set1.color = ContextCompat.getColor(requireActivity(),R.color.chart_stroke)
//        set1.fillColor = ContextCompat.getColor(requireActivity(),R.color.chart_fill_otherchart)
        set1.fillAlpha = 100

        set1.cubicIntensity = 0.2f

        lineChart.axisLeft.textColor = ContextCompat.getColor(
            requireActivity(),
            R.color.hint_white2
        )
        xAxis.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
        set1.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
        set1.fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)

        /** Refresh all chart, i use this when i again setup a new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
        second_chart_description.text = getString(R.string.mainscreen_chart_title_forecasttomorrow) +": ${PreferenceMaestro.leftChartMonthandDay}"
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
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false // description of define line


        legend.position= Legend.LegendPosition.BELOW_CHART_CENTER

        val leftAxis = lineChart.axisLeft
        leftAxis.removeAllLimitLines()

        //leftAxis.enableGridDashedLine(30F, 30F, 0F)

        leftAxis.setDrawLimitLinesBehindData(true)

        //Disable right axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false


        val yValues = ArrayList<Entry>()
        Timber.i("vvv4 $arrayData")
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i).toFloat()))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)

        lineChart.data = data



        //   X
        val xAxis = lineChart.xAxis
        try {
            xAxis.valueFormatter = MyXAxisValuesFormatter(values)
        }catch (e: Exception){}

        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.mode = LineDataSet.Mode.CUBIC_BEZIER

        set1.setDrawFilled(true)
//        set1.color = ContextCompat.getColor(requireActivity(),R.color.chart_stroke)
//        set1.fillColor = ContextCompat.getColor(requireActivity(),R.color.chart_fill_otherchart)
        set1.fillAlpha = 100

        set1.cubicIntensity = 0.2f

        lineChart.axisLeft.textColor = ContextCompat.getColor(
            requireActivity(),
            R.color.hint_white2
        )
        xAxis.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
        set1.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
        set1.fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)

        /** Refresh all chart, i use this when i again setup a new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
        third_chart_description.text = getString(R.string.mainscreen_chart_title_forecast_aftertomorrow) +
                ": ${PreferenceMaestro.rightChartMonthandDay}"

//        third_chart_description.text = getString(R.string.mainscreen_chart_title_forecasttomorrow) +
//        "\n(Σ= ${scaleOfkWh(sumOfCharts(arrayData), true)}): ${PreferenceMaestro.leftChartMonthandDay}"
    }

    override fun onRefresh() {

        (activity as MainActivity).let {
            it.viewModelMain!!.manualRequest()
        }
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