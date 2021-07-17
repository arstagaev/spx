package com.revolve44.solarpanelx.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.model.db.FirstChartDataTransitor
import com.revolve44.solarpanelx.domain.core.MyXAxisValuesFormatter
import com.revolve44.solarpanelx.ui.MainActivity
import com.revolve44.solarpanelx.ui.customviews.daylightmap.WeatherAnim
import com.revolve44.solarpanelx.ui.viewmodels.MainViewModel
import com.revolve44.solarpanelx.ui.viewmodels.MassiveViewModelProviderFactory
import timber.log.Timber


class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    private val mainViewmodelF : MainViewModel by activityViewModels()
    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
   // private val mainViewmodel : MainViewModel by viewModels()
    private lateinit var lineChart1 : LineChart
    private lateinit var rtr : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //activity?.let {
        //    val viewmodelS = ViewModelProvider(it).get(MainViewModel::class.java)
        //    viewmodelS
        //}
        mainViewmodelF.allDataForCharts.observe(viewLifecycleOwner) {
            fillAllCharts(it)

        }





        lineChart1 = view.findViewById<LineChart>(R.id.lineChart1)

        val timer =  object : CountDownTimer(4000,1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {

            }

        }.start()



        //mainViewmodel.allDataForCharts.observe(viewLifecycleOwner, Observer {
        //    fillAllCharts(it)
        //})



    }

    override fun onResume() {
        super.onResume()
        //WeatherAnim.quickSetup()
        //WeatherAnim.geometryL()
        //val viewModelFactory = MassiveViewModelProviderFactory(application,spxRepository)
        //mainViewmodel = ViewModelProvider(this).get(MainViewModel::class.java)

        //mainViewmodel = (activity as MainActivity).viewModelMain


    }

    fun fillAllCharts(arrayList: ArrayList<FirstChartDataTransitor>) {

        //val lineChart2 = view.findViewById<LineChart>(R.id.lineChart2)
        //val lineChart3 = view.findViewById<LineChart>(R.id.lineChart3)

        var description: Description = lineChart1.description
        val legend = lineChart1.legend
        topLineChartSetup(lineChart1,legend,arrayList.get(0))
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
        //topChartDescription.text = getString(R.string.mainscreen_chart_title_forecast20hr)+" (Σ= ${scaleOfkWh(
        //    sumOfCharts(arrayData),
        //    true
        //)})"

    }



    companion object {

    }
}