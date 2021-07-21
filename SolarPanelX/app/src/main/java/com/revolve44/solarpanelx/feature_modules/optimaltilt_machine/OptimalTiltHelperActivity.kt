package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.snackbar.Snackbar
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels.TiltCalcMachine
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels.OrientationSolarPanelViewModel
import com.revolve44.solarpanelx.R

class OptimalTiltHelperActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private var mSensorManager:  SensorManager? = null
    private var mRotationSensor: Sensor? = null
    private var msensor:         Sensor? = null

    private val SENSOR_DELAY = 500 * 1000 // 500ms

    private val  FROM_RADS_TO_DEGS = -57
    lateinit var viewModel : OrientationSolarPanelViewModel

    private var currentAzimuth = 0f
    private var sotwFormatter: SOTWFormatter? = null
    private var compass: Compass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sm_activity_orientation_helper)
        initNavigation()

        val viewModelProviderFactory = TiltCalcMachine(application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            OrientationSolarPanelViewModel::class.java)
        sotwFormatter =
            SOTWFormatter(this@OptimalTiltHelperActivity)
        try {

            mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            mRotationSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            msensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            mSensorManager!!.registerListener(this, mRotationSensor, SENSOR_DELAY)
            mSensorManager!!.registerListener(this, msensor, SENSOR_DELAY)
            setupCompass()

        } catch (e: Exception) {
            Toast.makeText(this, "Hardware compatibility issue", Toast.LENGTH_LONG).show()
        }
    }

    private fun adjustSotwLabel(azimuth: Float) {
        //sotwLabel!!.text = sotwFormatter!!.format(azimuth)
        //azi!!.text = "az= $azimuth"
    }

    private fun getCompassListener(): Compass.CompassListener {
        return object : Compass.CompassListener {
            override fun onNewAzimuth(azimuth: Float) {
                viewModel.azimuthDirectionOfSolarPanel.value = azimuth
            }
            // UI updates only in UI thread
            // https://stackoverflow.com/q/11140285/444966



            //this.runOnUiThread(java.lang.Runnable {
            //    adjustArrow(azimuth)
            //    adjustSotwLabel(azimuth)
            //})

        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("ddd", "start compass")
        compass!!.start()
    }

    override fun onPause() {
        super.onPause()
        compass!!.stop()
    }

    override fun onResume() {
        super.onResume()
        compass!!.start()
    }

    override fun onStop() {
        super.onStop()
        Log.d("ddd", "stop compass")
        compass!!.stop()
    }


    private fun setupCompass() {

        compass = Compass(this)
        //compass!!.start()

        val cl:   Compass.CompassListener = getCompassListener()
        compass!!.setListener(cl)
    }

    private fun initNavigation(){
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor == mRotationSensor) {
            if (event?.values?.size!! > 4) {
                var truncatedRotationVector = FloatArray(4)
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(event.values);
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun update(vectors: FloatArray) {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors)
        val worldAxisX = SensorManager.AXIS_X
        val worldAxisZ = SensorManager.AXIS_Z
        val adjustedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            worldAxisX,
            worldAxisZ,
            adjustedRotationMatrix
        )
        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)
        val yaw = orientation[0] * FROM_RADS_TO_DEGS
        val pitch = orientation[1] * FROM_RADS_TO_DEGS
        val roll = orientation[2] * FROM_RADS_TO_DEGS
        //PITCH = pitch
        viewModel.pitch.value = pitch
        //viewModel.directionOfSolarPanel.value =
        //(findViewById<View>(R.id.pitch) as TextView).text = "Pitch: $pitch"
        //(findViewById<View>(R.id.roll) as TextView).text = "Roll: $roll"
        //(findViewById<View>(R.id.yaw) as TextView).text = "Yaw: $yaw"
    }

    fun helper_azimuth1(view: View) {
        Snackbar.make(findViewById(android.R.id.content), "This is a compass. The arrow always points north ", Snackbar.LENGTH_LONG).show()


    }


}