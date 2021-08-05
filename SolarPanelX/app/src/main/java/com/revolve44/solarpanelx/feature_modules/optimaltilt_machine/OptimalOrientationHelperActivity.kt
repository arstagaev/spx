package com.revolve44.solarpanelx.feature_modules.optimaltilt_machine

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.snackbar.Snackbar
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels.TiltCalcMachine
import com.revolve44.solarpanelx.feature_modules.optimaltilt_machine.viewmodels.OrientationSolarPanelViewModel
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.global_utils.Constants.Companion.is_COMPASS_WORKING_FINE
import com.revolve44.solarpanelx.global_utils.Constants.Companion.is_TYPE_ROTATION_VECTOR_WORKING
import com.revolve44.solarpanelx.ui.MainActivity
import timber.log.Timber

class OptimalOrientationHelperActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private var mSensorManager:  SensorManager? = null
    private var mRotationSensor: Sensor? = null
    private var magneticSensor:  Sensor? = null

    private val SENSOR_DELAY = 950 * 1000 // 500ms

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
            SOTWFormatter(this@OptimalOrientationHelperActivity)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager


        try {

            mRotationSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            is_TYPE_ROTATION_VECTOR_WORKING = true

            if (mRotationSensor == null) {
                //Toast.makeText(this, "ACCELEROMETER work fine", Toast.LENGTH_LONG).show()
                mRotationSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

                if (mRotationSensor != null){
                    is_TYPE_ROTATION_VECTOR_WORKING = false

                }

            }

            mSensorManager!!.registerListener(this, mRotationSensor, SENSOR_DELAY)
            Timber.i("type rotation is ${mRotationSensor!!.type}")

        } catch (e: Exception) {
            Toast.makeText(this, "Hardware compatibility issue. Rotation sensor", Toast.LENGTH_LONG).show()
        }

        try {
            magneticSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            mSensorManager!!.registerListener(this, magneticSensor, SENSOR_DELAY)
            setupCompass()
        }catch (e :Exception){
            is_COMPASS_WORKING_FINE = false
            //Toast.makeText(this, "Compass don`t work on your device!", Toast.LENGTH_SHORT).show()
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
                is_COMPASS_WORKING_FINE = true
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//        Log.d("ddd", "start compass")
//        compass!!.start()
//    }



    override fun onResume() {
        super.onResume()
        try{
            compass!!.start()
        }catch (e : Exception){
            Handler(Looper.getMainLooper()).postDelayed({
                restartCompass()
            }, 1500)
        }

    }

    override fun onStop() {
        super.onStop()
        try {
            compass!!.stop()
        }catch (e : Exception){

        }
    }


    private fun setupCompass() {

        compass = Compass(this)
        //compass!!.start()

        val cl:   Compass.CompassListener = getCompassListener()
        compass!!.setListener(cl)
    }

    private fun restartCompass(){
        try {
            compass!!.start()
        }catch (e : Exception){
            Snackbar.make(findViewById(android.R.id.content),"Compass doesn't work on your phone"+"\uD83D\uDE15",Snackbar.LENGTH_LONG).show()
            is_COMPASS_WORKING_FINE = false
        }

    }

    private fun initNavigation(){
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //Timber.i("vvv onSensorChanged")
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            if (event?.values?.size!! > 4) {
                var truncatedRotationVector = FloatArray(4)
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(event.values);
            }
        }
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun update(vectors: FloatArray) {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors)
        //Timber.i("vvv start Work gravi")
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
        // convert raw values to Degrees
        //val yaw   = orientation[0]  *  FROM_RADS_TO_DEGS
        //val roll  = orientation[2]  *  FROM_RADS_TO_DEGS
        val pitch = orientation[1] * FROM_RADS_TO_DEGS
        //PITCH = pitch
        try {
            viewModel.pitch.value = pitch.toFloat()
        }catch (e :Exception){
            Timber.e("pizdec ccc")
        }

        //viewModel.directionOfSolarPanel.value =
        //(findViewById<View>(R.id.pitch) as TextView).text = "Pitch: $pitch"
        //(findViewById<View>(R.id.roll) as TextView).text = "Roll: $roll"
        //(findViewById<View>(R.id.yaw) as TextView).text = "Yaw: $yaw"
    }

    private fun getAccelerometer(event: SensorEvent) {
        // Movement
        val x = event.values[0] / SensorManager.GRAVITY_EARTH
        val y = event.values[1] / SensorManager.GRAVITY_EARTH
        val z = event.values[2] / SensorManager.GRAVITY_EARTH


        // val roll = (Math.atan2(
        //     x.toDouble(),
        //     Math.sqrt((y * y + z * z).toDouble())
        // ) / Math.PI * 2.0).toFloat()
        val pitch = (Math.atan2(
            y.toDouble(),
            Math.sqrt((x * x + z * z).toDouble())
        ) / Math.PI * 2.0).toFloat()

        try {
            viewModel.pitch.value = pitch.toFloat()*100f
        }catch (e :Exception){
            Timber.e("pizdec ccc")
        }

    }

    fun helper_azimuth1(view: View) {
        Snackbar.make(findViewById(android.R.id.content), "This is a compass. The arrow always points north ", Snackbar.LENGTH_LONG).show()


    }

    fun closeOptimalTiltActivity(){
        val intent = Intent(this@OptimalOrientationHelperActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}